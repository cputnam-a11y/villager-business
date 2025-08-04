package net.marum.villagebusiness.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.BlockPos;

public record OpenSalesStandPayload(BlockPos pos) {
    public static final PacketCodec<PacketByteBuf, OpenSalesStandPayload> CODEC = BlockPos.PACKET_CODEC.xmap(
            OpenSalesStandPayload::new,
            OpenSalesStandPayload::pos
    ).cast();
}
