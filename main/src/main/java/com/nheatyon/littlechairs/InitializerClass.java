package com.nheatyon.littlechairs;

import com.nheatyon.littlechairs.commands.CommandsManager;
import com.nheatyon.littlechairs.events.EventsListener;
import com.nheatyon.littlechairs.nms.VersionChooser;
import com.nheatyon.littlechairs.nms.VersionWrapper;
import com.nheatyon.littlechairs.utils.ConfigManager;
import com.nheatyon.littlechairs.utils.MessagesTypes;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.core.BukkitHandler;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.exception.NoPermissionException;
import revxrsal.commands.exception.TooManyArgumentsException;

import java.util.function.BiConsumer;

public final class InitializerClass extends JavaPlugin {

    @Getter private static InitializerClass instance;
    @Getter private static ConfigManager configManager;
    @Getter private static VersionWrapper wrapper;

    private void setupCommandManager() {
        BukkitHandler handler = new BukkitHandler(this);
        CommandsManager commandsManager = new CommandsManager();
        handler.register(commandsManager);
        BiConsumer<CommandActor, String> consumer = (actor, message) ->
                ((BukkitCommandActor) actor).getSender().sendMessage(configManager.getMessage(message)
        );
        handler.failOnTooManyArguments();
        handler.registerExceptionHandler(TooManyArgumentsException.class, (actor, ex) -> consumer.accept(actor, MessagesTypes.DEFAULT.getValue()));
        handler.registerExceptionHandler(NoPermissionException.class, (actor, ex) -> consumer.accept(actor, "no_permission"));
        setDescriptions(commandsManager.getClass());
    }

    private void setDescriptions(final Class<?> clazz) {
        // Setting description for commands (not using Lamp)
        if (clazz.isAnnotationPresent(Command.class) && clazz.isAnnotationPresent(Description.class)) {
            Command commands = clazz.getAnnotation(Command.class);
            Description description = clazz.getAnnotation(Description.class);
            for (String cmd : commands.value()) {
                getCommand(cmd).setDescription(description.value());
            }
        }
    }

    @Override
    public void onEnable() {
        instance = this;
        // Setup ConfigManager and VersionWrapper
        configManager = new ConfigManager(this);
        wrapper = VersionChooser.choose();
        if (wrapper == null) return;
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new EventsListener(this), this);
        saveDefaultConfig();
        setupCommandManager();
        configManager.checkForChanges();
        // Load bStats
        new Metrics(this, 16545);
    }
}
