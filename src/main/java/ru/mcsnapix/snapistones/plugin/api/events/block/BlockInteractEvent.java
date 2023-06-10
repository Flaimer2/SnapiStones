package ru.mcsnapix.snapistones.plugin.api.events.block;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ru.mcsnapix.snapistones.plugin.ClickAction;
import ru.mcsnapix.snapistones.plugin.api.region.Region;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class BlockInteractEvent extends Event {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    @NonNull
    private Player player;
    @NonNull
    private ClickAction action;
    @NonNull
    private Region region;

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}
