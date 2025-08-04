package net.marum.villagebusiness.init;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.marum.villagebusiness.VillagerBusiness;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Function;

public class VillagerBusinessItemInit {
    public static final Item EMERALD_NUGGET = register("emerald_nugget", Item::new, new Item.Settings());

    public static <T extends Item> T register(String name, Function<Item.Settings, T> factory, Item.Settings settings) {
        var key = RegistryKey.of(RegistryKeys.ITEM, VillagerBusiness.id(name));
        var item = factory.apply(settings.registryKey(key));
        return Registry.register(Registries.ITEM, key, item);
    }

    public static void load() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> entries.add(EMERALD_NUGGET));
    }
}
