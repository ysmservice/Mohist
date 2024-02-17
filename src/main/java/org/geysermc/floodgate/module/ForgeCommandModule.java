package org.geysermc.floodgate.module;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.geysermc.floodgate.platform.command.CommandUtil;
import org.geysermc.floodgate.player.FloodgateCommandPreprocessor;
import org.geysermc.floodgate.player.UserAudience;
import org.geysermc.floodgate.util.ForgeCommandUtil;

@Mod.EventBusSubscriber(modid = "floodgate", bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ForgeCommandModule extends CommandModule {
    private final CommandDispatcher<CommandSource> dispatcher = new CommandDispatcher<>();

    public ForgeCommandModule() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        // Forgeのコマンドシステムにコマンドを登録する
        //dispatcher.register(new FloodgateCommandPreprocessor<>((CommandUtil)new ForgeCommandUtil(null, null,null)));
    }

    // CommandManagerを提供するメソッドは削除され、Forgeのコマンドシステムに直接コマンドを登録する
}
