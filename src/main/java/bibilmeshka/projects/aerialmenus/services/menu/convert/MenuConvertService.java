package bibilmeshka.projects.aerialmenus.services.menu.convert;

import bibilmeshka.projects.aerialmenus.menu.Menu;
import bibilmeshka.projects.aerialmenus.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public abstract class MenuConvertService<T> {

    public abstract Menu convert(final T resource);
    public abstract List<Menu> convertList(final List<T> resources);

    private List<MenuItem> sortItemsByPrioriry(List<MenuItem> items) {
        if (items.size() < 2) {
            return items;
        }

        final var middle = items.size() / 2;
        final var pivot = items.get(middle).getData().getPriority();
        final var less = new ArrayList<MenuItem>();
        final var more = new ArrayList<MenuItem>();

        for (int i = 0; i <= (items.size() - 1); i++) {
            if (i != middle) {
                if (items.get(i).getData().getPriority() <= pivot) {
                    less.add(items.get(i));
                }
            }
        }

        for (int i = 0; i <= (items.size() - 1); i++) {
            if (i != middle) {
                if (items.get(i).getData().getPriority() > pivot) {
                    more.add(items.get(i));
                }
            }
        }

        final var list = new ArrayList<MenuItem>();
        list.addAll(sortItemsByPrioriry(less));
        list.add(items.get(middle));
        list.addAll(sortItemsByPrioriry(more));
        return list;
    }

    protected MenuItem[] placeInSlots(List<MenuItem> items, int menuSize) {
        final var itemsInSlots = new MenuItem[menuSize];
        final var occupiedSlots = new ArrayList<Integer>();

        for (final var item : items) {
            if (!item.getData().getSlots().isEmpty()) {
                for (final var slot : item.getData().getSlots()) {
                    if (!occupiedSlots.contains(slot)) {
                        itemsInSlots[slot] = item;
                        occupiedSlots.add(slot);
                    }
                }
            } else {
                final var slot = item.getData().getSlot();
                if (!occupiedSlots.contains(slot)) {
                    itemsInSlots[slot] = item;
                    occupiedSlots.add(slot);
                }
            }
        }
        return itemsInSlots;
    }
}