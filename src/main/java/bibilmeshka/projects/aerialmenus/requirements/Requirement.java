package bibilmeshka.projects.aerialmenus.requirements;

import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Requirement {

    @Getter
    private RequirementType type;
    @Getter
    protected String requirementName;
    @Getter
    private List<String> denyCommands;
    @Getter
    private List<String> successCommands;
    private boolean optional;
    protected final DebugService debugService;

    public Requirement(RequirementType type, String requirementName, List<String> denyCommands, List<String> successCommands, boolean optional, DebugService debugService) {
        this.type = type;
        this.requirementName = requirementName;
        this.denyCommands = denyCommands;
        this.successCommands = successCommands;
        this.optional = optional;
        this.debugService = debugService;
    }

    protected boolean getWithPropetryNegative(boolean result) {
        if (type.isNegative()) return !result;
        return result;
    }

    public boolean getOptional() {
        return this.optional;
    }

    public abstract boolean check(Player player);

}
