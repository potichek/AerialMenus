package bibilmeshka.projects.aerialmenus.services.update;

import bibilmeshka.projects.aerialmenus.AerialMenus;
import bibilmeshka.projects.aerialmenus.menu.Menu;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import bibilmeshka.projects.aerialmenus.services.player.PlayerServiceConsumer;
import bibilmeshka.projects.aerialmenus.services.update.GuiUpdater;

public class GuiUpdaterConvertService {

    private final PlayerServiceConsumer playerServiceConsumer;
    private final AerialMenus plugin;
    private final DebugService debugService;

    public GuiUpdaterConvertService(final PlayerServiceConsumer playerServiceConsumer, final AerialMenus plugin, final DebugService debugService) {
        this.playerServiceConsumer = playerServiceConsumer;
        this.plugin = plugin;
        this.debugService = debugService;
    }

    public GuiUpdater createGuiUpdater(final Menu menu) {
        return new GuiUpdater(menu, this.playerServiceConsumer, this.debugService, this.plugin);
    }

}
