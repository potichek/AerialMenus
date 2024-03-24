package bibilmeshka.projects.aerialmenus.services.skull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MaterialCheckConsumer {

    private final MaterialCheckService service;

    public MaterialCheckConsumer(final MaterialCheckService service) {
        this.service = service;
    }

    public ItemStack create(final Object source, final Player player) {
        return this.service.create(source, player);
    }

}
