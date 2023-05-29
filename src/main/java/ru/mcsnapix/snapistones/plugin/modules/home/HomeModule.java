package ru.mcsnapix.snapistones.plugin.modules.home;

import lombok.Getter;
import lombok.experimental.Accessors;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.api.SnapPlayer;
import ru.mcsnapix.snapistones.plugin.modules.home.settings.HomeConfig;
import ru.mcsnapix.snapistones.plugin.modules.interfaces.IModule;
import ru.mcsnapix.snapistones.plugin.settings.Configuration;

@Accessors(fluent = true)
@Getter
public class HomeModule implements IModule {
    private SnapiStones plugin;
    private Configuration<HomeConfig> homeConfig;

    @Override
    public void load(SnapiStones plugin) {
        this.plugin = plugin;
        homeConfig = Configuration.create(
                plugin,
                plugin.module().pathSettings(),
                "config.yml",
                HomeConfig.class,
                plugin.options()
        );
    }

    @Override
    public void reload() {
        homeConfig.reloadConfig();
    }

    public HomeManager homeManager(SnapPlayer player) {
        return new HomeManager(this, player);
    }
}
