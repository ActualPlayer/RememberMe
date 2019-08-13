package com.actualplayer.rememberme.handlers;

import com.actualplayer.rememberme.RememberMe;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;
import me.lucko.luckperms.api.manager.UserManager;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class LuckPermsHandler implements IRememberMeHandler {

    private final LuckPermsApi api;

    public LuckPermsHandler(LuckPermsApi api) {
        this.api = api;
    }

    @Override
    public CompletableFuture<String> getLastServerName(UUID uuid) {
        UserManager userManager = api.getUserManager();
        CompletableFuture<User> userFuture = userManager.loadUser(uuid);
        return userFuture.thenApply(user -> {
            if (user != null) {
                Optional<Contexts> contextsOpt = api.getContextManager().lookupApplicableContexts(user);

                if (contextsOpt.isPresent()) {
                    MetaData metaData = user.getCachedData().getMetaData(contextsOpt.get());
                    return metaData.getMeta().getOrDefault("last-server", null);
                }
            }

            return null;
        });
    }

    @Override
    public void setLastServerName(UUID uuid, String serverName) {
        User user = api.getUser(uuid);
        if (user != null) {
            // Remove last server
            user.clearMatching(n -> n.isMeta() && n.getPermission().contains("meta.last-server"));

            // Add current server as last server
            Node node = api.getNodeFactory().newBuilder("meta.last-server." + serverName).build();
            user.setPermission(node);
            api.getUserManager().saveUser(user);
        }
    }
}
