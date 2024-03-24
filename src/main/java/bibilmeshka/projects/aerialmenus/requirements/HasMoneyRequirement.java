package bibilmeshka.projects.aerialmenus.requirements;

import bibilmeshka.projects.aerialmenus.AerialMenus;
import bibilmeshka.projects.aerialmenus.services.debug.DebugLevel;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

@Setter
public class HasMoneyRequirement extends Requirement{

    private Object amount;
    private Economy economy;

    public HasMoneyRequirement(RequirementType type, String requirementName, List<String> denyCommands, List<String> successCommands, boolean optional, DebugService debugService) {
        super(type, requirementName, denyCommands, successCommands, optional, debugService);
    }

    public boolean check(Player player) {
        if (amount instanceof String) {
            final var amount = Integer.parseInt(PlaceholderAPI.setPlaceholders(player, (String) this.amount));
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Player money: " + this.economy.getBalance(player));
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "need: " + amount);
            if (this.economy.getBalance(player) >= amount) {
                return true;
            }
            return false;
        }

        if (this.economy.getBalance(player) >= (Integer) amount) return getWithPropetryNegative(true);

        return false;
    }

}
