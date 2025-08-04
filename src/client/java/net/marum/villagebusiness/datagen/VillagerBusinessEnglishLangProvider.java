package net.marum.villagebusiness.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.marum.villagebusiness.init.VillagerBusinessBlockInit;
import net.marum.villagebusiness.init.VillagerBusinessItemInit;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class VillagerBusinessEnglishLangProvider extends FabricLanguageProvider {
    public VillagerBusinessEnglishLangProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(VillagerBusinessBlockInit.REQUEST_STAND_BLOCK, "Request Stand");
        translationBuilder.add(VillagerBusinessBlockInit.SALES_STAND_BLOCK, "Sales Stand");
        translationBuilder.add(VillagerBusinessItemInit.EMERALD_NUGGET, "Emerald Nugget");
        translationBuilder.add("village_business.price", "Price");
        translationBuilder.add("village_business.chance_tip", "Chance of sale each visit");
        translationBuilder.add("village_business.return_tip", "Time for customers to return");
        translationBuilder.add("village_business.seller_chance_tip", "Chance that the villager sells");
        translationBuilder.add("village_business.seller_return_tip", "Time for sellers to return");
        translationBuilder.add("village_business.request", "Request");
        translationBuilder.add("village_business.request_slot", "Item to request");
    }
}
