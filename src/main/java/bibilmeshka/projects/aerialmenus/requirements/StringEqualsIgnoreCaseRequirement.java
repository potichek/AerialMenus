package bibilmeshka.projects.aerialmenus.requirements;

import bibilmeshka.projects.aerialmenus.services.debug.DebugLevel;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.List;

@Setter
public class StringEqualsIgnoreCaseRequirement extends Requirement {

    private String input;
    private String output;

    public StringEqualsIgnoreCaseRequirement(RequirementType type, String requirementName, List<String> denyCommands, List<String> successCommands, boolean optional, DebugService debugService) {
        super(type, requirementName, denyCommands, successCommands, optional, debugService);
    }

    @Override
    public boolean check(Player player) {
        super.debugService.debug("&eИгрок " + player.getName() + " Проверяется на требования " + this.requirementName, DebugLevel.HIGHEST);
        final var input = PlaceholderAPI.setPlaceholders(player, this.input);
        final var output = PlaceholderAPI.setPlaceholders(player, this.output);
        return getWithPropetryNegative(input.equals(output));
    }

}
