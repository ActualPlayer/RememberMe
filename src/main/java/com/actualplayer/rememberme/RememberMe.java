package com.actualplayer.rememberme;

import com.actualplayer.rememberme.handlers.LoginHandler;
import com.actualplayer.rememberme.handlers.ServerChangeHandler;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(id = "@ID@", name = "@NAME@", version = "@VERSION@", description = "@Description@", authors = {"ActualPlayer"})
public class RememberMe {

    @Getter
    private final ProxyServer server;

    @Getter
    private final Logger logger;

    @Inject
    @DataDirectory
    @Getter
    private Path dataFolderPath;

    @Inject
    public RememberMe(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        server.getEventManager().register(this, new LoginHandler(this));
        server.getEventManager().register(this, new ServerChangeHandler(this));
    }
}
