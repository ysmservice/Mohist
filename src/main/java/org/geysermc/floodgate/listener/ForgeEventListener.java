package org.geysermc.floodgate.listener;

import com.google.inject.Inject;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.logger.FloodgateLogger;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.geysermc.floodgate.util.LanguageManager;

@Mod.EventBusSubscriber(modid = "floodgate")
public final class ForgeEventListener {
    @Inject private FloodgateApi api;
    @Inject private FloodgateLogger logger;
    @Inject private LanguageManager languageManager;

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        FloodgatePlayer player = api.getPlayer(event.getEntity().getUUID());
        if (player != null) {
            logger.translatedInfo(
                    "floodgate.ingame.login_name",
                    player.getCorrectUsername(), player.getCorrectUniqueId()
            );
            languageManager.loadLocale(player.getLanguageCode());
        }
    }
}
