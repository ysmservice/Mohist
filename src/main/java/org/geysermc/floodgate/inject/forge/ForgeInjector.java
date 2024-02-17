package org.geysermc.floodgate.inject.forge;

import io.netty.channel.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.geysermc.floodgate.inject.CommonPlatformInjector;

@RequiredArgsConstructor
@Mod.EventBusSubscriber(modid = "floodgate", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ForgeInjector extends CommonPlatformInjector {
    private static ForgeInjector instance;

    @Getter
    private final boolean injected = true;
    
    public boolean isInjected() {
    	return injected;
    }
    

    @Override
    public boolean inject() throws Exception {
        return true;
    }

    public void injectClient(ChannelFuture future) {
        future.channel().pipeline().addFirst("floodgate-init", new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                super.channelRead(ctx, msg);

                Channel channel = (Channel) msg;
                channel.pipeline().addLast(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) {
                        injectAddonsCall(channel, false);
                        addInjectedClient(channel);
                        channel.closeFuture().addListener(listener -> {
                            channelClosedCall(channel);
                            removeInjectedClient(channel);
                        });
                    }
                });
            }
        });
    }

    @Override
    public boolean removeInjection() throws Exception {
        return true;
    }

    public static ForgeInjector getInstance() {
        return instance;
    }

    public static void setInstance(ForgeInjector injector) {
        instance = injector;
    }

    // Forgeの場合、NetworkEvent.ClientConnectedToServerEventを利用してクライアント接続時の処理を行います
    // モッドの初期化時にイベントを登録する
    @Mod.EventBusSubscriber(modid = "floodgate", bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class EventHandler {
        public EventHandler() {
            IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
            modEventBus.addListener(this::onPlayerLoggedIn);
        }

        private void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
            // Forgeのネットワーキングにおいて、クライアントがサーバーに接続したときの処理
            // event.getNetworkManager() を利用してネットワークマネージャーを取得し、適切な操作を行う
        }
    }
}