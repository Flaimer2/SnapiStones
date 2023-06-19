package ru.mcsnapix.snapistones.plugin;

import co.aikar.idb.DB;
import co.aikar.idb.Database;
import co.aikar.idb.DatabaseOptions;
import co.aikar.idb.PooledDatabaseOptions;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import ru.mcsnapix.snapistones.plugin.api.region.RegionRegistry;
import ru.mcsnapix.snapistones.plugin.commands.Commands;
import ru.mcsnapix.snapistones.plugin.handlers.BlockHandler;
import ru.mcsnapix.snapistones.plugin.handlers.PlayerHandler;
import ru.mcsnapix.snapistones.plugin.handlers.ProtectedBlockHandler;
import ru.mcsnapix.snapistones.plugin.listeners.RegionListener;
import ru.mcsnapix.snapistones.plugin.modules.Modules;
import ru.mcsnapix.snapistones.plugin.placeholder.RegionExpansion;
import ru.mcsnapix.snapistones.plugin.settings.Configuration;
import ru.mcsnapix.snapistones.plugin.settings.config.MainConfig;
import ru.mcsnapix.snapistones.plugin.settings.config.MySQLConfig;
import ru.mcsnapix.snapistones.plugin.settings.config.block.BlockConfig;
import ru.mcsnapix.snapistones.plugin.settings.message.Message;
import ru.mcsnapix.snapistones.plugin.util.WGRegionUtil;
import space.arim.dazzleconf.ConfigurationOptions;
import space.arim.dazzleconf.sorter.AnnotationBasedSorter;
import space.arim.morepaperlib.MorePaperLib;

@Getter
public final class SnapiStones extends JavaPlugin {
    private static SnapiStones plugin;
    private final Logger log = getSLF4JLogger();
    private MorePaperLib morePaperLib;
    private Modules modules;
    private Commands commands;
    private ConfigurationOptions options;
    private BukkitAudiences adventure;
    private Configuration<MainConfig> mainConfig;
    private Configuration<MySQLConfig> mysqlConfig;
    private Configuration<Message> message;
    private Configuration<BlockConfig> blockConfig;
    private WorldGuardPlugin worldGuard;

    public static SnapiStones get() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        morePaperLib = new MorePaperLib(plugin);
        PluginManager pluginManager = getServer().getPluginManager();
        loadWorldGuard();
        loadConfigs();
        enableMySQL();
        registerRegions();
        modules = new Modules();
        commands = new Commands();
        adventure = BukkitAudiences.create(this);
        registerHandlers(pluginManager);
        registerListeners(pluginManager);
        if (isPluginEnable("PlaceholderAPI")) {
            new RegionExpansion().register();
        }
    }

    public void onReload() {
        reloadConfigs();
        modules.reloadModules();
    }

    @Override
    public void onDisable() {
        modules.disableModules();
        if (adventure != null) {
            adventure.close();
            adventure = null;
        }
        disableMySQL();
    }

    private void loadWorldGuard() {
        if (!(isPluginEnable("WorldGuard") && isPluginEnable("WorldEdit"))) {
            log.error("§cWorldGuard или WorldEdit не работает! Плагин SnapiStones выключается");
            getServer().getPluginManager().disablePlugin(plugin);
            return;
        }
        worldGuard = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
    }

    private void loadConfigs() {
        options = new ConfigurationOptions.Builder()
                .setCreateSingleElementCollections(true)
                .sorter(new AnnotationBasedSorter())
                .build();
        mainConfig = Configuration.create(plugin, "config.yml", MainConfig.class, options);
        mysqlConfig = Configuration.create(plugin, "mysql.yml", MySQLConfig.class, options);
        message = Configuration.create(plugin, "message.yml", Message.class, options);
        blockConfig = Configuration.create(plugin, "block.yml", BlockConfig.class, options);

        log.info("§aКонфиги загрузились");
    }

    private void enableMySQL() {
        MySQLConfig config = mysqlConfig.data();
        DatabaseOptions databaseOptions = DatabaseOptions.builder().mysql(config.username(), config.password(), config.database(), config.host()).build();
        Database db = PooledDatabaseOptions.builder().options(databaseOptions).createHikariDatabase();
        DB.setGlobalDatabase(db);

        log.info("§aMySQL запустился");
    }

    private void registerHandlers(PluginManager pluginManager) {
        pluginManager.registerEvents(new BlockHandler(plugin), plugin);
        pluginManager.registerEvents(new ProtectedBlockHandler(plugin), plugin);
        pluginManager.registerEvents(new PlayerHandler(plugin), plugin);

        log.info("§aHandlers загрузились");
    }

    private void registerListeners(PluginManager pluginManager) {
        pluginManager.registerEvents(new RegionListener(plugin), plugin);

        log.info("§aListeners загрузились");
    }

    private void reloadConfigs() {
        mainConfig.reloadConfig();
        mysqlConfig.reloadConfig();
        message.reloadConfig();
        blockConfig.reloadConfig();

        log.info("§aКонфиги перезагрузились");
    }

    private void disableMySQL() {
        DB.close();

        log.info("§aMySQL выключился");
    }

    public @NonNull BukkitAudiences adventure() {
        if (this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    public void callEvent(Event event) {
        getServer().getPluginManager().callEvent(event);
    }

    public boolean isPluginEnable(String name) {
        return getServer().getPluginManager().isPluginEnabled(name);
    }

    public void registerRegions() {
        MainConfig config = mainConfig.data();
        for (World world : Bukkit.getWorlds()) {
            if (!mainConfig.data().enableWorld().contains(world.getName())) continue;

            for (ProtectedRegion region : WGRegionUtil.getRegions(world)) {
                if (config.disableRegion().contains(region.getId())) continue;
                RegionRegistry.get().addRegion(region.getId(), region);
            }
        }
    }
}