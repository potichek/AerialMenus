package bibilmeshka.projects.aerialmenus.services.menu.convert;

import bibilmeshka.projects.aerialmenus.menu.Menu;

import java.util.List;

public class MenuConvertServiceConsumer {

    private final MenuConvertService service;

    public MenuConvertServiceConsumer(final MenuConvertService service) {
        this.service = service;
    }

    public Menu convert(final Object converted) {
        return this.service.convert(converted);
    }

    public List<Menu> convertList(final List<?> configFiles) {
        return this.service.convertList(configFiles);
    }

}
