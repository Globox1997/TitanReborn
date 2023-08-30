package net.shirojr.titanfabric;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.shirojr.titanfabric.event.KeyBindEvents;
import net.shirojr.titanfabric.screen.TitanFabricScreenHandlers;
import net.shirojr.titanfabric.screen.custom.DiamondFurnaceScreen;
import net.shirojr.titanfabric.util.ModelPredicateProviders;

public class TitanFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelPredicateProviders.register();
        KeyBindEvents.register();
        HandledScreens.register(TitanFabricScreenHandlers.DIAMOND_FURNACE_SCREEN_HANDLER, DiamondFurnaceScreen::new);
    }
}
