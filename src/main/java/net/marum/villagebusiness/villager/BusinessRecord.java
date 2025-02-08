package net.marum.villagebusiness.villager;

import net.minecraft.nbt.NbtCompound;

public class BusinessRecord {
	private String itemId;
    private Long timestamp;

	public BusinessRecord(String pItemId, Long pTimestamp) {
		this.itemId = pItemId;
		this.timestamp = pTimestamp;
	}

	public BusinessRecord(NbtCompound nbt) {
		this.itemId = nbt.getString("itemId");
		this.timestamp = nbt.getLong("timestamp");
	}

	/**
	 * Returns the first buy item of this trade offer.
	 */
	public String getItemID() {
		return this.itemId;
	}

    /**
	 * Returns the time of the purchase.
	 */
	public Long getTimestamp() {
		return this.timestamp;
	}

	public NbtCompound toNbt() {
		NbtCompound nbtCompound = new NbtCompound();
		nbtCompound.putString("itemId", this.itemId);
		nbtCompound.putLong("timestamp", this.timestamp);
		return nbtCompound;
	}
}

