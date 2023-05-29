package ru.mcsnapix.snapistones.plugin.api.events.block;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ru.mcsnapix.snapistones.plugin.api.ProtectedBlock;
import ru.mcsnapix.snapistones.plugin.api.SnapPlayer;
import ru.mcsnapix.snapistones.plugin.api.enums.ClickAction;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class BlockInteractEvent extends Event {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    @NonNull
    private SnapPlayer player;
    @NonNull
    private ClickAction action;
    @NonNull
    private ProtectedRegion region;
    @NonNull
    private ProtectedBlock protectedBlock;
    @NonNull
    private Location location;

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}