package bibilmeshka.projects.aerialmenus.menu;

import lombok.Getter;
import org.bukkit.entity.Player;

public class MenuComponents {

    @Getter
    private final MenuArgs menuArgs;
    @Getter
    private final Player placeholderOwner;

    public MenuComponents(final MenuArgs menuArgs, final Player placeholderOwner) {
        this.menuArgs = menuArgs;
        this.placeholderOwner = placeholderOwner;
    }
}
