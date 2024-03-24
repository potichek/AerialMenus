package bibilmeshka.projects.aerialmenus.services.item.convert;

import bibilmeshka.projects.aerialmenus.menu.items.MenuItemBannerMeta;
import bibilmeshka.projects.aerialmenus.menu.items.MenuItemColor;
import bibilmeshka.projects.aerialmenus.menu.items.MenuItemEnchantment;
import bibilmeshka.projects.aerialmenus.menu.items.MenuItemPotionEffect;
import bibilmeshka.projects.aerialmenus.services.cutter.StringCutterService;
import bibilmeshka.projects.aerialmenus.services.debug.DebugLevel;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import bibilmeshka.projects.aerialmenus.services.dump.DumpService;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.potion.PotionEffectType;

public class ItemMetaConvertServiceConfig implements ItemMetaConvertService<YamlConfiguration> {
    private final StringCutterService stringCutterService;
    private final DebugService debugService;

    public ItemMetaConvertServiceConfig(StringCutterService stringCutterService, DebugService debugService) {
        this.stringCutterService = stringCutterService;
        this.debugService = debugService;
    }

    @Override
    public MenuItemBannerMeta createBannerMeta(YamlConfiguration resource, String itemName) {
        try {
            final var bannerMeta = stringCutterService.cutArgumentsFromString(
                    resource.getString("items" + "." + itemName + "." + "banner_meta"), ';');
            return new MenuItemBannerMeta(
                    DyeColor.valueOf(bannerMeta.get(0).toUpperCase()),
                    PatternType.valueOf(bannerMeta.get(1).toUpperCase()));
        } catch (Exception e) {
            this.debugService.debug("&cНе получилось создать мету для банера", DebugLevel.LOW);
        }
        return null;
    }

    @Override
    public MenuItemColor createItemColor(YamlConfiguration resource, String itemName) {
        try {
            final var colorMeta = stringCutterService.cutArgumentsFromString(
                    resource.getString("items" + "." + itemName + "." + "rgb", "255,255,255"), ',');
            return new MenuItemColor(
                    Integer.parseInt(colorMeta.get(0)),
                    Integer.parseInt(colorMeta.get(1)),
                    Integer.parseInt(colorMeta.get(2)));
        } catch (Exception e) {
            this.debugService.debug("&cНе получилось создать цвет для предмета", DebugLevel.LOW);
        }
        return null;
    }

    @Override
    public MenuItemEnchantment createItemEnchantment(YamlConfiguration resource, String itemName) {
        try {
            final var enchantmentMeta = stringCutterService.cutArgumentsFromString(
                    resource.getString("items" + "." + itemName + "." + "enchantments"), ';');
            return new MenuItemEnchantment(
                    (Enchantment) new EnchantmentWrapper(enchantmentMeta.get(0).toLowerCase()),
                    Integer.parseInt(enchantmentMeta.get(1)));
        } catch (Exception e) {
            this.debugService.debug("&cНе получилось создать зачарование для предмета", DebugLevel.LOW);
        }
        return null;
    }

    @Override
    public MenuItemPotionEffect createItemPotionEffect(YamlConfiguration resource, String itemName) {
        try {
            final var potionEffectMeta = stringCutterService.cutArgumentsFromString(
                    resource.getString("items" + "." + itemName + "." + "enchantments"), ';');
            return new MenuItemPotionEffect(
                    PotionEffectType.getByName(potionEffectMeta.get(0)),
                    Integer.parseInt(potionEffectMeta.get(1)),
                    Integer.parseInt(potionEffectMeta.get(2)));
        } catch (Exception e) {
            this.debugService.debug("&cНе получилось создать эффект зелья для предмета", DebugLevel.LOW);
        }
        return null;
    }

}
