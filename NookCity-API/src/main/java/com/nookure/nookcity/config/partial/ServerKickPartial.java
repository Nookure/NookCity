package com.nookure.nookcity.config.partial;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class ServerKickPartial {
  public boolean checkReason = true;

  public List<String> reasons = List.of(
      "Server closed",
      "Restarting",
      "Unknown"
  );
}
