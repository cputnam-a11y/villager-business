package net.marum.villagebusiness.villager;

import java.util.ArrayList;

import net.minecraft.nbt.NbtCompound;

public class BusinessRecordList extends ArrayList<BusinessRecord> {
	public BusinessRecordList() {
	}

	public BusinessRecordList(NbtCompound nbt) {
		nbt.getKeys().forEach(key -> {
			Long value = nbt.getLong(key);
			this.add(new BusinessRecord(key, value));
		});
	}

	public NbtCompound toNbt() {
		NbtCompound nbtCompound = new NbtCompound();

		for (int i = 0; i < this.size(); i++) {
			BusinessRecord record = (BusinessRecord)this.get(i);
			nbtCompound.putLong(record.getItemID(), record.getTimestamp());
		}

		return nbtCompound;
	}
}
