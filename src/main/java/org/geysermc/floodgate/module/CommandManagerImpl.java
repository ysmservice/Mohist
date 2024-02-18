package org.geysermc.floodgate.module;

import cloud.commandframework.CommandManager;
import cloud.commandframework.CommandTree;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.execution.CommandResult;
import cloud.commandframework.internal.CommandRegistrationHandler;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.meta.SimpleCommandMeta;
import lombok.NonNull;

import org.bukkit.command.CommandSender;
import org.geysermc.floodgate.ForgeMod;
import org.geysermc.floodgate.player.UserAudience;

import java.util.function.Function;

public class CommandManagerImpl extends CommandManager<UserAudience> {
    public static ForgePluginRegistrationHandler rh = new ForgePluginRegistrationHandler();
    public CommandManagerImpl(
			@NonNull Function<@NonNull CommandTree<UserAudience>, @NonNull CommandExecutionCoordinator<UserAudience>> commandExecutionCoordinator,
			@NonNull CommandRegistrationHandler commandRegistrationHandler) {
    	super(commandExecutionCoordinator, commandRegistrationHandler);
    }


	public CommandManagerImpl(ForgeMod owningPlugin, Function<CommandTree<UserAudience>, CommandExecutionCoordinator<UserAudience>> commandExecutionCoordinator, Function<CommandSender, UserAudience> commandSenderMapper, Function<UserAudience, CommandSender> backwardsCommandSenderMapper) throws Exception {
		// TODO Auto-generated constructor stub
		super(commandExecutionCoordinator,rh);
		rh.im = this;
	}


	@Override
    public CommandMeta createDefaultCommandMeta() {
        return ForgeCommandMetaBuilder.builder().withDescription("").build();
    }

    @Override
    public boolean hasPermission(UserAudience audience, String permission) {
        // Implement permission checking logic for Forge audience
        // You may need to use audience.getSource() to get the CommandSource in Forge
        return false;
    }

    // You might need to override other methods as needed

    // Note: This is a basic structure, and you need to adapt it based on the actual
    // implementation details provided by the Floodgate library for Forge.
}
