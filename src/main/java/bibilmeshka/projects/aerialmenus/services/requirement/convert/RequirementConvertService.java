package bibilmeshka.projects.aerialmenus.services.requirement.convert;

import bibilmeshka.projects.aerialmenus.menu.items.MenuItemClickType;
import bibilmeshka.projects.aerialmenus.requirements.Requirement;
import bibilmeshka.projects.aerialmenus.requirements.RequirementType;
import bibilmeshka.projects.aerialmenus.requirements.RequirementsData;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;

import java.util.List;


public abstract class RequirementConvertService<T> {

    protected final DebugService debugService;

    public RequirementConvertService(final DebugService debugService) {
        this.debugService = debugService;
    }

    public abstract RequirementsData getOpenRequirements(T resource);
    public abstract RequirementsData getItemViewRequirements(T resource, String itemName);
    public abstract RequirementsData getItemClickRequirements(T resource, String itemName, MenuItemClickType menuItemClickType);
    public abstract RequirementsData getItemPutRequirements(T resource, String itemName);
    public abstract RequirementsData getItemPickRequirements(T resource, String itemName);

    protected Requirement createSimpleRequirement(RequirementType type,
                                                  String name,
                                                  Class<? extends Requirement> clazz,
                                                  boolean optional,
                                                  List<String> denyCommands,
                                                  List<String> successCommands
    ) {
        try {
            return clazz.getConstructor(RequirementType.class, String.class, List.class, List.class, boolean.class, DebugService.class)
                    .newInstance(type, name, denyCommands, successCommands, optional, this.debugService);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
