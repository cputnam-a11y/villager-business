package net.marum.villagebusiness;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.impl.client.rendering.BlockEntityRendererRegistryImpl;
import net.marum.villagebusiness.block.entity.RequestStandBlockEntityRenderer;
import net.marum.villagebusiness.block.entity.SalesStandBlockEntityRenderer;
import net.marum.villagebusiness.init.VillageBusinessBlockEntityTypeInit;
import net.marum.villagebusiness.screen.RequestStandScreen;
import net.marum.villagebusiness.screen.SalesStandScreen;
import net.marum.villagebusiness.screen.VillageBusinessScreenHandlers;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class VillageBusinessClient implements ClientModInitializer{

    @Override
    public void onInitializeClient() {
        HandledScreens.register(VillageBusinessScreenHandlers.SALES_STAND_SCREEN_HANDLER, SalesStandScreen::new);
        BlockEntityRendererRegistryImpl.register(VillageBusinessBlockEntityTypeInit.SALES_STAND_ENTITY, SalesStandBlockEntityRenderer::new);
        
        HandledScreens.register(VillageBusinessScreenHandlers.REQUEST_STAND_SCREEN_HANDLER, RequestStandScreen::new);
        BlockEntityRendererRegistryImpl.register(VillageBusinessBlockEntityTypeInit.REQUEST_STAND_ENTITY, RequestStandBlockEntityRenderer::new);

        ClientPlayNetworking.registerGlobalReceiver(new Identifier(VillageBusiness.MOD_ID, "sales_stand_update"), (client, handler, buf, responseSender) -> {
        
            BlockPos pos = buf.readBlockPos();
            NbtCompound nbt = buf.readNbt();

            client.execute(() -> {
                if (client.world != null) {
                    BlockEntity blockEntity = client.world.getBlockEntity(pos);
                    if (blockEntity != null) {
                        blockEntity.readNbt(nbt);
                        VillageBusiness.LOGGER.info(nbt.asString());
                    }
                }
            });
        });
    }
}
