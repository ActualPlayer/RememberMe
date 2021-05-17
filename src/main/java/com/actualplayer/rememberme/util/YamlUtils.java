package com.actualplayer.rememberme.util;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.*;
import java.nio.file.Path;

public class YamlUtils {

    public static String readServer(File file) throws FileNotFoundException {
        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder().path(Path.of(file.toURI())).build();
        CommentedConfigurationNode root;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.err.println("RememberMe - An error occurred while loading the users last server file: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            return null;
        }

        return root.node("server").getString();
    }

    public static void writeServer(File file, String server) throws IOException {
        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder().path(Path.of(file.toURI())).build();
        CommentedConfigurationNode root;
        try {
            root = loader.load();
            root.node("server").set(server);
            loader.save(root);
        } catch (IOException e) {
            System.err.println("RememberMe - An error occurred while saving users last server to file: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        }
    }
}
