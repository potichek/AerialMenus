package bibilmeshka.projects.aerialmenus.menu.items;

public enum MenuItemClickType {
    CLICK_REQUIREMENT("click_requirement"),
    LEFT_CLICK_REQUIREMENT("left_click_requirement"),
    RIGHT_CLICK_REQUIREMENT("right_click_requirement"),
    MIDDLE_CLICK_REQUIREMENT("middle_click_requirement"),
    SHIFT_LEFT_CLICK_REQUIREMENT("shift_left_click_requirement"),
    SHIFT_RIGHT_CLICK_REQUIREMENT("shift_right_click_requirement");

    private final String stringType;

    MenuItemClickType(final String stringType) {
        this.stringType = stringType;
    }

    public String getStringType() {
        return this.stringType;
    }
}
