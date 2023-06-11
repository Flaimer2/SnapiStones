package ru.mcsnapix.snapistones.plugin.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.World;
import org.bukkit.entity.Player;
import ru.mcsnapix.snapistones.plugin.api.ProtectedBlock;
import ru.mcsnapix.snapistones.plugin.api.region.Region;
import ru.mcsnapix.snapistones.plugin.api.region.RegionRegistry;
import ru.mcsnapix.snapistones.plugin.serializers.ListSerializer;
import ru.mcsnapix.snapistones.plugin.serializers.LocationSerializer;
import ru.mcsnapix.snapistones.plugin.settings.config.block.BlockOption;

public class RegionExpansion extends PlaceholderExpansion {
    @Override
    public String getAuthor() {
        return "SnapiX";
    }

    @Override
    public String getIdentifier() {
        return "region";
    }

    @Override
    public String getVersion() {
        return "0.0.3-alpha";
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        World world = player.getWorld();

        // %region_snapiPass_name%
        String[] param = params.split("_");
        if (param.length < 2) return null;
        String regionName = param[0];
        String id = param[1];
        String additionalParameter = null;
        if (param.length == 3) {
            additionalParameter = param[2];
        }

        Region region = RegionRegistry.get().getRegion(world, regionName);
        if (region == null) return null;

        ProtectedBlock protectedBlock = region.protectedBlock();
        BlockOption blockOption = protectedBlock.blockOption();

        if (id.equalsIgnoreCase("owners")) return ListSerializer.serialize(region.owners());
        if (id.equalsIgnoreCase("members")) return ListSerializer.serialize(region.members());
        if (id.equalsIgnoreCase("homeLocation")) return LocationSerializer.serialize(region.homeLocation());
        if (id.equalsIgnoreCase("effects")) return ListSerializer.serialize(region.effects());
        if (id.equalsIgnoreCase("hasEffect") && additionalParameter != null)
            return Boolean.toString(region.hasEffect(additionalParameter));
        if (id.equalsIgnoreCase("activeEffects")) return ListSerializer.serialize(region.activeEffects());
        if (id.equalsIgnoreCase("hasActiveEffects")) return Boolean.toString(region.hasActiveEffects());
        if (id.equalsIgnoreCase("hasActiveEffect") && additionalParameter != null)
            return Boolean.toString(region.hasActiveEffect(additionalParameter));
        if (id.equalsIgnoreCase("location")) return LocationSerializer.serialize(protectedBlock.center());
        if (id.equalsIgnoreCase("blockMaterial")) return protectedBlock.blockMaterial().name();
        if (id.equalsIgnoreCase("radius")) return Integer.toString(blockOption.radius());
        if (id.equalsIgnoreCase("formattedRadius")) return blockOption.formatRadius();

        return null;
    }
}
