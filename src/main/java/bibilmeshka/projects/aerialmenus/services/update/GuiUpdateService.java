package bibilmeshka.projects.aerialmenus.services.update;

import bibilmeshka.projects.aerialmenus.menu.Menu;
import org.bukkit.command.CommandSender;

public interface GuiUpdateService {

    void initialize(final GuiUpdater guiUpdater);
    void runTasks();
    void runTask(final GuiUpdater guiUpdater);
    void disableTasks();
    void disableTask(final GuiUpdater guiUpdater);
    void clear();
    void remove(final GuiUpdater guiUpdater);
    void showAll(final CommandSender commandSender);

}
