package org.geysermc.floodgate.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.server.players.UserWhiteListEntry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraftforge.server.permission.PermissionAPI;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.floodgate.MinecraftServerHolder;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.logger.FloodgateLogger;
import org.geysermc.floodgate.module.ForgeCommandModule;
import org.geysermc.floodgate.platform.command.CommandUtil;
import org.geysermc.floodgate.player.UserAudience;

import java.util.*;

public final class ForgeCommandUtil extends CommandUtil {

    private MinecraftServer server;

    public ForgeCommandUtil(LanguageManager manager, MinecraftServer server, FloodgateApi api) {
        super(manager, api);
        this.server = server;
        ForgeCommandModule.cmdutil = this;
    }

    public ForgeCommandUtil(LanguageManager manager, FloodgateApi api, FloodgateLogger logger) {
		// TODO Auto-generated constructor stub
    	super(manager, api);
    	ForgeCommandModule.cmdutil = this;
	}

	@Override
    public UserAudience getUserAudience(final @NonNull Object sourceObj) {
        if (!(sourceObj instanceof Player source)) {
            throw new IllegalArgumentException();
        }
        if (source == null) {
            return new UserAudience.ConsoleAudience(source, this);
        }
        return new UserAudience.PlayerAudience(source.getUUID(),
                source.getName().getString(), "en_US", source, this, true);
    }

    @Override
    protected String getUsernameFromSource(@NonNull Object source) {
        return ((Player) source).getName().getString();
    }

    @Override
    protected UUID getUuidFromSource(@NonNull Object source) {
        return ((Player) source).getUUID();
    }

    @Override
    public Object getPlayerByUuid(@NonNull UUID uuid) {
        GameProfileCache cache = ServerLifecycleHooks.getCurrentServer().getProfileCache();
        return cache.get(uuid);
    }

    @Override
    public Object getPlayerByUsername(@NonNull String username) {
        // Try to get the UUID from the cache
        UUID uuid = null;
    	for (Map.Entry<UUID, String> entry: UsernameCache.getMap().entrySet()) {
            if (entry.getValue() == username) {
            	uuid = entry.getKey();
            }
    	}

        if (uuid != null) {
            return getPlayerByUuid(uuid);
        }

        // If not found in the cache, try online players
        return ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByName(username);
    }

    @Override
    protected Collection<?> getOnlinePlayers() {
        return ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers();
    }

    @Override
    public boolean hasPermission(Object source, String permission) {
            Player player = (Player)source instanceof Player ? (Player) source : null;

            return player.hasPermissions(permission.hashCode());
        
    }

    @Override
    public void sendMessage(Object target, String message) {
        Component textComponent = Component.Serializer.fromJsonLenient(message);
        ((CommandSource) target).sendSystemMessage(textComponent);
    }

    @Override
    public void kickPlayer(Object o, String message) {
    }

    @Override
    public boolean whitelistPlayer(UUID uuid, String username) {
        GameProfile profile = ServerLifecycleHooks.getCurrentServer().getProfileCache().get(uuid).get();
        ServerLifecycleHooks.getCurrentServer().getPlayerList().getWhiteList().add(new UserWhiteListEntry(profile));
        return true;
    }

    @Override
    public boolean removePlayerFromWhitelist(UUID uuid, String username) {
        GameProfile profile = ServerLifecycleHooks.getCurrentServer().getProfileCache().get(uuid).get();
        ServerLifecycleHooks.getCurrentServer().getPlayerList().getWhiteList().remove(profile);
        return true;
    }
}