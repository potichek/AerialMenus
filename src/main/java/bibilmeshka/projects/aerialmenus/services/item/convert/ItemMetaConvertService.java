package bibilmeshka.projects.aerialmenus.services.item.convert;

import bibilmeshka.projects.aerialmenus.menu.items.MenuItemBannerMeta;
import bibilmeshka.projects.aerialmenus.menu.items.MenuItemColor;
import bibilmeshka.projects.aerialmenus.menu.items.MenuItemEnchantment;
import bibilmeshka.projects.aerialmenus.menu.items.MenuItemPotionEffect;

public interface ItemMetaConvertService<S> {

    MenuItemBannerMeta createBannerMeta(S resource, String itemName);
    MenuItemColor createItemColor(S resource, String itemName);
    MenuItemEnchantment createItemEnchantment(S resource, String itemName);
    MenuItemPotionEffect createItemPotionEffect(S resource, String itemName);

}
