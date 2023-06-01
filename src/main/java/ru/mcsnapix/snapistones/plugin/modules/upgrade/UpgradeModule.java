package ru.mcsnapix.snapistones.plugin.modules.upgrade;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.modules.Module;
import ru.mcsnapix.snapistones.plugin.modules.interfaces.IModule;
import ru.mcsnapix.snapistones.plugin.modules.upgrade.settings.UpgradeConfig;
import ru.mcsnapix.snapistones.plugin.settings.Configuration;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class UpgradeModule implements IModule {
    @NonNull
    private Module module;
    private Configuration<UpgradeConfig> upgradeConfig;

    @Override
    public void load() {
        SnapiStones plugin = module.plugin();

        upgradeConfig = Configuration.create(
                plugin,
                module.pathSettings(),
                "upgrade.yml",
                UpgradeConfig.class,
                plugin.options()
        );
//        plugin.getServer().getPluginManager().registerEvents(new EffectListener(this), plugin);
    }

    @Override
    public void reload() {
        upgradeConfig.reloadConfig();
    }
}
