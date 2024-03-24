package bibilmeshka.projects.aerialmenus.commands;

import bibilmeshka.projects.aerialmenus.menu.MenuArgs;
import bibilmeshka.projects.aerialmenus.menu.MenuComponents;
import bibilmeshka.projects.aerialmenus.services.debug.DebugLevel;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import bibilmeshka.projects.aerialmenus.services.dump.DumpService;
import bibilmeshka.projects.aerialmenus.services.menu.MenuManagementService;
import bibilmeshka.projects.aerialmenus.services.menu.command.processing.CommandProcessingConsumer;
import bibilmeshka.projects.aerialmenus.services.player.PlayerServiceConsumer;
import bibilmeshka.projects.aerialmenus.services.update.GuiUpdateConsumer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class AerialCommand implements CommandExecutor {

    private final PlayerServiceConsumer playerServiceConsumer;
    private final MenuManagementService menuManagementService;
    private final CommandProcessingConsumer commandProcessingConsumer;
    private final DumpService dumpService;
    private final GuiUpdateConsumer guiUpdateConsumer;
    private final DebugService debugService;
    private final JavaPlugin plugin;

    public AerialCommand(final JavaPlugin plugin,
                         final PlayerServiceConsumer playerServiceConsumer,
                         final MenuManagementService menuManagementService,
                         final CommandProcessingConsumer commandProcessingConsumer,
                         final DumpService dumpService,
                         final GuiUpdateConsumer guiUpdateConsumer,
                         final DebugService debugService) {
        this.plugin = plugin;
        this.playerServiceConsumer = playerServiceConsumer;
        this.menuManagementService = menuManagementService;
        this.commandProcessingConsumer = commandProcessingConsumer;
        this.dumpService = dumpService;
        this.guiUpdateConsumer = guiUpdateConsumer;
        this.debugService = debugService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 0) {
            sendDefaultMessage(commandSender);
            return true;
        }

        if (strings[0].equalsIgnoreCase("execute")) {
            if (commandSender.isOp()) {
                final var player = Bukkit.getPlayer(strings[1]);

                if (player == null) {
                    commandSender.sendMessage(ChatColor.RED + "Игрок не найден!");
                    return true;
                }

                var stringCommand = "";
                for (var index = 2; index <= (strings.length - 1); index++) {
                    stringCommand += strings[index] + " ";
                }
                commandSender.sendMessage(stringCommand);

                this.commandProcessingConsumer.executeCommand(stringCommand, player);
            }
        }

        if (strings.length == 1) {
            if (strings[0].equalsIgnoreCase("list")) {
                if (commandSender.hasPermission("aerialmenus.list")) {
                    commandSender.sendMessage(ChatColor.AQUA + "Зарегестрированые меню");
                    for (final var menu : this.playerServiceConsumer.getMenus()) {
                        commandSender.sendMessage(ChatColor.YELLOW + menu.getMenuName());
                    }
                    return true;
                }
            } else if (strings[0].equalsIgnoreCase("reload")) {
                if (commandSender.hasPermission("aerialmenus.reload")) {
                    this.menuManagementService.reloadMenus();
                    this.plugin.reloadConfig();
                    this.debugService.setMainLevel(DebugLevel.valueOf(this.plugin.getConfig().getString("debug", "LOWEST")));

                    commandSender.sendMessage(ChatColor.AQUA + "Меню перезагружены");
                    commandSender.sendMessage(ChatColor.AQUA + "Конфиг перезагружены");
                    return true;
                }
            } else if (strings[0].equalsIgnoreCase("test")) {
                if (commandSender.hasPermission("aerialmenus.test")) {
                    commandSender.sendMessage(ChatColor.AQUA + "Active workers");
                    for (final var worker : Bukkit.getScheduler().getActiveWorkers()) {
                        commandSender.sendMessage(ChatColor.YELLOW + String.valueOf(worker.getTaskId()));
                    }

                    commandSender.sendMessage(ChatColor.AQUA + "Pending tasks");
                    for (final var worker : Bukkit.getScheduler().getPendingTasks()) {
                        commandSender.sendMessage(ChatColor.YELLOW + String.valueOf(worker.getTaskId()));
                    }

                    commandSender.sendMessage(ChatColor.AQUA + "Menus statistic");
                    this.playerServiceConsumer.showAll(commandSender);
                    commandSender.sendMessage(ChatColor.AQUA + "GuiUpdaters statistic");
                    this.guiUpdateConsumer.showAll(commandSender);
                    return true;
                }
            }
            sendDefaultMessage(commandSender);
            return true;
        }

        if (strings.length == 2) {
            if (strings[0].equalsIgnoreCase("open")) {
                if (!(commandSender.hasPermission("aerialmenus.open"))) return true;
                if (commandSender instanceof Player player) {
                    final var menu = this.playerServiceConsumer.getMenuByName(strings[1]);
                    if (menu == null) {
                        commandSender.sendMessage(ChatColor.RED + "Меню не найдено!");
                        return true;
                    }
                    menu.open(player, player, new MenuArgs());
                    return true;
                }
                commandSender.sendMessage(ChatColor.RED + "Эту команду должен использовать игрок!");
            } else if (strings[0].equalsIgnoreCase("dump")) {
                if (!(commandSender.hasPermission("aerialmenus.admin"))) return true;
                final var menu = this.playerServiceConsumer.getMenuByName(strings[1]);
                if (menu == null) {
                    commandSender.sendMessage(ChatColor.RED + "Меню не найдено!");
                    return true;
                }
                commandSender.sendMessage(ChatColor.GREEN + "Вот ваш конфиг: " + this.dumpService.sendPost(menu));
            } else if (strings[0].equalsIgnoreCase("reload")) {
                if (!(commandSender.hasPermission("aerialmenus.reload"))) return false;
                final var menu = this.playerServiceConsumer.getMenuByName(strings[1]);
                if (menu == null) {
                    commandSender.sendMessage(ChatColor.RED + "Меню не найдено!");
                    return true;
                }
                this.menuManagementService.reloadMenu(menu);
                commandSender.sendMessage(ChatColor.GREEN + "Меню " + menu.getMenuName() + " успешно перезагружено");
            }
            return true;
        }

        if (strings.length == 3) {
            if (strings[0].equalsIgnoreCase("open")) {

                final var menu = this.playerServiceConsumer.getMenuByName(strings[1]);
                if (menu == null) {
                    commandSender.sendMessage(ChatColor.RED + "Меню не найдено!");
                    return true;
                }

                if (commandSender.hasPermission("aerialmenus.placeholdersfor")) {
                    if (strings[2].startsWith("-p:")) {
                        if (commandSender instanceof Player sender) {
                            final var placeholderOwner = Bukkit.getPlayer(strings[2].replace("-p:", ""));
                            if (placeholderOwner == null) {
                                commandSender.sendMessage(ChatColor.RED + "Игрок не найден!");
                                return true;
                            }
                            if (placeholderOwner.hasPermission("aerialmenus.placeholdersfor.exempt")) {
                                sender.sendMessage(ChatColor.RED + "Вы не можете использовать заполнители этого игрока!");
                                return true;
                            }
                            menu.open(sender, placeholderOwner, new MenuArgs());
                            return true;
                        }
                    }
                }

                if (commandSender.hasPermission("aerialmenus.open.others")) {
                    final var sender = Bukkit.getPlayer(strings[2]);
                    if (Bukkit.getPlayer(strings[2]) == null) {
                        commandSender.sendMessage(ChatColor.RED + "Игрок не найден!");
                        return true;
                    }
                    menu.open(sender, sender, new MenuArgs());
                }
            }
        }

        if (strings.length == 4) {
            if (strings[0].equalsIgnoreCase("open")) {
                if (commandSender.hasPermission("aerialmenus.placeholdersfor")) {
                    final var menu = this.playerServiceConsumer.getMenuByName(strings[1]);
                    final var sender = Bukkit.getPlayer(strings[2]);
                    final var placeholderOwner = Bukkit.getPlayer(strings[3].replace("-p:", ""));

                    if (menu == null) {
                        commandSender.sendMessage(ChatColor.RED + "Меню не найдено!");
                        return true;
                    }

                    if (sender == null || placeholderOwner == null) {
                        commandSender.sendMessage(ChatColor.RED + "Игрок не найден!");
                        return true;
                    }

                    menu.open(sender, placeholderOwner, new MenuArgs());
                    return true;
                }
            }
        }
        return true;
    }

    private void sendDefaultMessage(final CommandSender commandSender) {
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&7[&l&dAerial&bMenus&r&7] &eВведите один из аргументов\n" +
                        "&areload - перезагружает все меню и конфиг\n" +
                        "&atest - выводит: содержимое всех контеинеров, активные потоки\n" +
                        "&aopen <menu> - открывает меню с данным именем\n" +
                        "&aopen <menu> <player> - открывает игроку меню с данным именем\n" +
                        "&aopen <menu> -p:<target> - открывает вам меню с заполнителями игрока\n" +
                        "&aopen <menu> <viewer> -p:<target> - открывает игроку меню с данным именем и с заполнителями указаного игрока \n" +
                        "&alist - выводит список всех зарегестрированых меню\n" +
                        "&aexecute <player> <action> - выполняет действие от имени игрока\n"));
    }

}
