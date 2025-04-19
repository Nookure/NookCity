package com.nookure.nookcity.listener;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.inject.Inject;
import com.nookure.core.config.ConfigurationContainer;
import com.nookure.core.manager.PlayerWrapperManager;
import com.nookure.nookcity.ProxyPlayerWrapper;
import com.nookure.nookcity.config.CoreConfig;
import com.nookure.nookcity.config.Messages;
import com.nookure.nookcity.manager.ServerStatusManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;

public class ServerKickListener {
    @Inject private ConfigurationContainer<CoreConfig> config;
    @Inject private PlayerWrapperManager<Player, ProxyPlayerWrapper> manager;
    @Inject private ServerStatusManager serverStatusManager;
    @Inject private ConfigurationContainer<Messages> messages;

    private final Cache<UUID, String> triedToConnect =
            Caffeine.newBuilder().expireAfterWrite(5, TimeUnit.SECONDS).build();

    @Subscribe
    public void onServerKick(KickedFromServerEvent event) {
        ProxyPlayerWrapper player =
                (ProxyPlayerWrapper)
                        manager.getPlayerWrapper(event.getPlayer().getUniqueId()).orElse(null);

        if (player == null) {
            return;
        }

        if (!config.get().serverKick.checkReason) {
            event.setResult(redirectToLobbyOrKick(event.getPlayer()));
            return;
        }

        String reason;

        if (event.getServerKickReason().isPresent()) {
            reason =
                    PlainTextComponentSerializer.plainText()
                            .serialize(event.getServerKickReason().get());
        } else {
            reason = "Unknown";
        }

        if (config.get().serverKick.reasons.contains(reason)) {
            event.setResult(redirectToLobbyOrKick(event.getPlayer()));
            return;
        }

        event.setResult(
                KickedFromServerEvent.DisconnectPlayer.create(
                        event.getServerKickReason().orElse(null)));
    }

    private KickedFromServerEvent.ServerKickResult redirectToLobbyOrKick(@NotNull Player player) {
        RegisteredServer server = serverStatusManager.getServerWithLessPlayers();

        if (server == null
                || Objects.equals(
                        triedToConnect.getIfPresent(player.getUniqueId()),
                        server.getServerInfo().getName())) {
            return KickedFromServerEvent.DisconnectPlayer.create(
                    MiniMessage.miniMessage()
                            .deserialize(messages.get().lobby.errorWhileConnecting));
        } else {
            triedToConnect.put(player.getUniqueId(), server.getServerInfo().getName());
            return KickedFromServerEvent.RedirectPlayer.create(server);
        }
    }
}
