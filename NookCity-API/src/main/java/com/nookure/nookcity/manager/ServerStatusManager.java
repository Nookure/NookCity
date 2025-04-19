package com.nookure.nookcity.manager;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nookure.core.logger.Logger;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;

@Singleton
public final class ServerStatusManager {
    private final HashMap<RegisteredServer, Boolean> serverStatus = new HashMap<>();

    @Inject private Logger logger;

    public void setServerStatus(RegisteredServer server, boolean status) {
        serverStatus.put(server, status);
    }

    public boolean getServerStatus(RegisteredServer server) {
        return serverStatus.get(server);
    }

    public void removeServerStatus(RegisteredServer server) {
        serverStatus.remove(server);
    }

    public void updateServerStatus(RegisteredServer server, boolean status) {
        if (serverStatus.containsKey(server)) {
            serverStatus.replace(server, status);
        } else {
            logger.severe(
                    new IllegalArgumentException(
                            "Server "
                                    + server.getServerInfo().getName()
                                    + " is not registered in the server status manager."));
        }
    }

    @Nullable("If there are no servers online") public RegisteredServer getServerWithLessPlayers() {
        return serverStatus.keySet().stream()
                .filter(serverStatus::get)
                .min(Comparator.comparingInt(server -> server.getPlayersConnected().size()))
                .orElse(null);
    }

    public Stream<RegisteredServer> getOnlineServers() {
        return serverStatus.entrySet().stream().filter(Map.Entry::getValue).map(Map.Entry::getKey);
    }

    public Stream<RegisteredServer> getAllServers() {
        return serverStatus.keySet().stream();
    }
}
