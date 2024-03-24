package bibilmeshka.projects.aerialmenus.nms;

import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import org.bukkit.inventory.ItemStack;

public abstract class NMSService {

    protected final DebugService debugService;

    public NMSService(final DebugService debugService) {
        this.debugService = debugService;
    }

    public abstract boolean hasTag(final ItemStack item, final String key);
    public abstract ItemStack setTag(final ItemStack item, final String key, final String value);
    public abstract ItemStack setTag(final ItemStack item, final String key, final Integer value);
    public abstract String getTags(final ItemStack item);

}
