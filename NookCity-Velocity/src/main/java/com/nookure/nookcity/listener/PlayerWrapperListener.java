package com.nookure.nookcity.listener;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.nookure.core.logger.Logger;
import com.nookure.core.manager.PlayerWrapperManager;
import com.nookure.nookcity.ProxyPlayerWrapper;
import com.nookure.nookcity.VelocityProxyPlayerWrapper;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;

public class PlayerWrapperListener {
  @Inject
  private PlayerWrapperManager<Player, ProxyPlayerWrapper> wrapper;
  @Inject
  private Injector injector;
  @Inject
  private Logger logger;

  @Subscribe
  public void onPlayerJoin(PostLoginEvent event) {
    Player player = event.getPlayer();

    wrapper.addPlayerWrapper(player, new VelocityProxyPlayerWrapper(player, injector));
    logger.debug("PlayerWrapper added for player: " + player.getUsername());
  }

  @Subscribe
  public void onPlayerLeave(DisconnectEvent event) {
    Player player = event.getPlayer();

    if (wrapper.getPlayerWrapper(player).isEmpty()) {
      logger.debug("PlayerWrapper not found for player: " + player.getUsername());
      return;
    }

    wrapper.removePlayerWrapper(player);
    logger.debug("PlayerWrapper removed for player: " + player.getUsername());
  }
}
