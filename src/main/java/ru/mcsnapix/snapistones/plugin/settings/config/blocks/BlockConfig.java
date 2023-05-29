package ru.mcsnapix.snapistones.plugin.settings.config.blocks;

import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.SubSection;

import java.util.HashMap;
import java.util.Map;

public interface BlockConfig {
    static Map<String, BlockOptions> defaultBlocks() {
        Map<String, BlockOptions> blockOptionsMap = new HashMap<>();
        blockOptionsMap.put("RED_MUSHROOM_BLOCK", BlockOptions.of("RM", 33, "ENTITY_SHULKER_TELEPORT", "ENTITY_SHULKER_OPEN", PlaceEffect.of("DRAGON_BREATH", 100)));
        return blockOptionsMap;
    }
    @ConfDefault.DefaultObject("defaultBlocks")
    Map<String, @SubSection BlockOptions> blocks();
}