package bibilmeshka.projects.aerialmenus.services.player;

import bibilmeshka.projects.aerialmenus.menu.Menu;
import bibilmeshka.projects.aerialmenus.menu.MenuArgs;
import bibilmeshka.projects.aerialmenus.menu.MenuComponents;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

public class PlayerServiceOnMemory implements PlayerService {

    private final HashMap<Menu, HashMap<UUID, MenuComponents>> playersInMenu = new HashMap<>();

    @Override
    public void initialize(final Menu menu) {
        this.playersInMenu.put(menu, new HashMap<UUID, MenuComponents>());
    }

    @Override
    public void clear() {
        this.playersInMenu.clear();
    }

    @Override
    public void add(final Menu menu, final UUID uuid) {
        if (!inMenu(uuid)) this.playersInMenu.get(menu).put(uuid, null);
    }

    @Override
    public void add(final Menu menu, final UUID uuid, final MenuComponents menuComponents) {
        if (!inMenu(uuid)) this.playersInMenu.get(menu).put(uuid, menuComponents);
    }

    @Override
    public void removePlayer(final UUID uuid) {
        for (final var menu : this.playersInMenu.keySet()) {
            this.playersInMenu.get(menu).remove(uuid);
        }
    }

    @Override
    public void removeMenu(final Menu menu) {
        this.playersInMenu.remove(menu);
    }

    @Override
    public boolean inMenu(final UUID uuid) {
        for (final var menu : this.playersInMenu.keySet()) {
            if (this.playersInMenu.get(menu).containsKey(uuid)) return true;
        }
        return false;
    }

    @Override
    public @Nullable Menu getMenuByName(final String name) {
        for (final var menu : this.playersInMenu.keySet()) {
            if (menu.getMenuName().equals(name)) return menu;
        }
        return null;
    }

    @Override
    public Menu getMenuByCommand(final String command) {
        for (var menu : this.playersInMenu.keySet()) {
            if (menu.commandContains(command)) return menu;
        }
        return null;
    }

    @Override
    public @Nullable Menu getMenuOfPlayer(final UUID uuid) {
        for (final var menu : this.playersInMenu.keySet()) {
            for (final var player : this.playersInMenu.get(menu).keySet()) {
                if (player.equals(uuid)) return menu;
            }
        }
        return null;
    }

    @Override
    public @NotNull Set<Menu> getMenus() {
        return this.playersInMenu.keySet();
    }

    @Override
    public MenuComponents getMenuComponents(final Menu menu, final UUID uuid) {
        return this.playersInMenu.get(menu).get(uuid);
    }

    @Override
    public List<Player> getPlayersInMenu(final Menu menu) {
        final var players = new ArrayList<Player>();
        for (final var uuid : this.playersInMenu.get(menu).keySet()) {
            final var player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            players.add(player);
        }
        return players;
    }

    @Override
    public void showAll(final CommandSender commandSender) {
        for (final var menu : this.playersInMenu.keySet()) {
            commandSender.sendMessage(
                    ChatColor.YELLOW + "Menu: " + menu.getMenuName() + " uuid: " + this.playersInMenu.get(menu));
        }
    }
}
