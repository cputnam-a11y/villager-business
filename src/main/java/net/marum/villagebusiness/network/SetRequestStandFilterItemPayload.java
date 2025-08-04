package net.marum.villagebusiness.network;

import net.marum.villagebusiness.VillagerBusiness;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public record SetRequestStandFilterItemPayload(BlockPos pos, ItemStack filterStack) implements CustomPayload {
    public static final Id<SetRequestStandFilterItemPayload> ID = new Id<>(VillagerBusiness.id("request"));
    public static final PacketCodec<RegistryByteBuf, SetRequestStandFilterItemPayload> CODEC = PacketCodec.tuple(
            BlockPos.PACKET_CODEC,
            SetRequestStandFilterItemPayload::pos,
            ItemStack.PACKET_CODEC,
            SetRequestStandFilterItemPayload::filterStack,
            SetRequestStandFilterItemPayload::new
    );
    public static final Type<RegistryByteBuf, SetRequestStandFilterItemPayload> TYPE = new Type<>(ID, CODEC);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
