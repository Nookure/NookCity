package com.nookure.nookcity.config.serializer;

import com.nookure.nookcity.config.MotdConfig;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class VersionPartialSerializer implements TypeSerializer<MotdConfig.VersionPartial> {
  public final static VersionPartialSerializer INSTANCE = new VersionPartialSerializer();

  private VersionPartialSerializer() {
  }

  @Override
  public MotdConfig.VersionPartial deserialize(Type type, ConfigurationNode node) throws SerializationException {
    if (node.node("protocol").getInt() == 0 || node.node("friendly-name").getString() == null) {
      return null;
    }

    return new MotdConfig.VersionPartial(
        node.node("protocol").getInt(),
        node.node("friendly-name").getString()
    );
  }

  @Override
  public void serialize(Type type, MotdConfig.@Nullable VersionPartial obj, ConfigurationNode node) throws SerializationException {
    if (obj == null) {
      return;
    }

    if (obj.protocol == 0 || obj.friendlyName == null) {
      return;
    }

    node.node("protocol").set(obj.protocol);
    node.node("friendly-name").set(obj.friendlyName);
  }
}
