package ru.mcsnapix.snapistones.plugin;

import com.sk89q.worldguard.protection.regions.GlobalProtectedRegion;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.Player;

@EqualsAndHashCode(callSuper = false)
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
