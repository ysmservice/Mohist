package org.geysermc.floodgate.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import lombok.RequiredArgsConstructor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.commands.CommandSource;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.logger.FloodgateLogger;
import org.geysermc.floodgate.inject.CommonPlatformInjector;
import org.geysermc.floodgate.inject.forge.ForgeInjector;
import org.geysermc.floodgate.listener.ForgeEventListener;
import org.geysermc.floodgate.logger.Log4jFloodgateLogger;
import org.apache.logging.log4j.LogManager;
import org.geysermc.floodgate.platform.command.CommandUtil;
import org.geysermc.floodgate.platform.listener.ListenerRegistration;
import org.geysermc.floodgate.platform.util.PlatformUtils;
import org.geysermc.floodgate.pluginmessage.ForgePluginMessageRegistration;
import org.geysermc.floodgate.pluginmessage.ForgePluginMessageUtils;
import org.geysermc.floodgate.pluginmessage.ForgeSkinApplier;
import org.geysermc.floodgate.pluginmessage.PluginMessageRegistration;
import org.geysermc.floodgate.util.ForgeCommandUtil;
import org.geysermc.floodgate.util.ForgePlatformUtils;
import org.geysermc.floodgate.skin.SkinApplier;
import org.geysermc.floodgate.util.LanguageManager;
import org.geysermc.floodgate.listener.ForgeEventRegistration;

@RequiredArgsConstructor
public final class ForgePlatformModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PlatformUtils.class).to(ForgePlatformUtils.class);
    }

    @Provides
    @Singleton
    public FloodgateLogger floodgateLogger(LanguageManager languageManager) {
        return new Log4jFloodgateLogger(LogManager.getLogger("floodgate"), languageManager);
    }

    @Provides
    @Singleton
    public CommandUtil commandUtil(
            FloodgateApi api,
            FloodgateLogger logger,
            LanguageManager languageManager) {
        return new ForgeCommandUtil(languageManager, api, logger);
    }

    @Provides
    @Singleton
    public ListenerRegistration<ForgeEventListener> listenerRegistration() {
        return new ForgeEventRegistration();
    }

    /*
    DebugAddon / PlatformInjector
     */

    @Provides
    @Singleton
    public CommonPlatformInjector platformInjector() {
        return ForgeInjector.getInstance(); // Forgeのインジェクターに変更
    }

    @Provides
    @Named("packetEncoder")
    public String packetEncoder() {
        return "encoder";
    }

    @Provides
    @Named("packetDecoder")
    public String packetDecoder() {
        return "decoder";
    }

    @Provides
    @Named("packetHandler")
    public String packetHandler() {
        return "packet_handler";
    }

    @Provides
    @Singleton
    public ForgePluginMessageUtils pluginMessageUtils() {
        return new ForgePluginMessageUtils(); // Forge向けに変更
    }

    @Provides
    @Named("implementationName")
    public String implementationName() {
        return "Forge";
    }

    @Provides
    @Singleton
    public PluginMessageRegistration pluginMessageRegister() {
        return new ForgePluginMessageRegistration(); // Forge向けに変更
    }

    @Provides
    @Singleton
    public SkinApplier skinApplier() {
        return new ForgeSkinApplier(); // Forge向けに変更
    }
}