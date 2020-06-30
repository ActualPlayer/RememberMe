package com.actualplayer.rememberme;

import com.actualplayer.rememberme.handlers.*;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(id = "@ID@", name = "@NAME@", version = "@VERSION@", description = "@DESCRIPTION@", authors = {"ActualPlayer"}, dependencies = { @Dependency(id = "luckperms", optional = true) })
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

    private boolean hasLuckPerms;

    @Inject()
    public RememberMe(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Inject(optional = true)
    public void initLuckPerms(@Named("luckperms")PluginContainer luckPermsContainer) {
        this.hasLuckPerms = luckPermsContainer != null;
    }

    /**
     * If LuckPerms is present, use the User meta tags to save last server
     * @param event Velocity init event
     */
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        if (hasLuckPerms) {
            LuckPerms api = LuckPermsProvider.get();
            handler = new LuckPermsHandler(api);
            getLogger().info("LuckPerms is installed, using LuckPerms meta-data to store last server info");
        } else {
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
                    getServer().getServer(lastServerName).ifPresent(lastServer -> {
                        loginEvent.getPlayer().createConnectionRequest(lastServer).connectWithIndication();
                    });
                }
            }).join();
        }
    }

    @Subscribe
    public void onServerChange(ServerConnectedEvent serverConnectedEvent) {
        handler.setLastServerName(serverConnectedEvent.getPlayer().getUniqueId(), serverConnectedEvent.getServer().getServerInfo().getName());
    }
}
