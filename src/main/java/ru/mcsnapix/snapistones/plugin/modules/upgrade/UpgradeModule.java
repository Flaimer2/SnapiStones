package ru.mcsnapix.snapistones.plugin.modules.upgrade;

import lombok.Getter;
import lombok.experimental.Accessors;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.modules.interfaces.IModule;
import ru.mcsnapix.snapistones.plugin.modules.upgrade.listeners.EffectListener;
import ru.mcsnapix.snapistones.plugin.modules.upgrade.settings.UpgradeConfig;
import ru.mcsnapix.snapistones.plugin.settings.Configuration;

@Accessors(fluent = true)
@Getter
public class UpgradeModule implements IModule {
    private SnapiStones plugin;
    private Configuration<UpgradeConfig> upgradeConfig;

    @Override
    public void load(SnapiStones plugin) {
        this.plugin = plugin;
        upgradeConfig = Configuration.create(
                plugin,
                plugin.module().pathSettings(),
                "config.yml",
                UpgradeConfig.class,
                plugin.options()
        );
        plugin.getServer().getPluginManager().registerEvents(new EffectListener(this), plugin);
    }

    @Override
    public void reload() {
        upgradeConfig.reloadConfig();
    }
}
