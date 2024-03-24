package bibilmeshka.projects.aerialmenus.services.menu.interact;

import bibilmeshka.projects.aerialmenus.menu.Menu;

import java.io.File;
import java.util.List;

public interface MenuInteractService {

    void startUp();
    List<Menu> getAllMenu();
    Menu getMenuByName(final String menuName);
    void saveMenu(final File menuFile);
    void saveAllMenus();

}
