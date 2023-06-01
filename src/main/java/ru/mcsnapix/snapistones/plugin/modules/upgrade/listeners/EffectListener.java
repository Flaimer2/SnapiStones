package ru.mcsnapix.snapistones.plugin.modules.upgrade.listeners;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.NonNull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import ru.mcsnapix.snapistones.plugin.api.SnapPlayer;
import ru.mcsnapix.snapistones.plugin.api.events.region.RegionEnterEvent;
import ru.mcsnapix.snapistones.plugin.api.events.region.RegionLeaveEvent;
import ru.mcsnapix.snapistones.plugin.database.Database;
import ru.mcsnapix.snapistones.plugin.modules.upgrade.UpgradeModule;
import ru.mcsnapix.snapistones.plugin.modules.upgrade.settings.UpgradeConfig;

import java.util.List;

public class EffectListener implements Listener {
    private final UpgradeConfig upgradeConfig;

    public EffectListener(@NonNull UpgradeModule module) {
        upgradeConfig = module.upgradeConfig().data();
    }

    @EventHandler
    public void onRegionEnter(RegionEnterEvent event) {
        SnapPlayer player = event.player();
        ProtectedRegion region = event.region();
        Database database = new Database(region);

        if (!database.hasRegion()) {
            return;
        }

        if (!database.hasActiveEffects()) {
            return;
        }

        List<String> activeEffects = database.activeEffects();

        for (String effects : activeEffects) {
            player.addPotionEffect(upgradeConfig.effects().get(effects));
        }
    }

    @EventHandler
    public void onRegionLeave(RegionLeaveEvent event) {
        SnapPlayer player = event.player();
        ProtectedRegion region = event.region();
        Database database = new Database(region);

        if (!database.hasRegion()) {
            return;
        }

        for (PotionEffect effect : player.activePotionEffects()) {
            for (String s : database.activeEffects()) {
                if (s.equalsIgnoreCase(effect.getType().getName())) {
                    player.removePotionEffect(effect);
                }
            }
        }
    }
}
