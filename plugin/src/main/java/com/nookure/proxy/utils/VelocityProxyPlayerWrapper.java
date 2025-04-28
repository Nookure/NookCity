package com.nookure.proxy.utils;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.nookure.core.PlayerWrapperBase;
import com.nookure.core.config.ConfigurationContainer;
import com.nookure.proxy.utils.config.Messages;
import com.nookure.proxy.utils.manager.ServerStatusManager;
import com.nookure.proxy.utils.util.DefaultFontInfo;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import java.util.Set;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class VelocityProxyPlayerWrapper implements ProxyPlayerWrapper {
    private final Player player;
    @Inject private ServerStatusManager serverStatusManager;
    @Inject private ConfigurationContainer<Messages> messages;

    public VelocityProxyPlayerWrapper(Player player, Injector injector) {
        this.player = player;
        injector.injectMembers(this);
    }

    @Override
    public void sendPluginMessage(@NotNull String channel, byte @NotNull [] message) {
        player.sendPluginMessage(MinecraftChannelIdentifier.from(channel), message);
    }

    @Override
    public @NotNull Set<String> getListeningPluginChannels() {
        return Set.of();
    }

    @Override
    public void teleport(@NotNull PlayerWrapperBase to) {}

    @Override
    public void sendMiniMessage(@NotNull String message, String... placeholders) {
        ProxyPlayerWrapper.super.sendMiniMessage(
                DefaultFontInfo.centerIfContains(message), placeholders);
    }

    @Override
    public void sendMessage(@NotNull Component component) {
        player.sendMessage(component);
    }

    @Override
    public void sendActionbar(@NotNull Component component) {
        player.sendActionBar(component);
    }

    @Override
    public int getPing() {
        return (int) player.getPing();
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.text(player.getUsername());
    }

    @Override
    public @NotNull String getName() {
        return player.getUsername();
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public void goToLobby() {
        final RegisteredServer lobby = serverStatusManager.getServerWithLessPlayers();
        if (lobby == null) {
            sendMiniMessage(DefaultFontInfo.center(messages.get().lobby.errorWhileConnecting));
        } else {
            player.createConnectionRequest(lobby).fireAndForget();
        }
    }
}
