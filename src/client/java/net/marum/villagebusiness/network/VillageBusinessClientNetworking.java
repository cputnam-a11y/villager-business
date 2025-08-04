package net.marum.villagebusiness.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class VillageBusinessClientNetworking {
    public static void registerClientNetworking() {
        VillagerBusinessNetworking.offerSendToServerHook(ClientPlayNetworking::send);
        //TODO: Seems unused?
//        ClientPlayNetworking.registerGlobalReceiver(new Identifier(VillageBusiness.MOD_ID, "sales_stand_update"), (client, handler, buf, responseSender) -> {
//
//            BlockPos pos = buf.readBlockPos();
//            NbtCompound nbt = buf.readNbt();
//
//            client.execute(() -> {
//                if (client.world != null) {
//                    BlockEntity blockEntity = client.world.getBlockEntity(pos);
//                    if (blockEntity != null) {
//                        blockEntity.readNbt(nbt);
//                        VillageBusiness.LOGGER.info(nbt.asString());
//                    }
//                }
//            });
//        });
    }

}
