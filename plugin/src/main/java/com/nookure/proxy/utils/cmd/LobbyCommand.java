package com.nookure.proxy.utils.cmd;

import com.nookure.core.CommandSender;
import com.nookure.core.command.Command;
import com.nookure.core.command.CommandData;
import com.nookure.proxy.utils.VelocityProxyPlayerWrapper;
import java.util.List;
import org.jetbrains.annotations.NotNull;

@CommandData(
        name = "lobby",
        permission = "npu.lobby",
        description = "Teleport to the lobby.",
        aliases = {"hub"})
public class LobbyCommand extends Command {
    @Override
    public void onCommand(
            @NotNull CommandSender commandSender, @NotNull String s, @NotNull List<String> list) {
        if (commandSender instanceof VelocityProxyPlayerWrapper wrapper) {
            wrapper.goToLobby();
        } else {
            commandSender.sendMiniMessage(
                    "Why you want to go to the lobby? You are a proxy console!");
        }
    }
}
