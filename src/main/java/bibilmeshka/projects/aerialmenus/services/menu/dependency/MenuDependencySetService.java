package bibilmeshka.projects.aerialmenus.services.menu.dependency;

import bibilmeshka.projects.aerialmenus.menu.Menu;
import bibilmeshka.projects.aerialmenus.services.cutter.StringCutterService;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import bibilmeshka.projects.aerialmenus.services.menu.command.processing.CommandProcessingConsumer;
import bibilmeshka.projects.aerialmenus.services.requirement.check.RequirementCheckService;

public class MenuDependencySetService {

    private final RequirementCheckService requirementCheckService;
    private final CommandProcessingConsumer commandProcessingConsumer;
    private final DebugService debugService;

    public MenuDependencySetService(final RequirementCheckService requirementCheckService,
                                    final CommandProcessingConsumer commandProcessingConsumer,
                                    final DebugService debugService) {

        this.requirementCheckService = requirementCheckService;
        this.commandProcessingConsumer = commandProcessingConsumer;
        this.debugService = debugService;
    }

    public void setInMenu(Menu menu) {
        menu.setRequirementCheckService(this.requirementCheckService);
        menu.setCommandProcessingConsumer(this.commandProcessingConsumer);
        menu.setDebugService(this.debugService);
    }

}
