package bibilmeshka.projects.aerialmenus.services.item.convert;

import bibilmeshka.projects.aerialmenus.menu.items.MenuItemBannerMeta;
import bibilmeshka.projects.aerialmenus.menu.items.MenuItemColor;
import bibilmeshka.projects.aerialmenus.menu.items.MenuItemEnchantment;
import bibilmeshka.projects.aerialmenus.menu.items.MenuItemPotionEffect;
import bibilmeshka.projects.aerialmenus.services.item.ItemConvertConsumer;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.potion.PotionEffectType;

public class ItemMetaConvertConsumer {

    private final ItemMetaConvertService service;

    public ItemMetaConvertConsumer(ItemMetaConvertService service) {
        this.service = service;
    }

    public MenuItemBannerMeta createBannerMeta(YamlConfiguration resource, String itemName) {
        return this.service.createBannerMeta(resource, itemName);
    }

    public MenuItemColor createItemColor(YamlConfiguration resource, String itemName) {
        return this.service.createItemColor(resource, itemName);
    }

    public MenuItemEnchantment createItemEnchantment(YamlConfiguration resource, String itemName) {
        return this.service.createItemEnchantment(resource, itemName);
    }

    public MenuItemPotionEffect createItemPotionEffect(YamlConfiguration resource, String itemName) {
        return this.service.createItemPotionEffect(resource, itemName);
    }

}
