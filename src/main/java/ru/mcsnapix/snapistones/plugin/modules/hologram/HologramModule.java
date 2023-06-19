package ru.mcsnapix.snapistones.plugin.modules.hologram;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.api.holograms.HologramManager;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.mcsnapix.snapistones.plugin.Placeholders;
import ru.mcsnapix.snapistones.plugin.SnapiStones;
import ru.mcsnapix.snapistones.plugin.api.ProtectedBlock;
import ru.mcsnapix.snapistones.plugin.api.SnapApi;
import ru.mcsnapix.snapistones.plugin.api.region.Region;
import ru.mcsnapix.snapistones.plugin.modules.IModule;
import ru.mcsnapix.snapistones.plugin.modules.Modules;
import ru.mcsnapix.snapistones.plugin.modules.hologram.config.HologramConfig;
import ru.mcsnapix.snapistones.plugin.modules.hologram.config.HologramOption;
import ru.mcsnapix.snapistones.plugin.settings.Configuration;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class HologramModule implements IModule {
    private final HologramManager hologramManager = DecentHologramsAPI.get().getHologramManager();
    private final Modules modules;
    private Configuration<HologramConfig> hologramConfig;

    @Override
    public void enable() {
        SnapiStones plugin = modules.getPlugin();
        hologramConfig = Configuration.create(plugin,
                modules.getPathSettings(),
                "hologram.yml",
                HologramConfig.class,
                plugin.getOptions()
        );
        plugin.getServer().getPluginManager().registerEvents(new HologramListener(this), plugin);
        plugin.getMorePaperLib().scheduling().asyncScheduler().run(() -> {
            SnapApi.getRegions().forEach(this::createHologram);

            for (Player p : Bukkit.getOnlinePlayers()) {
                for (Region region : SnapApi.getRegionsByPlayer(p.getName())) {
                    updateHologramForPlayer(region, p);
                }
            }
        });
    }

    @Override
    public void reload() {
        hologramConfig.reloadConfig();
    }

    @Override
    public void disable() {
        SnapApi.getRegions().forEach(this::removeHologram);
    }

    public void createHologram(Region region) {
        if (hologramManager.getHologram(region.name()) != null) return;
        Placeholders placeholders = new Placeholders(region);
        ProtectedBlock protectedBlock = region.protectedBlock();
        String id = region.name();
        String blockMaterialName = protectedBlock.blockMaterialName();
        HologramOption hologramOption = hologramConfig.data().blocks().get(blockMaterialName);
        if (hologramOption == null) return;

        Hologram hologram = DHAPI.createHologram(id, protectedBlock.center().add(0.5, hologramOption.otherPlayerHeight(), 0.5), placeholders.replacePlaceholders(hologramOption.otherPlayerLines()));
        HologramPage page = hologram.addPage();
        if (page != null) {
            for (String s : hologramOption.lines()) {
                page.addLine(new HologramLine(page, page.getNextLineLocation(), placeholders.replacePlaceholders(s)));
            }
        }

        hologram.showAll();
        hologram.setDisplayRange(hologramOption.viewDistance());

        hologram.save();

        hologramManager.registerHologram(hologram);
    }

    public void removeHologram(Region region) {
        String id = region.name();
        Hologram hologram = hologramManager.getHologram(id);
        if (hologram == null) return;
        hologram.delete();
        hologramManager.removeHologram(id);
    }

    public void changeHologramForPlayer(Player player, Hologram hologram, int page) {
        hologram.show(player, page);
    }

    public void updateHologramForPlayer(Region region, Player player) {
        if (region == null) return;
        if (player == null) return;

        ProtectedBlock protectedBlock = region.protectedBlock();
        String blockMaterialName = protectedBlock.blockMaterialName();
        HologramOption hologramOption = hologramConfig.data().blocks().get(blockMaterialName);
        Hologram hologram = hologramManager.getHologram(region.name());

        if (hologram == null) return;
        if (region.hasPlayerInRegion(player.getName())) {
            changeHologramForPlayer(player, hologram, 1);
            updateHeight(hologram, hologramOption.height(), 1);
        } else {
            changeHologramForPlayer(player, hologram, 0);
        }
    }

    private void updateHeight(Hologram hologram, double height, int page) {
        HologramPage hologramPage = hologram.getPage(page);
        for (HologramLine line : hologramPage.getLines()) {
            line.setOffsetY(height);
        }
        hologram.realignLines();
        hologram.save();
    }
}
