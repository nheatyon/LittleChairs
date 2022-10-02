package com.nheatyon.littlechairs.nms;

import com.nheatyon.littlechairs.InitializerClass;
import com.nheatyon.littlechairs.utils.MessagesTypes;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VersionChooser {

    public static VersionWrapper choose() {
        String packageName = String.format("%s.%s.Wrapper",
                VersionChooser.class.getPackage().getName(), Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3]);
        try {
            return (VersionWrapper) Class.forName(packageName).getConstructors()[0].newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            Bukkit.getLogger().severe(MessagesTypes.VERSION_ERROR.getValue().replace("%err", e.toString()));
            Bukkit.getPluginManager().disablePlugin(InitializerClass.getInstance());
            return null;
        }
    }
}
