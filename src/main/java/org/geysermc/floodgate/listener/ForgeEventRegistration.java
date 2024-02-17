package org.geysermc.floodgate.listener;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.server.ServerStartedEvent;
import org.geysermc.floodgate.platform.listener.ListenerRegistration;

@Mod.EventBusSubscriber(modid = "floodgate")
public final class ForgeEventRegistration implements ListenerRegistration<ForgeEventListener> {
    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        ForgeEventListener listener = new ForgeEventListener(); // Instantiate your ForgeEventListener
        // Register your listener to the appropriate Forge events here
        // Example: MinecraftForge.EVENT_BUS.register(listener);
    }

    @Override
    public void register(ForgeEventListener listener) {
        // You can also register Forge events directly here if you're not using event subscribers
    }
}
