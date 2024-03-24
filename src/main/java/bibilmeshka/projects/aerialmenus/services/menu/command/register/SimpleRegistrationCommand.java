package bibilmeshka.projects.aerialmenus.services.menu.command.register;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SimpleRegistrationCommand extends BukkitCommand {

    public SimpleRegistrationCommand(String name) {
        super(name, "", "", new ArrayList<>());
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        return true;
    }
}
