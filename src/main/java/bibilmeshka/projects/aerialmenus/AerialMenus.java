package bibilmeshka.projects.aerialmenus;

import bibilmeshka.projects.aerialmenus.commands.AerialCommand;
import bibilmeshka.projects.aerialmenus.completers.AerialCommandCompliter;
import bibilmeshka.projects.aerialmenus.listeners.HeadDataBase;
import bibilmeshka.projects.aerialmenus.listeners.PlayerInteractMenu;
import bibilmeshka.projects.aerialmenus.nms.NMSConsumer;
import bibilmeshka.projects.aerialmenus.services.cutter.StringCutterService;
import bibilmeshka.projects.aerialmenus.services.debug.DebugLevel;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import bibilmeshka.projects.aerialmenus.services.dump.DumpService;
import bibilmeshka.projects.aerialmenus.services.item.ItemConvertConsumer;
import bibilmeshka.projects.aerialmenus.services.item.ItemConvertServiceOnConfig;
import bibilmeshka.projects.aerialmenus.services.item.convert.ItemMetaConvertConsumer;
import bibilmeshka.projects.aerialmenus.services.item.convert.ItemMetaConvertServiceConfig;
import bibilmeshka.projects.aerialmenus.services.menu.MenuManagementService;
import bibilmeshka.projects.aerialmenus.services.menu.command.register.MenuCommandRegisterService;
import bibilmeshka.projects.aerialmenus.services.menu.convert.MenuConvertServiceConsumer;
import bibilmeshka.projects.aerialmenus.services.menu.convert.MenuConvertServiceOnFile;
import bibilmeshka.projects.aerialmenus.services.menu.dependency.MenuDependencySetService;
import bibilmeshka.projects.aerialmenus.services.menu.interact.MenuInteractConsumer;
import bibilmeshka.projects.aerialmenus.services.menu.interact.MenuInteractServiceOnFile;
import bibilmeshka.projects.aerialmenus.services.metadata.MetaDataFactory;
import bibilmeshka.projects.aerialmenus.services.player.PlayerServiceConsumer;
import bibilmeshka.projects.aerialmenus.services.player.PlayerServiceOnMemory;
import bibilmeshka.projects.aerialmenus.services.menu.command.processing.CommandProcessingConsumer;
import bibilmeshka.projects.aerialmenus.services.menu.command.processing.CommandProcessingServiceOnString;
import bibilmeshka.projects.aerialmenus.services.requirement.check.RequirementCheckService;
import bibilmeshka.projects.aerialmenus.services.requirement.convert.RequirementConvertConsumer;
import bibilmeshka.projects.aerialmenus.services.requirement.convert.RequirementConvertServiceOnConfig;
import bibilmeshka.projects.aerialmenus.services.requirement.dependency.RequirementDependencySetService;
import bibilmeshka.projects.aerialmenus.services.skull.MaterialCheckConsumer;
import bibilmeshka.projects.aerialmenus.services.skull.MaterialCheckServiceOnString;
import bibilmeshka.projects.aerialmenus.services.update.GuiUpdateConsumer;
import bibilmeshka.projects.aerialmenus.services.update.GuiUpdateServiceOnMemory;
import bibilmeshka.projects.aerialmenus.services.update.GuiUpdaterConvertService;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.logging.Level;

public final class AerialMenus extends JavaPlugin {

    private Economy economy;
    private BukkitAudiences audiences;
    private CommandMap commandMap;
    private NMSConsumer nmsConsumer;
    private DumpService dumpService;
    private HeadDatabaseAPI headDatabaseAPI;
    private StringCutterService stringCutterService;
    private PlayerServiceConsumer playerServiceConsumer;
    private MenuManagementService menuManagementService;
    private MenuInteractConsumer menuInteractConsumer;
    private GuiUpdateConsumer guiUpdateConsumer;
    private GuiUpdaterConvertService guiUpdaterConvertService;
    private RequirementCheckService requirementCheckService;
    private RequirementDependencySetService requirementDependencySetService;
    private MenuDependencySetService menuDependencySetService;
    private MenuConvertServiceConsumer menuConvertServiceConsumer;
    private ItemMetaConvertConsumer itemMetaConvertConsumer;
    private RequirementConvertConsumer requirementConvertConsumer;
    private MaterialCheckConsumer playerSkullFactoryConsumer;
    private MetaDataFactory metaDataFactory;
    private CommandProcessingConsumer commandProcessingConsumer;
    private ItemConvertConsumer itemConvertConsumer;
    private MenuCommandRegisterService menuCommandRegisterService;
    private DebugService debugService;

