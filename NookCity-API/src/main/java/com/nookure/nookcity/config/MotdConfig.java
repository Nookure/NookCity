package com.nookure.nookcity.config;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.List;

@ConfigSerializable
public class MotdConfig {
  @Setting
  @Comment("""
      The type of MOTD to use.
      STATIC: Use a static MOTD.
      RANDOM: Use a random MOTD from the list.
      """)
  public MotdMode mode = MotdMode.RANDOM;

  @Setting
  @Comment("""
      The list of MOTDs to use when the mode is set to RANDOM.
      The condition is a version range that the MOTD will be displayed for.
      """)
  public List<MotdPartial> motds = List.of(
      new MotdPartial(null),
      new MotdPartial("""
          <red>         <bold>Nookure<white>Network <dark_gray>◆ <gray>|</bold> <white>1.18.x - 1.21.x
          <red>              Please update to 1.18.x
          """, "<757", new VersionPartial(-1, "Nookure Network 🚫"))
  );

  @ConfigSerializable
  public static class MotdPartial {
    public MotdPartial() {
    }

    public MotdPartial(@Nullable VersionPartial partial) {
      this.version = partial;
    }

    public MotdPartial(String motd, String condition, @Nullable VersionPartial partial) {
      this.motd = motd;
      this.condition = condition;
      this.version = partial;
    }

    public MotdPartial(String motd, String condition) {
      this(motd, condition, null);
    }

    @Setting
    public String motd = """
        <red>         <bold>Nookure<white>Network <dark_gray>◆ <gray>|</bold> <white>1.18.x - 1.21.x
        <red>              discord.nookure.com
        """;

    @Setting
    public String condition = ">=757";

    @Setting
    public VersionPartial version = new VersionPartial();
  }

  @ConfigSerializable
  public static class VersionPartial {
    public VersionPartial() {
      this.protocol = 0;
      this.friendlyName = null;
    }

    public VersionPartial(int protocol, String friendlyName) {
      this.protocol = protocol;
      this.friendlyName = friendlyName;
    }

    @Setting
    @Comment("If sets to 0, it will be ignored.")
    public final int protocol;

    @Setting
    @Comment("If sets to null, it will be ignored.")
    public final String friendlyName;
  }
}
