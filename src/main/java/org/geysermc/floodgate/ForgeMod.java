package org.geysermc.floodgate;


import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.geysermc.floodgate.inject.forge.ForgeInjector;
import org.geysermc.floodgate.module.ForgeAddonModule;
import org.geysermc.floodgate.module.*;
import org.geysermc.floodgate.mixin.*;
import org.geysermc.floodgate.api.logger.FloodgateLogger;

import com.google.inject.Guice;
import com.google.inject.Injector;

import lombok.Getter;

import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.inject.PlatformInjector;
import org.geysermc.floodgate.api.logger.FloodgateLogger;

import javax.inject.Inject;

@Mod("floodgate")
public class ForgeMod {
	private static final Logger LOGGER = Logger.getLogger("floodgate");
    private FloodgatePlatform platform;
    Injector injector;

    @Inject
    public ForgeMod() {
    	ForgeInjector.injected = true;
        MinecraftForge.EVENT_BUS.register(this);
        System.out.println("confdir" + FMLPaths.CONFIGDIR.get().resolve("floodgate").toString());
        injector = Guice.createInjector(
                new ServerCommonModule(FMLPaths.CONFIGDIR.get().resolve("floodgate")),
                new ForgePlatformModule(this)
        );
        this.platform = injector.getInstance(FloodgatePlatform.class);
        
        platform.enable(new ForgeCommandModule(this));
        platform.enable(
        		new ForgeAddonModule(),
                new ForgeListenerModule(),
                new PluginMessageModule()
        );
        

    }
    @SubscribeEvent
    public void onServerAboutToStart(ServerAboutToStartEvent event) {
    	MinecraftServerHolder.set(event.getServer());
    	try {
    		ForgeInjector.injected = false;
			new ForgeInjector().inject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("failed inject floodgate");
		}
    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        long ctm = System.currentTimeMillis();

        long endCtm = System.currentTimeMillis();
        FloodgateLogger logger = injector.getInstance(FloodgateLogger.class);
        logger.translatedInfo("floodgate.core.finish", endCtm - ctm);
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        // サーバー停止時の処理
    }
	public static Logger getLogger() {
		// TODO Auto-generated method stub
		return LOGGER;
	}
}