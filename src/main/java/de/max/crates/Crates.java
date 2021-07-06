package de.max.crates;

import de.max.crates.Commands.CrateCommand;
import de.max.crates.api.CratesAPI;
import de.max.crates.utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Crates extends JavaPlugin {

    Crates plugin;

    private static Crates instance;

    @Override
    public void onEnable() {
        plugin = this;
        instance = this;

        listener();

        getCommand("crate").setExecutor(new CrateCommand());

        try {
            Config.onConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void listener() {
        Bukkit.getPluginManager().registerEvents(new CratesAPI(), this);
    }

    public static Crates getInstance() {
        return instance;
    }
}
