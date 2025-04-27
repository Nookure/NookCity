package com.nookure.proxy.utils.config;

import com.nookure.proxy.utils.config.partial.messages.CommandMessagesPartial;
import com.nookure.proxy.utils.config.partial.messages.LobbyMessagesPartial;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class Messages {
    @Setting public LobbyMessagesPartial lobby = new LobbyMessagesPartial();

    @Setting public CommandMessagesPartial commands = new CommandMessagesPartial();
}
