package ru.mcsnapix.snapistones.plugin.api.events.region;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ru.mcsnapix.snapistones.plugin.api.region.Region;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public class RegionAddPlayerEvent extends Event {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final Player player;
    private final Region region;

    @SuppressWarnings("unused") // Used by Bukkit
    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}
