package net.marum.villagebusiness.mixin;

import net.marum.villagebusiness.villager.BusinessRecordList;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

@Mixin(VillagerEntity.class)
public class VillageBusinessMixin {

    @Nullable
	public BusinessRecordList businessRecords;
	public Long lastLuredByBusiness = 0L;

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void addCustomNBTToVillager(CallbackInfo ci, @Local NbtCompound nbt) {
        if (this.businessRecords == null) {
            nbt.put("BusinessRecords", new NbtCompound());
        } else {
            nbt.put("BusinessRecords", this.businessRecords.toNbt());
        }

        nbt.putLong("LastLuredByBusiness", this.lastLuredByBusiness);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void loadCustomNBTFromVillager(CallbackInfo ci, @Local NbtCompound nbt) {
        if (nbt.contains("BusinessRecords")) {
            this.businessRecords = new BusinessRecordList(nbt.getCompound("BusinessRecords"));
        }

        if (nbt.contains("LastLuredByBusiness")) {
            this.lastLuredByBusiness = nbt.getLong("LastLuredByBusiness");
        }
    }
}