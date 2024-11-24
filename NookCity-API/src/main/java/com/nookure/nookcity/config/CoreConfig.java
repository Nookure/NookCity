package com.nookure.nookcity.config;

import com.nookure.nookcity.config.partial.LobbyPartial;
import com.nookure.nookcity.config.partial.ModulesPartial;
import com.nookure.nookcity.config.partial.ServerKickPartial;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class CoreConfig {
  public ModulesPartial modules = new ModulesPartial();
  public LobbyPartial lobby = new LobbyPartial();
  public ServerKickPartial serverKick = new ServerKickPartial();
}
