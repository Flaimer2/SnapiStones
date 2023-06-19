package ru.mcsnapix.snapistones.plugin.modules.menu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.modules.IModule;
import ru.mcsnapix.snapistones.plugin.modules.Modules;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class MenuModule implements IModule {
    private final Modules modules;

    @Override
    public void enable() {
        SnapiStones plugin = modules.getPlugin();
        plugin.getServer().getPluginManager().registerEvents(new MenuListener(plugin), plugin);
    }

    @Override
    public void reload() {
        // This module does not require reload
    }

    @Override
    public void disable() {
        // Disabling the module, in this case the module does not need to disable anything
    }
}
