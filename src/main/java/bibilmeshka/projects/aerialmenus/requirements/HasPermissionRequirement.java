package bibilmeshka.projects.aerialmenus.requirements;

import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.List;

@Setter
public class HasPermissionRequirement extends Requirement {

    private String permission;

    public HasPermissionRequirement(RequirementType type,  String requirementName, List<String> denyCommands,List<String> successCommands, boolean optional, DebugService debugService) {
        super(type, requirementName, denyCommands, successCommands, optional, debugService);
    }

    @Override
    public boolean check(Player player) {
        return getWithPropetryNegative(player.hasPermission(permission));
    }

}
