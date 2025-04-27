package com.nookure.proxy.utils.config.partial;

import java.util.List;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class ServerKickPartial {
    public boolean checkReason = true;

    public List<String> reasons = List.of("Server closed", "Restarting", "Unknown");
}
