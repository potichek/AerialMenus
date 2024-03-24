package bibilmeshka.projects.aerialmenus.services.update;

import bibilmeshka.projects.aerialmenus.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class GuiUpdateServiceOnMemory implements GuiUpdateService {

    private final List<GuiUpdater> updaters = new ArrayList<>();

    @Override
    public void initialize(final GuiUpdater guiUpdater) {
        this.updaters.add(guiUpdater);
    }

    @Override
    public void clear() {
        this.updaters.clear();
    }

    @Override
    public void disableTask(final GuiUpdater guiUpdater) {
        for (final var updater : this.updaters) {
            if (updater.equals(guiUpdater)) {
                updater.stopTask();
                return;
            }
        }
    }

    @Override
    public void remove(final GuiUpdater guiUpdater) {
        this.updaters.remove(guiUpdater);
    }

    @Override
    public void runTasks() {
        for (final var updater : this.updaters) {
            updater.startTask();
        }
    }

    @Override
    public void runTask(final GuiUpdater guiUpdater) {
        for (final var updater : this.updaters) {
            if (updater.equals(guiUpdater)) {
                updater.startTask();
                return;
            }
        }
    }

    @Override
    public void disableTasks() {
        for (final var updater : this.updaters) {
            updater.stopTask();
        }
    }

    @Override
    public void showAll(final CommandSender commandSender) {
        for (final var updater : this.updaters) {
            commandSender.sendMessage(
                    ChatColor.YELLOW + "Updater: " + updater.getMenu().getMenuName() + " id: " + updater.getTaskId());
        }
    }
}
