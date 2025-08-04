package net.marum.villagebusiness;

import net.fabricmc.api.ClientModInitializer;
import net.marum.villagebusiness.block.entity.RequestStandBlockEntityRenderer;
import net.marum.villagebusiness.block.entity.SalesStandBlockEntityRenderer;
import net.marum.villagebusiness.init.VillagerBusinessBlockEntityTypeInit;
import net.marum.villagebusiness.network.VillageBusinessClientNetworking;
import net.marum.villagebusiness.screen.RequestStandScreen;
import net.marum.villagebusiness.screen.SalesStandScreen;
import net.marum.villagebusiness.screen.VillagerBusinessScreenHandlers;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class VillagerBusinessClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HandledScreens.register(VillagerBusinessScreenHandlers.SALES_STAND_SCREEN_HANDLER, SalesStandScreen::new);
        BlockEntityRendererFactories.register(
                VillagerBusinessBlockEntityTypeInit.SALES_STAND_ENTITY,
                SalesStandBlockEntityRenderer::new
        );

        HandledScreens.register(VillagerBusinessScreenHandlers.REQUEST_STAND_SCREEN_HANDLER, RequestStandScreen::new);
        BlockEntityRendererFactories.register(
                VillagerBusinessBlockEntityTypeInit.REQUEST_STAND_ENTITY,
                RequestStandBlockEntityRenderer::new
        );

        VillageBusinessClientNetworking.registerClientNetworking();
    }
}
