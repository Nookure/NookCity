package com.nookure.proxy.utils.config;

import com.nookure.proxy.utils.config.partial.LobbyPartial;
import com.nookure.proxy.utils.config.partial.ModulesPartial;
import com.nookure.proxy.utils.config.partial.ServerKickPartial;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class CoreConfig {
    public ModulesPartial modules = new ModulesPartial();
    public LobbyPartial lobby = new LobbyPartial();
    public ServerKickPartial serverKick = new ServerKickPartial();
}
