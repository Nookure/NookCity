package com.nookure.nookcity.config.partial;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class ModulesPartial {
    @Setting
    @Comment(
            """
            Enable or disable the MOTD module.
            """)
    public boolean motd = true;

    @Setting
    @Comment(
            """
            Enable or disable the lobby module.
            """)
    public boolean lobby = true;

    @Setting
    @Comment(
            """
            Enable or disable the server kick redirect module.
            """)
    public boolean serverKickRedirect = true;
}
