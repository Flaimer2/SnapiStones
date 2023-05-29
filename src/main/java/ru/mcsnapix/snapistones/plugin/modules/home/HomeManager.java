package ru.mcsnapix.snapistones.plugin.modules.home;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import ru.mcsnapix.snapistones.plugin.api.SnapPlayer;
import ru.mcsnapix.snapistones.plugin.database.Database;
import ru.mcsnapix.snapistones.plugin.modules.home.settings.HomeConfig;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class HomeManager {
    private final HomeModule module;
    private final SnapPlayer player;
    private final Database database = new Database(player.region());
    private final HomeConfig homeConfig = module.homeConfig().data();

    private Location location() {
        return database.location();
    }

    public boolean hasLocation() {
        return database.location() != null;
    }

    public boolean hasLocation(ProtectedRegion region) {
        Database database = new Database(region);
        return database.location() != null;
    }

    public List<ProtectedRegion> listRegionWithHome() {
        return player.memberRegions().stream().filter(this::hasLocation).collect(Collectors.toList());
    }

    public void add(Location location) {
        database.location(location);
    }

    public void teleport() {
        if (hasLocation()) {
            player.teleport(location());
            player.sendMessage(homeConfig.commands().home().success());
            return;
        }
        player.sendMessage(homeConfig.commands().home().noHomeInRegion());
    }
}
