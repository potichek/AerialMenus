package bibilmeshka.projects.aerialmenus.services.requirement.check;

import bibilmeshka.projects.aerialmenus.requirements.RequirementsData;
import bibilmeshka.projects.aerialmenus.services.debug.DebugLevel;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import bibilmeshka.projects.aerialmenus.services.menu.command.processing.CommandProcessingConsumer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RequirementCheckService {

    private final CommandProcessingConsumer commandProcessingConsumer;
    private final DebugService debugService;

    public RequirementCheckService(final CommandProcessingConsumer commandProcessingConsumer, final DebugService debugService) {
        this.commandProcessingConsumer = commandProcessingConsumer;
        this.debugService = debugService;
    }

    public boolean check(final RequirementsData requirementsData, final Player player) {
        debugService.debug("&eНачинается проверка " + player.getName() + " на требования", DebugLevel.HIGHEST);
        if (requirementsData.requirements().isEmpty()) return true;
        var completed = 0;
        for (final var requirement : requirementsData.requirements()) {
            debugService.debug("&eПроверятся " + player.getName() + " на требование " + requirement.getRequirementName(), DebugLevel.HIGHEST);
            if (requirement.check(player)) {
                completed++;
                if (requirementsData.stopAtSuccess() || completed >= requirementsData.minimumRequirements()) {
                    this.commandProcessingConsumer.executeСommands(requirement.getSuccessCommands(), player);
                    return true;
                }
            } else {
                this.commandProcessingConsumer.executeСommands(requirement.getDenyCommands(), player);
                return false;
            }
        }
        return false;
    }
}