package ru.mcsnapix.snapistones.plugin.modules.hologram;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.modules.IModule;
import ru.mcsnapix.snapistones.plugin.modules.Modules;
import ru.mcsnapix.snapistones.plugin.modules.hologram.config.HologramConfig;
import ru.mcsnapix.snapistones.plugin.settings.Configuration;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class HologramModule implements IModule {
    private final Modules modules;
    private Configuration<HologramConfig> flagConfig;

    @Override
    public void load() {
        SnapiStones plugin = modules.getPlugin();
        flagConfig = Configuration.create(plugin,
                modules.getPathSettings(),
                "flag.yml",
                HologramConfig.class,
                plugin.getOptions()
        );

//        plugin.getServer().getPluginManager().registerEvents(new HologramListener(flagConfig.data()), plugin);
    }

    @Override
    public void reload() {
        flagConfig.reloadConfig();
    }
}
