package ru.mcsnapix.snapistones.plugin.modules.flags;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.modules.Module;
import ru.mcsnapix.snapistones.plugin.modules.flags.settings.FlagConfig;
import ru.mcsnapix.snapistones.plugin.modules.interfaces.IModule;
import ru.mcsnapix.snapistones.plugin.settings.Configuration;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class FlagModule implements IModule {
    @NonNull
    private final Module module;
    private Configuration<FlagConfig> flagConfig;

    @Override
    public void load() {
        SnapiStones plugin = module.plugin();
        flagConfig = Configuration.create(plugin,
                module.pathSettings(),
                "flag.yml",
                FlagConfig.class,
                plugin.options()
        );

        plugin.getServer().getPluginManager().registerEvents(new FlagListener(flagConfig.data()), plugin);
    }

    @Override
    public void reload() {
        flagConfig.reloadConfig();
    }
}
