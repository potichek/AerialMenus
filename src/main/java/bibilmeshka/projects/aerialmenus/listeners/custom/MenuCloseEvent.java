package bibilmeshka.projects.aerialmenus.listeners.custom;

import bibilmeshka.projects.aerialmenus.menu.Menu;
import bibilmeshka.projects.aerialmenus.menu.MenuComponents;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MenuCloseEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled;
    @Getter
    private final Player player;
    @Getter
    private final Menu menu;
    @Getter
    private MenuComponents menuComponents;

    public MenuCloseEvent(final Player player, final Menu menu) {
        this.player = player;
        this.menu = menu;
    }

    public MenuCloseEvent(final Player player, final Menu menu, final MenuComponents menuComponents) {
        this.player = player;
        this.menu = menu;
        this.menuComponents = menuComponents;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

}
