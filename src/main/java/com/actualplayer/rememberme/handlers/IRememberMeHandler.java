package com.actualplayer.rememberme.handlers;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IRememberMeHandler {

    /**
     * Get the user's last server name
     * @param uuid user's UUID
     * @return A future that contains a String with the last server's name once resolved or NULL
     */
    CompletableFuture<String> getLastServerName(UUID uuid);

    /**
     * Sets the user's last server on server switch
     * @param uuid user's UUID
     * @param serverName name of the server the user moved to
     */
    void setLastServerName(UUID uuid, String serverName);
}
