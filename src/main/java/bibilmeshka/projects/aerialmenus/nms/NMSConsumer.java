package bibilmeshka.projects.aerialmenus.nms;

import bibilmeshka.projects.aerialmenus.nms.v1_19_R3.NMSv1_19_R3;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class NMSConsumer {

    private NMSService service;
    private final DebugService debugService;

    public NMSConsumer(final DebugService debugService) {
        this.debugService = debugService;
    }

    public void setup() {
        final var version = Bukkit.getBukkitVersion();
        if (version.startsWith("1.19.4")) {
            this.service = new NMSv1_19_R3(this.debugService);
        }
    }

    public boolean hasTag(final ItemStack item, final String key) {
        return this.service.hasTag(item, key);
    }

    public ItemStack setTag(final ItemStack item, final String key, final String value) {
        return this.service.setTag(item, key, value);
    }

    public ItemStack setTag(final ItemStack item, final String key, final Integer value) {
        return this.service.setTag(item, key, value);
    }

    public String getTags(final ItemStack item) {
        return this.service.getTags(item);
    }

}
