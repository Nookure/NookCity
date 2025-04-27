package com.nookure.proxy.utils.cmd;

import com.google.inject.Inject;
import com.nookure.core.CommandSender;
import com.nookure.core.command.Command;
import com.nookure.core.command.CommandData;
import com.nookure.core.config.ConfigurationContainer;
import com.nookure.proxy.utils.config.CoreConfig;
import com.nookure.proxy.utils.config.Messages;
import com.nookure.proxy.utils.config.MotdConfig;
import java.util.List;
import org.jetbrains.annotations.NotNull;

@CommandData(
        name = "nookcity",
        permission = "nookcity.admin",
        description = "Main command of the NookCity plugin",
        aliases = {"nc"})
public class NookCityCommand extends Command {
    private final ConfigurationContainer<CoreConfig> coreConfig;
    private final ConfigurationContainer<Messages> messages;
    private final ConfigurationContainer<MotdConfig> motdConfig;

    @Inject
    public NookCityCommand(
            ConfigurationContainer<CoreConfig> coreConfig,
            ConfigurationContainer<Messages> messages,
            ConfigurationContainer<MotdConfig> motdConfig) {
        this.coreConfig = coreConfig;
        this.messages = messages;
        this.motdConfig = motdConfig;
    }

    @Override
    public void onCommand(
            @NotNull CommandSender commandSender, @NotNull String s, @NotNull List<String> list) {
        if (list.isEmpty()) {
            commandSender.sendMiniMessage(String.join("\n", messages.get().commands.help));
            return;
        }

        String subCommand = list.getFirst();
        if (subCommand.equals("reload")) {
            coreConfig.reload();
            messages.reload();
            motdConfig.reload();
            commandSender.sendMiniMessage(messages.get().commands.reload);
        } else {
            commandSender.sendMiniMessage(String.join("\n", messages.get().commands.help));
        }
    }
}
