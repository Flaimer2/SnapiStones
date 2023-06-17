package ru.mcsnapix.snapistones.plugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.alessiodp.lastloginapi.api.LastLogin;
import com.alessiodp.lastloginapi.api.interfaces.LastLoginAPI;
import com.alessiodp.lastloginapi.api.interfaces.LastLoginPlayer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.mcsnapix.snapistones.plugin.Placeholders;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.api.SnapApi;
import ru.mcsnapix.snapistones.plugin.api.region.Region;
import ru.mcsnapix.snapistones.plugin.modules.Modules;
import ru.mcsnapix.snapistones.plugin.modules.home.config.HomeConfig;
import ru.mcsnapix.snapistones.plugin.modules.upgrades.config.UpgradeConfig;
import ru.mcsnapix.snapistones.plugin.settings.config.MainConfig;
import ru.mcsnapix.snapistones.plugin.settings.message.Message;
import ru.mcsnapix.snapistones.plugin.util.FormatterUtil;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@CommandAlias("rg|region")
public class RegionCommand extends BaseCommand {
    private final LastLoginAPI lastLoginAPI = LastLogin.getApi();
    private final SnapiStones plugin = SnapiStones.get();
    private final Message message = plugin.getMessage().data();
    private final Modules modules = plugin.getModules();

    @Subcommand("info")
    @CommandCompletion("@regionlist")
    @SuppressWarnings("unused") // Used by ACF
    public void onInfo(Player player, String[] args) {
        Placeholders placeholders = new Placeholders(player);
        Region region;

        if (args.length == 0) {
            region = SnapApi.getRegion(player.getLocation());
            if (region == null) {
                placeholders.sendMessage(message.regionInfo().usage());
                return;
            }
            sendInfo(player, region);
            return;
        }

        String id = args[0];
        region = SnapApi.getRegion(id);

        if (region == null) {
            placeholders.sendMessage(message.regionNotExist().replace("%region_id%", id));
            return;
        }

        sendInfo(player, region);
    }

    @Subcommand("list")
    @SuppressWarnings("unused") // Used by ACF
    public void onList(Player player) {
        Audience p = plugin.adventure().player(player);
        StringBuilder s = new StringBuilder();
        Component header = MiniMessage.miniMessage().deserialize(message.regionList().header());
        p.sendMessage(header);
        Iterator<Region> iterator = SnapApi.getRegionsByPlayer(player.getName()).iterator();
        int count = 0;
        while (iterator.hasNext()) {
            count++;
            Region region = iterator.next();
            Location center = region.protectedBlock().center();
            s.append(message.regionList().region()
                    .replace("<count>", Integer.toString(count))
                    .replace("<region_id>", region.name())
                    .replace("<location>", FormatterUtil.formatLocation(center))
            );
            if (iterator.hasNext()) {
                s.append("<newline>");
            }
        }
        if (count == 0) {
            s.append(message.regionList().noRegion());
        }
        s.append("<newline>");
        Component main = MiniMessage.miniMessage().deserialize(s.toString());
        p.sendMessage(main);
    }

    @Subcommand("remove")
    @CommandCompletion("@regionlistbyowner @playerinregion")
    @SuppressWarnings("unused") // Used by ACF
    public void onRemove(Player player, String[] args) {
        Placeholders placeholders = new Placeholders(player);
        var command = message.command();
        var removeCommand = command.remove();

        if (args.length == 0) {
            placeholders.sendMessage(removeCommand.usage());
            return;
        }

        String id = args[0];
        Region region = SnapApi.getRegion(id);
        placeholders.setRegion(region);

        if (region == null) {
            placeholders.sendMessage(message.regionNotExist().replace("%region_id%", id));
            return;
        }

        if (!region.hasOwnerInRegion(player.getName())) {
            placeholders.sendMessage(message.notOwner());
            return;
        }

        if (args.length == 1) {
            placeholders.sendMessage(removeCommand.usageAlt());
            return;
        }

        String name = args[1];
        placeholders.setOtherPlayer(name);

        if (name.equalsIgnoreCase(player.getName())) {
            placeholders.sendMessage(removeCommand.cannotRemoveSelf());
            return;
        }

        LastLoginPlayer lastLoginPlayer = lastLoginAPI.getPlayerByName(name).stream().findFirst().orElse(null);
        if (lastLoginPlayer == null) {
            placeholders.sendMessage(command.playerNotFound());
            return;
        }

        boolean member = region.hasMemberInRegion(lastLoginPlayer.getName());
        boolean owner = region.hasOwnerInRegion(lastLoginPlayer.getName());

        if (!(owner || member)) {
            placeholders.sendMessage(command.playerNotInRegion());
            return;
        }

        if (member) {
            region.removeMember(lastLoginPlayer.getName());
        }

        if (owner) {
            region.removeOwner(lastLoginPlayer.getName());
        }

        placeholders.sendMessage(removeCommand.success());
    }

