package bibilmeshka.projects.aerialmenus.services.player;

import bibilmeshka.projects.aerialmenus.menu.Menu;
import bibilmeshka.projects.aerialmenus.menu.MenuComponents;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PlayerServiceConsumer {

    private final PlayerService service;

    public PlayerServiceConsumer(PlayerService playerService) {
        this.service = playerService;
    }

    public void initialize(Menu menu) {
        this.service.initialize(menu);
    }

    public void clear() {
        this.service.clear();
    }

    public void add(Menu menu, UUID uuid) {
        this.service.add(menu, uuid);
    }

    public void add(Menu menu, UUID uuid, MenuComponents menuComponents) {
        this.service.add(menu, uuid, menuComponents);
    }

    public void removePlayer(UUID uuid) {
        this.service.removePlayer(uuid);
    }

    public void removeMenu(final Menu menu) {
        this.service.removeMenu(menu);
    }

    public @Nullable Menu getMenuByName(final String name) {
        return this.service.getMenuByName(name);
    }


    public Menu getMenuByCommand(String command) {
        return this.service.getMenuByCommand(command);
    }

    public @Nullable Menu getMenuOfPlayer(final UUID uuid) {
        return this.service.getMenuOfPlayer(uuid);
    }

    public @NotNull Set<Menu> getMenus() {
        return this.service.getMenus();
    }

    public MenuComponents getMenuComponents(final Menu menu, final UUID uuid) {
        return this.service.getMenuComponents(menu, uuid);
    }

    public List<Player> getPlayersInMenu(final Menu menu) {
        return this.service.getPlayersInMenu(menu);
    }

    public void showAll(final CommandSender commandSender) {
        this.service.showAll(commandSender);
    }

}
