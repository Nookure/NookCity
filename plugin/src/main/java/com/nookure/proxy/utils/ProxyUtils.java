package com.nookure.proxy.utils;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.nookure.core.config.ConfigurationContainer;
import com.nookure.proxy.utils.cmd.LobbyCommand;
import com.nookure.proxy.utils.cmd.NPUCommand;
import com.nookure.proxy.utils.cmd.core.VelocityCommandManager;
import com.nookure.proxy.utils.config.CoreConfig;
import com.nookure.proxy.utils.listener.PlayerWrapperListener;
import com.nookure.proxy.utils.listener.ServerKickListener;
import com.nookure.proxy.utils.listener.ServerPingListener;
import com.nookure.proxy.utils.manager.ServerStatusManager;
import com.nookure.proxy.utils.module.ProxyUtilsModule;
import com.nookure.proxy.utils.task.ServerStatusTask;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;
import java.util.stream.Stream;
import org.slf4j.Logger;

@Plugin(
        name = "nookure-proxy-utils",
        version = Constants.VERSION,
        description = "A Velocity Proxy Core plugin",
        authors = {"Nookure", "Lunna5", "Angelillo15"},
        url = "https://nookure.com",
        id = "nookure-proxy-utils")
public class ProxyUtils {
    @Inject private ProxyServer server;
    @Inject private Logger slf4jLogger;
    @Inject @DataDirectory private Path dataDirectory;
    @Inject private Injector injector;

    private com.nookure.core.logger.Logger logger;
    private VelocityCommandManager commandManager;
    private ConfigurationContainer<CoreConfig> coreConfig;
    private ServerStatusManager serverStatusManager;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.injector = injector.createChildInjector(new ProxyUtilsModule(dataDirectory, server));
        logger = injector.getInstance(com.nookure.core.logger.Logger.class);
        commandManager = injector.getInstance(VelocityCommandManager.class);
        coreConfig = injector.getProvider(new Key<ConfigurationContainer<CoreConfig>>() {}).get();
        serverStatusManager = injector.getInstance(ServerStatusManager.class);

        loadListeners();
        loadCommands();
        loadLobbyServers();
        loadTasks();

        logger.info("Nookure Proxy Utils has been initialized!");
        logger.debug("Debug mode is enabled!");
    }

    public void loadCommands() {
        if (coreConfig.get().modules.lobby) {
            commandManager.registerCommand(LobbyCommand.class);
        }

        commandManager.registerCommand(NPUCommand.class);
    }

    public void loadListeners() {
        Stream.of(PlayerWrapperListener.class).forEach(this::registerListener);

        if (coreConfig.get().modules.motd) {
            registerListener(ServerPingListener.class);
        }

        if (coreConfig.get().modules.serverKickRedirect) {
            registerListener(ServerKickListener.class);
        }
    }

    public void loadLobbyServers() {
        coreConfig
                .get()
                .lobby
                .lobbyServers
                .forEach(
                        serverName -> {
                            Optional<RegisteredServer> s = server.getServer(serverName);

                            if (s.isPresent()) {
                                logger.info("Loaded lobby server: " + serverName);
                                serverStatusManager.setServerStatus(s.get(), false);
                            } else {
                                logger.warning("Failed to load lobby server: " + serverName);
                            }
                        });
    }

    private void loadTasks() {
        server.getScheduler()
                .buildTask(this, injector.getInstance(ServerStatusTask.class))
                .repeat(Duration.ofSeconds(20))
                .schedule();
    }

    private void registerListener(Class<?> clazz) {
        server.getEventManager().register(this, injector.getInstance(clazz));
        logger.debug("Registered listener: " + clazz.getSimpleName());
    }
}
