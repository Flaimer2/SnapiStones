package ru.mcsnapix.snapistones.plugin.util;

import lombok.experimental.UtilityClass;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.settings.config.block.BlockConfig;
import ru.mcsnapix.snapistones.plugin.settings.config.block.BlockOption;
import ru.mcsnapix.snapistones.plugin.xseries.XMaterial;

@UtilityClass
public class BlockUtil {
    private final SnapiStones plugin = SnapiStones.get();
    private final BlockConfig blockConfig = plugin.getBlockConfig().data();

    public BlockOption getBlockOption(XMaterial xMaterial) {
        return blockConfig.blocks().get(xMaterial.name());
    }

    public boolean hasBlockOption(XMaterial xMaterial) {
        return blockConfig.blocks().containsKey(xMaterial.name());
    }
}
