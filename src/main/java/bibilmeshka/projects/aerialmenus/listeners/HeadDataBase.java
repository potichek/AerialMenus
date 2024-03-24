package bibilmeshka.projects.aerialmenus.listeners;


import bibilmeshka.projects.aerialmenus.AerialMenus;
import bibilmeshka.projects.aerialmenus.services.debug.DebugLevel;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import me.arcaniax.hdb.api.DatabaseLoadEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class HeadDataBase implements Listener {

    private final AerialMenus plugin;
    private final DebugService debugService;

    public HeadDataBase(AerialMenus plugin, DebugService debugService) {
        this.plugin = plugin;
        this.debugService = debugService;
    }

    @EventHandler
    public void onLoad(DatabaseLoadEvent event) {
        plugin.createHeadDatabaseAPI();
        debugService.debug("&aDatabase плагин успешно подключён", DebugLevel.MEDIUM);
    }

}
