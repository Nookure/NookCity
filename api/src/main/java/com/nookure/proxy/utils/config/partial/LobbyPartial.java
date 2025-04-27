package com.nookure.proxy.utils.config.partial;

import java.util.List;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class LobbyPartial {
    @Setting
    @Comment(
            """
            Configure the lobby servers that will be used to connect players.
            """)
    public List<String> lobbyServers = List.of("lobby1", "lobby2", "lobby3");
}
