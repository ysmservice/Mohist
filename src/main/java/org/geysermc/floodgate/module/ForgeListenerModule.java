package org.geysermc.floodgate.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.ProvidesIntoSet;
import org.geysermc.floodgate.listener.ForgeEventListener;
import org.geysermc.floodgate.register.ListenerRegister;

public final class ForgeListenerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(new TypeLiteral<ListenerRegister<ForgeEventListener>>() {}).asEagerSingleton();
    }

    @Singleton
    @ProvidesIntoSet
    public ForgeEventListener fabricEventListener() {
        return new ForgeEventListener();
    }
}