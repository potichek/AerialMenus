package bibilmeshka.projects.aerialmenus.menu.items;

import lombok.Getter;
import org.bukkit.enchantments.Enchantment;

public class MenuItemEnchantment {

    @Getter
    private Enchantment enchantment;
    @Getter
    private int level;

    public MenuItemEnchantment(Enchantment enchantment, int level) {
        this.enchantment = enchantment;
        this.level = level;
    }

}
