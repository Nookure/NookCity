package com.nookure.nookcity.config.partial;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.List;

@ConfigSerializable
public class LobbyPartial {
  @Setting
  @Comment("""
      Configure the lobby servers that will be used to connect players.
      """)
  public final List<String> lobbyServers = List.of("lobby1", "lobby2", "lobby3");
}
