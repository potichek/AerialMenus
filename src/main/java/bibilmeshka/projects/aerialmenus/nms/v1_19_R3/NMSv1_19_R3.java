package bibilmeshka.projects.aerialmenus.nms.v1_19_R3;

import bibilmeshka.projects.aerialmenus.nms.NMSService;
import bibilmeshka.projects.aerialmenus.services.debug.DebugLevel;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NMSv1_19_R3 extends NMSService {

    public NMSv1_19_R3(final DebugService debugService) {
        super(debugService);
    }

    @Override
    public boolean hasTag(final ItemStack item, final String key) {
        if (item == null || key == null)
            return false;
        try {
            final var nmsItem = CraftItemStack.asNMSCopy(item);
            return (nmsItem.t() && nmsItem.u().b(key));
        } catch (Exception ex) {
            super.debugService.debug("&cНе удалось проверить тег по ключу: " + key, DebugLevel.MEDIUM);
            return false;
        }
    }

    @Override
    public ItemStack setTag(final ItemStack item, final String key, final String value) {
        try {
            final var nmsItem = CraftItemStack.asNMSCopy(item);
            final var tag = (nmsItem.t()) ? nmsItem.u() : new NBTTagCompound();
            tag.a(key, NBTTagString.a(value));
            final var tagField = nmsItem.getClass().getDeclaredField("v");
            tagField.setAccessible(true);
            tagField.set(nmsItem, tag);
            return CraftItemStack.asBukkitCopy(nmsItem);
        } catch (Exception ex) {
            super.debugService.debug("&cНе удалось установить строковой тег по ключу: " + key, DebugLevel.MEDIUM);
            return item;
        }
    }

    @Override
    public ItemStack setTag(final ItemStack item, final String key, final Integer value) {
        try {
            final var nmsItem = CraftItemStack.asNMSCopy(item);
            final var tag = (nmsItem.t()) ? nmsItem.u() : new NBTTagCompound();
            tag.a(key.strip(), NBTTagInt.a(value));
            final var tagField = nmsItem.getClass().getDeclaredField("v");
            tagField.setAccessible(true);
            tagField.set(nmsItem, tag);
            return CraftItemStack.asBukkitCopy(nmsItem);
        } catch (Exception ex) {
            super.debugService.debug("&cНе удалось установить строковой тег по ключу: " + key, DebugLevel.MEDIUM);
            return item;
        }
    }

    @Override
    public String getTags(final ItemStack item) {
        if (item == null) return null;
        final var nmsItem = CraftItemStack.asNMSCopy(item);
        if (nmsItem.u() == null) return null;
        return nmsItem.u().toString();
    }

}