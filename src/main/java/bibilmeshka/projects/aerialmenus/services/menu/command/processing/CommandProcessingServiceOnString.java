package bibilmeshka.projects.aerialmenus.services.menu.command.processing;

import bibilmeshka.projects.aerialmenus.AerialMenus;
import bibilmeshka.projects.aerialmenus.services.cutter.StringCutterService;
import bibilmeshka.projects.aerialmenus.services.debug.DebugLevel;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import bibilmeshka.projects.aerialmenus.services.metadata.MetaDataFactory;
import bibilmeshka.projects.aerialmenus.services.metadata.MetaValueType;
import bibilmeshka.projects.aerialmenus.services.metadata.Operation;
import bibilmeshka.projects.aerialmenus.services.player.PlayerServiceConsumer;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class CommandProcessingServiceOnString extends CommandProcessingService<String> {

    private final StringCutterService stringCutterService;
    private final MetaDataFactory metaDataFactory;
    private final DebugService debugService;

    public CommandProcessingServiceOnString(final BukkitAudiences audiences,
                                            final PlayerServiceConsumer playerServiceConsumer,
                                            final AerialMenus plugin,
                                            final StringCutterService stringCutterService,
                                            final MetaDataFactory metaDataFactory,
                                            final Economy economy,
                                            final DebugService debugService) {
        super(audiences, playerServiceConsumer, plugin, economy);
        this.stringCutterService = stringCutterService;
        this.metaDataFactory = metaDataFactory;
        this.debugService = debugService;
    }

    private String getTag(final String string, final String tag) {
        final var matcher = Pattern.compile(tag, Pattern.MULTILINE).matcher(string);

        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    private String getChanceTag(final String string) {
        return getTag(string, "<chance=\\w+>");
    }

    private String getDelayTag(final String string) {
        return getTag(string, "<delay=\\w+>");
    }

    private int getDelay(final String string) {
        try {
            return Integer.parseInt(getTag(getDelayTag(string), "\\d+"));
        } catch (Exception e) {
            return 0;
        }
    }

    private int getChance(final String string) {
        try {
            return Integer.parseInt(getTag(getChanceTag(string), "\\d+"));
        } catch (Exception e) {
            return 100;
        }
    }

    @Override
    public void executeСommand(String command, final Player player) {
        if (!(super.chance(getChance(command)))) return;
        final var delay = getDelay(command);
        if (delay != 0) {
            Bukkit.getScheduler().runTaskLater(super.plugin, () -> {
                processCommand(command, player);
            }, new Long(delay * 20));
            return;
        }
        processCommand(command, player);
    }

    private void processCommand(String command, final Player player) {
        try {
            final var chanceTag = getChanceTag(command);
            final var delayTag = getDelayTag(command);
            var onlyCommand = PlaceholderAPI.setPlaceholders(player, getOnlyCommand(command));

            if (!(chanceTag.isEmpty())) onlyCommand = onlyCommand.replace(chanceTag, "");
            if (!(delayTag.isEmpty())) onlyCommand = onlyCommand.replace(delayTag, "");

            command = command.strip();
            if (command.startsWith("[player]")) {
                super.performPlayerCommand(player, PlaceholderAPI.setPlaceholders(player, onlyCommand));
            } else if (command.startsWith("[console]")) {
                super.performConsoleCommand(player, PlaceholderAPI.setPlaceholders(player, onlyCommand));
            } else if (command.startsWith("[commandevent]")) {
                super.commandEvent(player, onlyCommand);
            } else if (command.startsWith("[placeholder]")) {
                super.placeholder(player, onlyCommand);
            } else if (command.startsWith("[message]")) {
                super.sendMessage(player, onlyCommand);
            } else if (command.startsWith("[openguimenu]")) {
                super.openMenu(player, onlyCommand);
            } else if (command.startsWith("[refresh]")) {
                super.refreshMenu(player);
            } else if (command.startsWith("[connect]")) {
                super.connect(player, onlyCommand);
            } else if (command.startsWith("[close]")) {
                super.close(player);
            } else if (command.startsWith("[broadcast]")) {
                super.broadcast(onlyCommand);
            } else if (command.startsWith("[minimessage]")) {
                super.sendMinimessage(player, onlyCommand);
            } else if (command.startsWith("[minibroadcast]")) {
                super.broadcastMinimessage(onlyCommand);
            } else if (command.startsWith("[json]")) {
                super.sendJsonMessage(player, onlyCommand);
            } else if (command.startsWith("[jsonbroadcast]")) {
                super.broadcastJsonMessage(onlyCommand);
            } else if (command.startsWith("[broadcastsound]")) {
                final var soundMeta = this.stringCutterService.cutArgumentsFromString(onlyCommand, ' ', Float.class);
                super.broadcastSound(
                        player,
                        Sound.valueOf((String) soundMeta.get(0)),
                        (Float) soundMeta.get(1),
                        (Float) soundMeta.get(2));
            } else if (command.startsWith("[broadcastsoundworld]")) {
                final var soundMeta = this.stringCutterService.cutArgumentsFromString(onlyCommand, ' ', Float.class);
                super.broadcastSoundWorld(
                        player,
                        Sound.valueOf((String) soundMeta.get(0)),
                        (Float) soundMeta.get(1),
                        (Float) soundMeta.get(2));
            } else if (command.startsWith("[sound]")) {
                final var soundMeta = this.stringCutterService.cutArgumentsFromString(onlyCommand, ' ', Float.class);
                super.playSound(
                        player,
                        Sound.valueOf((String) soundMeta.get(0)),
                        (Float) soundMeta.get(1),
                        (Float) soundMeta.get(2));
            } else if (command.startsWith("[takemoney]")) {
                super.takeMoney(player, Double.parseDouble(onlyCommand));
            } else if (command.startsWith("[givemoney]")) {
                super.givemoney(player, Double.parseDouble(onlyCommand));
            } else if (command.startsWith("[takeexp]")) {
                super.takeexp(player, Integer.parseInt(onlyCommand));
            } else if (command.startsWith("[giveexp]")) {
                super.giveexp(player, Integer.parseInt(onlyCommand));
            } else if (command.startsWith("[givepermission]")) {
                super.givepermission(player, onlyCommand);
            } else if (command.startsWith("[takepermission]")) {
                super.takepermission(player, onlyCommand);
            } else if (command.startsWith("[meta]")) {
                final var meta = this.stringCutterService.cutArgumentsFromString(onlyCommand, ' ', Double.class);
                final var operation = ((String) meta.get(0)).toUpperCase();
                final var key = (String) meta.get(1);
                final var type = MetaValueType.valueOf(((String) meta.get(2)).toUpperCase());
                final var value = meta.get(3);
                final var metaDataValue = this.metaDataFactory.create(type, value);

                super.meta(player, Operation.valueOf(operation), key, type, value, metaDataValue);
            } else if (command.startsWith("[chat]")) {
                super.chat(player, ChatColor.translateAlternateColorCodes('&', onlyCommand));
            } else {
                this.debugService.debug("&cНе получилось определить тип действия команды " + command, DebugLevel.LOW);
            }
        } catch (Exception e) {
            this.debugService.debug("&cНе получилось обработать команду " + command, DebugLevel.MEDIUM);
        }
    }

    private String getOnlyCommand(String str) {
        str = str.strip();
        str = str.substring(str.indexOf("]") + 1);
        return str.strip();
    }

}