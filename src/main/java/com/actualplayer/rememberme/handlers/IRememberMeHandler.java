package com.actualplayer.rememberme.handlers;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IRememberMeHandler {

    CompletableFuture<String> getLastServerName(UUID uuid);

    void setLastServerName(UUID uuid, String serverName);
}
