package net.marum.villagebusiness.block.entity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.marum.villagebusiness.VillagerBusiness;
import net.marum.villagebusiness.attachment.VillagerBusinessAttachmentInit;
import net.marum.villagebusiness.init.VillagerBusinessBlockEntityTypeInit;
import net.marum.villagebusiness.init.VillagerBusinessItemInit;
import net.marum.villagebusiness.network.OpenRequestStandPayload;
import net.marum.villagebusiness.network.SetRequestStandFilterItemPayload;
import net.marum.villagebusiness.network.VillagerBusinessNetworking;
import net.marum.villagebusiness.pricing.ItemPrice;
import net.marum.villagebusiness.pricing.ItemPrices;
import net.marum.villagebusiness.screen.RequestStandScreenHandler;
import net.marum.villagebusiness.util.VillagerLure;
import net.marum.villagebusiness.villager.BusinessRecordList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RequestStandBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<OpenRequestStandPayload>, ImplementedInventory, SidedStorageBlockEntity {
    private ItemStack filterItem = ItemStack.EMPTY;

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);
    private static final int OUTPUT_SLOT = 0;
    private static final int INPUT_SLOT_NUGGETS = 3;
    private static final int INPUT_SLOT_EMERALDS = 2;
    private static final int INPUT_SLOT_BLOCKS = 1;
    private static final int ATTRACT_CHANCE = 1;

    private List<VillagerEntity> foundVillagers = new ArrayList<VillagerEntity>();
    private Set<VillagerLure> luringVillagers = new HashSet<VillagerLure>();
    private Set<VillagerLure> markedForRemovalVillagers = new HashSet<VillagerLure>();

    private static final int RADIUS = 50;
    private static final int SUCCESSFUL_PURCHASE_COOLDOWN = 1;
    private static final int REJECTED_PURCHASE_COOLDOWN = 1;
    private static final int LURED_BY_SALES_COOLDOWN = 1;

    private double priceMultiplier;
    private double cooldownMultiplier;

    private int ticks = 0;

    private int inputCount = 0;
    private int inputNuggetCount = 0;
    private int inputEmeraldCount = 0;
    private int inputBlockCount = 0;

    private ItemPrice itemPrice;
    private int priceSetting = 1;

    private int lastUpdatedOutputCount = 0;
    private int lastUpdatedOutputRawId = 0;
    private int lastUpdatedNuggetCount = 0;
    private int lastUpdatedEmeraldCount = 0;
    private int lastUpdatedBlockCount = 0;

    public RequestStandBlockEntity(BlockPos pos, BlockState state) {
        super(VillagerBusinessBlockEntityTypeInit.REQUEST_STAND_ENTITY, pos, state);
    }

    public ItemPrice getItemPrice() {
        return itemPrice;
    }

    public int getPriceSetting() {
        return priceSetting;
    }

    public void sendRequestToServer(ItemStack itemStack) {
        VillagerBusinessNetworking.sendToServerIfClient(new SetRequestStandFilterItemPayload(pos, itemStack));
        this.filterItem = itemStack;
        updatePrices();
    }

    public ItemStack getFilterItem() {
        return filterItem;
    }

    public void setFilterItem(ItemStack stack) {
        if (stack.getItem() == Items.EMERALD || stack.getItem() == Items.EMERALD_BLOCK || stack.getItem() == VillagerBusinessItemInit.EMERALD_NUGGET)
            return;
        this.filterItem = stack;
        updatePrices();
        markDirty();
    }

    public static void tick(World world, BlockPos pos, BlockState state, RequestStandBlockEntity entity) {
        if (world == null || world.isClient())
            return;

        if (entity.ticks == 0) {
            entity.updatePrices();
            entity.markDirty();
        }

        // Lure villagers every 5 seconds
        if (entity.ticks % 100 == 0) {
            if (entity.canBuy()) {
                entity.attractVillager();
            }
        }

        // Move lured villagers every 0.5 second
        if (entity.ticks % 10 == 0) {
            entity.luringVillagers.forEach(lure -> {
                if (lure.hasExpired()) {
                    entity.getFrustrated(lure.villager);
                    entity.markedForRemovalVillagers.add(lure);
                } else {
                    if (entity.villagerIsBusy(lure.villager)) {
                        entity.markedForRemovalVillagers.add(lure);
                    } else {
                        entity.moveVillagerTowardBlock(lure.villager);
                        if (lure.villager.getBlockPos().isWithinDistance(pos, 3)) {
                            entity.evaluateSale(lure.villager);
                            entity.markedForRemovalVillagers.add(lure);
                        }
                    }
                }
            });
            entity.markedForRemovalVillagers.forEach(lure -> {
                entity.luringVillagers.remove(lure);
            });
            entity.markedForRemovalVillagers.clear();

            boolean inventoryChanged = false;
            if (entity.getStack(OUTPUT_SLOT).getCount() != entity.lastUpdatedOutputCount) {
                entity.lastUpdatedOutputCount = entity.getStack(OUTPUT_SLOT).getCount();
                inventoryChanged = true;
            }
            if (Item.getRawId(entity.getStack(OUTPUT_SLOT).getItem()) != entity.lastUpdatedOutputRawId) {
                entity.lastUpdatedOutputRawId = Item.getRawId(entity.getStack(OUTPUT_SLOT).getItem());
                inventoryChanged = true;
            }
            if (entity.getStack(INPUT_SLOT_NUGGETS).getCount() != entity.lastUpdatedBlockCount ||
                    entity.getStack(INPUT_SLOT_EMERALDS).getCount() != entity.lastUpdatedEmeraldCount ||
                    entity.getStack(INPUT_SLOT_BLOCKS).getCount() != entity.lastUpdatedNuggetCount) {
                inventoryChanged = true;
                entity.lastUpdatedNuggetCount = entity.getStack(INPUT_SLOT_NUGGETS).getCount();
                entity.lastUpdatedEmeraldCount = entity.getStack(INPUT_SLOT_EMERALDS).getCount();
                entity.lastUpdatedBlockCount = entity.getStack(INPUT_SLOT_BLOCKS).getCount();
            }
            if (inventoryChanged) {
                entity.updatePrices();
                entity.updateListeners();
            }
        }

        // Find nearby villagers every 20 seconds
        if (entity.ticks >= 400) {
            entity.foundVillagers = world.getEntitiesByClass(VillagerEntity.class,
                    Box.from(BlockBox.create(pos.add(-RADIUS, -RADIUS, -RADIUS), pos.add(RADIUS, RADIUS, RADIUS))),
                    villager -> true);
            //VillageBusiness.LOGGER.info("Found "+foundVillagers.size()+" villagers");
            entity.ticks = world.random.nextBetween(-10, 10);
        }

        entity.ticks++;
    }

    private boolean villagerIsBusy(VillagerEntity villager) {
        Brain<VillagerEntity> brain = villager.getBrain();
        return (brain.hasActivity(Activity.REST) ||
                brain.hasActivity(Activity.HIDE) ||
                brain.hasActivity(Activity.PANIC) ||
                brain.hasActivity(Activity.SWIM) ||
                brain.hasActivity(Activity.PLAY) ||
                brain.hasActivity(Activity.WORK));
    }

    private void attractVillager() {
        if (!foundVillagers.isEmpty()) {
            foundVillagers.forEach(villager -> {
                if (!villager.isBaby()) {
                    if (world.random.nextInt(100) < ATTRACT_CHANCE) {
                        if (world.random.nextInt(100) < itemPrice.getRequestChance()) {
                            if (!villagerIsBusy(villager)) {
                                boolean willShop = true;
                                if (villager.hasAttached(VillagerBusinessAttachmentInit.LAST_LURED_BY_BUSINESS)) {
                                    if (villager.getAttached(VillagerBusinessAttachmentInit.LAST_LURED_BY_BUSINESS) + LURED_BY_SALES_COOLDOWN * 1000 > System.currentTimeMillis()) {
                                        willShop = false;
                                    }
                                }

                                if (willShop && villager.hasAttached(VillagerBusinessAttachmentInit.BUSINESS_RECORDS)) {
                                    BusinessRecordList records = villager.getAttached(VillagerBusinessAttachmentInit.BUSINESS_RECORDS);
                                    String itemId = getSellingItemID();
                                    if (records.contains(itemId)) {
                                        if (records.get(itemId).timestamp() > System.currentTimeMillis()) {
                                            willShop = false;
                                        }
                                    }
                                }
                                if (willShop) {
                                    luringVillagers.add(new VillagerLure(villager, 180));
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    private void moveVillagerTowardBlock(VillagerEntity villager) {
        NbtCompound nbt = new NbtCompound();
        villager.setAttached(VillagerBusinessAttachmentInit.LAST_LURED_BY_BUSINESS, System.currentTimeMillis());

        Brain<VillagerEntity> brain = villager.getBrain();
        brain.doExclusively(Activity.IDLE);
        brain.remember(MemoryModuleType.LOOK_TARGET, new BlockPosLookTarget(pos));
        brain.remember(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosLookTarget(pos), 0.5F, 2));
        //((ServerWorld) world).spawnParticles(ParticleTypes.CRIT, villager.getX(), villager.getY() + 1, villager.getZ(), 5, 0.5, 0.5, 0.5, 0.1);
    }

    private void evaluateSale(VillagerEntity villager) {
        if (itemPrice != null && canBuy()) {
            performSale(villager);
        }
    }

    private void performSale(VillagerEntity villager) {
        int outputCount = getStack(OUTPUT_SLOT).getCount();
        this.setStack(OUTPUT_SLOT, new ItemStack(filterItem.getItem(), outputCount + itemPrice.getSellAmount(priceSetting)));

        if (getStack(INPUT_SLOT_NUGGETS).isEmpty())
            inputNuggetCount = 0;
        else
            inputNuggetCount = getStack(INPUT_SLOT_NUGGETS).getCount();

        if (getStack(INPUT_SLOT_EMERALDS).isEmpty())
            inputEmeraldCount = 0;
        else
            inputEmeraldCount = getStack(INPUT_SLOT_EMERALDS).getCount();

        if (getStack(INPUT_SLOT_BLOCKS).isEmpty())
            inputBlockCount = 0;
        else
            inputBlockCount = getStack(INPUT_SLOT_BLOCKS).getCount();

        int resultingNuggets = inputNuggetCount - getRequestPrice();
        int resultingEmeralds = inputEmeraldCount;
        int resultingBlocks = inputBlockCount;
        while (resultingNuggets < 0) {
            resultingEmeralds -= 1;
            resultingNuggets += 9;
            while (resultingEmeralds < 0) {
                resultingBlocks -= 1;
                resultingEmeralds += 9;
            }
        }

        if (resultingNuggets != inputNuggetCount) {
            inputNuggetCount = resultingNuggets;
            this.setStack(INPUT_SLOT_NUGGETS, new ItemStack(VillagerBusinessItemInit.EMERALD_NUGGET, resultingNuggets));
        }

        if (resultingEmeralds != inputEmeraldCount) {
            inputEmeraldCount = resultingEmeralds;
            this.setStack(INPUT_SLOT_EMERALDS, new ItemStack(Items.EMERALD, resultingEmeralds));
        }

        if (resultingBlocks != inputBlockCount) {
            inputBlockCount = resultingBlocks;
            this.setStack(INPUT_SLOT_BLOCKS, new ItemStack(Items.EMERALD_BLOCK, inputBlockCount));
        }

        if (villager != null) {
            var records = villager.getAttachedOrCreate(VillagerBusinessAttachmentInit.BUSINESS_RECORDS);
            long timeMultiplier = VillagerBusiness.CONFIG.getOrDefault("request_time_multiplier", 2);
            records = records.add(getSellingItemID(), System.currentTimeMillis() + (int) (SUCCESSFUL_PURCHASE_COOLDOWN * 1000 * itemPrice.getCooldown(this.priceSetting) * timeMultiplier));
            villager.setAttached(VillagerBusinessAttachmentInit.BUSINESS_RECORDS, records);

            world.playSound(null, pos, SoundEvents.ENTITY_VILLAGER_TRADE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
            ((ServerWorld) world).spawnParticles(ParticleTypes.HAPPY_VILLAGER, villager.getX(), villager.getY() + 1, villager.getZ(), 5, 0.5, 0.5, 0.5, 0.1);
        }

        if (getStack(OUTPUT_SLOT).isEmpty()) {
            updatePrices();
        }
        updateListeners();
    }

    private void getFrustrated(VillagerEntity villager) {
        var records = villager.getAttachedOrCreate(VillagerBusinessAttachmentInit.BUSINESS_RECORDS);
        records = records.add(getSellingItemID(), System.currentTimeMillis() + (int) (REJECTED_PURCHASE_COOLDOWN * 1000 * itemPrice.getCooldown(this.priceSetting))); // Ignore item for 5 minutes
        villager.setAttached(VillagerBusinessAttachmentInit.BUSINESS_RECORDS, records);

        world.playSound(null, pos, SoundEvents.ENTITY_VILLAGER_NO, SoundCategory.BLOCKS, 1.0F, 1.0F);
        ((ServerWorld) world).spawnParticles(ParticleTypes.ANGRY_VILLAGER, villager.getX(), villager.getY() + 1, villager.getZ(), 5, 1, 1, 1, 0.1);
        villager.setHeadRollingTimeLeft(40);

        Brain<VillagerEntity> brain = villager.getBrain();
        brain.remember(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosLookTarget(villager.getPos()), 0.5F, 2));
        brain.doExclusively(Activity.IDLE);
    }

    private boolean canBuy() {
        if (world.isReceivingRedstonePower(pos))
            return false;
        if (!hasEnoughSpace())
            return false;
        return hasEnoughEmeralds();
    }

    public boolean hasFilter() {
        if (filterItem == null) return false;
        return !filterItem.isEmpty();
    }

    public boolean hasProduct() {
        if (itemPrice == null) return false;
        return !getStack(OUTPUT_SLOT).isEmpty();
    }

    public boolean hasEnoughSpace() {
        if (itemPrice == null) return false;
        if (getStack(OUTPUT_SLOT).isEmpty()) return true;
        if (!getStack(OUTPUT_SLOT).isOf(filterItem.getItem())) return false;
        return getStack(OUTPUT_SLOT).getCount() + itemPrice.getSellAmount(1) <= filterItem.getMaxCount();
    }

    public boolean hasEnoughEmeralds() {
        inputNuggetCount = getStack(INPUT_SLOT_NUGGETS).getCount();
        inputEmeraldCount = getStack(INPUT_SLOT_EMERALDS).getCount();
        inputBlockCount = getStack(INPUT_SLOT_BLOCKS).getCount();
        return inputNuggetCount + inputEmeraldCount * 9 + inputBlockCount * 81 >= getRequestPrice();
    }

    public int getRequestPrice() {
        return (int) Math.round(itemPrice.getPrice(1) * priceMultiplier);
    }

    public int getRequestCooldown() {
        return (int) Math.round(itemPrice.getCooldown(1) * cooldownMultiplier);
    }

    public int getRequestChance() {
        return itemPrice.getRequestChance();
    }

    @Override
    public void setStack(int slot, ItemStack stack) {

        inventory.set(slot, stack);

        lastUpdatedOutputCount = getStack(OUTPUT_SLOT).getCount();
        lastUpdatedOutputRawId = Item.getRawId(getStack(OUTPUT_SLOT).getItem());
        lastUpdatedNuggetCount = getStack(INPUT_SLOT_NUGGETS).getCount();
        lastUpdatedEmeraldCount = getStack(INPUT_SLOT_EMERALDS).getCount();
        lastUpdatedBlockCount = getStack(INPUT_SLOT_BLOCKS).getCount();

        updateListeners();
    }

    private void updatePrices() {
        ItemStack stack = getFilterItem();
        if (stack.isEmpty()) {
            itemPrice = null;
            return;
        }

        itemPrice = ItemPrices.priceList.getOrDefault(stack.getItem(), null);

        priceMultiplier = VillagerBusiness.CONFIG.getOrDefault("request_price_multiplier", 1f);
        cooldownMultiplier = VillagerBusiness.CONFIG.getOrDefault("request_cooldown_multiplier", 1f);
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {
        if (stack.getItem() == Items.EMERALD_BLOCK && slot == INPUT_SLOT_BLOCKS) return true;
        if (stack.getItem() == Items.EMERALD && slot == INPUT_SLOT_EMERALDS) return true;
        return stack.getItem() == VillagerBusinessItemInit.EMERALD_NUGGET && slot == INPUT_SLOT_NUGGETS;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction side) {
        if (world.isReceivingRedstonePower(pos))
            return true;
        return slot == OUTPUT_SLOT;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.village_business.request_stand");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new RequestStandScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    public DefaultedList<ItemStack> getInventory() {
        return inventory;
    }

    @Override
    public OpenRequestStandPayload getScreenOpeningData(ServerPlayerEntity player) {
        return new OpenRequestStandPayload(this.pos);
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        Inventories.readData(view, inventory);
        view.getOptionalInt("InputCount").ifPresent(value -> this.inputCount = value);
        view.getOptionalInt("OutputNuggetCount").ifPresent(value -> this.inputNuggetCount = value);
        view.getOptionalInt("OutputEmeraldCount").ifPresent(value -> this.inputEmeraldCount = value);
        view.getOptionalInt("OutputBlockCount").ifPresent(value -> this.inputBlockCount = value);
        filterItem = view.read("FilterItem", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
        if (world != null && world.isClient()) {
            itemPrice = new ItemPrice(
                    getStack(OUTPUT_SLOT).getItem(),
                    view.getInt("SellAmount", 0),
                    view.getInt("Price", 0),
                    0,
                    view.getInt("RequestChance", 0),
                    view.getInt("Cooldown", 0)
            );
        }
        updatePrices();
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        Inventories.writeData(view, inventory);
        view.putInt("InputCount", this.inputCount);
        view.putInt("OutputNuggetCount", this.inputNuggetCount);
        view.putInt("OutputEmeraldCount", this.inputEmeraldCount);
        view.putInt("OutputBlockCount", this.inputBlockCount);
        if (itemPrice != null) {
            view.putInt("SellAmount", itemPrice.getSellAmount(1));
            view.putInt("Price", itemPrice.getPrice(1));
            view.putInt("RequestChance", itemPrice.getRequestChance());
            view.putInt("Cooldown", itemPrice.getCooldown(1));
        } else {
            view.putInt("SellAmount", 0);
            view.putInt("Price", 0);
            view.putInt("RequestChance", 0);
            view.putInt("Cooldown", 0);
        }
        if (!filterItem.isEmpty()) {
            view.put("FilterItem", ItemStack.OPTIONAL_CODEC, filterItem);
        }
    }

    private String getSellingItemID() {
        return getStack(OUTPUT_SLOT).getItem().toString();
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        NbtCompound nbt = createNbt(registries);
        updatePrices();
        return nbt;
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public void updateListeners() {
        this.inputCount = getStack(OUTPUT_SLOT).getCount();
        this.inputNuggetCount = getStack(INPUT_SLOT_NUGGETS).getCount();
        this.inputEmeraldCount = getStack(INPUT_SLOT_EMERALDS).getCount();
        this.inputBlockCount = getStack(INPUT_SLOT_BLOCKS).getCount();
        this.markDirty();
        this.getWorld().updateListeners(pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_ALL);
    }

    public int getInputCount() {
        return inputCount;
    }

    public int getInputNuggetCount() {
        return inputNuggetCount;
    }

    public int getInputEmeraldCount() {
        return inputEmeraldCount;
    }

    public int getInputBlockCount() {
        return inputBlockCount;
    }
}
