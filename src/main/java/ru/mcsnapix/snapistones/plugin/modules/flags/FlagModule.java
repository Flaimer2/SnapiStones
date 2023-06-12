package ru.mcsnapix.snapistones.plugin.modules.flags;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.modules.IModule;
import ru.mcsnapix.snapistones.plugin.modules.Modules;
import ru.mcsnapix.snapistones.plugin.modules.flags.config.FlagConfig;
import ru.mcsnapix.snapistones.plugin.settings.Configuration;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class FlagModule implements IModule {
    private final Modules modules;
    private Configuration<FlagConfig> flagConfig;

    @Override
    public void load() {
        SnapiStones plugin = modules.getPlugin();
        flagConfig = Configuration.create(plugin,
                modules.getPathSettings(),
                "flag.yml",
                FlagConfig.class,
                plugin.getOptions()
        );

        plugin.getServer().getPluginManager().registerEvents(new FlagListener(flagConfig.data()), plugin);
    }

    @Override
    public void reload() {
        flagConfig.reloadConfig();
    }
}
