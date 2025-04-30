package com.nookure.proxy.utils.cmd.core;

import static java.util.Objects.requireNonNull;

import com.google.inject.Inject;
import com.nookure.core.command.Command;
import com.nookure.core.command.ConsoleCommandSender;
import com.nookure.core.manager.PlayerWrapperManager;
import com.nookure.proxy.utils.ProxyPlayerWrapper;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;

public class TemplateCommand implements SimpleCommand {
    private static final CompletableFuture<List<String>> NO_SUGGESTIONS_FUTURE = CompletableFuture.completedFuture(List.of());
    @Inject
    private PlayerWrapperManager<Player, ProxyPlayerWrapper> playerWrapperManager;
    @Inject
    private ConsoleCommandSender consoleCommandSender;

    private final Command command;

    public TemplateCommand(@NotNull Command command) {
        requireNonNull(command, "Command cannot be null.");
        this.command = command;
    }

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            command.onCommand(
                    consoleCommandSender, invocation.alias(), List.of(invocation.arguments()));
            return;
        }
        playerWrapperManager.getPlayerWrapper(player.getUniqueId()).ifPresent(
                playerWrapperBase ->
                        command.onCommand(
                                playerWrapperBase,
                                invocation.alias(),
                                List.of(invocation.arguments())));
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            return CompletableFuture.completedFuture(
                    command.onTabComplete(
                            consoleCommandSender,
                            invocation.alias(),
                            List.of(invocation.arguments())));
        }
        return playerWrapperManager.getPlayerWrapper(player.getUniqueId())
                .map(playerWrapperBase -> CompletableFuture.completedFuture(
                        command.onTabComplete(
                                playerWrapperBase,
                                invocation.alias(),
                                List.of(invocation.arguments())))).orElse(NO_SUGGESTIONS_FUTURE);
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        final String permission = command.getCommandData().permission();
        if (permission == null) {
            return true;
        }

        if (permission.isEmpty() || permission.isBlank()) {
            return true;
        }

        return invocation.source().hasPermission(command.getCommandData().permission());
    }
}
