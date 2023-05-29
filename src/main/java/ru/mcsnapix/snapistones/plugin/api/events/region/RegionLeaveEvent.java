package ru.mcsnapix.snapistones.plugin.api.events.region;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ru.mcsnapix.snapistones.plugin.api.SnapPlayer;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class RegionLeaveEvent extends Event {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    @NonNull
    private SnapPlayer player;
    @NonNull
    private final ProtectedRegion region;

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}
