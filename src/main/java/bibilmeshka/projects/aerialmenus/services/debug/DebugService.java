package bibilmeshka.projects.aerialmenus.services.debug;

import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class DebugService {

    @Setter
    private DebugLevel mainLevel;

    public DebugService(final DebugLevel mainLevel) {
        this.mainLevel = mainLevel;
    }

    public void debug(final String message, final DebugLevel debugLevel) {
        if (!(this.mainLevel.getPriority() >= debugLevel.getPriority())) return;
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&l&dAerial&bMenus&r&7] " + message));
    }

}
