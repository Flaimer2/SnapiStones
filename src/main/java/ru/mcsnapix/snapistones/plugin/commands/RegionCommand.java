package ru.mcsnapix.snapistones.plugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.mcsnapix.snapistones.plugin.FakeProtectedRegion;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.api.ProtectedBlock;
import ru.mcsnapix.snapistones.plugin.api.SnapAPI;
import ru.mcsnapix.snapistones.plugin.api.SnapPlayer;
import ru.mcsnapix.snapistones.plugin.database.Database;
import ru.mcsnapix.snapistones.plugin.modules.home.HomeManager;
import ru.mcsnapix.snapistones.plugin.modules.home.HomeModule;
import ru.mcsnapix.snapistones.plugin.modules.home.settings.HomeConfig;
import ru.mcsnapix.snapistones.plugin.modules.upgrade.settings.UpgradeConfig;
import ru.mcsnapix.snapistones.plugin.settings.message.Message;
import ru.mcsnapix.snapistones.plugin.utils.BlockUtil;
import ru.mcsnapix.snapistones.plugin.utils.FormatterUtil;
import ru.mcsnapix.snapistones.plugin.utils.RegionUtil;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@CommandAlias("rg|region")
public class RegionCommand extends BaseCommand {
    private final SnapiStones plugin = SnapiStones.get();
    private final Message message = plugin.message().data();

    @Subcommand("info")
    @CommandCompletion("@regionlist")
    public void onInfo(Player player, String[] args) {
        SnapPlayer snapPlayer = SnapAPI.player(player);
        ProtectedRegion region;

        if (args.length == 0) {
            region = snapPlayer.regionUtil().getRegion(player.getLocation());
            if (region == null || region == snapPlayer.regionUtil().getGlobalRegion()) {
                snapPlayer.sendMessage(message.regionInfo().usage());
                return;
            }
            sendInfo(snapPlayer, region);
            return;
        }

        String id = args[0];
        if (!hasRegion(snapPlayer, id)) {
            return;
        }

        region = snapPlayer.regionUtil().getRegion(id);

        sendInfo(snapPlayer, region);
    }

