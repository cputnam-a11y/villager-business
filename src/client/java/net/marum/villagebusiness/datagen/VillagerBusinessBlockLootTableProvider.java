package net.marum.villagebusiness.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.marum.villagebusiness.init.VillagerBusinessBlockInit;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.CopyNameLootFunction;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class VillagerBusinessBlockLootTableProvider extends FabricBlockLootTableProvider {
    protected VillagerBusinessBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        this.addDrop(
                VillagerBusinessBlockInit.REQUEST_STAND_BLOCK,
                LootTable.builder()
                        .pool(
                                LootPool.builder()
                                        .with(
                                                ItemEntry.builder(VillagerBusinessBlockInit.REQUEST_STAND_BLOCK)
                                                        .apply(CopyNameLootFunction.builder(CopyNameLootFunction.Source.BLOCK_ENTITY))

                                        )
                                        .conditionally(SurvivesExplosionLootCondition.builder())
                                        .build()
                        )
                        .randomSequenceId(Registries.BLOCK.getId(VillagerBusinessBlockInit.REQUEST_STAND_BLOCK).withPrefixedPath("blocks/"))
        );
        this.addDrop(
                VillagerBusinessBlockInit.SALES_STAND_BLOCK,
                LootTable.builder()
                        .pool(
                                LootPool.builder()
                                        .with(
                                                ItemEntry.builder(VillagerBusinessBlockInit.SALES_STAND_BLOCK)
                                                        .apply(CopyNameLootFunction.builder(CopyNameLootFunction.Source.BLOCK_ENTITY))

                                        )
                                        .conditionally(SurvivesExplosionLootCondition.builder())
                                        .build()
                        )
                        .randomSequenceId(Registries.BLOCK.getId(VillagerBusinessBlockInit.SALES_STAND_BLOCK).withPrefixedPath("blocks/"))
        );
    }
}
