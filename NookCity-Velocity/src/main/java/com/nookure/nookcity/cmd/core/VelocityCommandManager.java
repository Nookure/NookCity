package com.nookure.nookcity.cmd.core;

import static java.util.Objects.requireNonNull;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.nookure.core.command.Command;
import com.nookure.core.command.CommandData;
import com.nookure.core.command.CommandManager;
import com.nookure.nookcity.NookCity;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.proxy.ProxyServer;
import org.jetbrains.annotations.NotNull;

public class VelocityCommandManager extends CommandManager {
    @Inject private ProxyServer proxy;
    @Inject private NookCity nookCity;
    @Inject private Injector injector;

    public void registerCommand(@NotNull Class<? extends Command> command) {
        requireNonNull(command, "Command class cannot be null!");
        registerCommand(injector.getInstance(command));
    }

    @Override
    public void registerCommand(@NotNull Command command) {
        requireNonNull(command, "Command cannot be null!");
        com.velocitypowered.api.command.CommandManager commandManager = proxy.getCommandManager();

        TemplateCommand templateCommand = new TemplateCommand(command);
        injector.injectMembers(templateCommand);

        CommandData commandData = command.getCommandData();

        CommandMeta commandMeta =
                commandManager
                        .metaBuilder(commandData.name())
                        .aliases(commandData.aliases())
                        .plugin(nookCity)
                        .build();

        commandManager.register(commandMeta, templateCommand);
    }

    @Override
    public void unregisterCommand(@NotNull Command command) {
        com.velocitypowered.api.command.CommandManager commandManager = proxy.getCommandManager();
        CommandData commandData = command.getCommandData();

        commandManager.unregister(commandData.name());
    }
}
