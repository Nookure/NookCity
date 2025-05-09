package com.nookure.proxy.utils.module;

import com.google.inject.TypeLiteral;
import com.nookure.core.config.ConfigurationContainer;
import com.nookure.proxy.utils.cmd.core.VelocityCommandManager;
import com.nookure.proxy.utils.config.CoreConfig;
import com.nookure.proxy.utils.config.Messages;
import com.nookure.proxy.utils.config.MotdConfig;
import com.nookure.proxy.utils.config.serializer.VersionPartialSerializer;
import com.velocitypowered.api.proxy.ProxyServer;
import java.io.IOException;
import java.nio.file.Path;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

public class ProxyUtilsModule extends NookCoreModule {
    private final Path dataFolder;

    private static final String header =
            """
            This file is part of Nookure Proxy Utils.
            Nookure Proxy Utils is free software: you can redistribute it and/or modify
            it under the terms of the GNU General Public License as published by
            the Free Software Foundation, either version 3 of the License, or
            any later version.

            Support: https://discord.nookure.com
            """;

    public ProxyUtilsModule(Path dataFolder, ProxyServer proxyServer) {
        super(dataFolder, proxyServer);
        this.dataFolder = dataFolder;
    }

    @Override
    protected void configure() {
        super.configure();
        bind(VelocityCommandManager.class).asEagerSingleton();

        try {
            bind(new TypeLiteral<ConfigurationContainer<MotdConfig>>() {})
                    .toInstance(loadMotdConfig());

            bind(new TypeLiteral<ConfigurationContainer<CoreConfig>>() {})
                    .toInstance(loadConfig(CoreConfig.class, "config.yml"));

            bind(new TypeLiteral<ConfigurationContainer<Messages>>() {})
                    .toInstance(loadConfig(Messages.class, "messages.yml"));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private ConfigurationContainer<MotdConfig> loadMotdConfig() throws IOException {
        return ConfigurationContainer.load(
                dataFolder,
                MotdConfig.class,
                "motd.yml",
                header,
                builder -> {
                    builder.registerAll(TypeSerializerCollection.defaults());
                    builder.register(
                            MotdConfig.VersionPartial.class, VersionPartialSerializer.INSTANCE);
                });
    }

    private <T> ConfigurationContainer<T> loadConfig(Class<T> configClass, String fileName)
            throws IOException {
        return ConfigurationContainer.load(dataFolder, configClass, fileName, header);
    }
}
