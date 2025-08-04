package net.marum.villagebusiness.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.marum.villagebusiness.VillagerBusiness;
import net.marum.villagebusiness.block.entity.RequestStandBlockEntity;
import net.marum.villagebusiness.block.entity.SalesStandBlockEntity;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class VillagerBusinessNetworking {
    private static Consumer<CustomPayload> SEND_TO_SERVER;
    public static final Identifier REQUEST_PACKET = VillagerBusiness.id("request");

    public static void registerCommonNetworking() {
        PayloadTypeRegistry.playC2S().register(SetSalesStandPricePayload.ID, SetSalesStandPricePayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(SetSalesStandPricePayload.ID, (payload, context) ->
                context.server().execute(() -> {
                    if (context.player().getWorld().getBlockEntity(payload.pos()) instanceof SalesStandBlockEntity blockEntity) {
                        blockEntity.serverSetPriceSetting(payload.price());
                        blockEntity.updateListeners();
                    }
                })
        );

        PayloadTypeRegistry.playC2S().register(SetRequestStandFilterItemPayload.ID, SetRequestStandFilterItemPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(SetRequestStandFilterItemPayload.ID, (payload, context) ->
                context.server().execute(() -> {
                    if (context.player().getWorld().getBlockEntity(payload.pos()) instanceof RequestStandBlockEntity blockEntity) {
                        blockEntity.setFilterItem(payload.filterStack());
                        blockEntity.updateListeners();
                    }
                })
        );
    }

    public static void offerSendToServerHook(Consumer<CustomPayload> hook) {
        if (hook != null) {
            SEND_TO_SERVER = hook;
        }
    }

    public static void sendToServerIfClient(CustomPayload payload) {
        //noinspection StatementWithEmptyBody
        if (SEND_TO_SERVER != null) {
            SEND_TO_SERVER.accept(payload);
        } else {
            // TODO
            // VillageBusiness.LOGGER.warn("No sendToServer hook registered, payload not sent: {}", payload);
        }
    }
}
