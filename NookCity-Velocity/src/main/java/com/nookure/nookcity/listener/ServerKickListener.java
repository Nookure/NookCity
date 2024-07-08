package com.nookure.nookcity.listener;

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
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class ServerKickListener {
  @Inject
  private ConfigurationContainer<CoreConfig> config;
  @Inject
  private PlayerWrapperManager<Player, ProxyPlayerWrapper> manager;
  @Inject
  private ServerStatusManager serverStatusManager;
  @Inject
  private ConfigurationContainer<Messages> messages;

  @Subscribe
  public void onServerKick(KickedFromServerEvent event) {
    ProxyPlayerWrapper player = (ProxyPlayerWrapper) manager.getPlayerWrapper(event.getPlayer().getUniqueId()).orElse(null);

    if (player == null) {
      return;
    }

    if (!config.get().serverKick.checkReason) {
      event.setResult(redirectToLobbyOrKick());
      return;
    }

    String reason;

    if (event.getServerKickReason().isPresent()) {
      reason = PlainTextComponentSerializer.plainText().serialize(event.getServerKickReason().get());
    } else {
      reason = "Unknown";
    }

    if (config.get().serverKick.reasons.contains(reason)) {
      event.setResult(redirectToLobbyOrKick());
      return;
    }

    event.setResult(KickedFromServerEvent.DisconnectPlayer.create(event.getServerKickReason().orElse(null)));
  }

  private KickedFromServerEvent.ServerKickResult redirectToLobbyOrKick() {
    RegisteredServer server = serverStatusManager.getServerWithLessPlayers();
    if (server == null) {
      return KickedFromServerEvent.DisconnectPlayer
          .create(MiniMessage.miniMessage().deserialize(messages.get().lobby.errorWhileConnecting));
    } else {
      return KickedFromServerEvent.RedirectPlayer.create(server);
    }
  }
}
