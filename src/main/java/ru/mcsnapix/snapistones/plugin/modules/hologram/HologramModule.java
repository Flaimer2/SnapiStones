package ru.mcsnapix.snapistones.plugin.modules.hologram;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.modules.Module;
import ru.mcsnapix.snapistones.plugin.modules.hologram.listeners.HologramListener;
import ru.mcsnapix.snapistones.plugin.modules.hologram.settings.HologramConfig;
import ru.mcsnapix.snapistones.plugin.modules.interfaces.IModule;
import ru.mcsnapix.snapistones.plugin.settings.Configuration;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class HologramModule implements IModule {
    @NonNull
    private Module module;
    private Configuration<HologramConfig> hologramConfig;

    @Override
    public void load() {
        SnapiStones plugin = module.plugin();
        hologramConfig = Configuration.create(plugin,
                module.pathSettings(),
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
