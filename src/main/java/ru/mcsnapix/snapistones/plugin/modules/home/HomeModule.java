package ru.mcsnapix.snapistones.plugin.modules.home;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.bukkit.entity.Player;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.api.SnapAPI;
import ru.mcsnapix.snapistones.plugin.api.SnapPlayer;
import ru.mcsnapix.snapistones.plugin.modules.Module;
import ru.mcsnapix.snapistones.plugin.modules.home.settings.HomeConfig;
import ru.mcsnapix.snapistones.plugin.modules.interfaces.IModule;
import ru.mcsnapix.snapistones.plugin.settings.Configuration;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class HomeModule implements IModule {
    @NonNull
    private Module module;
    private Configuration<HomeConfig> homeConfig;

    @Override
    public void load() {
        SnapiStones plugin = module.plugin();
        homeConfig = Configuration.create(
                plugin,
                module.pathSettings(),
                "home.yml",
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

    public HomeManager homeManager(Player player, ProtectedRegion region) {
        SnapPlayer snapPlayer = SnapAPI.player(player, region);
        return new HomeManager(this, snapPlayer);
    }
}
