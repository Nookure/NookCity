package com.nookure.proxy.utils.config.partial.messages;

import java.util.List;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class CommandMessagesPartial {
    public List<String> help =
            List.of(
                    "<red><b>Nookure Proxy Utils <white>| <yellow>Commands",
                    "<gray>/npu help <dark_gray>- <white>Show this help message",
                    "<gray>/npu reload <dark_gray>- <white>Reload the config");

    public String reload = "<green>Config reloaded!";
}
