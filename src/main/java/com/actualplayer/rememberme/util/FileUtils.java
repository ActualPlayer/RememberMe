package com.actualplayer.rememberme.util;

import com.actualplayer.rememberme.RememberMe;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {

    public static File getOrCreate(Path folder, String name) {
        File directory = getOrCreateDirectory(folder.getParent(), folder.getFileName().toString());
        File file = new File(directory, name);
        if (!file.exists()) {
            try {
                try (InputStream input = RememberMe.class.getResourceAsStream("/" + name)) {
                    if (input != null) {
                        Files.copy(input, file.toPath());
                    } else {
                        file.createNewFile();
                    }
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return file;
    }

    public static File getOrCreateDirectory(Path folder, String name) {
        File file = new File(folder.toFile(), name);
        if (file.exists()) {
            if (!file.isDirectory()) {
                file.delete();
                file.mkdirs();
            }
        } else {
            file.mkdirs();
        }
        return file;
    }
}
