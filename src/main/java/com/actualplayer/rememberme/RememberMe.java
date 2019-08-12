package com.actualplayer.rememberme;

import com.actualplayer.rememberme.handlers.*;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import lombok.Getter;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(id = "@ID@", name = "@NAME@", version = "@VERSION@", description = "@Description@", authors = {"ActualPlayer"}, dependencies = { @Dependency(id = "luckperms", optional = true) })
public class RememberMe {

    @Getter
    private final ProxyServer server;

    @Getter
    private final Logger logger;

    @Inject
    @DataDirectory
    @Getter
    private Path dataFolderPath;

    private IRememberMeHandler handler;

    @Inject
    public RememberMe(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    /**
     * If LuckPerms is present, use the User meta tags to save last server
     * @param event Velocity init event
     */
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        try {
            LuckPermsApi api = LuckPerms.getApi();
            handler = new LuckPermsHandler(api);
            getLogger().info("LuckPerms is installed, using LuckPerms meta-data to store last server info");
        } catch (IllegalStateException ex) {
            getLogger().error(ex.toString());
            handler = new FileHandler(this);
            getLogger().info("Using file-based storage");
        }
    }

    @Subscribe
    public void onLoginEvent(LoginEvent loginEvent) {
        // Ignore plugin when user has notransfer permission
        if (!loginEvent.getPlayer().hasPermission("rememberme.notransfer")) {
            handler.getLastServerName(loginEvent.getPlayer().getUniqueId()).thenAcceptAsync(lastServerName -> {
                if (lastServerName != null) {
                    getServer().getServer(lastServerName).ifPresent(lastServer -> loginEvent.getPlayer().createConnectionRequest(lastServer).connectWithIndication());
                }
            });
        }
    }

    @Subscribe
    public void onServerChange(ServerConnectedEvent serverConnectedEvent) {
        handler.setLastServerName(serverConnectedEvent.getPlayer().getUniqueId(), serverConnectedEvent.getServer().getServerInfo().getName());
    }
}