    @Override
    public void onEnable() {
        this.debugService = new DebugService(DebugLevel.valueOf(getConfig().getString("debug", "LOWEST")));

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "\n" +
                "&5||||||||||||||||||||&0||||||||||||||||||||\n" +
                        "&5||||||||||||||||||||&0||||||||||||||||||||\n" +
                        "&5||||||||||||||||||||&0||||||||||||||||||||\n" +
                        "&5||||||||||||||||||||&0||||||||||||||||||||\n" +
                        "&5||||||||||||||||||||&0||||||||||||||||||||\n" +
                        "&5||||||||||||||||||||&0||||||||||||||||||||\n" +
                        "&5||||||||||||||||||||&0||||||||||||||||||||\n" +
                        "&0||||||||||||||||||||&5||||||||||||||||||||\n" +
                        "&0||||||||||||||||||||&5||||||||||||||||||||\n" +
                        "&0||||||||||||||||||||&5||||||||||||||||||||\n" +
                        "&0||||||||||||||||||||&5||||||||||||||||||||\n" +
                        "&0||||||||||||||||||||&5||||||||||||||||||||\n" +
                        "&0||||||||||||||||||||&5||||||||||||||||||||\n" +
                        "&0||||||||||||||||||||&5||||||||||||||||||||\n" +
                        "by bibilmeshka"
                ));

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            this.debugService.debug("&cНе обнаружен PlaceholderAPI, заполнители будут отсутствовать", DebugLevel.LOWEST);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (!setupEconomy()) {
            this.debugService.debug("&cНе обнаружен Vault, экономика будет отсутствовать", DebugLevel.LOWEST);
        }

        if (Bukkit.getPluginManager().getPlugin("ItemsAdder") == null) {
            this.debugService.debug("&cНе обнаружен ItemsAdder, материалы кастомных предметов будут отсутствовать", DebugLevel.LOWEST);
        }

        if (Bukkit.getPluginManager().getPlugin("Essentials") == null) {
            this.debugService.debug("&cНе обнаружен EssentialsX, экономика и другие фунции будут отсутствовать", DebugLevel.LOWEST);
        }



        this.audiences = BukkitAudiences.create(this);
        this.commandMap = setupCommandMap();
        this.stringCutterService = new StringCutterService();
        this.nmsConsumer = new NMSConsumer(this.debugService);
        this.nmsConsumer.setup();
        this.dumpService = new DumpService(this, this.debugService);
        this.playerServiceConsumer = new PlayerServiceConsumer(
                new PlayerServiceOnMemory());
        this.guiUpdaterConvertService = new GuiUpdaterConvertService(this.playerServiceConsumer, this, this.debugService);
        this.guiUpdateConsumer = new GuiUpdateConsumer(new GuiUpdateServiceOnMemory());
        this.playerSkullFactoryConsumer = new MaterialCheckConsumer(
                new MaterialCheckServiceOnString(this.headDatabaseAPI, this.debugService));
        this.requirementDependencySetService = new RequirementDependencySetService(this.economy);
        this.requirementConvertConsumer = new RequirementConvertConsumer(
                new RequirementConvertServiceOnConfig(this.stringCutterService, requirementDependencySetService, this.debugService));
        this.itemMetaConvertConsumer = new ItemMetaConvertConsumer(
                new ItemMetaConvertServiceConfig(this.stringCutterService, this.debugService));
        this.metaDataFactory = new MetaDataFactory(this, this.debugService);
        this.commandProcessingConsumer = new CommandProcessingConsumer(
                new CommandProcessingServiceOnString(
                        this.audiences,
                        this.playerServiceConsumer,
                        this,
                        this.stringCutterService,
                        this.metaDataFactory,
                        this.economy,
                        this.debugService));
        this.requirementCheckService = new RequirementCheckService(this.commandProcessingConsumer, this.debugService);
        this.itemConvertConsumer =
                new ItemConvertConsumer(
                        new ItemConvertServiceOnConfig(
                                this.requirementConvertConsumer,
                                this.itemMetaConvertConsumer,
                                this.commandProcessingConsumer,
                                this.stringCutterService,
                                this.requirementCheckService,
                                this.playerSkullFactoryConsumer,
                                this.nmsConsumer,
                                this.debugService));
        this.menuDependencySetService = new MenuDependencySetService(this.requirementCheckService, this.commandProcessingConsumer, this.debugService);
        this.menuConvertServiceConsumer = new MenuConvertServiceConsumer(
                new MenuConvertServiceOnFile(
                        this.playerServiceConsumer,
                        this.requirementConvertConsumer,
                        this.itemConvertConsumer,
                        this.menuDependencySetService,
                        this.debugService));
        this.menuInteractConsumer = new MenuInteractConsumer(
                new MenuInteractServiceOnFile(this, this.menuConvertServiceConsumer, this.debugService));
        this.menuCommandRegisterService = new MenuCommandRegisterService(this.setupCommandMap(), this.debugService);
        this.menuManagementService = new MenuManagementService(
                this.menuInteractConsumer,
                this.playerServiceConsumer,
                this.guiUpdaterConvertService,
                this.guiUpdateConsumer,
                this.menuCommandRegisterService,
                this.debugService);

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        reloadConfig();

        this.menuManagementService.startUp();

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        Bukkit.getPluginManager().registerEvents(new PlayerInteractMenu(this.playerServiceConsumer), this);
        if (Bukkit.getPluginManager().getPlugin("HeadDatabase") == null) {
            this.debugService.debug("&cНе обнаружен HeadDatabase, головы из этого плагина будут отсутствовать", DebugLevel.LOWEST);
        } else {
            Bukkit.getPluginManager().registerEvents(new HeadDataBase(this, this.debugService), this);
        }

        getCommand("aerialmenus").setExecutor(new AerialCommand(this,
                this.playerServiceConsumer,
                this.menuManagementService,
                this.commandProcessingConsumer,
                this.dumpService,
                this.guiUpdateConsumer,
                this.debugService));
        getCommand("aerialmenus").setTabCompleter(new AerialCommandCompliter());
    }

    @Override
    public void onDisable() {
        if (this.audiences == null) return;
        this.audiences.close();
        this.audiences = null;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        final var rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        this.economy = rsp.getProvider();
        return this.economy != null;
    }


    private CommandMap setupCommandMap() {
        try {
            final var commandMapField = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            return (CommandMap) commandMapField.get(Bukkit.getPluginManager());
        } catch (Exception e) {
            this.debugService.debug("&cНе получилось зарегистрировать CommandMap", DebugLevel.LOW);
        }
        return null;
    }

    public void createHeadDatabaseAPI() {
        this.headDatabaseAPI = new HeadDatabaseAPI();
    }

}
