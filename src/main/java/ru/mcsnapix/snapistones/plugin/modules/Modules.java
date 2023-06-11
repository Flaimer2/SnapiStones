package ru.mcsnapix.snapistones.plugin.modules;

import lombok.Getter;
import lombok.SneakyThrows;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.modules.flags.FlagModule;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Modules {
    @Getter
    private final SnapiStones plugin = SnapiStones.get();
    private final Map<ModuleEnum, IModule> moduleMap = Map.of(
            ModuleEnum.HOLOGRAM, new HologramModule(this),
            ModuleEnum.HOME, new HomeModule(this),
            ModuleEnum.FLAGS, new FlagModule(this),
            ModuleEnum.UPGRADE, new UpgradeModule(this)
    );
    @Getter
    private Path pathSettings;

    public Modules() {
        createPathSettings();
        moduleMap.forEach(this::enableModule);
    }

    private void enableModule(ModuleEnum moduleEnum, IModule module) {
        module.load();
        plugin.getLog().info("§fМодуль §a{} §fзагружен", moduleEnum.name().toLowerCase());
    }

    public void reloadModules() {
        moduleMap.forEach(this::reloadModule);
    }

    private void reloadModule(ModuleEnum moduleEnum, IModule module) {
        module.reload();
        plugin.getLog().info("§fМодуль §a{} §fперезагружен", moduleEnum.name().toLowerCase());
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

    @SneakyThrows
    public void createPathSettings() {
        String pathPlugin = plugin.getDataFolder().toPath().toString();
        Path dir = Paths.get(pathPlugin + "/modules");
        Files.createDirectories(dir);

        pathSettings = dir;
    }
}
