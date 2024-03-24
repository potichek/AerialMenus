package bibilmeshka.projects.aerialmenus.services.menu.command.register;

import bibilmeshka.projects.aerialmenus.menu.Menu;
import bibilmeshka.projects.aerialmenus.services.debug.DebugLevel;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import org.bukkit.command.CommandMap;

public class MenuCommandRegisterService {

    private final CommandMap commandMap;
    private final DebugService debugService;

    public MenuCommandRegisterService(final CommandMap commandMap, final DebugService debugService) {
        this.commandMap = commandMap;
        this.debugService = debugService;
    }

    public void register(final Menu menu) {
        try {
            if (!(menu.isNeedRegister())) return;
            for (final var commandName : menu.getCommands()) {
                final var command = new SimpleRegistrationCommand(commandName);
                this.commandMap.register(commandName, command);
                command.register(this.commandMap);
            }
        } catch (Exception e) {
            this.debugService.debug("&cНе получилось зарегистрировать команду ", DebugLevel.HIGH);
        }
    }

}
