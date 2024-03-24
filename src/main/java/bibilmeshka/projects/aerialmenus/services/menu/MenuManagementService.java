package bibilmeshka.projects.aerialmenus.services.menu;

import bibilmeshka.projects.aerialmenus.menu.Menu;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import bibilmeshka.projects.aerialmenus.services.menu.command.register.MenuCommandRegisterService;
import bibilmeshka.projects.aerialmenus.services.menu.interact.MenuInteractConsumer;
import bibilmeshka.projects.aerialmenus.services.player.PlayerServiceConsumer;
import bibilmeshka.projects.aerialmenus.services.update.GuiUpdateConsumer;
import bibilmeshka.projects.aerialmenus.services.update.GuiUpdaterConvertService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class MenuManagementService {

    private final MenuInteractConsumer interactService;
    private final PlayerServiceConsumer playerService;
    private final GuiUpdaterConvertService guiUpdaterConvertService;
    private final GuiUpdateConsumer guiUpdateService;
    private final MenuCommandRegisterService menuCommandRegisterService;
    private final DebugService debugService;

    public MenuManagementService(final MenuInteractConsumer interactService,
                                 final PlayerServiceConsumer playerService,
                                 final GuiUpdaterConvertService guiUpdaterConvertService,
                                 final GuiUpdateConsumer guiUpdateService,
                                 final MenuCommandRegisterService menuCommandRegisterService,
                                 final DebugService debugService) {
        this.interactService = interactService;
        this.playerService = playerService;
        this.guiUpdaterConvertService = guiUpdaterConvertService;
        this.guiUpdateService = guiUpdateService;
        this.menuCommandRegisterService = menuCommandRegisterService;
        this.debugService = debugService;
    }

    public void startUp() {
        this.interactService.startUp();
        registerMenus();
    }

    private void registerMenu(final Menu menu) {
        final var updater = this.guiUpdaterConvertService.createGuiUpdater(menu);
        this.playerService.initialize(menu);
        this.guiUpdateService.initialize(updater);
        this.guiUpdateService.runTask(updater);
        this.menuCommandRegisterService.register(menu);
    }

    private void unRegister(final Menu menu) {
        this.guiUpdateService.disableTask(this.guiUpdaterConvertService.createGuiUpdater(menu));
        this.guiUpdateService.remove(this.guiUpdaterConvertService.createGuiUpdater(menu));
        this.playerService.removeMenu(menu);
    }


    private void registerMenus() {
        this.playerService.clear();
        this.guiUpdateService.disableTasks();
        this.guiUpdateService.clear();
        for (final var menu : this.interactService.getAllMenu()) {
            registerMenu(menu);
        }
    }

    public void reloadMenu(final Menu menu) {
        this.interactService.saveMenu(menu.getMenuFile());
        unRegister(menu);
        final var newMenu = this.interactService.getMenuByName(menu.getMenuName());
        registerMenu(newMenu);
    }

    public void reloadMenus() {
        this.interactService.saveAllMenus();
        registerMenus();
    }

}
