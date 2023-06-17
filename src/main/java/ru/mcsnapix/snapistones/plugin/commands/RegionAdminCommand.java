package ru.mcsnapix.snapistones.plugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.api.SnapApi;
import ru.mcsnapix.snapistones.plugin.api.region.Region;
import ru.mcsnapix.snapistones.plugin.modules.Modules;
import ru.mcsnapix.snapistones.plugin.modules.upgrades.config.EffectOptions;
import ru.mcsnapix.snapistones.plugin.settings.message.Message;

@CommandAlias("rga|regionadmin")
public class RegionAdminCommand extends BaseCommand {
    private final SnapiStones plugin = SnapiStones.get();
    private final Message message = plugin.getMessage().data();
    private final Modules modules = plugin.getModules();

    @Subcommand("addeffect")
    @SuppressWarnings("unused") // Used by ACF
    public void onAddEffect(CommandSender sender, String[] args) {
        String id = args[0];
        Region region = SnapApi.getRegion(id);

        if (region == null) {
            sender.sendMessage(message.regionNotExist().replace("%region_id%", id));
            return;
        }
        String effectName = args[1];
        EffectOptions effectOptions = modules.upgrade().upgradeConfig().data().effects().get(effectName);
        if (effectOptions == null) {
            sender.sendMessage("Эффекта нет");
            return;
        }
        region.addEffect(effectName);
    }


    @Subcommand("addactiveeffect")
    @SuppressWarnings("unused") // Used by ACF
    public void onSetActiveEffect(CommandSender sender, String[] args) {
        String id = args[0];
        Region region = SnapApi.getRegion(id);

        if (region == null) {
            sender.sendMessage(message.regionNotExist().replace("%region_id%", id));
            return;
        }
        String effectName = args[1];
        EffectOptions effectOptions = modules.upgrade().upgradeConfig().data().effects().get(effectName);
        if (effectOptions == null) {
            sender.sendMessage("Эффекта нет");
            return;
        }
        region.addActiveEffect(effectName);

        for (Player player : region.playersInRegion()) {
            player.addPotionEffect(PotionEffectType.getByName(effectOptions.effect()).createEffect(Integer.MAX_VALUE, effectOptions.level()));
        }
    }

    @Subcommand("removeactiveeffect")
    @SuppressWarnings("unused") // Used by ACF
    public void onRemoveActiveEffect(CommandSender sender, String[] args) {
        String id = args[0];
        Region region = SnapApi.getRegion(id);

        if (region == null) {
            sender.sendMessage(message.regionNotExist().replace("%region_id%", id));
            return;
        }
        String effectName = args[1];
        EffectOptions effectOptions = modules.upgrade().upgradeConfig().data().effects().get(effectName);
        if (effectOptions == null) {
            sender.sendMessage("Эффекта нет");
            return;
        }
        region.removeActiveEffect(effectName);

        for (Player player : region.playersInRegion()) {
            player.removePotionEffect(PotionEffectType.getByName(effectOptions.effect()));
        }
    }
}
