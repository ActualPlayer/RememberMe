package com.actualplayer.rememberme.handlers;

import com.actualplayer.rememberme.RememberMe;
import com.actualplayer.rememberme.models.UserServer;
import com.actualplayer.rememberme.util.FileUtils;
import com.actualplayer.rememberme.util.YamlUtils;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServerChangeHandler {

    private RememberMe rememberMe;

    public ServerChangeHandler(RememberMe rememberMe) {
        this.rememberMe = rememberMe;
    }

    @Subscribe
    public void onServerConnectedEvent(ServerConnectedEvent serverConnectedEvent) throws IOException {
        File userFile = FileUtils.getOrCreate(rememberMe.getDataFolderPath().resolve("data"), serverConnectedEvent.getPlayer().getUniqueId().toString() + ".yml");
        Map<String, String> userServer = new HashMap<>();
        userServer.put("server", serverConnectedEvent.getServer().getServerInfo().getName());

        YamlUtils.writeFile(userFile, userServer);
    }
}
