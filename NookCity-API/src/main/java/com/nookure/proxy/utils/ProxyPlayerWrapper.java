package com.nookure.proxy.utils;

import com.nookure.core.PlayerWrapperBase;

public interface ProxyPlayerWrapper extends PlayerWrapperBase {
    /**
     * Sends the player to the lobby.
     */
    void goToLobby();
}
