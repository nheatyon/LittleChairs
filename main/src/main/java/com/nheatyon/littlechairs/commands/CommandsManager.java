package com.nheatyon.littlechairs.commands;

import com.nheatyon.littlechairs.InitializerClass;
import com.nheatyon.littlechairs.utils.ConfigManager;
import com.nheatyon.littlechairs.utils.MessagesTypes;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Command({"littlechairs", "lc"})
@Description("Main command of LittleChairs")
public final class CommandsManager {

    private final ConfigManager config = InitializerClass.getConfigManager();

    private void sendMessage(final CommandSender sender, final String message) {
        if (sender instanceof Player) {
            sender.sendMessage(message);
        } else {
            Bukkit.getConsoleSender().sendMessage(message);
        }
    }

    @Default
    private void onDefault(final CommandSender sender) {
        sendMessage(sender, config.translate(MessagesTypes.DEFAULT.getValue()));
    }

    @Subcommand("reload")
    @Description("Command to reload the plugin")
    @CommandPermission("littlechairs.reload")
    private void onReload(final CommandSender sender) {
        sendMessage(sender, config.getMessage("reload_msg"));
        InitializerClass.getInstance().reloadConfig();
    }

}
