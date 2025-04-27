package com.nookure.proxy.utils.module;

import com.google.inject.AbstractModule;
import com.nookure.core.annotation.PluginColoredName;
import com.nookure.core.annotation.PluginDataFolder;
import com.nookure.core.annotation.PluginDebug;
import com.nookure.core.annotation.PluginVersion;
import com.nookure.core.logger.Logger;
import com.nookure.core.logger.annotation.PluginAudience;
import com.nookure.core.logger.annotation.PluginLoggerColor;
import com.nookure.core.logger.annotation.PluginName;
import com.nookure.nookcity.Constants;
import com.velocitypowered.api.proxy.ProxyServer;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.format.NamedTextColor;

public class NookCoreModule extends AbstractModule {
    private final Path dataFolder;
    private final ProxyServer proxyServer;
    private final AtomicBoolean debug = new AtomicBoolean(false);

    public NookCoreModule(Path dataFolder, ProxyServer proxyServer) {
        this.dataFolder = dataFolder;
        this.proxyServer = proxyServer;

        if (System.getProperty("nookcity.debug") != null) {
            debug.set(Boolean.parseBoolean(System.getProperty("nookcity.debug")));
        }
    }

    @Override
    protected void configure() {
        bind(String.class)
                .annotatedWith(PluginColoredName.class)
                .toInstance("<b><green>Nook</green></b><white>City</white>");

        bind(Path.class).annotatedWith(PluginDataFolder.class).toInstance(dataFolder);

        bind(AtomicBoolean.class).annotatedWith(PluginDebug.class).toInstance(debug);

        bind(String.class).annotatedWith(PluginVersion.class).toInstance(Constants.VERSION);

        bind(Audience.class)
                .annotatedWith(PluginAudience.class)
                .toInstance(proxyServer.getConsoleCommandSource());

        bind(NamedTextColor.class)
                .annotatedWith(PluginLoggerColor.class)
                .toInstance(NamedTextColor.GREEN);

        bind(String.class).annotatedWith(PluginName.class).toInstance("NookCity");

        bind(Logger.class).asEagerSingleton();
    }
}
