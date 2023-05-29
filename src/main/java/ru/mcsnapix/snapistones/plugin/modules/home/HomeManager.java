package ru.mcsnapix.snapistones.plugin.modules.home;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.NonNull;
import org.bukkit.Location;
import ru.mcsnapix.snapistones.plugin.api.SnapPlayer;
import ru.mcsnapix.snapistones.plugin.database.Database;
import ru.mcsnapix.snapistones.plugin.modules.home.settings.HomeConfig;

import java.util.List;
import java.util.stream.Collectors;

public class HomeManager {
    @NonNull
    private final HomeModule module;
    @NonNull
    private final SnapPlayer player;
    private final Database database;
    private final HomeConfig homeConfig;

    protected HomeManager(@NonNull HomeModule module, @NonNull SnapPlayer player) {
        this.module = module;
        this.player = player;
        database = new Database(player.region());
        homeConfig = module.homeConfig().data();
    }

    private Location location() {
        return database.location();
    }

    public boolean hasLocation() {
        return database.location() != null;
    }

    public boolean hasLocation(ProtectedRegion region) {
        return new Database(region).location() != null;
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
