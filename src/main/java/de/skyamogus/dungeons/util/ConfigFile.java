package de.skyamogus.dungeons.util;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigFile extends YamlConfiguration {

    private final File file;

    public ConfigFile(File file) {

        this.file = file;

        try {

            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void save() {
        try {
            save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        try {
            load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

}
