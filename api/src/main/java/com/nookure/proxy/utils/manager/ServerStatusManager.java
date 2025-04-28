package com.nookure.proxy.utils.manager;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nookure.core.logger.Logger;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.util.*;
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
        // As this method is called in the loop of serverStatus map's values, the given server is always
        // registered, so we don't need to make a lookup for it.
        serverStatus.compute(server, /* We update this entry's value. */ (key, value) -> status);
    }

    @Nullable("If there are no servers online") public RegisteredServer getServerWithLessPlayers() {
        RegisteredServer lessPlayersServer = null;
        for (final Map.Entry<RegisteredServer, Boolean> entry : serverStatus.entrySet()) {
            if (!entry.getValue()) {
                // Server is not online.
                continue;
            }
            RegisteredServer server = entry.getKey();
            if (lessPlayersServer != null && lessPlayersServer.getPlayersConnected().size() < server.getPlayersConnected().size()) {
                // by now, this server keeps being the one with the lowest players amount.
                continue;
            }
            if (server.getPlayersConnected().size() < Integer.MAX_VALUE) {
                // it should be the server with the lowest players amount, by now.
                lessPlayersServer = server;
            }
            server = null;
        }
        return lessPlayersServer;
    }

    public List<RegisteredServer> getOnlineServers() {
        final List<RegisteredServer> onlineServers = new ArrayList<>(serverStatus.size());
        for (final Map.Entry<RegisteredServer, Boolean> entry : serverStatus.entrySet()) {
            if (!entry.getValue()) {
                continue;
            }
            onlineServers.add(entry.getKey());
        }
        return onlineServers;
    }

    public Set<RegisteredServer> getAllServers() {
        return serverStatus.keySet();
    }
}
