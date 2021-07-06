package de.max.crates.utils;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Config {

    public static File file = new File("plugins/Crates/Config.yml");
    public static YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

    public static void onConfig() {
        cfg.addDefault("Prefix", "§8▐ §cCrates §8» ");

        cfg.options().copyDefaults(true);
        try {
            cfg.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
