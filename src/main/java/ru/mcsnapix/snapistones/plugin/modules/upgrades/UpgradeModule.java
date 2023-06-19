package ru.mcsnapix.snapistones.plugin.modules.upgrades;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.api.SnapApi;
import ru.mcsnapix.snapistones.plugin.api.region.Region;
import ru.mcsnapix.snapistones.plugin.modules.IModule;
import ru.mcsnapix.snapistones.plugin.modules.Modules;
import ru.mcsnapix.snapistones.plugin.modules.upgrades.config.EffectOptions;
import ru.mcsnapix.snapistones.plugin.modules.upgrades.config.UpgradeConfig;
import ru.mcsnapix.snapistones.plugin.settings.Configuration;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class UpgradeModule implements IModule {
    private final Modules modules;
    private Configuration<UpgradeConfig> upgradeConfig;

    @Override
    public void enable() {
        SnapiStones plugin = modules.getPlugin();

        upgradeConfig = Configuration.create(
                plugin,
                modules.getPathSettings(),
                "upgrade.yml",
                UpgradeConfig.class,
                plugin.getOptions()
        );
        plugin.getServer().getPluginManager().registerEvents(new EffectListener(upgradeConfig.data()), plugin);
    }

    @Override
    public void reload() {
        upgradeConfig.reloadConfig();
    }

    @Override
    public void disable() {
        for (Region region : SnapApi.getRegions()) {
            for (Player player : region.playersInRegion()) {
                for (String s : region.activeEffects()) {
                    EffectOptions effectOptions = upgradeConfig.data().effects().get(s);
                    if (effectOptions == null) continue;

                    for (PotionEffect effect : player.getActivePotionEffects()) {
                        if (effectOptions.effect().equalsIgnoreCase(effect.getType().getName())) {
                            player.removePotionEffect(effect.getType());
                        }
                    }
                }
            }
        }
    }
}
