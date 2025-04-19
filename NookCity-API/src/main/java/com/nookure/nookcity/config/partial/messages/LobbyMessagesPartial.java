package com.nookure.nookcity.config.partial.messages;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class LobbyMessagesPartial {
    public String errorWhileConnecting =
            """
            <red><b>◆ ERROR ◆</b>

            gray>There was an error while connecting to the server.
            <gray>Please contact an administrator if this issue persists.
            """;
}
