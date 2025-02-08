package net.marum.villagebusiness.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.marum.villagebusiness.VillageBusiness;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;

public class VillageBusinessScreenHandlers {
    public static final ScreenHandlerType<SalesStandScreenHandler> SALES_STAND_SCREEN_HANDLER =
        Registry.register(Registries.SCREEN_HANDLER, VillageBusiness.id("sales_stand"),
        new ExtendedScreenHandlerType<>(SalesStandScreenHandler::new));
    public static final ScreenHandlerType<RequestStandScreenHandler> REQUEST_STAND_SCREEN_HANDLER =
        Registry.register(Registries.SCREEN_HANDLER, VillageBusiness.id("request_stand"),
        new ExtendedScreenHandlerType<>(RequestStandScreenHandler::new));
    
    public static void load() {}
}
