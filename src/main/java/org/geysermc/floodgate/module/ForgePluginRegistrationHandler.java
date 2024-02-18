package org.geysermc.floodgate.module;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.internal.CommandRegistrationHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import cloud.commandframework.context.CommandContext;
import net.minecraft.world.entity.player.*;

import java.util.HashMap;
import java.util.Map;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

@Mod.EventBusSubscriber(modid = "floodgate", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgePluginRegistrationHandler implements CommandRegistrationHandler {
    private final Map<String, Command<?>> registeredCommands = new HashMap<>();
    private final CommandArgument<?, ?>[] camds = new CommandArgument<?, ?>[1000];
    public static CommandManagerImpl im = null;

    public ForgePluginRegistrationHandler(CommandManagerImpl i) {
    	im = i;
    }

    public ForgePluginRegistrationHandler() {
		// TODO Auto-generated constructor stub
	}

	@SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        
        // Register commands stored in camds array
        for (CommandArgument<?, ?> commandArgument : camds) {
            if (commandArgument != null) {
                Command<?> command = (Command<?>) registeredCommands.get(commandArgument.getName());
                if (command != null) {
                    dispatcher.register(Commands.literal(commandArgument.getName())
                            .executes(context -> {
                                command.getCommandExecutionHandler().execute(this.convertToCloudContext(context));
                                return 1;
                            }));
                }
            }
        }
    }
    
    public static cloud.commandframework.context.CommandContext convertToCloudContext(com.mojang.brigadier.context.CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        // 例: MinecraftForgeのCommandContextから必要な情報を抽出
        CommandSourceStack sourceStack = context.getSource();
        Player player = sourceStack.getPlayerOrException();

        // 例: CloudのCommandContextを構築
        cloud.commandframework.context.CommandContext cloudContext = new cloud.commandframework.context.CommandContext(player, im);

        return cloudContext;
    }
    
    
    
   
    @Override
    public final boolean registerCommand(final Command<?> command) {
        // We only care about the root command argument
        CommandArgument<?, ?> commandArgument = command.getArguments().get(0);
        if (registeredCommands.containsKey(commandArgument.getName())) {
            return false;
        }
        registeredCommands.put(commandArgument.getName(), command);
        return true;
    }
}