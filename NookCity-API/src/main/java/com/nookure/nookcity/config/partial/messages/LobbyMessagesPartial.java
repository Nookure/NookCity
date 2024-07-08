package com.nookure.nookcity.config.partial.messages;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class LobbyMessagesPartial {
  public final String errorWhileConnecting = """
      {center}<red><b>◆ ERROR ◆</b>
      
      {center}<gray>There was an error while connecting to the server.
      {center}<gray>Please contact an administrator if this issue persists.
      """;
}
