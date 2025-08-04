package net.marum.villagebusiness.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.marum.villagebusiness.VillagerBusiness;
import net.marum.villagebusiness.init.VillagerBusinessBlockInit;
import net.marum.villagebusiness.init.VillagerBusinessItemInit;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.data.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class VillagerBusinessRecipeProvider extends FabricRecipeProvider {

    public VillagerBusinessRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
        return new RecipeGenerator(registryLookup, exporter) {
            @Override
            public void generate() {
                this.offerReversibleCompactingRecipes(
                        RecipeCategory.MISC,
                        VillagerBusinessItemInit.EMERALD_NUGGET,
                        RecipeCategory.MISC,
                        Items.EMERALD,
                        VillagerBusiness.id("emerald_from_nuggets").toString(),
                        null,
                        VillagerBusiness.id("emerald_nugget").toString(),
                        null
                );
                ShapedRecipeJsonBuilder.create(
                                Registries.ITEM,
                                RecipeCategory.MISC,
                                VillagerBusinessBlockInit.REQUEST_STAND_BLOCK
                        )
                        .input('#', Items.BARREL)
                        .input('X', Items.GOLD_INGOT)
                        .pattern("X")
                        .pattern("#")
                        .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                        .criterion(hasItem(Items.BARREL), conditionsFromItem(Items.BARREL))
                        .showNotification(true)
                        .offerTo(exporter);
                ShapedRecipeJsonBuilder.create(
                                Registries.ITEM,
                                RecipeCategory.MISC,
                                VillagerBusinessBlockInit.SALES_STAND_BLOCK
                        )
                        .input('#', Items.BARREL)
                        .input('X', Items.EMERALD)
                        .pattern("X")
                        .pattern("#")
                        .criterion(hasItem(Items.EMERALD), conditionsFromItem(Items.EMERALD))
                        .criterion(hasItem(Items.BARREL), conditionsFromItem(Items.BARREL))
                        .showNotification(true)
                        .offerTo(exporter);

            }
        };
    }

    @Override
    public String getName() {
        return "Recipe Provider";
    }
}
