package com.nookure.nookcity.listener;

import com.google.inject.Inject;
import com.nookure.core.config.ConfigurationContainer;
import com.nookure.nookcity.config.MotdConfig;
import com.nookure.nookcity.config.MotdMode;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.InboundConnection;
import com.velocitypowered.api.proxy.server.ServerPing;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

import static java.util.Objects.requireNonNull;

public class ServerPingListener {
  private final static Random RANDOM = new Random();

  @Inject
  private ConfigurationContainer<MotdConfig> motdConfig;

  @Subscribe
  public void onServerPing(ProxyPingEvent event) {
    if (motdConfig.get().mode == MotdMode.STATIC) {
      if (motdConfig.get().motds.isEmpty()) return;

      event.setPing(getServerPing(motdConfig.get().motds.get(0), event.getPing().asBuilder()));
      return;
    }

    MotdConfig.MotdPartial partial = getRandomMotd(event.getConnection());

    if (partial == null) return;

    event.setPing(getServerPing(partial, event.getPing().asBuilder()));
  }

  public ServerPing getServerPing(@NotNull MotdConfig.MotdPartial motdPartial, @NotNull ServerPing.Builder builder) {
    requireNonNull(motdPartial, "MOTD partial cannot be null");
    requireNonNull(builder, "ServerPing builder cannot be null");

    builder.description(MiniMessage.miniMessage().deserialize(motdPartial.motd));

    if (motdPartial.version != null) {
      if (motdPartial.version.protocol == 0 || motdPartial.version.friendlyName == null) {
        return builder.build();
      }

      builder.version(new ServerPing.Version(motdPartial.version.protocol, motdPartial.version.friendlyName));
    }

    return builder.build();
  }

  public MotdConfig.MotdPartial getRandomMotd(@NotNull InboundConnection connection) {
    List<MotdConfig.MotdPartial> motds = motdConfig.get().motds;

    if (motds.isEmpty()) return null;

    MotdConfig.MotdPartial motdPartial = motds.get(RANDOM.nextInt(motds.size()));

    if (canUseMotd(motdPartial, connection)) {
      return motdPartial;
    } else {
      return getRandomMotd(connection);
    }
  }

  public boolean canUseMotd(@NotNull MotdConfig.MotdPartial motdPartial, @NotNull InboundConnection connection) {
    int protocolVersion = connection.getProtocolVersion().getProtocol();

    if (protocolVersion == -1) {
      return true;
    }

    if (motdPartial.condition == null || motdPartial.condition.isEmpty()) {
      return true;
    }

    if (motdPartial.condition.startsWith("<=")) {
      return protocolVersion <= Integer.parseInt(motdPartial.condition.substring(2));
    }

    if (motdPartial.condition.startsWith(">=")) {
      return protocolVersion >= Integer.parseInt(motdPartial.condition.substring(2));
    }

    if (motdPartial.condition.startsWith("<")) {
      return protocolVersion < Integer.parseInt(motdPartial.condition.substring(1));
    }

    if (motdPartial.condition.startsWith(">")) {
      return protocolVersion > Integer.parseInt(motdPartial.condition.substring(1));
    }

    if (motdPartial.condition.startsWith("=")) {
      return protocolVersion == Integer.parseInt(motdPartial.condition.substring(1));
    }

    return true;
  }
}
