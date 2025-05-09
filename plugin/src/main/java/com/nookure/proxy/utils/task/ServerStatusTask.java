package com.nookure.proxy.utils.task;

import com.google.inject.Inject;
import com.nookure.core.logger.Logger;
import com.nookure.proxy.utils.manager.ServerStatusManager;
import com.velocitypowered.api.proxy.server.RegisteredServer;

public class ServerStatusTask implements Runnable {
    @Inject
    private ServerStatusManager serverStatusManager;
    @Inject
    private Logger logger;

    @Override
    public void run() {
        for (final RegisteredServer server : serverStatusManager.getAllServers()) {
            server.ping().whenComplete(
                    (ping, throwable) -> {
                        logger.debug(
                                "Server %s was pinged.",
                                server.getServerInfo().getName());
                        logger.debug(
                                "Status: %s",
                                (throwable == null)
                                        ? "Online"
                                        : "Offline");

                        if (ping.getPlayers().isPresent()) {
                            logger.debug(
                                    "PlayerCount: {}", ping.getPlayers()
                                            .get()
                                            .getOnline());
                            logger.debug(
                                    "MaxPlayers: {}", ping.getPlayers()
                                            .get()
                                            .getMax());
                        }

                        logger.debug("Version: {}", ping.getVersion());

                        serverStatusManager.updateServerStatus(server, throwable == null);
                    });
        }
    }
}
