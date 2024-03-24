package bibilmeshka.projects.aerialmenus.services.requirement.convert;

import bibilmeshka.projects.aerialmenus.menu.items.MenuItemClickType;
import bibilmeshka.projects.aerialmenus.requirements.RequirementsData;
import org.bukkit.configuration.file.YamlConfiguration;

public class RequirementConvertConsumer {

    private final RequirementConvertService service;

    public RequirementConvertConsumer(final RequirementConvertService service) {
        this.service = service;
    }

    public RequirementsData getOpenRequirements(Object resource) {
        return this.service.getOpenRequirements(resource);
    }

    public RequirementsData getItemViewRequirements(Object resource, String itemName) {
        return this.service.getItemViewRequirements(resource, itemName);
    }

    public RequirementsData getItemClickRequirements(Object resource, String itemName, MenuItemClickType menuItemClickType) {
        return this.service.getItemClickRequirements(resource, itemName, menuItemClickType);
    }

    public RequirementsData getItemPutRequirements(Object resource, String itemName) {
        return this.service.getItemPutRequirements(resource, itemName);
    }

    public RequirementsData getItemPickRequirements(Object resource, String itemName) {
        return this.service.getItemPickRequirements(resource, itemName);
    }

}
