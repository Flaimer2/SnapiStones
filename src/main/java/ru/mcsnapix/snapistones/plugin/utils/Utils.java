package ru.mcsnapix.snapistones.plugin.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.OfflinePlayer;
import ru.mcsnapix.snapistones.plugin.SnapiStones;

@UtilityClass
public class Utils {
    private final SnapiStones plugin = SnapiStones.get();

    public boolean isPluginEnable(String name) {
        return plugin.getServer().getPluginManager().isPluginEnabled(name);
    }

    public OfflinePlayer offlinePlayer(String name) {
        return plugin.getServer().getOfflinePlayer(name);
    }
}
