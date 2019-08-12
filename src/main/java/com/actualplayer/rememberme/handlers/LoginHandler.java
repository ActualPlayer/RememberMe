package com.actualplayer.rememberme.handlers;

import com.actualplayer.rememberme.RememberMe;
import com.actualplayer.rememberme.models.UserServer;
import com.actualplayer.rememberme.util.FileUtils;
import com.actualplayer.rememberme.util.YamlUtils;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.io.*;
import java.util.UUID;

public class LoginHandler {

    private RememberMe rememberMe;

    public LoginHandler(RememberMe rememberMe) {
        this.rememberMe = rememberMe;
    }

    @Subscribe
    public void onLoginEvent(LoginEvent loginEvent) {
        // Ignore plugin when user has notransfer permission
        if (!loginEvent.getPlayer().hasPermission("rememberme.notransfer")) {
            RegisteredServer lastServer = getLastServer(loginEvent.getPlayer().getUniqueId());
            if (lastServer != null) {
                loginEvent.getPlayer().createConnectionRequest(lastServer).connectWithIndication();
            }
        }
    }

    /**
     * Gets last server for user
     * @param uniqueId user's UUID
     * @return server user last moved to or NULL if user hasn't joined yet
     */
    private RegisteredServer getLastServer(UUID uniqueId) {
        try {
            File userFile = FileUtils.getOrCreate(rememberMe.getDataFolderPath().resolve("data"), uniqueId.toString() + ".yml");
            UserServer userServer = YamlUtils.readFile(userFile, UserServer.class);

            if(userServer == null) return null;

            return rememberMe.getServer().getServer(userServer.getServer()).orElse(null);
        } catch (IOException ex) {
            return null;
        }
    }
}
