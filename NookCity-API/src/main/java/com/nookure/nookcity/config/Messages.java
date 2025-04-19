package com.nookure.nookcity.config;

import com.nookure.nookcity.config.partial.messages.CommandMessagesPartial;
import com.nookure.nookcity.config.partial.messages.LobbyMessagesPartial;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class Messages {
  @Setting
  public LobbyMessagesPartial lobby = new LobbyMessagesPartial();

    @Setting
    public CommandMessagesPartial commands = new CommandMessagesPartial();
}
