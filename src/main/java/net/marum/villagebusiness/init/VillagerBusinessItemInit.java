package net.marum.villagebusiness.init;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.marum.villagebusiness.VillageBusiness;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class VillagerBusinessItemInit {
    public static final Item EMERALD_NUGGET = register("emerald_nugget", new Item(new FabricItemSettings()));

    private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries) {
        entries.add(EMERALD_NUGGET);
    }

    public static <T extends Item> T register(String name, T item) {
        return Registry.register(Registries.ITEM, VillageBusiness.id(name), item);
    }

    public static void load() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(VillagerBusinessItemInit::addItemsToIngredientItemGroup);
    }
}