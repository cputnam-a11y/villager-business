package net.marum.villagebusiness.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.BlockPos;

public record OpenRequestStandPayload(BlockPos pos) {
    public static final PacketCodec<PacketByteBuf, OpenRequestStandPayload> CODEC = BlockPos.PACKET_CODEC.xmap(
            OpenRequestStandPayload::new,
            OpenRequestStandPayload::pos
    ).cast();
}
