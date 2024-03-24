package bibilmeshka.projects.aerialmenus.services.update;

import bibilmeshka.projects.aerialmenus.AerialMenus;
import bibilmeshka.projects.aerialmenus.menu.Menu;
import bibilmeshka.projects.aerialmenus.services.debug.DebugLevel;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import bibilmeshka.projects.aerialmenus.services.player.PlayerServiceConsumer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GuiUpdater {

    private final PlayerServiceConsumer playerServiceConsumer;
    private final DebugService debugService;
    private final AerialMenus plugin;
    @Getter
    private final Menu menu;
    @Getter
    private int taskId = -1;

    public GuiUpdater(final Menu menu, final PlayerServiceConsumer playerServiceConsumer, final DebugService debugService, final AerialMenus plugin) {
        this.menu = menu;
        this.playerServiceConsumer = playerServiceConsumer;
        this.plugin = plugin;
        this.debugService = debugService;
    }

    public void startTask() {
        if (this.menu.getUpdateInterval() == 0) return;

        this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {
            this.debugService.debug("&cОбновление заполнителей для меню: " + menu.getMenuName(), DebugLevel.HIGHEST);
            for (final var player : this.playerServiceConsumer.getPlayersInMenu(this.menu)) {
                final var menuComponents = this.playerServiceConsumer.getMenuComponents(menu, player.getUniqueId());
                menu.updatePlaceholders(player, menuComponents.getPlaceholderOwner(), menuComponents.getMenuArgs());
            }
        }, 1L, ((long) this.menu.getUpdateInterval() * 20));
    }

    public void stopTask() {
        Bukkit.getScheduler().cancelTask(this.taskId);
    }


    @Override
    public boolean equals(Object object) {
        if (object instanceof GuiUpdater guiUpdater) {
            return this.menu.equals(guiUpdater.menu);
        }
        return false;
    }

}