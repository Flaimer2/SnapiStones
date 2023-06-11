package ru.mcsnapix.snapistones.plugin.api.events.block;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import ru.mcsnapix.snapistones.plugin.ClickAction;
import ru.mcsnapix.snapistones.plugin.api.region.Region;

/**
 * Represents an {@link Event} that can be listened to with the {@link Listener} class, which sends the {@link Player}, {@link ClickAction}, {@link Region}
 */
@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class BlockInteractEvent extends Event {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final Player player;
    private final ClickAction action;
    private final Region region;
    private final boolean ownerRegion;

    @SuppressWarnings("unused") // Used by Bukkit
    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}
