package com.nookure.proxy.utils.cmd.core;

import static java.util.Objects.requireNonNull;

import com.google.inject.Inject;
import com.nookure.core.PlayerWrapperBase;
import com.nookure.core.command.Command;
import com.nookure.core.command.ConsoleCommandSender;
import com.nookure.core.manager.PlayerWrapperManager;
import com.nookure.proxy.utils.ProxyPlayerWrapper;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

public class TemplateCommand implements SimpleCommand {
    @Inject private PlayerWrapperManager<Player, ProxyPlayerWrapper> playerWrapperManager;
    @Inject private ConsoleCommandSender consoleCommandSender;

    private final Command command;

    public TemplateCommand(@NotNull Command command) {
        requireNonNull(command, "Command cannot be null.");
        this.command = command;
    }

    @Override
    public void execute(Invocation invocation) {
        if (invocation.source() instanceof Player player) {
            Optional<PlayerWrapperBase> playerWrapper =
                    playerWrapperManager.getPlayerWrapper(player.getUniqueId());
            playerWrapper.ifPresent(
                    playerWrapperBase ->
                            command.onCommand(
                                    playerWrapperBase,
                                    invocation.alias(),
                                    List.of(invocation.arguments())));
        } else {
            command.onCommand(
                    consoleCommandSender, invocation.alias(), List.of(invocation.arguments()));
        }
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        if (invocation.source() instanceof Player player) {
            Optional<PlayerWrapperBase> playerWrapper =
                    playerWrapperManager.getPlayerWrapper(player.getUniqueId());
            if (playerWrapper.isPresent()) {
                return CompletableFuture.completedFuture(
                        command.onTabComplete(
                                playerWrapper.get(),
                                invocation.alias(),
                                List.of(invocation.arguments())));
            }
        } else {
            return CompletableFuture.completedFuture(
                    command.onTabComplete(
                            consoleCommandSender,
                            invocation.alias(),
                            List.of(invocation.arguments())));
        }

        return CompletableFuture.completedFuture(List.of());
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        if (command.getCommandData().permission() == null) {
            return true;
        }

        if (command.getCommandData().permission().isEmpty()
                || command.getCommandData().permission().isBlank()) {
            return true;
        }

        return invocation.source().hasPermission(command.getCommandData().permission());
    }
}
