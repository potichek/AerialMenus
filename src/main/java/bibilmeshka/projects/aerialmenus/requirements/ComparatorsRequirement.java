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
public class ComparatorsRequirement extends Requirement {

    private String operator;
    private Object input;
    private Object output;

    public ComparatorsRequirement(RequirementType type, String requirementName, List<String> denyCommands, List<String> successCommands, boolean optional, DebugService debugService) {
        super(type, requirementName, denyCommands, successCommands, optional, debugService);
    }

    @Override
    public boolean check(Player player) {
        super.debugService.debug("&eИгрок " + player.getName() + " Проверяется на требования " + this.requirementName, DebugLevel.HIGHEST);
        if (input instanceof String && output instanceof String) {

            final var input = PlaceholderAPI.setPlaceholders(player, (String) this.input);
            final var output = PlaceholderAPI.setPlaceholders(player, (String) this.output);

            if (operator.equals("==")) {
                if (input.length() == output.length()) return true;
            } else if (operator.equals(">=")) {
                if (input.length() >= output.length()) return true;
            } else if (operator.equals("<=")) {
                if (input.length() <= output.length()) return true;
            } else if (operator.equals("!=")) {
                if (input.length() != output.length()) return true;
            } else if (operator.equals(">")) {
                if (input.length() > output.length()) return true;
            } else if (operator.equals("<")) {
                if (input.length() < output.length()) return true;
            }

        } else if (input instanceof Integer && output instanceof Integer) {

            final var input = (Integer) this.input;
            final var output = (Integer) this.output;

            if (operator.equals("==")) {
                if (input == output) return true;
            } else if (operator.equals(">=")) {
                if (input >= output) return true;
            } else if (operator.equals("<=")) {
                if (input <= output) return true;
            } else if (operator.equals("!=")) {
                if (input != output) return true;
            } else if (operator.equals(">")) {
                if (input > output) return true;
            } else if (operator.equals("<")) {
                if (input < output) return true;
            }

        } else {
            super.debugService.debug("&cНе получается поверить входящее и исходящее значения " + this.requirementName, DebugLevel.MEDIUM);
            return false;
        }
        return false;
    }

}
