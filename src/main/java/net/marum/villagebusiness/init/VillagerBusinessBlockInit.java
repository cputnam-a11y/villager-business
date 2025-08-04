package net.marum.villagebusiness.init;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.marum.villagebusiness.VillagerBusiness;
import net.marum.villagebusiness.block.RequestStandBlock;
import net.marum.villagebusiness.block.SalesStandBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Function;

public class VillagerBusinessBlockInit {
    public static final SalesStandBlock SALES_STAND_BLOCK = registerWithItem(
            "sales_stand",
            SalesStandBlock::new,
            AbstractBlock.Settings.copy(Blocks.BARREL).allowsSpawning(Blocks::never),
            new Item.Settings()
    );

    public static final RequestStandBlock REQUEST_STAND_BLOCK = registerWithItem(
            "request_stand",
            RequestStandBlock::new,
            AbstractBlock.Settings.copy(Blocks.BARREL).allowsSpawning(Blocks::never),
            new Item.Settings()
    );

    public static <T extends Block> T register(String name, Function<AbstractBlock.Settings, T> factory, AbstractBlock.Settings settings) {
        var key = RegistryKey.of(RegistryKeys.BLOCK, VillagerBusiness.id(name));
        var block = factory.apply(settings.registryKey(key));
        return Registry.register(Registries.BLOCK, key, block);
    }

    public static <T extends Block> T registerWithItem(String name, Function<AbstractBlock.Settings, T> factory, AbstractBlock.Settings blockSettings, Item.Settings settings) {
        T registered = register(name, factory, blockSettings);
        VillagerBusinessItemInit.register(name, s -> new BlockItem(registered, s), settings);
        return registered;
    }

    public static void load() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.add(VillagerBusinessBlockInit.SALES_STAND_BLOCK);
            entries.add(VillagerBusinessBlockInit.REQUEST_STAND_BLOCK);
        });
    }
}
