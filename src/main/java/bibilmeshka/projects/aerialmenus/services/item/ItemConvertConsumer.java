package bibilmeshka.projects.aerialmenus.services.item;

import bibilmeshka.projects.aerialmenus.menu.MenuItem;

import java.util.List;

public class ItemConvertConsumer {

    private final ItemConvertService service;

    public ItemConvertConsumer(ItemConvertService service) {
        this.service = service;
    }

    public List<MenuItem> getItems(Object resource) {
        return this.service.getItems(resource);
    }

}
