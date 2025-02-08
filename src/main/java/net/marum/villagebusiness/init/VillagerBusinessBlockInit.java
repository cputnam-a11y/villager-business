package net.marum.villagebusiness.init;

import net.marum.villagebusiness.VillageBusiness;
import net.marum.villagebusiness.block.RequestStandBlock;
import net.marum.villagebusiness.block.SalesStandBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class VillagerBusinessBlockInit {
    public static final SalesStandBlock SALES_STAND_BLOCK = registerWithItem("sales_stand",
        new SalesStandBlock(AbstractBlock.Settings.copy(Blocks.BARREL).allowsSpawning((state, world, pos, entityType) -> false)), new Item.Settings());
    public static final RequestStandBlock REQUEST_STAND_BLOCK = registerWithItem("request_stand",
        new RequestStandBlock(AbstractBlock.Settings.copy(Blocks.BARREL).allowsSpawning((state, world, pos, entityType) -> false)), new Item.Settings());

    public static <T extends Block> T register(String name, T block) {
        return Registry.register(Registries.BLOCK, VillageBusiness.id(name), block);
    }

    public static <T extends Block> T registerWithItem(String name, T block, Item.Settings settings) {
        T registered = register(name, block);
        VillagerBusinessItemInit.register(name, new BlockItem(block, settings));
        return registered;
    }

    public static void load() {
        
    }
}
