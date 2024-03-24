package bibilmeshka.projects.aerialmenus.services.skull;

import bibilmeshka.projects.aerialmenus.services.debug.DebugLevel;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MaterialCheckServiceOnString extends MaterialCheckService<String> {

    public MaterialCheckServiceOnString(final HeadDatabaseAPI headDatabaseAPI, final DebugService debugService) {
        super(headDatabaseAPI, debugService);
    }

    @Override
    public ItemStack create(final String source, final Player player) {
        if (source.startsWith("head-")) {
            return super.getHeadByPlayerName(source.replace("head-", ""));
        } else if (source.startsWith("basehead-")) {
            return super.getHeadByBase(source.replace("basehead-", ""));
        } else if (source.startsWith("texture-")) {
            return super.getHeadByTexture(source.replace("texture-", ""));
        } else if (source.startsWith("hdb-")) {
            return super.getHeadByDatabase(source.replace("hdb-", ""));
        } else if (source.startsWith("itemsadder-namespace:")) {
            return super.getStackByItemsAdder(source.replace("itemsadder-namespace:", ""));
        } else if (source.startsWith("placeholder-")) {
            return super.getStackByPlaceholder(PlaceholderAPI.setPlaceholders(player, source.replace("placeholder-", "")));
        } else if (source.equalsIgnoreCase("main_hand")) {
            return super.getMainHandStack(player);
        } else if (source.equalsIgnoreCase("off_hand")) {
            return super.getOffHandStack(player);
        } else if (source.equalsIgnoreCase("armor_helmet")) {
            return super.getArmorHelmetStack(player);
        } else if (source.equalsIgnoreCase("armor_chestplate")) {
            return super.getArmorChestplateStack(player);
        } else if (source.equalsIgnoreCase("armor_leggings")) {
            return super.getArmorLeggingsStack(player);
        } else if (source.equalsIgnoreCase("armor_boots")) {
            return super.getArmorBootsStack(player);
        }
        return new ItemStack(Material.valueOf(source.toUpperCase()));
    }
}
