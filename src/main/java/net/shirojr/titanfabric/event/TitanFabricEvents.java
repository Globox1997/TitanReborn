package net.shirojr.titanfabric.event;

import net.shirojr.titanfabric.event.custom.*;

public class TitanFabricEvents {
    public static void registerEvents() {
        CommandRegistrationEvent.register();
    }

    public static void registerClientEvents() {
        TitanFabricClientTickEvents.register();
        ParachuteFeatureRendererEventHandler.register();
        HudEvent.register();
    }
}
