package bibilmeshka.projects.aerialmenus.services.menu.interact;


import bibilmeshka.projects.aerialmenus.menu.Menu;

import java.io.File;
import java.util.List;

public class MenuInteractConsumer {

    private final MenuInteractService service;

    public MenuInteractConsumer(MenuInteractService service) {
        this.service = service;
    }

    public void startUp() {
        this.service.startUp();
    }

    public List<Menu> getAllMenu() {
        return this.service.getAllMenu();
    }

    public Menu getMenuByName(final String menuName) {
        return this.service.getMenuByName(menuName);
    }

    public void saveAllMenus() {
        this.service.saveAllMenus();
    }
    public void saveMenu(File menuFile) {
        this.service.saveMenu(menuFile);
    }

}
