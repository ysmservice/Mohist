package org.geysermc.floodgate.pluginmessage;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.Channel.VersionTest;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;
import org.geysermc.floodgate.MinecraftServerHolder;
import org.geysermc.floodgate.platform.pluginmessage.PluginMessageUtils;

import java.util.UUID;
import java.util.function.Supplier;

public class ForgePluginMessageUtils extends PluginMessageUtils {

    @Override
    public boolean sendMessage(UUID uuid, String channel, byte[] data) {
        try {
            ServerPlayer player = MinecraftServerHolder.get().getPlayerList().getPlayer(uuid);
            ResourceLocation resource = new ResourceLocation(channel); // automatically splits over the :
            FriendlyByteBuf dataBuffer = new FriendlyByteBuf(Unpooled.wrappedBuffer(data));
            ChannelBuilder channelBuilder = ChannelBuilder.named(resource);
            channelBuilder.clientAcceptedVersions(VersionTest.ACCEPT_MISSING);
            channelBuilder.serverAcceptedVersions(VersionTest.ACCEPT_MISSING);
            channelBuilder.networkProtocolVersion(1);
            SimpleChannel channe = channelBuilder.simpleChannel();
            // メッセージを送信する
            channe.send( new PluginMessagePacket(resource, dataBuffer), PacketDistributor.PLAYER.with(player));
            
            // 使用後にバッファを解放する
            dataBuffer.release();
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // Packetの定義
    public static class PluginMessagePacket implements Supplier<FriendlyByteBuf> {
        private final ResourceLocation resource;
        private final FriendlyByteBuf dataBuffer;

        public PluginMessagePacket(ResourceLocation resource, FriendlyByteBuf dataBuffer) {
            this.resource = resource;
            this.dataBuffer = dataBuffer;
        }

        @Override
        public FriendlyByteBuf get() {
            FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
            buffer.writeResourceLocation(resource);
            buffer.writeBytes(dataBuffer);
            return buffer;
        }
        
        public static void encode(PluginMessagePacket packet, FriendlyByteBuf buf) {
            buf.writeResourceLocation(packet.resource);
            buf.writeBytes(packet.dataBuffer);
        }

        public static PluginMessagePacket decode(FriendlyByteBuf buf) {
            ResourceLocation resource = buf.readResourceLocation();
            FriendlyByteBuf dataBuffer = new FriendlyByteBuf(Unpooled.buffer());
            buf.readBytes(dataBuffer, buf.readableBytes());
            return new PluginMessagePacket(resource, dataBuffer);
        }
    }
}
