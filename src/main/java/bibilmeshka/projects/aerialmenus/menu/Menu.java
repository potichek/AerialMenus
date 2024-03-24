package bibilmeshka.projects.aerialmenus.menu;

import bibilmeshka.projects.aerialmenus.listeners.custom.MenuCloseEvent;
import bibilmeshka.projects.aerialmenus.listeners.custom.MenuOpenEvent;
import bibilmeshka.projects.aerialmenus.services.debug.DebugLevel;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import bibilmeshka.projects.aerialmenus.services.menu.command.processing.CommandProcessingConsumer;
import bibilmeshka.projects.aerialmenus.services.requirement.check.RequirementCheckService;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.util.List;

public class Menu {

    @Getter
    private final String menuName;
    private final MenuData data;
    @Setter
    private RequirementCheckService requirementCheckService;
    @Setter
    private CommandProcessingConsumer commandProcessingConsumer;
    @Setter
    private DebugService debugService;

    public Menu(final String menuName, final MenuData data) {
        this.menuName = menuName;
        this.data = data;
    }

    private Inventory createInventory(final Player placeholdersOwner, final MenuArgs menuArgs) {
        this.debugService.debug("&aСоздан инвентарь для меню " + this.menuName, DebugLevel.HIGH);
        try {
            final var guiName = ChatColor.translateAlternateColorCodes(
                    '&',
                    menuArgs.setHolders(this.data.getGuiName()));

            if (this.data.getType() != InventoryType.CHEST) {
                return Bukkit.createInventory(
                        placeholdersOwner,
                        this.data.getType(),
                        guiName);
            }
            return Bukkit.createInventory(
                    placeholdersOwner,
                    this.data.getSize(),
                    guiName);
        } catch (Exception e) {
            this.debugService.debug("&cНе удаётся создать инвентарь для меню", DebugLevel.MEDIUM);
        }
        return null;
    }

    private Inventory setItems(final Inventory inventory, final Player viewer, final Player placeholdersOwner, final MenuArgs menuArgs) {
        this.debugService.debug("&aИнвентарь меню " + this.menuName + " заполнен предметами", DebugLevel.HIGH);
        try {
            for (int slot = 0; slot <= (this.data.getItems().length - 1); slot++) {
                final var item = this.data.getItems()[slot];
                if (item == null || item.isAir()) continue;
                if (!(this.requirementCheckService.check(item.getData().getViewRequirement(), viewer))) continue;
                inventory.setItem(slot, item.convertToItemStack(placeholdersOwner, menuArgs));
            }
            return inventory;
        } catch (Exception e) {
            this.debugService.debug("&cНе удаётся заполнить инвентарь меню", DebugLevel.MEDIUM);
        }
        return inventory;
    }

    public Inventory build(final Player viewer, final Player placeholdersOwner, final MenuArgs menuArgs) {
        return setItems(createInventory(placeholdersOwner, menuArgs), viewer, placeholdersOwner, menuArgs);
    }

    public void open(final Player viewer, final Player placeholdersOwner, final MenuArgs menuArgs) {
        this.debugService.debug("&eОткрылось меню " + this.menuName + " для " + viewer.getName() + " с заполнителями " + placeholdersOwner.getName(), DebugLevel.HIGH);
        close(viewer, false);

        if (!(this.data.getArgs().isEmpty())) {
            if (menuArgs.isEnoughHolders()) {
                final var argsMessage = this.data.getArgsUsageMessage();
                if (argsMessage.isEmpty()) return;
                viewer.sendMessage(ChatColor.translateAlternateColorCodes('&', argsMessage));
                return;
            }
        }

        if (!(viewer.hasPermission("aerialmenus.openrequirement.bypass.*"))) {
            if (!(viewer.hasPermission("aerialmenus.openrequirement.bypass." + this.menuName))) {
                if (!(this.requirementCheckService.check(this.data.getOpenRequirements(), viewer))) return;
            }
        }

        if (this.data.isClearInventory()) viewer.getInventory().clear();

        this.commandProcessingConsumer.executeСommands(this.data.getOpenCommands(), viewer);
        viewer.openInventory(build(viewer, placeholdersOwner, menuArgs));
        Bukkit.getPluginManager().callEvent(
                new MenuOpenEvent(
                        viewer,
                        this,
                        new MenuComponents(new MenuArgs(), placeholdersOwner)));
    }

