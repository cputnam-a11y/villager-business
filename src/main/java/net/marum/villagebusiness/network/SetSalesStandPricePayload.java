package net.marum.villagebusiness.network;

import net.marum.villagebusiness.VillagerBusiness;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public record SetSalesStandPricePayload(BlockPos pos, int price) implements CustomPayload {
	public static final Id<SetSalesStandPricePayload> ID = new Id<>(VillagerBusiness.id("price_setting"));
	public static final PacketCodec<PacketByteBuf, SetSalesStandPricePayload> CODEC = PacketCodec.tuple(
			BlockPos.PACKET_CODEC,
			SetSalesStandPricePayload::pos,
			PacketCodecs.VAR_INT,
			SetSalesStandPricePayload::price,
			SetSalesStandPricePayload::new
	);
	public static final Type<PacketByteBuf, SetSalesStandPricePayload> TYPE = new Type<>(ID, CODEC);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
