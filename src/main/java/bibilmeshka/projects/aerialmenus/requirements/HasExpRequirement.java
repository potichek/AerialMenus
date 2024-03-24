package bibilmeshka.projects.aerialmenus.requirements;

import bibilmeshka.projects.aerialmenus.services.debug.DebugLevel;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

@Setter
public class HasExpRequirement extends Requirement {

    private Object amount;

    private boolean level;

    public HasExpRequirement(RequirementType type, String requirementName, List<String> denyCommands, List<String> successCommands, boolean optional, DebugService debugService) {
        super(type, requirementName, denyCommands, successCommands, optional, debugService);
    }

    private double parseAmount(Player player) {

        if (amount instanceof String) {
            amount = PlaceholderAPI.setPlaceholders(player, (String) amount);
            return Double.parseDouble((String) amount);
        }

        return new Double((Integer) amount);
    }

    @Override
    public boolean check(Player player) {
        super.debugService.debug("&eИгрок " + player.getName() + " Проверяется на требования " + this.requirementName, DebugLevel.HIGHEST);

        final var amount = parseAmount(player);
        if (level) {
            if (player.getLevel() >= (int) amount) return getWithPropetryNegative(true);
        } else {
            if (player.getTotalExperience() >= amount)  return getWithPropetryNegative(true);
        }

        return getWithPropetryNegative(false);
    }

}