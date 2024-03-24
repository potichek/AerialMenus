package bibilmeshka.projects.aerialmenus.services.player;

import bibilmeshka.projects.aerialmenus.menu.Menu;
import bibilmeshka.projects.aerialmenus.menu.MenuComponents;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface PlayerService {

    void initialize(final Menu menu);
    void clear();
    boolean inMenu(final UUID uuid);
    void add(final Menu menu, final UUID uuid);
    public void add(final Menu menu, final UUID uuid, final MenuComponents menuComponents);
    void removePlayer(final UUID uuid);
    void removeMenu(final Menu menu);
    Menu getMenuByName(final String name);
    Menu getMenuByCommand(final String command);
    Menu getMenuOfPlayer(final UUID uuid);
    MenuComponents getMenuComponents(final Menu menu, final UUID uuid);
    Set<Menu> getMenus();
    List<Player> getPlayersInMenu(final Menu menu);
    void showAll(final CommandSender commandSender);

}
