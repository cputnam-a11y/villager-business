package net.marum.villagebusiness.screen;

import net.marum.villagebusiness.block.entity.SalesStandBlockEntity;
import net.marum.villagebusiness.init.VillagerBusinessItemInit;
import net.marum.villagebusiness.network.OpenSalesStandPayload;
import net.marum.villagebusiness.util.NonEmeraldSlot;
import net.marum.villagebusiness.util.OutputOnlySlot;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class SalesStandScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    public final SalesStandBlockEntity blockEntity;

    public SalesStandScreenHandler(int syncId, PlayerInventory inventory, OpenSalesStandPayload payload) {
        this(syncId, inventory, inventory.player.getWorld().getBlockEntity(payload.pos()));
    }

    public SalesStandScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity) {
        super(VillagerBusinessScreenHandlers.SALES_STAND_SCREEN_HANDLER, syncId);
        this.inventory = (Inventory) blockEntity;
        inventory.onOpen(playerInventory.player);
        this.blockEntity = (SalesStandBlockEntity) blockEntity;

        this.addSlot(new NonEmeraldSlot(inventory, 3, 15, 21));
        this.addSlot(new OutputOnlySlot(inventory, 2, 109, 21, VillagerBusinessItemInit.EMERALD_NUGGET));
        this.addSlot(new OutputOnlySlot(inventory, 1, 127, 21, Items.EMERALD));
        this.addSlot(new OutputOnlySlot(inventory, 0, 145, 21, Items.EMERALD_BLOCK));

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    @Override
    public boolean canInsertIntoSlot(Slot slot) {
        return slot.getIndex() == 0;
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.getIndex() == 0;
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
