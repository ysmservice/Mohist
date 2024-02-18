package org.geysermc.floodgate.pluginmessage;

import net.minecraftforge.network.SimpleChannel;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.Channel.VersionTest;
import net.minecraft.resources.ResourceLocation;
import org.geysermc.floodgate.pluginmessage.ForgePluginMessageUtils.PluginMessagePacket;
import net.minecraft.network.FriendlyByteBuf;
import java.util.function.Supplier;

public class ForgePluginMessageRegistration implements PluginMessageRegistration {
    private static final int PROTOCOL_VERSION = 1; // プロトコルバージョン
    private static SimpleChannel CHANNEL;
    public static PluginMessageChannel channel;

    @Override
    public void register(PluginMessageChannel channel) {
        // PluginMessageChannelの登録処理を行う場合はここに記述する
    	this.channel = channel;
        CHANNEL = net.minecraftforge.network.ChannelBuilder.named(new ResourceLocation(channel.getIdentifier()))
                .clientAcceptedVersions(VersionTest.ACCEPT_MISSING)
                .serverAcceptedVersions(VersionTest.ACCEPT_MISSING)
                .networkProtocolVersion(PROTOCOL_VERSION)
                .simpleChannel();
        
        CHANNEL.messageBuilder(PluginMessagePacket.class, 0)
        .encoder(PluginMessagePacket::encode)
        .decoder(PluginMessagePacket::decode)
        .consumerMainThread(ForgePluginMessageRegistration::handle)
        .add();
    }


    private static void handle(PluginMessagePacket packet, CustomPayloadEvent.Context ctx) {
        // FriendlyByteBufからデータを読み取る
        FriendlyByteBuf buf = packet.get();
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        // プラグインメッセージのデータを処理するロジックを記述する
        channel.handleServerCall(bytes, ctx.getSender().getUUID(), ctx.getSender().getGameProfile().getName());
    }
}