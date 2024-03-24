package bibilmeshka.projects.aerialmenus.menu.items;

import lombok.Getter;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.PatternType;

@Getter
public class MenuItemBannerMeta {

    private DyeColor dyeColor;
    private PatternType patternType;

    public MenuItemBannerMeta(DyeColor dyeColor, PatternType patternType) {
        this.dyeColor = dyeColor;
        this.patternType = patternType;
    }
}
