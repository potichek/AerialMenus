package bibilmeshka.projects.aerialmenus.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuArgs extends HashMap<String, String> {

    private final List<String> args;
    private final String holders;
    private List<String> holdersList;

    public MenuArgs() {
        this.args = null;
        this.holders = null;
    }

    public MenuArgs(final List<String> args, final String holders) {
        this.args = args;
        this.holders = holders;
        setUp();
    }

    private void setUp() {
        this.holdersList = splitIntoArguments(this.holders);
        assignArgToHolder(args, holdersList);
    }

    public boolean isEnoughHolders() {
        if (args == null || holders == null) return true;
        return args.size() > holdersList.size();
    }

    public @NotNull String setHolders(String string) {
        if (args == null || holders == null) return string;
        for (final var arg : this.args) {
            final var holder = "{" + arg.strip() + "}";
            if (!(string.contains(holder))) continue;
            string = string.replace(holder, super.get(arg));
        }
        return string;
    }

    private @NotNull List<String> splitIntoArguments(String string) {
        final var holders = new ArrayList<String>();
        string = string.strip();

        while (!(string.isEmpty())) {
            String arg;

            if (string.indexOf(" ") == -1) {
                arg = string.substring(0, string.length());
                holders.add(arg);
                break;
            } else {
                arg = string.substring(0, string.indexOf(" "));
                holders.add(arg);
                string = string.replaceFirst(arg, "").strip();
            }
        }

        return holders;
    }

    private void assignArgToHolder(final List<String> args, final List<String> holders) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Args: " + args);
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Holders: " + holders);
        if (args.isEmpty() || holdersList == null) return;

        final var argSize = args.size();
        final var holderSize = holders.size();
        final var generalSize = (argSize >= holderSize ? holderSize : argSize);

        for (var index = 0; index <= (generalSize - 2); index++) {
            super.put(args.get(index), holders.get(index));
        }

        var lastArgument = "";
        for (var index = (generalSize - 1); index <= (holderSize - 1); index++) {
            lastArgument += holders.get(index);
        }

        super.put(args.get(generalSize - 1), lastArgument);
    }
}
