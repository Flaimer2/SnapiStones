package ru.mcsnapix.snapistones.plugin;

import com.sk89q.worldguard.protection.regions.GlobalProtectedRegion;
import org.bukkit.entity.Player;

public class FakeProtectedRegion extends GlobalProtectedRegion {
    private final Player owner;

    public FakeProtectedRegion(String id, Player owner) {
        super(id);
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }
}
