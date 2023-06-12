package ru.mcsnapix.snapistones.plugin.api.events.region;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import ru.mcsnapix.snapistones.plugin.api.region.Region;

/**
 * Represents an {@link Event} that can be listened to with the {@link Listener} class,
 * which sends the {@link Player}, {@link Region}
 */
@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class RegionCreateEvent extends Event {
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
