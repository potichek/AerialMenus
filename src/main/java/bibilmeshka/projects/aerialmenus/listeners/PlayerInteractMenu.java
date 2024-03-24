package bibilmeshka.projects.aerialmenus.listeners;

import bibilmeshka.projects.aerialmenus.listeners.custom.MenuCloseEvent;
import bibilmeshka.projects.aerialmenus.listeners.custom.MenuOpenEvent;
import bibilmeshka.projects.aerialmenus.menu.MenuArgs;
import bibilmeshka.projects.aerialmenus.services.player.PlayerServiceConsumer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerInteractMenu implements Listener {

    private final PlayerServiceConsumer playerServiceConsumer;

    public PlayerInteractMenu(final PlayerServiceConsumer playerServiceConsumer) {
        this.playerServiceConsumer = playerServiceConsumer;
    }

    @EventHandler
    public void onCloseMenu(final MenuCloseEvent event) {
        this.playerServiceConsumer.removePlayer(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onShow(final MenuOpenEvent event) {
        this.playerServiceConsumer.add(
                event.getMenu(),
                event.getPlayer().getUniqueId(),
                event.getMenuComponents());
    }

    @EventHandler
    public void onClick(final InventoryClickEvent event) {
        final var clicker = (Player) event.getWhoClicked();
        final var actionType = event.getAction();
        final var inventory = event.getClickedInventory();
        if (event.getClickedInventory() == null) return;
        final var menu = this.playerServiceConsumer.getMenuOfPlayer(clicker.getUniqueId());
        if (menu == null) return;
        if (inventory.getType() == InventoryType.PLAYER && (actionType == InventoryAction.MOVE_TO_OTHER_INVENTORY || actionType == InventoryAction.COLLECT_TO_CURSOR)) {
            event.setCancelled(true);
            return;
        }
        if (!(menu.isThisInventory(inventory))) return;

        final var slot = event.getSlot();
        final var clickType = event.getClick();
        menu.executeClickCommands(clicker, slot);
        if (clickType == ClickType.LEFT) {
            menu.executeLeftClickItemCommands(clicker, slot);
        } else if (clickType == ClickType.RIGHT) {
            menu.executeRightClickItemCommands(clicker, slot);
        } else if (clickType == ClickType.MIDDLE) {
            menu.executeMiddleClickItemCommands(clicker, slot);
        } else if (clickType == ClickType.SHIFT_LEFT) {
            menu.executeShiftLeftClickItemCommands(clicker, slot);
        } else if (clickType == ClickType.SHIFT_RIGHT) {
            menu.executeShiftRightClickItemCommands(clicker, slot);
        }

        if (actionType == InventoryAction.PICKUP_ALL || actionType == InventoryAction.PICKUP_HALF ||
                actionType == InventoryAction.PICKUP_ONE || actionType == InventoryAction.PICKUP_SOME ||
                actionType == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            if (!(menu.canPickItem(clicker, slot))) {
                event.setCancelled(true);
                return;
            }
        } else if (actionType == InventoryAction.PLACE_ALL || actionType == InventoryAction.PLACE_ONE ||
                actionType == InventoryAction.PLACE_SOME) {
            if (!(menu.canPutItem(clicker, slot))) {
                event.setCancelled(true);
                return;
            }
        } else {
            event.setCancelled(true);
            return;
        }

    }

    @EventHandler
    public void onDrag(final InventoryDragEvent event) {
        final var menu = this.playerServiceConsumer.getMenuOfPlayer(((Player) event.getWhoClicked()).getUniqueId());
        if (menu == null) return;
        if (event.getView().getInventory(event.getRawSlots().stream().toList().get(0)).getType() != InventoryType.PLAYER) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onExecute(final PlayerCommandPreprocessEvent event) {
        var command = event.getMessage().substring(1).strip();
        final var viewer = event.getPlayer();

        if (command.indexOf(" ") != -1) {
            final var arguments = command.substring(command.indexOf(" "));
            command = command.substring(0, command.indexOf(" "));

            final var menu = this.playerServiceConsumer.getMenuByCommand(command);
            if (menu == null) return;
            menu.open(viewer, viewer, new MenuArgs(menu.getArgs(), arguments));
            event.setCancelled(true);
            return;
        }

        final var menu = this.playerServiceConsumer.getMenuByCommand(command);
        if (menu == null) return;
        menu.open(viewer, viewer, new MenuArgs());
        event.setCancelled(true);
    }

    @EventHandler()
    public void onOpen(final InventoryOpenEvent event) {
        final var menu = this.playerServiceConsumer.getMenuOfPlayer(event.getPlayer().getUniqueId());
        final var player = (Player) event.getPlayer();
        if (menu == null) return;
        this.playerServiceConsumer.add(menu, player.getUniqueId());
    }

    @EventHandler
    public void onCloseInventory(final InventoryCloseEvent event) {
        final var player = (Player) event.getPlayer();
        final var menu = this.playerServiceConsumer.getMenuOfPlayer(player.getUniqueId());
        if (menu == null) return;
        menu.close(player, false);
    }
}
