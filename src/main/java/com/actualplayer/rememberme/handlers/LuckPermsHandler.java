package com.actualplayer.rememberme.handlers;

import com.actualplayer.rememberme.RememberMe;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.context.ContextManager;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.query.QueryMode;
import net.luckperms.api.query.QueryOptions;
import org.checkerframework.checker.nullness.Opt;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class LuckPermsHandler implements IRememberMeHandler {

    private final LuckPerms api;

    public LuckPermsHandler(LuckPerms api) {
        this.api = api;
    }

    @Override
    public CompletableFuture<String> getLastServerName(UUID uuid) {
        UserManager userManager = api.getUserManager();
        CompletableFuture<User> userFuture = userManager.loadUser(uuid);
        return userFuture.thenApply(user -> {
            if (user != null) {
                ContextManager cm = api.getContextManager();
                ImmutableContextSet context = cm.getContext(user).orElse(cm.getStaticContext());

                CachedMetaData metaData = user.getCachedData().getMetaData(QueryOptions.builder(QueryMode.CONTEXTUAL).context(context).build());
                return metaData.getMetaValue("last-server");
            }

            return null;
        });
    }

    @Override
    public void setLastServerName(UUID uuid, String serverName) {
        User user = api.getUserManager().getUser(uuid);
        if (user != null) {
            // Remove last server
            user.getNodes().removeIf(n -> n.getType() == NodeType.META && n.getKey().contains("last-server"));

            // Add current server as last server
            Node node = api.getNodeBuilderRegistry().forMeta().key("last-server").value(serverName).build();
            user.getNodes().add(node);
            api.getUserManager().saveUser(user);
        }
    }
}
