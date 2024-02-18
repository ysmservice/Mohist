package org.geysermc.floodgate.module;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.bukkit.command.CommandSender;
import cloud.commandframework.CommandManager;
import cloud.commandframework.CommandTree;
import cloud.commandframework.LockableCommandManager;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.internal.CommandRegistrationHandler;
import cloud.commandframework.util.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.minecraft.commands.CommandSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Function;

import org.geysermc.floodgate.ForgeMod;
import org.geysermc.floodgate.MinecraftServerHolder;
import org.geysermc.floodgate.platform.command.CommandUtil;
import org.geysermc.floodgate.player.FloodgateCommandPreprocessor;
import org.geysermc.floodgate.player.UserAudience;
import org.geysermc.floodgate.util.ForgeCommandUtil;


@Mod.EventBusSubscriber(modid = "floodgate", bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ForgeCommandModule extends CommandModule {
    private CommandDispatcher<CommandSource> dispatcher1 = new CommandDispatcher<>();
    public static ForgeCommandUtil cmdutil = new ForgeCommandUtil(null, MinecraftServerHolder.get(), null);
    private final ForgeMod plugin;
    
    @Override
    protected void configure() {
        super.configure();
    }
    
    @Provides
    @Singleton
    @SneakyThrows
    public CommandManager<UserAudience> commandManager(CommandUtil commandUtil) {
        CommandManager<UserAudience> commandManager = (CommandManager<UserAudience>) new CommandManagerImpl(
        	    plugin,
        	    CommandExecutionCoordinator.simpleCoordinator(),
        	    commandUtil::getUserAudience,
        	    audience -> (CommandSender) audience.source()
        	);
        commandManager.registerCommandPreProcessor(new FloodgateCommandPreprocessor<>(commandUtil));
        return commandManager;
    }
    
    public ForgeCommandModule(ForgeMod f) {
    	plugin = f;
        MinecraftForge.EVENT_BUS.register(this);
    }

    private final CommandDispatcher<CommandSource> dispatcher = new CommandDispatcher<>();

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        // Forge のコマンドシステムにコマンドを登録する
    	FloodgateCommandPreprocessor<CommandUtil> floodgateCommandPreprocessor = new FloodgateCommandPreprocessor<>((CommandUtil) cmdutil);

    	// Create a LiteralArgumentBuilder and configure it as needed
    	//LiteralArgumentBuilder<CommandSource> literalArgumentBuilder = LiteralArgumentBuilder
    	//        .<CommandSource>literal("your_command_name_here")
    	//        .requires(source -> /* Your permission check here */)
    	//        .executes(context -> {
    	            // Your command logic here
    	//            return 1; // Return any value based on your command logic
    	//       });

    	// Register the FloodgateCommandPreprocessor as a child of the LiteralArgumentBuilder
    	//literalArgumentBuilder.preprocessor(floodgateCommandPreprocessor);

    	// Register the LiteralArgumentBuilder with the CommandDispatcher
    	//dispatcher1.register(literalArgumentBuilder);
    }
    

}
