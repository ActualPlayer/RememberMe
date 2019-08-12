package com.actualplayer.rememberme.util;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;

public class YamlUtils {

    public static <T> T readFile(File file, Class<T> clazz) throws FileNotFoundException {
        Yaml yaml = new Yaml(new Constructor(clazz));
        InputStream stream = new FileInputStream(file);

        return yaml.loadAs(stream, clazz);
    }

    public static void writeFile(File file, Object object) throws IOException {
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Yaml yaml = new Yaml(options);

        yaml.dump(object, new FileWriter(file));
    }
}
