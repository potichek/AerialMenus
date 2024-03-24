package bibilmeshka.projects.aerialmenus.services.item;

import bibilmeshka.projects.aerialmenus.menu.MenuItem;

import java.util.List;

public interface ItemConvertService<T> {

    List<MenuItem> getItems(T resource);

}
