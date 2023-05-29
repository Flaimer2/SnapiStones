package ru.mcsnapix.snapistones.plugin.api;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.modules.upgrade.settings.EffectOptions;
import ru.mcsnapix.snapistones.plugin.utils.PlaceholderParser;
import ru.mcsnapix.snapistones.plugin.utils.RegionUtil;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Accessors(fluent = true)
@Getter
public class SnapPlayer {
    private final SnapiStones plugin = SnapiStones.get();

    @NonNull private final Player player;
    @NonNull private final PlaceholderParser placeholderParser;
    private ProtectedRegion region;

    private final LocalPlayer localPlayer;
    private final RegionUtil regionUtil;

    protected SnapPlayer(@NonNull Player player, @NonNull PlaceholderParser placeholderParser) {
        this.player = player;
        this.placeholderParser = placeholderParser;

        localPlayer = plugin.worldGuard().wrapPlayer(player);
        regionUtil = new RegionUtil(player);
    }

    protected SnapPlayer(@NonNull Player player,  @NonNull ProtectedRegion region, @NonNull PlaceholderParser placeholderParser) {
        this.player = player;
        this.region = region;
        this.placeholderParser = placeholderParser;

        localPlayer = plugin.worldGuard().wrapPlayer(player);
        regionUtil = new RegionUtil(player);
    }

    public void sendMessage(@NonNull String message) {
        String parsedMessage = placeholderParser.parseString(ChatColor.translateAlternateColorCodes('&', message));

        player.sendMessage(parsedMessage);
    }

    public void sendMessage(@NonNull List<String> messageList) {
        for (String message : messageList) {
            sendMessage(message);
        }
    }

    public String createRegionID(String symbol) {
        int regionCount = ownerRegions().size() + 1;
        return name() + "_" + symbol + regionCount;
    }

    public boolean hasPlayerInRegion() {
        if (region != null) {
            return hasPlayerInRegion(region);
        }

        return false;
    }

    public boolean hasPlayerInRegion(@NonNull ProtectedRegion region) {
        return region.getOwners().contains(uuid()) || region.getMembers().contains(uuid());
    }

    public boolean hasOwnerInRegion() {
        if (region != null) {
            return hasOwnerInRegion(region);
        }

        return false;
    }

    public boolean hasOwnerInRegion(@NonNull ProtectedRegion region) {
        return region.getOwners().contains(uuid());
    }

    public boolean hasOwnerPlayerInRegion() {
        if (region != null) {
            return hasOwnerPlayerInRegion(region);
        }

        return false;
    }

    public boolean hasOwnerPlayerInRegion(@NonNull ProtectedRegion region) {
        return region.getOwners().contains(uuid());
    }

    public List<ProtectedRegion> memberRegions() {
        return regionUtil.getRegions().stream().filter(this::hasPlayerInRegion).collect(Collectors.toList());
    }

    public List<ProtectedRegion> ownerRegions() {
        return regionUtil.getRegions().stream().filter(r -> r.isOwner(localPlayer)).collect(Collectors.toList());
    }

    public void addPotionEffect(EffectOptions effectOptions) {
        String effect = effectOptions.effect();
        int level = effectOptions.level();

        player.addPotionEffect(PotionEffectType.getByName(effect).createEffect(Integer.MAX_VALUE, level));
    }

    public void removePotionEffect(PotionEffect effect) {
        player.removePotionEffect(effect.getType());
    }

    public Collection<PotionEffect> activePotionEffects() {
        return player.getActivePotionEffects();
    }

    public void teleport(Location location) {
        player.teleport(location);
    }

    public String name() {
        return player.getName();
    }

    public UUID uuid() {
        return player.getUniqueId();
    }
}