package com.actualplayer.rememberme.handlers;

import com.actualplayer.rememberme.RememberMe;
import com.actualplayer.rememberme.models.UserServer;
import com.actualplayer.rememberme.util.FileUtils;
import com.actualplayer.rememberme.util.YamlUtils;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.gson.GsonConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class FileHandler implements IRememberMeHandler {

    private RememberMe rememberMe;

    public FileHandler(RememberMe rememberMe) {
        this.rememberMe = rememberMe;
    }

    public CompletableFuture<String> getLastServerName(UUID uuid) {
        try {
            File userFile = FileUtils.getOrCreate(rememberMe.getDataFolderPath().resolve("data"), uuid.toString() + ".yml");
            UserServer userServer = YamlUtils.readFile(userFile, UserServer.class);

            if(userServer == null) return null;

            Optional<RegisteredServer> serverOpt = rememberMe.getServer().getServer(userServer.getServer());

            CompletableFuture<String> future = new CompletableFuture<>();
            future.complete(serverOpt.map(registeredServer -> registeredServer.getServerInfo().getName()).orElse(null));

            return future;
        } catch (IOException ex) {
            return null;
        }
    }

    public void setLastServerName(UUID uuid, String serverName) {
        File userFile = FileUtils.getOrCreate(rememberMe.getDataFolderPath().resolve("data"), uuid.toString() + ".yml");
        Map<String, String> userServer = new HashMap<>();
        userServer.put("server", serverName);

        try {
            YamlUtils.writeFile(userFile, userServer);
        } catch (IOException ex) {
            rememberMe.getLogger().error("Failed to write server name to user file.");
        }
    }
}
