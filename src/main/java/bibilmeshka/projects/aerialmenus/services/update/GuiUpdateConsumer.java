package bibilmeshka.projects.aerialmenus.services.update;

import org.bukkit.command.CommandSender;

public class GuiUpdateConsumer {

    private final GuiUpdateService service;

    public GuiUpdateConsumer(final GuiUpdateService service) {
        this.service = service;
    }

    public void initialize(final GuiUpdater guiUpdater) {
        this.service.initialize(guiUpdater);
    }

    public void runTasks() {
        this.service.runTasks();
    }
    public void runTask(final GuiUpdater guiUpdater) {
        this.service.runTask(guiUpdater);
    }
    public void disableTasks() {
        this.service.disableTasks();
    }

    public void disableTask(final GuiUpdater guiUpdater) {
        this.service.disableTask(guiUpdater);
    }

    public void clear() {
        this.service.clear();
    }

    public void remove(final GuiUpdater guiUpdater) {
        this.service.remove(guiUpdater);
    }

    public void showAll(final CommandSender commandSender) {
        this.service.showAll(commandSender);
    }

}
