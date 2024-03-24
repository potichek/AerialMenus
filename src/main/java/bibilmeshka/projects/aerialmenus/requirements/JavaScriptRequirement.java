package bibilmeshka.projects.aerialmenus.requirements;

import bibilmeshka.projects.aerialmenus.services.debug.DebugLevel;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.ScriptEngine;
import java.util.List;

public class JavaScriptRequirement extends Requirement {

    private NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
    private ScriptEngine engine = factory.getScriptEngine();
    @Setter
    private String expression;

    public JavaScriptRequirement(RequirementType type, String requirementName, List<String> denyCommands, List<String> successCommands, boolean optional, DebugService debugService) {
        super(type, requirementName, denyCommands, successCommands, optional, debugService);
    }

    @Override
    public boolean check(Player player) {
        super.debugService.debug("&eИгрок " + player.getName() + " Проверяется на требования " + this.requirementName, DebugLevel.HIGHEST);

        final var expression = PlaceholderAPI.setPlaceholders(player, this.expression);
        try {
            return (Boolean) engine.eval(expression);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}