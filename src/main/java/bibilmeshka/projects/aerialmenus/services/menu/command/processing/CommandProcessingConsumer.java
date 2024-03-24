package bibilmeshka.projects.aerialmenus.services.menu.command.processing;

import org.bukkit.entity.Player;

import java.util.List;

public class CommandProcessingConsumer {

    private final CommandProcessingService service;

    public CommandProcessingConsumer(CommandProcessingService service) {
        this.service = service;
    }

    public void executeСommands(final List<?> commands, final Player player) {
        this.service.executeСommands(commands, player);
    }

    public void executeCommand(final Object command, final Player player) {
        this.service.executeСommand(command, player);
    }

}
