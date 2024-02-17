package org.geysermc.floodgate;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geysermc.floodgate.inject.forge.ForgeInjector;
import org.geysermc.floodgate.module.ForgeAddonModule;
import org.geysermc.floodgate.module.*;
import org.geysermc.floodgate.api.logger.FloodgateLogger;

import com.google.inject.Guice;
import com.google.inject.Injector;

import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.inject.PlatformInjector;
import org.geysermc.floodgate.api.logger.FloodgateLogger;

import javax.inject.Inject;

@Mod("floodgate")
public class ForgeMod {
    private static final Logger LOGGER = LogManager.getLogger();
    private final ForgePlatform platform;
    Injector injector;

    @Inject
    public ForgeMod() {
        ForgeInjector.setInstance(new ForgeInjector());
        MinecraftForge.EVENT_BUS.register(this);
        System.out.println("confdir" + FMLPaths.CONFIGDIR.get().resolve("floodgate").toString());
        injector = Guice.createInjector(
                new ServerCommonModule(FMLPaths.CONFIGDIR.get().resolve("floodgate")),
                new ForgePlatformModule()
        );
        this.platform = injector.getInstance(ForgePlatform.class);
    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        long ctm = System.currentTimeMillis();

        MinecraftServerHolder.set(event.getServer());
        
        platform.enable(new ForgeCommandModule());


        platform.enable(
        		new ForgeAddonModule(),
                new ForgeListenerModule(),
                new PluginMessageModule()
        );

        long endCtm = System.currentTimeMillis();
        FloodgateLogger logger = injector.getInstance(FloodgateLogger.class);
        logger.translatedInfo("floodgate.core.finish", endCtm - ctm);
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        // サーバー停止時の処理
    }
}