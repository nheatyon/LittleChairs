package com.nheatyon.littlechairs.utils;

import com.nheatyon.littlechairs.InitializerClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public final class ConfigManager {

    private final InitializerClass main;

    public void checkForChanges() {
        InitializerClass main = InitializerClass.getInstance();
        File configFile = new File(main.getDataFolder(), "config.yml");
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
        for (String section : main.getConfig().getDefaultSection().getKeys(true)) {
            if (fileConfiguration.get(section) != null) continue;
            fileConfiguration.set(section, main.getConfig().get(section));
        }
        try {
            fileConfiguration.save(configFile);
        } catch (IOException e) {
            Bukkit.getLogger().severe(MessagesTypes.CONFIG_ERROR.getValue());
            Bukkit.getPluginManager().disablePlugin(main);
        }
    }

    public ConfigManager(final InitializerClass main) {
        this.main = main;
    }

    public String translate(final String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    private String getPrefix() {
        return translate(main.getConfig().getString("prefix"));
    }

    public String getMessage(final String value) {
        return getPrefix() + translate(main.getConfig().getString(value));
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(final String value) {
        return (T) main.getConfig().get(value);
    }

}