    public void close(final Player viewer, final boolean executeCloseCommands) {
        this.debugService.debug("&eЗакрылось меню " + this.menuName + " для " + viewer, DebugLevel.HIGH);

        Bukkit.getPluginManager().callEvent(new MenuCloseEvent(viewer, this));
        if (!executeCloseCommands) return;
        this.commandProcessingConsumer.executeСommands(
                this.data.getCloseCommands(), viewer);
    }

    public void updatePlaceholders(final Player viewer, final Player placeholdersOwner, final MenuArgs menuArgs) {
        this.debugService.debug("&eОбновились заполнители для меню " + this.menuName + " от " + placeholdersOwner, DebugLevel.HIGH);
        try {
            for (var slot = 0; slot <= (this.data.getItems().length - 1); slot++) {
                final var item = this.data.getItems()[slot];
                if (item == null || item.isAir() || !(item.isUpdate())) continue;
                viewer.getOpenInventory().setItem(slot, item.convertToItemStack(placeholdersOwner, menuArgs));
            }
        } catch (Exception e) {
            this.debugService.debug("&cНе удаётся обновить заполнители меню", DebugLevel.MEDIUM);
        }
    }

    public boolean isThisInventory(final Inventory inventory) {
        return inventory.getType() == this.data.getType();
    }

    public void executeClickCommands(final Player player, final int slot) {
        final var item = this.data.getItems()[slot];
        if (item == null) return;
        item.executeClickCommands(player);
        this.debugService.debug("&eВыполнены команды клика, меню " + this.menuName, DebugLevel.HIGHEST);
    }

    public void executeLeftClickItemCommands(final Player player, final int slot) {
        final var item = this.data.getItems()[slot];
        if (item == null) return;
        item.executeLeftClickCommands(player);
        this.debugService.debug("&eВыполнены команды левого клика, меню " + this.menuName, DebugLevel.HIGHEST);
    }

    public void executeRightClickItemCommands(final Player player, final int slot) {
        final var item = this.data.getItems()[slot];
        if (item == null) return;
        item.executeRightClickCommands(player);
        this.debugService.debug("&eВыполнены команды правого клика, меню " + this.menuName, DebugLevel.HIGHEST);
    }

    public void executeMiddleClickItemCommands(final Player player, final int slot) {
        final var item = this.data.getItems()[slot];
        if (item == null) return;
        item.executeMiddleClickCommands(player);
        this.debugService.debug("&eВыполнены команды среднего клика, меню " + this.menuName, DebugLevel.HIGHEST);
    }

    public void executeShiftLeftClickItemCommands(final Player player, final int slot) {
        final var item = this.data.getItems()[slot];
        if (item == null) return;
        item.executeShiftLeftClickCommands(player);
        this.debugService.debug("&eВыполнены команды левого с шифтом клика, меню " + this.menuName, DebugLevel.HIGHEST);
    }

    public void executeShiftRightClickItemCommands(final Player player, final int slot) {
        final var item = this.data.getItems()[slot];
        if (item == null) return;
        item.executeShiftRightClickCommands(player);
        this.debugService.debug("&eВыполнены команды правого с шифтом клика, меню " + this.menuName, DebugLevel.HIGHEST);
    }

    public boolean canPickItem(final Player player, final int slot) {
        final var item = this.data.getItems()[slot];
        if (item == null) return false;
        return item.canPick(player);
    }

    public boolean canPutItem(final Player player, final int slot) {
        final var item = this.data.getItems()[slot];
        if (item == null) return false;
        return item.canPut(player);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Menu menu) {
            return this.menuName.equals(menu.getMenuName());
        }
        return false;
    }

    public String getGuiName() {
        return this.data.getGuiName();
    }

    public int getUpdateInterval() {
        return this.data.getUpdateInterval();
    }

    public MenuItem[] getItems() {
        return this.data.getItems();
    }

    public boolean commandContains(String command) {
        return this.data.getCommands().contains(command);
    }

    public boolean isNeedRegister() {
        return this.data.isNeedRegister();
    }

    public List<String> getCommands() {
        return this.data.getCommands();
    }

    public List<String> getArgs() {
        return this.data.getArgs();
    }

    public String getPath() {
        return this.data.getPath();
    }

    public File getMenuFile() {
        return this.data.getMenuFile();
    }

}