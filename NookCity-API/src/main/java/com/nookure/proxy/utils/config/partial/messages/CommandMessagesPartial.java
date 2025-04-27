package com.nookure.proxy.utils.config.partial.messages;

import java.util.List;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class CommandMessagesPartial {
    public List<String> help =
            List.of(
                    "<red><b>NookCity <white>| <yellow>Commands",
                    "<gray>/nookcity help <dark_gray>- <white>Show this help message",
                    "<gray>/nookcity reload <dark_gray>- <white>Reload the config");

    public String reload = "<green>Config reloaded!";
}
