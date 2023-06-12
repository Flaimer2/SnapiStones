package ru.mcsnapix.snapistones.plugin.modules.upgrades;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.modules.IModule;
import ru.mcsnapix.snapistones.plugin.modules.Modules;
import ru.mcsnapix.snapistones.plugin.modules.upgrades.config.UpgradeConfig;
import ru.mcsnapix.snapistones.plugin.settings.Configuration;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class UpgradeModule implements IModule {
    private final Modules modules;
    private Configuration<UpgradeConfig> upgradeConfig;

    @Override
    public void load() {
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
}
