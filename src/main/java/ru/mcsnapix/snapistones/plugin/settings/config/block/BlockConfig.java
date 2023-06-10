package ru.mcsnapix.snapistones.plugin.settings.config.block;

import space.arim.dazzleconf.annote.SubSection;

import java.util.HashMap;
import java.util.Map;

import static space.arim.dazzleconf.annote.ConfDefault.DefaultObject;

public interface BlockConfig {
    static Map<String, BlockOption> defaultBlocks() {
        Map<String, BlockOption> blockOptionsMap = new HashMap<>();
        blockOptionsMap.put("RED_MUSHROOM_BLOCK", BlockOption.of("RM", 33, "ENTITY_SHULKER_TELEPORT", "ENTITY_SHULKER_OPEN", PlaceEffect.of("DRAGON_BREATH", 100)));
        return blockOptionsMap;
    }

    @DefaultObject("defaultBlocks")
    Map<String, @SubSection BlockOption> blocks();
}
