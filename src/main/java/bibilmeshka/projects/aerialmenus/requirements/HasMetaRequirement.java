package bibilmeshka.projects.aerialmenus.requirements;

import bibilmeshka.projects.aerialmenus.services.debug.DebugLevel;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

@Setter
public class HasMetaRequirement extends Requirement {

    private String key;
    private String metaType;
    private Object value;

    public HasMetaRequirement(RequirementType type, String requirementName, List<String> denyCommands, List<String> successCommands, boolean optional, DebugService debugService) {
        super(type, requirementName, denyCommands, successCommands, optional, debugService);
    }

    private void parseValue(Player player) {
        if (value instanceof String) {
            value = PlaceholderAPI.setPlaceholders(player, (String) value);
        }
    }

    @Override
    public boolean check(Player player) {
        super.debugService.debug("&eИгрок " + player.getName() + " Проверяется на требования " + this.requirementName, DebugLevel.HIGHEST);
        parseValue(player);
        if (player.hasMetadata(key)) {

            if (metaType.equalsIgnoreCase("STRING")) {

                for (var metaValue : player.getMetadata(key)) {
                    if (metaValue.asString().equals(value)) return getWithPropetryNegative(true);
                }

            } else if (metaType.equalsIgnoreCase("BOOLEAN")) {

                for (var metaValue : player.getMetadata(key)) {
                    return metaValue.asBoolean();
                }
                
            } else if (metaType.equalsIgnoreCase("DOUBLE")) {

                for (var metaValue : player.getMetadata(key)) {
                    if (metaValue.asDouble() >= (double) value) return getWithPropetryNegative(true);
                }
                
            } else if (metaType.equalsIgnoreCase("LONG")) {

                for (var metaValue : player.getMetadata(key)) {
                    if (metaValue.asLong() >= (long) value) return getWithPropetryNegative(true);
                }
                
            } else if (metaType.equalsIgnoreCase("INTEGER")) {

                for (var metaValue : player.getMetadata(key)) {
                    if (metaValue.asInt() >= (int) value) return getWithPropetryNegative(true);
                }
                
            }
        }
        return getWithPropetryNegative(false);
    }

}