package bibilmeshka.projects.aerialmenus.services.skull;

import bibilmeshka.projects.aerialmenus.services.debug.DebugLevel;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.lone.itemsadder.api.CustomStack;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Base64;
import java.util.UUID;

public abstract class MaterialCheckService<S> {

    private final HeadDatabaseAPI headDatabaseAPI;
    private final DebugService debugService;

    public MaterialCheckService(final HeadDatabaseAPI headDatabaseAPI, final DebugService debugService) {
        this.headDatabaseAPI = headDatabaseAPI;
        this.debugService = debugService;
    }

    public abstract ItemStack create(final S source, final Player player);

    protected ItemStack getHeadByPlayerName(final String playerName) {
        final var item = new ItemStack(Material.PLAYER_HEAD);
        final var skullMeta = (SkullMeta) item.getItemMeta();
        final var player = Bukkit.getOfflinePlayer(playerName);
        if (player == null) return item;
        skullMeta.setOwningPlayer(player);
        item.setItemMeta(skullMeta);
        return item;
    }

    protected ItemStack getHeadByBase(final String decodedString) {
        final var item = new ItemStack(Material.PLAYER_HEAD);
        final var skullMeta = (SkullMeta) item.getItemMeta();
        final var gameProfile = new GameProfile(UUID.randomUUID(), null);
        final var decodedBytes = Base64.getUrlDecoder().decode(decodedString);

        final var texture = Base64.getEncoder().encode(decodedBytes);
        gameProfile.getProperties().put("textures", new Property("textures", new String(texture)));

        try {
            final var skullProfileField = skullMeta.getClass().getDeclaredField("profile");
            skullProfileField.setAccessible(true);
            skullProfileField.set(skullMeta, gameProfile);
        } catch (Exception e) {
            this.debugService.debug("&cНе получилось получить голову игрока по декодированой строке", DebugLevel.MEDIUM);
        }
        item.setItemMeta(skullMeta);
        return item;
    }

    protected ItemStack getHeadByTexture(final String id) {
        final var item = new ItemStack(Material.PLAYER_HEAD);
        final var skullMeta = (SkullMeta) item.getItemMeta();
        final var gameProfile = new GameProfile(UUID.randomUUID(), null);
        final var encodedTexture = Base64.getEncoder()
                .encode(String.format("{textures:{SKIN:{url:\"%s\"}}}",
                        "http://textures.minecraft.net/texture/" + id).getBytes());
        gameProfile.getProperties().put("textures", new Property("textures", new String(encodedTexture)));

        try {
            final var skullProfileField = skullMeta.getClass().getDeclaredField("profile");
            skullProfileField.setAccessible(true);
            skullProfileField.set(skullMeta, gameProfile);
        } catch (Exception e) {
            this.debugService.debug("&cНе получилось получить голову игрока по текстуре", DebugLevel.MEDIUM);
        }

        item.setItemMeta(skullMeta);
        return item;
    }

    protected ItemStack getHeadByDatabase(final String id) {
        try {
            return this.headDatabaseAPI.getItemHead(id);
        } catch (Exception e) {
            this.debugService.debug("&cНе получилось получить голову игрока через плагин HeadByDatabase", DebugLevel.MEDIUM);
        }
        return null;
    }

    protected ItemStack getStackByItemsAdder(final String id) {
        try {
            return new ItemStack(CustomStack.getInstance(id).getItemStack().getType());
        } catch (Exception e) {
            this.debugService.debug("&cНе получилось получить матеиал через плагин ItemsAdder", DebugLevel.MEDIUM);
        }
        return null;
    }

    protected ItemStack getStackByPlaceholder(final String placeholder) {
        return new ItemStack(Material.valueOf(placeholder));
    }

    protected ItemStack getMainHandStack(final Player player) {
        return new ItemStack(player.getInventory().getItemInMainHand().getType());
    }

    protected ItemStack getOffHandStack(final Player player) {
        return new ItemStack(player.getInventory().getItemInOffHand().getType());
    }

    protected ItemStack getArmorHelmetStack(final Player player) {
        return new ItemStack(player.getInventory().getHelmet().getType());
    }

    protected ItemStack getArmorChestplateStack(final Player player) {
        return new ItemStack(player.getInventory().getChestplate().getType());
    }

    protected ItemStack getArmorLeggingsStack(final Player player) {
        return new ItemStack(player.getInventory().getLeggings().getType());
    }

    protected ItemStack getArmorBootsStack(final Player player) {
        return new ItemStack(player.getInventory().getBoots().getType());
    }

}
