package com.nookure.proxy.utils.listener;

import com.google.inject.Inject;
import com.nookure.core.config.ConfigurationContainer;
import com.nookure.proxy.utils.config.MotdConfig;
import com.nookure.proxy.utils.config.MotdMode;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.InboundConnection;
import com.velocitypowered.api.proxy.server.ServerPing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiPredicate;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

public class ServerPingListener {
    private static final Random RANDOM = new Random();
    @SuppressWarnings("rawtypes")
    private static final BiPredicate[] EVALUATIONS = new BiPredicate[] {
            // A bit ugly, but we can avoid us a Map usage.
            (a, b) -> (Integer) a <= (Integer) b,
            (a, b) -> (Integer) a >= (Integer) b,
            (a, b) -> (Integer) a < (Integer) b,
            (a, b) -> (Integer) a > (Integer) b,
            (a, b) -> a == b
    };
    private static final String[] OPERATORS = new String[] {"<=", ">=", "<", ">", "="};

    @Inject private ConfigurationContainer<MotdConfig> motdConfig;

    @Subscribe
    public void onServerPing(ProxyPingEvent event) {
        final MotdConfig config = motdConfig.get();
        if (config.mode == MotdMode.STATIC) {
            if (config.motds.isEmpty()) return;

            event.setPing(getServerPing(config.motds.get(0), event.getPing().asBuilder()));
            return;
        }

        final MotdConfig.MotdPartial partial = getRandomMotd(event.getConnection(), new ArrayList<>(config.motds));

        if (partial == null) return;

        event.setPing(getServerPing(partial, event.getPing().asBuilder()));
    }

    public ServerPing getServerPing(
            @NotNull MotdConfig.MotdPartial motdPartial, @NotNull ServerPing.Builder builder) {
        builder.description(MiniMessage.miniMessage().deserialize(motdPartial.motd));

        if (motdPartial.version == null || motdPartial.version.protocol == 0 || motdPartial.version.friendlyName == null) {
            return builder.build();
        }

        builder.version(
                new ServerPing.Version(
                        motdPartial.version.protocol, motdPartial.version.friendlyName));
        return builder.build();
    }

    public MotdConfig.MotdPartial getRandomMotd(
            @NotNull final InboundConnection connection,
            List<MotdConfig.MotdPartial> partials) {
        // Avoid ISE due to empty list for random -number.
        if (partials.isEmpty()) {
            return null;
        }
        final MotdConfig.MotdPartial motdPartial = partials.get(RANDOM.nextInt(partials.size()));
        if (canUseMotd(motdPartial, connection)) {
            return motdPartial;
        }
        partials.remove(motdPartial);
        return getRandomMotd(connection, partials);
    }

    @SuppressWarnings("unchecked")
    public boolean canUseMotd(
            @NotNull final MotdConfig.MotdPartial motdPartial,
            @NotNull final InboundConnection connection) {
        if (motdPartial.condition == null || motdPartial.condition.isEmpty()) {
            return true;
        }
        final int protocolVersion = connection.getProtocolVersion().getProtocol();
        if (protocolVersion == -1) {
            return true;
        }
        byte i = -1; // Index-value for both arrays.
        while (true) {
            i++;
            final String symbol = OPERATORS[i];
            if (!motdPartial.condition.startsWith(symbol)) {
                continue;
            }
            // Easy, it'll make the comparison, substring will take each symbol's length (>= -> 2 or = -> 1).
            return EVALUATIONS[i].test(protocolVersion, Integer.parseInt(motdPartial.condition.substring(
                    symbol.length() + 1)));
        }
    }
}
