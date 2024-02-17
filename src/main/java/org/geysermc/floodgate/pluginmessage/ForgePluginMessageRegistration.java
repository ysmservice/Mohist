package org.geysermc.floodgate.pluginmessage;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.network.CustomPayloadEvent.Context;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.SimpleChannel;
import net.minecraftforge.network.Channel.VersionTest;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.geysermc.floodgate.api.logger.FloodgateLogger;
import org.geysermc.floodgate.pluginmessage.ForgePluginMessageUtils.PluginMessagePacket;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = "floodgate")
public class ForgePluginMessageRegistration implements PluginMessageRegistration {
    private final SimpleChannel channel;

    public ForgePluginMessageRegistration() {
        this.channel = ChannelBuilder
                .named(new ResourceLocation("floodgate", "floodgate"))
                .clientAcceptedVersions(VersionTest.ACCEPT_MISSING)
                .serverAcceptedVersions(VersionTest.ACCEPT_MISSING)
                .networkProtocolVersion(1)
                .simpleChannel();
    }

    @Override
    public void register(PluginMessageChannel channel) {
        this.channel.messageBuilder(PluginMessagePacket.class, 0)
                .consumerNetworkThread(this::handleServerCall)
                .add();
    }

    private void handleServerCall(PluginMessagePacket packet, Context contextSupplier) {
        Context context = contextSupplier;
        ServerPlayer player = context.getSender();

        if (player != null) {
            FriendlyByteBuf data = packet.get();
            channel.send(new PluginMessagePacket(new ResourceLocation("floodgate", "floodgate"), data), PacketDistributor.PLAYER.with(player));
        }
        context.setPacketHandled(true);
    }
}