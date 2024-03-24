package bibilmeshka.projects.aerialmenus.services.menu.command.processing;

import bibilmeshka.projects.aerialmenus.AerialMenus;
import bibilmeshka.projects.aerialmenus.menu.MenuArgs;
import bibilmeshka.projects.aerialmenus.services.metadata.MetaValueType;
import bibilmeshka.projects.aerialmenus.services.metadata.Operation;
import bibilmeshka.projects.aerialmenus.services.player.PlayerServiceConsumer;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.PermissionAttachment;

import java.util.List;

public abstract class CommandProcessingService<S> {

    private final BukkitAudiences audiences;
    private final PlayerServiceConsumer playerServiceConsumer;
    protected final AerialMenus plugin;
    private final Economy economy;

    public CommandProcessingService(final BukkitAudiences audiences,
                                    final PlayerServiceConsumer playerServiceConsumer,
                                    final AerialMenus plugin,
                                    final Economy economy) {
        this.audiences = audiences;
        this.playerServiceConsumer = playerServiceConsumer;
        this.plugin = plugin;
        this.economy = economy;
    }

    public void executeСommands(final List<S> commands, final Player player) {
        for (final var command : commands) {
            executeСommand(command, player);
        }
    }

    public abstract void executeСommand(final S command, final Player player);

    protected boolean chance(final int chance) {
        return chance >= ((int) (Math.random() * 101));
    }

    protected void performPlayerCommand(final Player player, final String command) {
        player.performCommand(command);
    }

    protected void commandEvent(final Player player, final String command) {
        Bukkit.getPluginManager().callEvent(new PlayerCommandPreprocessEvent(player, command));
    }

    protected void placeholder(final Player player, final String placeholder) {
        Bukkit.getConsoleSender().sendMessage(PlaceholderAPI.setPlaceholders(player, placeholder));
    }

    protected void performConsoleCommand(final Player player, final String command) {
        Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(),
                command
                );
    }

    protected void sendMessage(final Player player, final String message) {
        player.sendMessage(
                PlaceholderAPI.setPlaceholders(player, ChatColor.translateAlternateColorCodes('&', message)));
    }

    protected void openMenu(final Player player, final String menuName) {
        final var menu = this.playerServiceConsumer.getMenuByName(menuName);
        if (menu == null) return;
        menu.open(player, player, new MenuArgs());
    }

    protected void refreshMenu(final Player player) {
        final var menu = this.playerServiceConsumer.getMenuOfPlayer(player.getUniqueId());
        if (menu == null) return;
        menu.updatePlaceholders(player, player, new MenuArgs());
    }

    protected void takeMoney(final Player player, final double amount) {
        this.economy.withdrawPlayer(player, amount);
    }

    protected void givemoney(final Player player, final double amount) {
        this.economy.depositPlayer(player, amount);
    }

    protected void takeexp(final Player player, final int xp) {
        if ((player.getExp() - xp) < 0) return;
        player.setTotalExperience(player.getTotalExperience() - xp);
    }

    protected void giveexp(final Player player, final int xp) {
        player.giveExp(xp);
    }

    protected void givepermission(final Player player, final String permissionName) {
        player.addAttachment(this.plugin, permissionName, true);
        for (final var permissiom : player.getEffectivePermissions()) {
            Bukkit.getConsoleSender().sendMessage("Perm: " + permissiom.getPermission());
        }
    }

    protected void takepermission(final Player player, final String permissionName) {
        final var attachment = new PermissionAttachment(this.plugin, player);
        player.removeAttachment(attachment);
    }

    protected void meta(final Player player,
                        final Operation operation,
                        final String key,
                        final MetaValueType valueType,
                        final Object value,
                        final MetadataValue metadataValue) {
        
        if (operation == Operation.SET) {
            player.setMetadata(key, metadataValue);
        } else if (operation == Operation.REMOVE) {
            player.removeMetadata(key, this.plugin);
        } else if (operation == Operation.ADD) {

            if (valueType == MetaValueType.DOUBLE) {
                player.setMetadata(key,
                        new FixedMetadataValue(
                                this.plugin, metadataValue.asDouble() + (double) value));
            } else if (valueType == MetaValueType.LONG) {
                player.setMetadata(key,
                        new FixedMetadataValue(
                                this.plugin, metadataValue.asLong() + (long) value));
            } else if (valueType == MetaValueType.INTEGER) {
                player.setMetadata(key,
                        new FixedMetadataValue(
                                this.plugin, metadataValue.asInt() + (int) value));
            }

        } else if (operation == Operation.SUBSTRACT) {

            if (valueType == MetaValueType.DOUBLE) {
                player.setMetadata(key,
                        new FixedMetadataValue(
                                this.plugin, metadataValue.asDouble() - (double) value));
            } else if (valueType == MetaValueType.LONG) {
                player.setMetadata(key,
                        new FixedMetadataValue(
                                this.plugin, metadataValue.asLong() - (long) value));
            } else if (valueType == MetaValueType.INTEGER) {
                player.setMetadata(key,
                        new FixedMetadataValue(
                                this.plugin, metadataValue.asInt() - (int) value));
            }

        } else if (operation == Operation.SWITCH) {
            player.setMetadata(key,
                    new FixedMetadataValue(
                            this.plugin, !metadataValue.asBoolean()));
        }
    }

    protected void chat(final Player player, final String message) {
        player.chat(message);
    }

    protected void connect(final Player player, final String serverName) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        try {
            output.writeUTF("Connect");
            output.writeUTF(serverName);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        player.sendPluginMessage(this.plugin, "BungeeCord", output.toByteArray());
    }

    protected void broadcast(final String message) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    protected void broadcastSoundWorld(final Player player, final Sound sound, final float volume, final float pitch) {
        for (final var target : player.getWorld().getPlayers()) {
            target.getWorld().playSound(target.getLocation(), sound, volume, pitch);
        }
    }

    protected void close(final Player player) {
        final var menu = this.playerServiceConsumer.getMenuOfPlayer(player.getUniqueId());
        if (menu == null) return;
        player.closeInventory();
        menu.close(player, true);
    }

    protected void sendJsonMessage(final Player player, final String message) {
        player.sendMessage(
                TextComponent.toLegacyText(
                        ComponentSerializer.parse(message)));
    }

    protected void broadcastJsonMessage(final String message) {
        Bukkit.broadcastMessage(
                TextComponent.toLegacyText(
                        ComponentSerializer.parse(message)));
    }

    protected void sendMinimessage(final Player player, final String message) {
        final var audience = this.audiences.player(player);
        audience.sendMessage(
                MiniMessage.miniMessage().deserialize(PlaceholderAPI.setPlaceholders(player, message)));
    }

    protected void broadcastMinimessage(final String message) {
        for (final var player : Bukkit.getOnlinePlayers()) {
            final var audiences = this.audiences.player(player);
            audiences.sendMessage(MiniMessage.miniMessage().deserialize(PlaceholderAPI.setPlaceholders(player, message)));
        }
    }

    protected void broadcastSound(final Player player, final Sound sound, final float volume, final float pitch) {
        for (final var p : Bukkit.getOnlinePlayers()) {
            p.getWorld().playSound(p.getLocation(), sound, volume, pitch);
        }
    }

    protected void playSound(final Player player, final Sound sound, final float volume, final float pitch) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

}