package ru.mcsnapix.snapistones.plugin.modules;

import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.modules.enums.ModuleEnum;
import ru.mcsnapix.snapistones.plugin.modules.flags.FlagModule;
import ru.mcsnapix.snapistones.plugin.modules.hologram.HologramModule;
import ru.mcsnapix.snapistones.plugin.modules.home.HomeModule;
import ru.mcsnapix.snapistones.plugin.modules.interfaces.IModule;
import ru.mcsnapix.snapistones.plugin.modules.upgrade.UpgradeModule;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Module {
    private final SnapiStones plugin = SnapiStones.get();
    private final Map<ModuleEnum, IModule> moduleMap = Map.of(
            ModuleEnum.HOLOGRAM, new HologramModule(),
            ModuleEnum.HOME, new HomeModule(),
            ModuleEnum.FLAGS, new FlagModule(),
            ModuleEnum.UPGRADE, new UpgradeModule()
    );

    public Module() {
        System.out.println(plugin.getDataFolder().toPath());
        System.out.println(pathSettings());
        moduleMap.forEach(this::enableModule);
    }

    private void enableModule(ModuleEnum moduleEnum, IModule modstule) {
        module.load(plugin);
        plugin.log().info("§fМодуль §a{} §fзагружен", moduleEnum.name().toLowerCase());
    }

    public void reloadModules() {
        moduleMap.forEach(this::reloadModule);
    }

    private void reloadModule(ModuleEnum moduleEnum, IModule module) {
        module.reload();
        plugin.log().info("§fМодуль §a{} §fперезагружен", moduleEnum.name().toLowerCase());
    }

    private IModule getModule(ModuleEnum moduleEnum) {
        return moduleMap.get(moduleEnum);
    }

    public HologramModule hologram() {
        return (HologramModule) getModule(ModuleEnum.HOLOGRAM);
    }

    public HomeModule home() {
        return (HomeModule) getModule(ModuleEnum.HOME);
    }

    public FlagModule flags() {
        return (FlagModule) getModule(ModuleEnum.FLAGS);
    }

    public UpgradeModule upgrade() {
        return (UpgradeModule) getModule(ModuleEnum.UPGRADE);
    }

    public Path pathSettings() {
        String pathPlugin = plugin.getDataFolder().toPath().toString();
        return Paths.get(pathPlugin + File.separator + "modules");
    }
}