package bibilmeshka.projects.aerialmenus.menu.items;

import org.bukkit.Color;

public class MenuItemColor {

    private final int red;
    private final int green;
    private final int blue;

    public MenuItemColor(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Color toColor() {
        return Color.fromRGB(red, green, blue);
    }

}