    @Subcommand("addmember")
    @CommandCompletion("@regionlistbyowner @players")
    @SuppressWarnings("unused") // Used by ACF
    public void onAddMember(Player player, String[] args) {
        Placeholders placeholders = new Placeholders(player);
        var command = message.command();
        var addCommand = command.addMember();

        if (args.length == 0) {
            placeholders.sendMessage(addCommand.usage());
            return;
        }

        String id = args[0];
        Region region = SnapApi.getRegion(id);
        placeholders.setRegion(region);

        if (region == null) {
            placeholders.sendMessage(message.regionNotExist().replace("%region_id%", id));
            return;
        }

        if (!region.hasOwnerInRegion(player.getName())) {
            placeholders.sendMessage(message.notOwner());
            return;
        }

        if (args.length == 1) {
            placeholders.sendMessage(addCommand.usageAlt());
            return;
        }

        String name = args[1];
        placeholders.setOtherPlayer(name);

        if (name.equalsIgnoreCase(player.getName())) {
            placeholders.sendMessage(addCommand.cannotAddSelf());
            return;
        }

        if (region.hasPlayerInRegion(name)) {
            placeholders.sendMessage(command.playerAlreadyInRegion());
            return;
        }

        LastLoginPlayer lastLoginPlayer = lastLoginAPI.getPlayerByName(name).stream().findFirst().orElse(null);
        if (lastLoginPlayer == null) {
            placeholders.sendMessage(command.playerNotFound());
            return;
        }

        // ! MODULE UPGRADE
        UpgradeConfig upgradeConfig = modules.upgrade().upgradeConfig().data();

        int maxMembers = upgradeConfig.limitMember().get(region.maxMembers());
        int member = region.members().size();

        if (maxMembers <= member) {
            placeholders.sendMessage(upgradeConfig.message().limit().addMember());
            return;
        }
        // ! MODULE UPGRADE END

        region.addMember(lastLoginPlayer.getName());
        placeholders.sendMessage(addCommand.success());
    }

    @Subcommand("addowner")
    @CommandCompletion("@regionlistbyowner @players")
    @SuppressWarnings("unused") // Used by ACF
    public void onAddOwner(Player player, String[] args) {
        Placeholders placeholders = new Placeholders(player);
        var command = message.command();
        var addCommand = command.addOwner();

        if (args.length == 0) {
            placeholders.sendMessage(addCommand.usage());
            return;
        }

        String id = args[0];
        Region region = SnapApi.getRegion(id);
        placeholders.setRegion(region);

        if (region == null) {
            placeholders.sendMessage(message.regionNotExist().replace("%region_id%", id));
            return;
        }

        if (!region.hasOwnerInRegion(player.getName())) {
            placeholders.sendMessage(message.notOwner());
            return;
        }

        if (args.length == 1) {
            placeholders.sendMessage(addCommand.usageAlt());
            return;
        }

        String name = args[1];
        placeholders.setOtherPlayer(name);

        if (name.equalsIgnoreCase(player.getName())) {
            placeholders.sendMessage(addCommand.cannotAddSelf());
            return;
        }

        if (region.hasMemberInRegion(name)) {
            placeholders.sendMessage(command.playerAlreadyInRegion());
            return;
        }

        if (region.hasOwnerInRegion(name)) {
            region.removeMember(name);
        }

        LastLoginPlayer lastLoginPlayer = lastLoginAPI.getPlayerByName(name).stream().findFirst().orElse(null);
        if (lastLoginPlayer == null) {
            placeholders.sendMessage(command.playerNotFound());
            return;
        }

        // ! MODULE UPGRADE
        UpgradeConfig upgradeConfig = modules.upgrade().upgradeConfig().data();

        int maxOwners = upgradeConfig.limitOwner().get(region.maxOwners());
        int owner = region.owners().size();

        if (maxOwners <= owner) {
            placeholders.sendMessage(upgradeConfig.message().limit().addOwner());
            return;
        }
        // ! MODULE UPGRADE END

        region.addOwner(lastLoginPlayer.getName());
        placeholders.sendMessage(addCommand.success());
    }

