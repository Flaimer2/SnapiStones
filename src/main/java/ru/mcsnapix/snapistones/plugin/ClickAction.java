package ru.mcsnapix.snapistones.plugin;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public enum ClickAction {
    LEFT,
    LEFT_SHIFT,
    RIGHT,
    RIGHT_SHIFT;

    public static ClickAction getClickAction(Player player, Action action) {
        if (player.isSneaking()) {
            if (action == Action.LEFT_CLICK_BLOCK) {
                return LEFT_SHIFT;
            } else {
                return RIGHT_SHIFT;
            }
        } else {
            if (action == Action.LEFT_CLICK_BLOCK) {
                return LEFT;
            } else {
                return RIGHT;
            }
        }
    }
}
