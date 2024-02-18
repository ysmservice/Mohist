package org.geysermc.floodgate.pluginmessage;

import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import org.geysermc.floodgate.MinecraftServerHolder;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.geysermc.floodgate.skin.SkinApplier;
import org.geysermc.floodgate.api.event.skin.SkinApplyEvent.SkinData;

import java.util.Collections;

public final class ForgeSkinApplier implements SkinApplier {

    @Override
    public void applySkin(FloodgatePlayer floodgatePlayer, SkinData skinData) {
        MinecraftServer server = MinecraftServerHolder.get();

        server.execute(() -> {
            ServerPlayer bedrockPlayer = server.getPlayerList().getPlayer(floodgatePlayer.getCorrectUniqueId());
            if (bedrockPlayer == null) {
                // Disconnected probably?
                return;
            }

            // Apply the new skin internally
            PropertyMap properties = bedrockPlayer.getGameProfile().getProperties();

            properties.removeAll("textures");
            properties.put("textures", new Property("textures", skinData.value(), skinData.signature()));

            // Skin is applied - now it's time to refresh the player for everyone.
            for (ServerPlayer otherPlayer : server.getPlayerList().getPlayers()) {
                if (otherPlayer == bedrockPlayer) {
                    continue;
                }

                // Send player info packet to remove the player
                Packet<?> removePacket = new ClientboundPlayerInfoRemovePacket(Collections.singletonList(bedrockPlayer.getUUID()));
                otherPlayer.connection.send(removePacket);

                // Send player info packet to add the player with new skin
                Packet<?> addPacket = ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(Collections.singletonList(bedrockPlayer));
                otherPlayer.connection.send(addPacket);
            }
        });
    }
}
