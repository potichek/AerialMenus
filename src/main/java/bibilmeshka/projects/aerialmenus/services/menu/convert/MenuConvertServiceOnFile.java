package bibilmeshka.projects.aerialmenus.services.menu.convert;

import bibilmeshka.projects.aerialmenus.menu.Menu;
import bibilmeshka.projects.aerialmenus.menu.MenuData;
import bibilmeshka.projects.aerialmenus.services.debug.DebugLevel;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import bibilmeshka.projects.aerialmenus.services.item.ItemConvertConsumer;
import bibilmeshka.projects.aerialmenus.services.menu.dependency.MenuDependencySetService;
import bibilmeshka.projects.aerialmenus.services.player.PlayerServiceConsumer;
import bibilmeshka.projects.aerialmenus.services.requirement.convert.RequirementConvertConsumer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MenuConvertServiceOnFile extends MenuConvertService<File> {

    private final PlayerServiceConsumer playerService;
    private final RequirementConvertConsumer requirementConvertConsumer;
    private final ItemConvertConsumer itemConvertConsumer;
    private final MenuDependencySetService dependencySetService;
    private final DebugService debugService;

    public MenuConvertServiceOnFile(final PlayerServiceConsumer playerService,
                                    final RequirementConvertConsumer requirementConvertConsumer,
                                    final ItemConvertConsumer itemConvertConsumer,
                                    final MenuDependencySetService dependencySetService,
                                    final DebugService debugService) {

        this.playerService = playerService;
        this.requirementConvertConsumer = requirementConvertConsumer;
        this.itemConvertConsumer = itemConvertConsumer;
        this.dependencySetService = dependencySetService;
        this.debugService = debugService;
    }

    @Override
    public Menu convert(final File resource) {
        try {
            final var config = YamlConfiguration.loadConfiguration(resource);
            final var menuData = new MenuData();

            menuData.setPath(resource.getAbsolutePath());
            menuData.setMenuFile(resource);
            menuData.setGuiName(config.getString("menu_title", "null"));
            if (config.getStringList("open_command").isEmpty()) {
                menuData.getCommands().add(config.getString("open_command"));
            } else {
                menuData.setCommands(config.getStringList("open_command"));
            }
            menuData.setOpenRequirements(this.requirementConvertConsumer.getOpenRequirements(config));
            menuData.setOpenCommands(config.getStringList("open_commands"));
            menuData.setCloseCommands(config.getStringList("close_commands"));
            menuData.setType(InventoryType.valueOf(config.getString("inventory_type", "CHEST")));
            menuData.setSize(config.getInt("size"));
            menuData.setNeedRegister(config.getBoolean("register_command"));
            menuData.setArgs(config.getStringList("args"));
            menuData.setArgsUsageMessage(config.getString("args_usage_message", ""));
            menuData.setUpdateInterval(config.getInt("update_interval"));
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Menu " + resource.getName() + " clear inv: " + config.getBoolean("clear_inventory"));
            menuData.setClearInventory(config.getBoolean("clear_inventory"));
            menuData.setItems(super.placeInSlots(this.itemConvertConsumer.getItems(config), config.getInt("size")));

            final var menu = new Menu(resource.getName(), menuData);
            this.dependencySetService.setInMenu(menu);
            return menu;
        } catch (Exception e) {
            this.debugService.debug("&cНе получилось создать меню " + resource.getName(), DebugLevel.LOWEST);
        }
        return null;
    }

    @Override
    public List<Menu> convertList(final List<File> resources) {
        final var menus = new ArrayList<Menu>();
        for (final var configFile : resources) {
            menus.add(convert(configFile));
        }
        return menus;
    }
}
