package dev.aangepast.feedbackbasecraft;

import dev.aangepast.feedbackbasecraft.commands.feedbackCommand;
import dev.aangepast.feedbackbasecraft.commands.teleporthereCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        getCommand("feedback").setExecutor(new feedbackCommand(this));
        getCommand("s").setExecutor(new teleporthereCommand(this));

        // Config

        FileConfiguration config = getConfig();
        config.addDefault("discord-webhook", "link-here");
        config.addDefault("cooldown-in-seconds", 60);
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}