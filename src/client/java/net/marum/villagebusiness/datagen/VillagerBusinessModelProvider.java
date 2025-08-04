package net.marum.villagebusiness.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.marum.villagebusiness.init.VillagerBusinessBlockInit;
import net.marum.villagebusiness.init.VillagerBusinessItemInit;
import net.minecraft.block.Block;
import net.minecraft.client.data.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Util;

public class VillagerBusinessModelProvider extends FabricModelProvider {
    public VillagerBusinessModelProvider(FabricDataOutput output) {
        super(output);
    }


    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        registerStand(VillagerBusinessBlockInit.REQUEST_STAND_BLOCK, blockStateModelGenerator);
        registerStand(VillagerBusinessBlockInit.SALES_STAND_BLOCK, blockStateModelGenerator);

    }

    public void registerStand(Block block, BlockStateModelGenerator gen) {
        gen.blockStateCollector.accept(
                VariantsBlockModelDefinitionCreator.of(block)
                        .with(BlockStateModelGenerator.createBooleanModelMap(
                                Properties.POWERED,
                                BlockStateModelGenerator.createWeightedVariant(
                                        Models.CUBE_BOTTOM_TOP.upload(
                                                block,
                                                "_powered",
                                                Util.make(
                                                        new TextureMap(),
                                                        map -> {
                                                            map.put(TextureKey.SIDE, ModelIds.getBlockSubModelId(
                                                                    block, "_side_closed"
                                                            ));
                                                            map.put(TextureKey.TOP, ModelIds.getBlockSubModelId(
                                                                    block, "_top_closed"
                                                            ));
                                                            map.put(TextureKey.BOTTOM, ModelIds.getBlockSubModelId(
                                                                    block, "_bottom"
                                                            ));
                                                        }
                                                ),
                                                gen.modelCollector
                                        )
                                ),
                                BlockStateModelGenerator.createWeightedVariant(
                                        Util.make(
                                                Models.CUBE_BOTTOM_TOP.upload(
                                                        block,
                                                        Util.make(
                                                                new TextureMap(),
                                                                map -> {
                                                                    map.put(TextureKey.SIDE, ModelIds.getBlockSubModelId(
                                                                            block, "_side"
                                                                    ));
                                                                    map.put(TextureKey.TOP, ModelIds.getBlockSubModelId(
                                                                            block, "_top"
                                                                    ));
                                                                    map.put(TextureKey.BOTTOM, ModelIds.getBlockSubModelId(
                                                                            block, "_bottom"
                                                                    ));
                                                                }
                                                        ),
                                                        gen.modelCollector
                                                ),
                                                id -> gen.itemModelOutput.accept(
                                                        block.asItem(),
                                                        ItemModels.basic(id)
                                                )
                                        )
                                )
                        ))
        );
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(VillagerBusinessItemInit.EMERALD_NUGGET, Models.GENERATED);


    }
}