    // ! MODULE HOME START
    @Subcommand("sethome")
    @SuppressWarnings("unused") // Used by ACF
    public void onSetHome(Player player, String[] ignoredArgs) {
        HomeConfig config = modules.home().homeConfig().data();
        Region region = SnapApi.getRegion(player.getLocation());
        Placeholders placeholders = new Placeholders(player, region);

        if (region == null) {
            placeholders.sendMessage(config.commands().setHome().notInRegion());
            return;
        }

        if (!region.hasOwnerInRegion(player.getName())) {
            placeholders.sendMessage(config.commands().setHome().notOwner());
            return;
        }

        region.homeLocation(player.getLocation());
        placeholders.sendMessage(config.commands().setHome().success());
    }

    @Subcommand("home")
    @CommandCompletion("@regionhomelistbyplayer")
    @SuppressWarnings("unused") // Used by ACF
    public void onHome(Player player, String[] args) {
        HomeConfig config = modules.home().homeConfig().data();
        List<Region> regionWithHomeList = SnapApi.getRegionsByPlayer(player.getName()).stream().filter(Region::hasHomeLocation).collect(Collectors.toList());
        int count = regionWithHomeList.size();

        if (count == 1) {
            Region region = regionWithHomeList.get(0);
            region.teleportHomeLocation(player);
            return;
        }

        Placeholders placeholders = new Placeholders(player);

        if (args.length == 0) {
            placeholders.sendMessage(config.commands().home().writeRegionName());
            return;
        }

        String id = args[0];
        Region region = SnapApi.getRegion(id);
        placeholders.setRegion(region);

        if (region == null) {
            placeholders.sendMessage(message.regionNotExist().replace("%region_id%", id));
            return;
        }

        if (!region.hasPlayerInRegion(player.getName())) {
            placeholders.sendMessage(config.commands().home().noMember());
            return;
        }

        region.teleportHomeLocation(player);
    }
    // ! MODULE HOME END

    @Default
    @HelpCommand
    @SuppressWarnings("unused") // Used by ACF
    public void onHelp(CommandSender sender) {
        sender.sendMessage(message.help().toArray(new String[0]));
    }

    @Subcommand("menu")
    @CommandCompletion("@regionlistbyplayer")
    @SuppressWarnings("unused") // Used by ACF
    public void onMenu(Player player, String[] args) {
        MainConfig config = plugin.getMainConfig().data();
        Placeholders placeholders = new Placeholders(player);
        List<Region> regionByPlayer = SnapApi.getRegionsByPlayer(player.getName());
        int count = regionByPlayer.size();

        if (count == 1) {
            Region region = regionByPlayer.get(0);
            placeholders.setRegion(region);
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), placeholders.replacePlaceholders(config.menuOpenCommand()));
            return;
        }

        if (args.length == 0) {
            placeholders.sendMessage(message.command().menu().usage());
            return;
        }

        String id = args[0];
        Region region = SnapApi.getRegion(id);
        if (region == null) {
            placeholders.sendMessage(message.regionNotExist().replace("%region_id%", id));
            return;
        }
        placeholders.setRegion(region);
        if (!region.hasPlayerInRegion(player.getName())) {
            placeholders.sendMessage(message.command().menu().noMember());
            return;
        }

        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), placeholders.replacePlaceholders(config.menuOpenCommand()));
    }

    private void sendInfo(Player player, Region region) {
        Placeholders placeholders = new Placeholders(player, region);
        placeholders.sendMessage(message.regionInfo().message());
    }
}
