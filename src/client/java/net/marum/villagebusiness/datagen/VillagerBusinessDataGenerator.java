package net.marum.villagebusiness.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class VillagerBusinessDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        var pack = fabricDataGenerator.createPack();
        pack.addProvider(VillagerBusinessRecipeProvider::new);
        pack.addProvider(VillagerBusinessModelProvider::new);
        pack.addProvider(VillagerBusinessEnglishLangProvider::new);
        pack.addProvider(VillagerBusinessBlockLootTableProvider::new);
    }
}
