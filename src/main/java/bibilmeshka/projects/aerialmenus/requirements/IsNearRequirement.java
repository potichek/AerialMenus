package bibilmeshka.projects.aerialmenus.requirements;

import bibilmeshka.projects.aerialmenus.services.debug.DebugLevel;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

@Setter
public class IsNearRequirement extends Requirement {

    private Location location;
    private double distance;

    public IsNearRequirement(RequirementType type, String requirementName, List<String> denyCommands, List<String> successCommands, boolean optional, DebugService debugService) {
        super(type, requirementName, denyCommands, successCommands, optional, debugService);
    }

    private boolean checkWorld(Player player) {
        return player.getLocation().getWorld().getName().equals(location.getWorld().getName());
    }

    private boolean checkX(Player player) {
        final var substractedX = player.getLocation().getX() - location.getX();

        if (substractedX >= 0) {
            if (substractedX <= distance) return true;
        } else {
            if ((substractedX * -1) <= distance) return true;
        }

        return false;
    }

    private boolean checkY(Player player) {
        final var substractedY = player.getLocation().getY() - location.getY();

        if (substractedY >= 0) {
            if (substractedY <= distance) return true;
        } else {
            if ((substractedY * -1) <= distance) return true;
        }

        return false;
    }

    private boolean checkZ(Player player) {
        final var substractedZ = player.getLocation().getZ() - location.getZ();

        if (substractedZ >= 0) {
            if (substractedZ <= distance) return true;
        } else {
            if ((substractedZ * -1) <= distance) return true;
        }

        return false;
    }

    @Override
    public boolean check(Player player) {
        super.debugService.debug("&eИгрок " + player.getName() + " Проверяется на требования " + this.requirementName, DebugLevel.HIGHEST);
        return getWithPropetryNegative(checkWorld(player) && checkX(player) && checkY(player) && checkZ(player));
    }

}
