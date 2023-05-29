package ru.mcsnapix.snapistones.plugin.modules.hologram;

import lombok.Getter;
import lombok.experimental.Accessors;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.modules.hologram.listeners.HologramListener;
import ru.mcsnapix.snapistones.plugin.modules.hologram.settings.HologramConfig;
import ru.mcsnapix.snapistones.plugin.modules.interfaces.IModule;
import ru.mcsnapix.snapistones.plugin.settings.Configuration;

@Accessors(fluent = true)
@Getter
public class HologramModule implements IModule {
    private SnapiStones plugin;
    private Configuration<HologramConfig> hologramConfig;

    @Override
    public void load(SnapiStones plugin) {
        this.plugin = plugin;
        hologramConfig = Configuration.create(
                plugin,
                plugin.module().pathSettings(),
                "config.yml",
                HologramConfig.class,
                plugin.options()
        );
        plugin.getServer().getPluginManager().registerEvents(new HologramListener(this), plugin);
    }

    @Override
    public void reload() {
        hologramConfig.reloadConfig();
    }
}
