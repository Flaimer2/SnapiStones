package ru.mcsnapix.snapistones.plugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.mcsnapix.snapistones.plugin.Placeholders;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.api.ProtectedBlock;
import ru.mcsnapix.snapistones.plugin.api.SnapApi;
import ru.mcsnapix.snapistones.plugin.api.region.Region;
import ru.mcsnapix.snapistones.plugin.api.region.RegionRegistry;
import ru.mcsnapix.snapistones.plugin.database.Database;
import ru.mcsnapix.snapistones.plugin.modules.Modules;
import ru.mcsnapix.snapistones.plugin.modules.home.HomeModule;
import ru.mcsnapix.snapistones.plugin.modules.home.config.HomeConfig;
import ru.mcsnapix.snapistones.plugin.modules.upgrades.config.UpgradeConfig;
import ru.mcsnapix.snapistones.plugin.settings.message.Message;
import ru.mcsnapix.snapistones.plugin.util.FormatterUtil;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@CommandAlias("rg|region")
public class RegionCommand extends BaseCommand {
    private final SnapiStones plugin = SnapiStones.get();
    private final Message message = plugin.getMessage().data();
    private final Modules modules = plugin.getModules();

    @Subcommand("info")
    @CommandCompletion("@regionlist")
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
        }

        String id = args[0];
        region = SnapApi.getRegion(id);

        if (region == null) {
            placeholders.sendMessage(message.regionNotExist());
            return;
        }

        sendInfo(player, region);
    }

    @Subcommand("list")
    @CommandCompletion("@regionlistbyplayer")
    public void onList(Player player) {
        Audience p = plugin.adventure().player(player);
        StringBuilder s = new StringBuilder();
        Component header = MiniMessage.miniMessage().deserialize(message.regionList().header());
        p.sendMessage(header);
        Iterator<Region> iterator = SnapApi.getRegionsByMember(player.getName()).iterator();
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
    @CommandCompletion("@regionlistbyowner @players")
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
            placeholders.sendMessage(message.regionNotExist());
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

        boolean member = region.hasMemberInRegion(name);
        boolean owner = region.hasOwnerInRegion(name);

        if (!(owner || member)) {
            placeholders.sendMessage(command.playerNotInRegion());
            return;
        }

        if (member) {
            region.removeMember(name);
        }

        if (owner) {
            region.removeOwner(name);
        }

        placeholders.sendMessage(removeCommand.success());
    }

    @Subcommand("addmember")
    @CommandCompletion("@regionlistbyowner @players")
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
            placeholders.sendMessage(message.regionNotExist());
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

        // ! MODULE UPGRADE
        UpgradeConfig upgradeConfig = modules.upgrade().upgradeConfig().data();

        int maxMembers = upgradeConfig.limitMember().get(region.maxMembers());
        int member = region.members().size();

        if (maxMembers <= member) {
            placeholders.sendMessage(upgradeConfig.message().limit().addMember());
            return;
        }
        // ! MODULE UPGRADE END

        region.addMember(name);
        placeholders.sendMessage(addCommand.success());
    }

    @Subcommand("addowner")
    @CommandCompletion("@regionlistbyowner @players")
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
            placeholders.sendMessage(message.regionNotExist());
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

        // ! MODULE UPGRADE
        UpgradeConfig upgradeConfig = modules.upgrade().upgradeConfig().data();

        int maxOwners = upgradeConfig.limitOwner().get(region.maxOwners());
        int owner = region.owners().size();

        if (maxOwners <= owner) {
            placeholders.sendMessage(upgradeConfig.message().limit().addOwner());
            return;
        }
        // ! MODULE UPGRADE END

        region.addOwner(name);
        placeholders.sendMessage(addCommand.success());
    }

    // ! MODULE HOME START
    @Subcommand("sethome")
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
            placeholders.sendMessage(message.regionNotExist());
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
    public void onHelp(CommandSender sender) {
        sender.sendMessage(message.help().toArray(new String[0]));
    }

    private void sendInfo(Player player, Region region) {
        Placeholders placeholders = new Placeholders(player, region);
        placeholders.sendMessage(message.regionInfo().message());
    }
}
