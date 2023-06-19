package ru.mcsnapix.snapistones.plugin.modules.upgrades;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.mcsnapix.snapistones.plugin.api.events.region.*;
import ru.mcsnapix.snapistones.plugin.api.region.Region;
import ru.mcsnapix.snapistones.plugin.modules.upgrades.config.EffectOptions;
import ru.mcsnapix.snapistones.plugin.modules.upgrades.config.UpgradeConfig;

@RequiredArgsConstructor
public class EffectListener implements Listener {
    private final UpgradeConfig upgradeConfig;

    @EventHandler
    public void onRegionEnter(RegionEnterEvent event) {
        Player player = event.player();
        Region region = event.region();

        if (!region.hasActiveEffects()) return;
        if (!region.hasPlayerInRegion(player.getName())) return;

        for (String effect : region.activeEffects()) {
            EffectOptions effectOptions = upgradeConfig.effects().get(effect);
            if (effectOptions == null) continue;
            String effectName = effectOptions.effect();
            int level = effectOptions.level();

            player.addPotionEffect(PotionEffectType.getByName(effectName).createEffect(Integer.MAX_VALUE, level - 1));
        }
    }

    @EventHandler
    public void onRegionLeave(RegionLeaveEvent event) {
        Player player = event.player();
        Region region = event.region();

        if (!region.hasActiveEffects()) return;

        for (PotionEffect effect : player.getActivePotionEffects()) {
            for (String s : region.activeEffects()) {
                EffectOptions effectOptions = upgradeConfig.effects().get(s);
                if (effectOptions == null) continue;
                if (effectOptions.effect().equalsIgnoreCase(effect.getType().getName())) {
                    player.removePotionEffect(effect.getType());
                }
            }
        }
    }

    @EventHandler
    public void onRegionRemove(RegionRemoveEvent event) {
        Player player = event.player();
        Region region = event.region();

        if (!region.hasActiveEffects()) return;

        for (PotionEffect effect : player.getActivePotionEffects()) {
            for (String s : region.activeEffects()) {
                EffectOptions effectOptions = upgradeConfig.effects().get(s);
                if (effectOptions == null) continue;
                if (effectOptions.effect().equalsIgnoreCase(effect.getType().getName())) {
                    player.removePotionEffect(effect.getType());
                }
            }
        }
    }

    @EventHandler
    public void onRegionAddPlayer(RegionAddPlayerEvent event) {
        Player player = event.player();
        Region region = event.region();

        if (!region.hasActiveEffects()) return;

        for (String effect : region.activeEffects()) {
            EffectOptions effectOptions = upgradeConfig.effects().get(effect);
            if (effectOptions == null) continue;
            String effectName = effectOptions.effect();
            int level = effectOptions.level();

            player.addPotionEffect(PotionEffectType.getByName(effectName).createEffect(Integer.MAX_VALUE, level - 1));
        }
    }

    @EventHandler
    public void onRegionRemovePlayer(RegionRemovePlayerEvent event) {
        Player player = event.player();
        Region region = event.region();

        if (!region.hasActiveEffects()) return;

        for (PotionEffect effect : player.getActivePotionEffects()) {
            for (String s : region.activeEffects()) {
                EffectOptions effectOptions = upgradeConfig.effects().get(s);
                if (effectOptions == null) continue;
                if (effectOptions.effect().equalsIgnoreCase(effect.getType().getName())) {
                    player.removePotionEffect(effect.getType());
                }
            }
        }
    }
}