    @Subcommand("list")
    @CommandCompletion("@myregionlistbymember")
    public void onList(Player player) {
        Audience p = plugin.adventure().player(player);
        SnapPlayer snapPlayer = SnapAPI.player(player);
        StringBuilder s = new StringBuilder();
        Component header = MiniMessage.miniMessage().deserialize(message.regionList().header());
        p.sendMessage(header);
        Iterator<ProtectedRegion> iterator = snapPlayer.memberRegions().iterator();
        int count = 0;
        while (iterator.hasNext()) {
            count++;
            ProtectedRegion region = iterator.next();
            Location center = snapPlayer.regionUtil().getCenter(region);
            s.append(message.regionList().region()
                    .replace("<count>", Integer.toString(count))
                    .replace("<region_id>", region.getId())
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
    @CommandCompletion("@myregionlistbyowner @players")
    public void onRemove(Player player, String[] args) {
        var command = message.command();
        var removeCommand = command.remove();

        SnapPlayer snapPlayer = SnapAPI.player(player);

        if (args.length == 0) {
            snapPlayer.sendMessage(removeCommand.usage());
            return;
        }

        String id = args[0];
        if (!hasRegion(snapPlayer, id)) {
            return;
        }

        ProtectedRegion region = snapPlayer.regionUtil().getRegion(id);
        snapPlayer = SnapAPI.player(player, region);

        if (!snapPlayer.hasOwnerPlayerInRegion()) {
            snapPlayer.sendMessage(message.notOwner());
            return;
        }

        if (args.length == 1) {
            snapPlayer.sendMessage(removeCommand.usageAlt());
            return;
        }

        String name = args[1];
        snapPlayer = SnapAPI.player(player, region, name);

        Database database = new Database(region);
        boolean member = database.isMember(name);
        boolean owner = database.isOwner(name);

        if (!(owner || member)) {
            snapPlayer.sendMessage(command.playerNotInRegion());
            return;
        }

        if (name.equalsIgnoreCase(snapPlayer.name())) {
            snapPlayer.sendMessage(removeCommand.cannotRemoveSelf());
            return;
        }

        if (member) {
            database.removeMember(name);
            region.getMembers().removePlayer(name);
        }

        if (owner) {
            database.removeOwner(name);
            region.getOwners().removePlayer(name);
        }

        snapPlayer.sendMessage(removeCommand.success());
    }

    @Subcommand("addmember")
    @CommandCompletion("@myregionlistbyowner @players")
    public void onAddMember(Player player, String[] args) {
        var command = message.command();
        var addCommand = command.addMember();

        SnapPlayer snapPlayer = SnapAPI.player(player);

        if (args.length == 0) {
            snapPlayer.sendMessage(addCommand.usage());
            return;
        }

        String id = args[0];
        if (!hasRegion(snapPlayer, id)) {
            return;
        }

        ProtectedRegion region = snapPlayer.regionUtil().getRegion(id);
        snapPlayer = SnapAPI.player(player, region);

        if (!snapPlayer.hasOwnerPlayerInRegion()) {
            snapPlayer.sendMessage(message.notOwner());
            return;
        }

        if (args.length == 1) {
            snapPlayer.sendMessage(addCommand.usageAlt());
            return;
        }

        String name = args[1];
        snapPlayer = SnapAPI.player(player, region, name);

        if (name.equalsIgnoreCase(snapPlayer.name())) {
            snapPlayer.sendMessage(addCommand.cannotAddSelf());
            return;
        }

        Database database = new Database(region);
        boolean isMember = database.isMember(name);
        boolean isOwner = database.isOwner(name);

        if (isMember || isOwner) {
            snapPlayer.sendMessage(command.playerAlreadyInRegion());
            return;
        }

        // ! MODULE UPGRADE
        UpgradeConfig upgradeConfig = plugin.module().upgrade().upgradeConfig().data();

        int maxMembers = upgradeConfig.limitMember().get(database.maxMembers());
        List<String> members = database.members();
        int member = 0;
        if (members != null) {
            member = members.size();
        }

        if (maxMembers <= member) {
            snapPlayer.sendMessage(upgradeConfig.message().limit().addMember());
            return;
        }
        // ! MODULE UPGRADE END

        region.getMembers().addPlayer(name);
        database.addMember(name);
        snapPlayer.sendMessage(addCommand.success());
    }

    @Subcommand("addowner")
    @CommandCompletion("@myregionlistbyowner @players")
    public void onAddOwner(Player player, String[] args) {
        var command = message.command();
        var addCommand = command.addOwner();

        SnapPlayer snapPlayer = SnapAPI.player(player);

        if (args.length == 0) {
            snapPlayer.sendMessage(addCommand.usage());
            return;
        }

        String id = args[0];
        if (!hasRegion(snapPlayer, id)) {
            return;
        }

        ProtectedRegion region = snapPlayer.regionUtil().getRegion(id);
        snapPlayer = SnapAPI.player(player, region);

        if (!snapPlayer.hasOwnerPlayerInRegion()) {
            snapPlayer.sendMessage(message.notOwner());
            return;
        }

        if (args.length == 1) {
            snapPlayer.sendMessage(addCommand.usageAlt());
            return;
        }

        String name = args[1];
        snapPlayer = SnapAPI.player(player, region, name);

        if (name.equalsIgnoreCase(snapPlayer.name())) {
            snapPlayer.sendMessage(addCommand.cannotAddSelf());
            return;
        }

        Database database = new Database(region);
        boolean isMember = database.isMember(name);
        boolean isOwner = database.isOwner(name);

        if (isOwner) {
            snapPlayer.sendMessage(command.playerAlreadyInRegion());
            return;
        }

        if (isMember) {
            region.getMembers().removePlayer(name);
        }

        // ! MODULE UPGRADE
        UpgradeConfig upgradeConfig = plugin.module().upgrade().upgradeConfig().data();

        int maxOwners = upgradeConfig.limitOwner().get(database.maxOwners());
        List<String> owners = database.owners();
        int owner = 0;
        if (owners != null) {
            owner = owners.size();
        }

        if (maxOwners <= owner) {
            snapPlayer.sendMessage(upgradeConfig.message().limit().addOwner());
            return;
        }
        // ! MODULE UPGRADE END

        region.getOwners().addPlayer(name);
        database.addOwner(name);
        snapPlayer.sendMessage(addCommand.success());
    }

    // ! MODULE HOME START
    @Subcommand("sethome")
    public void onSetHome(Player player, String[] ignoredArgs) {
        HomeModule home = plugin.module().home();
        HomeConfig config = home.homeConfig().data();
        RegionUtil regionUtil = new RegionUtil(player);

        ProtectedRegion region = regionUtil.getRegion(player.getLocation());

        if (region == null) {
            player.sendMessage(config.commands().setHome().notInRegion());
            return;
        }

        SnapPlayer snapPlayer = SnapAPI.player(player, region);

        if (!snapPlayer.hasOwnerInRegion()) {
            snapPlayer.sendMessage(config.commands().setHome().notOwner());
            return;
        }

        home.homeManager(snapPlayer).add(player.getLocation());
        player.sendMessage(config.commands().setHome().success());
    }

    @Subcommand("home")
    @CommandCompletion("@myregionhomelistbymember")
    public void onHome(Player player, String[] args) {
        HomeModule home = plugin.module().home();
        HomeConfig config = home.homeConfig().data();
        SnapPlayer snapPlayer = SnapAPI.player(player);

        List<ProtectedRegion> regionList = snapPlayer.memberRegions().stream().filter(r -> new Database(r).hasRegion()).collect(Collectors.toList());;
        int count = regionList.size();

        if (count == 1) {
            snapPlayer = SnapAPI.player(player, regionList.get(0));
            HomeManager homeManager = home.homeManager(snapPlayer);

            homeManager.teleport();
            return;
        }

        if (args.length == 0) {
            snapPlayer.sendMessage(config.commands().home().writeRegionName());
            return;
        }

        String id = args[0];
        if (!hasRegion(snapPlayer, id)) {
            return;
        }

        ProtectedRegion region = snapPlayer.regionUtil().getRegion(id);
        snapPlayer = SnapAPI.player(player, region);
        HomeManager homeManager = home.homeManager(snapPlayer);

        if (!snapPlayer.hasPlayerInRegion()) {
            snapPlayer.sendMessage(config.commands().home().noMember());
            return;
        }

        homeManager.teleport();
    }
    // ! MODULE HOME END

    @Default
    @HelpCommand
    public void onHelp(CommandSender sender) {
        sender.sendMessage(message.help().toArray(new String[0]));
    }

    private void sendInfo(SnapPlayer player, ProtectedRegion region) {
        RegionUtil regionUtil = new RegionUtil(player.player());
        if (region != null) {
            ProtectedBlock protectedBlock = BlockUtil.protectedBlock(regionUtil.getCenter(region));
            SnapPlayer snapPlayer = SnapAPI.player(player.player(), region, protectedBlock);
            snapPlayer.sendMessage(message.regionInfo().message());
            return;
        }
        plugin.log().error("Player or Region is null");
    }

    private boolean hasRegion(SnapPlayer player, String id) {
        if (!player.regionUtil().hasRegion(id)) {
            ProtectedRegion region = new FakeProtectedRegion(id, player.player());
            SnapPlayer snapPlayer = SnapAPI.player(player.player(), region);
            snapPlayer.sendMessage(message.regionNotExist());
            return false;
        }

        return true;
    }
}