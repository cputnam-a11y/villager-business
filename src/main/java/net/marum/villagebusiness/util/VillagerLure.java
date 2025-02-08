package net.marum.villagebusiness.util;

import net.minecraft.entity.passive.VillagerEntity;

public class VillagerLure {
    public VillagerEntity villager;
    public double expirationTimestamp;

    public VillagerLure(VillagerEntity newVillager, int expiresInSeconds) {
        villager = newVillager;
        expirationTimestamp = System.currentTimeMillis() + expiresInSeconds*1000;
    }

    public boolean hasExpired() {
        return System.currentTimeMillis() > expirationTimestamp;
    }
}
