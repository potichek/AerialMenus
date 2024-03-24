package bibilmeshka.projects.aerialmenus.menu.items;

import lombok.Getter;
import org.bukkit.potion.PotionEffectType;

@Getter
public class MenuItemPotionEffect {

    private PotionEffectType effectType;
    private int duration;
    private int amplifier;

    public MenuItemPotionEffect(PotionEffectType effectType, int duration, int amplifier) {
        this.effectType = effectType;
        this.duration = duration;
        this.amplifier = amplifier;
    }

}
