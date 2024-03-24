package bibilmeshka.projects.aerialmenus.requirements;

import bibilmeshka.projects.aerialmenus.services.debug.DebugLevel;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.regex.Pattern;

public class RegexRequirement extends Requirement {

    @Setter
    private String input;
    @Setter
    private String regex;

    public RegexRequirement(RequirementType type, String requirementName, List<String> denyCommands, List<String> successCommands, boolean optional, DebugService debugService) {
        super(type, requirementName, denyCommands, successCommands, optional, debugService);
    }

    @Override
    public boolean check(Player player) {
        super.debugService.debug("&eИгрок " + player.getName() + " Проверяется на требования " + this.requirementName, DebugLevel.HIGHEST);
        final var input = PlaceholderAPI.setPlaceholders(player, this.input);
        final var regex = PlaceholderAPI.setPlaceholders(player, this.regex);
        final var pattern = Pattern.compile(regex);
        return getWithPropetryNegative(pattern.matcher(input).find());
    }

}
