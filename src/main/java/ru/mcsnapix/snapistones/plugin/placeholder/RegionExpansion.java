package ru.mcsnapix.snapistones.plugin.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.api.ProtectedBlock;
import ru.mcsnapix.snapistones.plugin.api.region.Region;
import ru.mcsnapix.snapistones.plugin.api.region.RegionRegistry;
import ru.mcsnapix.snapistones.plugin.modules.upgrades.config.UpgradeConfig;
import ru.mcsnapix.snapistones.plugin.serializers.ListSerializer;
import ru.mcsnapix.snapistones.plugin.serializers.LocationSerializer;
import ru.mcsnapix.snapistones.plugin.settings.config.block.BlockOption;
import ru.mcsnapix.snapistones.plugin.util.FormatterUtil;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class RegionExpansion extends PlaceholderExpansion {
    @Override
    public @NotNull String getAuthor() {
        return "SnapiX";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "region";
    }

    @Override
    public @NotNull String getVersion() {
        return "2.0.0-beta";
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        String[] param = params.split(":");
        if (param.length < 2) return null;
        String regionName = param[0];
        String id = param[1];
        String additionalParameter = null;
        if (param.length == 3) {
            additionalParameter = param[2];
        }

        Region region = RegionRegistry.get().getRegion(regionName);
        if (region == null) return null;

        ProtectedBlock protectedBlock = region.protectedBlock();
        BlockOption blockOption = protectedBlock.blockOption();
        UpgradeConfig upgradeConfig = SnapiStones.get().getModules().upgrade().upgradeConfig().data();

        if (id.equalsIgnoreCase("author")) return region.author();
        if (id.equalsIgnoreCase("owners")) return ListSerializer.serialise(region.owners());
        if (id.equalsIgnoreCase("has_owner") && additionalParameter != null)
            return Boolean.toString(region.hasOwnerInRegion(additionalParameter));
        if (id.equalsIgnoreCase("owners_formatted")) return FormatterUtil.formatList(region.owners());
        if (id.equalsIgnoreCase("owners_size")) return Integer.toString(region.owners().size());
        if (id.equalsIgnoreCase("members")) return ListSerializer.serialise(region.members());
        if (id.equalsIgnoreCase("members_formatted")) return FormatterUtil.formatList(region.members());
        if (id.equalsIgnoreCase("members_size")) return Integer.toString(region.members().size());
        if (id.equalsIgnoreCase("homeLocation")) return LocationSerializer.serialise(region.homeLocation());
        if (id.equalsIgnoreCase("has_home")) return FormatterUtil.formatPlaceBoolean(region.hasHomeLocation());
        if (id.equalsIgnoreCase("effects")) return ListSerializer.serialise(region.effects());
        if (id.equalsIgnoreCase("hasEffect") && additionalParameter != null)
            return Boolean.toString(region.hasEffect(additionalParameter));
        if (id.equalsIgnoreCase("activeEffects")) return ListSerializer.serialise(region.activeEffects());
        if (id.equalsIgnoreCase("hasActiveEffects")) return Boolean.toString(region.hasActiveEffects());
        if (id.equalsIgnoreCase("hasActiveEffect") && additionalParameter != null)
            return Boolean.toString(region.hasActiveEffect(additionalParameter));
        if (id.equalsIgnoreCase("date")) return getTime(region.date() * 1000L);
        if (id.equalsIgnoreCase("maxOwners"))
            return Integer.toString(upgradeConfig.limitOwner().get(region.maxOwners()));
        if (id.equalsIgnoreCase("maxMembers"))
            return Integer.toString(upgradeConfig.limitMember().get(region.maxMembers()));
        if (id.equalsIgnoreCase("location")) return FormatterUtil.formatLocation(protectedBlock.center());
        if (id.equalsIgnoreCase("blockMaterial")) return protectedBlock.blockMaterialName();
        if (id.equalsIgnoreCase("radius")) return Integer.toString(blockOption.radius());
        if (id.equalsIgnoreCase("formattedRadius")) return blockOption.formatRadius();

        return null;
    }

    private String getTime(long millis) {
        var instance = java.time.Instant.ofEpochMilli(millis);
        var zonedDateTime = java.time.ZonedDateTime
                .ofInstant(instance, ZoneId.of("Europe/Moscow"));

        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        return zonedDateTime.format(formatter);
    }
}
