package ru.mcsnapix.snapistones.plugin.modules.home;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.modules.IModule;
import ru.mcsnapix.snapistones.plugin.modules.Modules;
import ru.mcsnapix.snapistones.plugin.modules.home.config.HomeConfig;
import ru.mcsnapix.snapistones.plugin.settings.Configuration;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class HomeModule implements IModule {
    private final Modules modules;
    private Configuration<HomeConfig> homeConfig;

    @Override
    public void enable() {
        SnapiStones plugin = modules.getPlugin();
        homeConfig = Configuration.create(
                plugin,
                modules.getPathSettings(),
                "home.yml",
                HomeConfig.class,
                plugin.getOptions()
        );
    }

    @Override
    public void reload() {
        homeConfig.reloadConfig();
    }

    @Override
    public void disable() {
        // Disabling the module, in this case the module does not need to disable anything
    }
}
