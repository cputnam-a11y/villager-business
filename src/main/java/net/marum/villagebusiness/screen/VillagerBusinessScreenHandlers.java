package net.marum.villagebusiness.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.marum.villagebusiness.VillagerBusiness;
import net.marum.villagebusiness.network.OpenRequestStandPayload;
import net.marum.villagebusiness.network.OpenSalesStandPayload;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;

public class VillagerBusinessScreenHandlers {
    public static final ScreenHandlerType<SalesStandScreenHandler> SALES_STAND_SCREEN_HANDLER = Registry.register(
            Registries.SCREEN_HANDLER,
            VillagerBusiness.id("sales_stand"),
            new ExtendedScreenHandlerType<>(SalesStandScreenHandler::new, OpenSalesStandPayload.CODEC)
    );

    public static final ScreenHandlerType<RequestStandScreenHandler> REQUEST_STAND_SCREEN_HANDLER = Registry.register(
            Registries.SCREEN_HANDLER,
            VillagerBusiness.id("request_stand"),
            new ExtendedScreenHandlerType<>(RequestStandScreenHandler::new, OpenRequestStandPayload.CODEC)
    );

    public static void load() {
    }
}
