package bibilmeshka.projects.aerialmenus.services.requirement.convert;

import bibilmeshka.projects.aerialmenus.menu.items.MenuItemClickType;
import bibilmeshka.projects.aerialmenus.requirements.*;
import bibilmeshka.projects.aerialmenus.services.cutter.StringCutterService;
import bibilmeshka.projects.aerialmenus.services.debug.DebugLevel;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import bibilmeshka.projects.aerialmenus.services.requirement.dependency.RequirementDependencySetService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RequirementConvertServiceOnConfig extends RequirementConvertService<YamlConfiguration> {

    private final StringCutterService stringCutterService;
    private final RequirementDependencySetService dependencySetService;

    public RequirementConvertServiceOnConfig(final StringCutterService stringCutterService, final RequirementDependencySetService dependencySetService, final DebugService debugService) {
        super(debugService);
        this.stringCutterService = stringCutterService;
        this.dependencySetService = dependencySetService;
    }

    @Override
    public RequirementsData getOpenRequirements(YamlConfiguration resource) {
        return createRequirementsData(resource, "open_requirement");
    }

    @Override
    public RequirementsData getItemViewRequirements(YamlConfiguration resource, String itemName) {
        return createRequirementsData(resource, "items" + "." + itemName + "." + "view_requirement");
    }

    @Override
    public RequirementsData getItemClickRequirements(YamlConfiguration resource, String itemName, MenuItemClickType menuItemClickType) {
        return createRequirementsData(resource, "items" + "." + itemName + "." + menuItemClickType.getStringType());
    }

    @Override
    public RequirementsData getItemPutRequirements(YamlConfiguration resource, String itemName) {
        return createRequirementsData(resource, "items" + "." + itemName + "." + "putable_requirement");
    }

    @Override
    public RequirementsData getItemPickRequirements(YamlConfiguration resource, String itemName) {
        return createRequirementsData(resource, "items" + "." + itemName + "." + "pickable_requirement");
    }

    @NotNull
    private List<Requirement> createRequirements(YamlConfiguration config, String path) {
        final var requirements = new ArrayList<Requirement>();
        if (config.getConfigurationSection(path + "." + "requirements") == null ) return new ArrayList<>();

        for (final var requirement : config.getConfigurationSection(path + "." + "requirements").getKeys(false)) {
            final var type = RequirementType.valueOf(config
                    .getString(path + "." + "requirements" + "." + requirement + "." + "type")
                    .replace(" ", "_")
                    .replace("!", "NOT_")
                    .toUpperCase()
                    .strip());

            requirements.add(createRequirement(config, path, requirement, type));
        }
        return requirements;
    }

    private Requirement createRequirement(YamlConfiguration config, String path, String requirement, RequirementType type) {

        try {
            final var optional = config.getBoolean(path + "." + "requirements" + "." + requirement + "." + "optional");
            final var denyCommands = config.getStringList(path + "." + "requirements" + "." + requirement + "." + "deny_commands");
            final var successCommands = config.getStringList(path + "." + "requirements" + "." + requirement + "." + "success_commands");

            if (type == RequirementType.HAS_PERMISSION || type == RequirementType.NOT_HAS_PERMISSION) {

                final var permRequirement = (HasPermissionRequirement) super.createSimpleRequirement(type, requirement, HasPermissionRequirement.class, optional, denyCommands, successCommands);
                final var permission = config.getString(path + "." + "requirements" + "." + requirement + "." + "permission");
                permRequirement.setPermission(permission);
                return permRequirement;

            } else if (type == RequirementType.HAS_MONEY || type == RequirementType.NOT_HAS_MONEY) {

                final var moneyRequirement = (HasMoneyRequirement) super.createSimpleRequirement(type, requirement, HasMoneyRequirement.class, optional, denyCommands, successCommands);
                final var amount = config.get(path + "." + "requirements" + "." + requirement + "." + "amount", 0);
                moneyRequirement.setAmount(amount);
                this.dependencySetService.setInMoneyRequirement(moneyRequirement);
                return moneyRequirement;

            } else if (type == RequirementType.HAS_ITEM || type == RequirementType.NOT_HAS_ITEM) {

                final var hasItemRequirement = (HasItemRequirement) super.createSimpleRequirement(type, requirement, HasItemRequirement.class, optional, denyCommands, successCommands);

                final var material = Material.getMaterial(config.getString(path + "." + "requirements" + "." + requirement + "." + "material", null).toUpperCase());
                final var name = ChatColor.translateAlternateColorCodes('&',config.getString(path + "." + "requirements" + "." + requirement + "." + "name", null));

                ItemRequirement item = new ItemRequirement(material, name);

                final var data = config.getInt(path + "." + "requirements" + "." + requirement + "." + "data", -1);
                final var itemAmount = config.getInt(path + "." + "requirements" + "." + requirement + "." + "amount", 1);
                final var lore = config.getStringList(path + "." + "requirements" + "." + requirement + "." + "lore");
                final var nameContains = config.getBoolean(path + "." + "requirements" + "." + requirement + "." + "name_contains");
                final var nameIgnoreCase = config.getBoolean(path + "." + "requirements" + "." + requirement + "." + "name_ignorecase");
                final var loreContains = config.getBoolean(path + "." + "requirements" + "." + requirement + "." + "lore_contains");
                final var loreIgnoreCase = config.getBoolean(path + "." + "requirements" + "." + requirement + "." + "lore_ignorecase");
                final var strict = config.getBoolean(path + "." + "requirements" + "." + requirement + "." + "strict");
                final var armor = config.getBoolean(path + "." + "requirements" + "." + requirement + "." + "armor");
                final var offhand = config.getBoolean(path + "." + "requirements" + "." + requirement + "." + "offhand");

                item.setData(data);
                item.setAmount(itemAmount);
                item.setLore(lore);
                item.setNameContains(nameContains);
                item.setNameIgnoreCase(nameIgnoreCase);
                item.setLoreContains(loreContains);
                item.setLoreIgnoreCase(loreIgnoreCase);
                item.setStrict(strict);
                item.setArmor(armor);
                item.setOffhand(offhand);

                hasItemRequirement.setItem(item);

                return hasItemRequirement;

            } else if (type == RequirementType.HAS_META || type == RequirementType.NOT_HAS_META) {

                final var hasMetaRequirement = (HasMetaRequirement) super.createSimpleRequirement(type, requirement, HasMetaRequirement.class, optional, denyCommands, successCommands);

                final var key = config.getString(path + "." + "requirements" + "." + requirement + "." + "key", null);
                final var metaType = config.getString(path + "." + "requirements" + "." + requirement + "." + "meta_type", null);
                final var value = config.get(path + "." + "requirements" + "." + requirement + "." + "value", null);

                hasMetaRequirement.setKey(key);
                hasMetaRequirement.setMetaType(metaType);
                hasMetaRequirement.setValue(value);

                return hasMetaRequirement;

            } else if (type == RequirementType.HAS_EXP || type == RequirementType.NOT_HAS_EXP) {

                final var hasExpRequirement = (HasExpRequirement) super.createSimpleRequirement(type, requirement, HasExpRequirement.class, optional, denyCommands, successCommands);

                final var expAmount = config.get(path + "." + "requirements" + "." + requirement + "." + "amount", 0);
                final var level = config.getBoolean(path + "." + "requirements" + "." + requirement + "." + "level", false);

                hasExpRequirement.setAmount(expAmount);
                hasExpRequirement.setLevel(level);

                return hasExpRequirement;

            } else if (type == RequirementType.IS_NEAR || type == RequirementType.NOT_IS_NEAR) {

                final var isNearRequirement = (IsNearRequirement) super.createSimpleRequirement(type, requirement, IsNearRequirement.class, optional, denyCommands, successCommands);

                final var locationMeta = this.stringCutterService.cutArgumentsFromString(
                        config.getString(path + "." + "requirements" + "." + requirement + "." + "location"),
                        ',',
                        Double.class);
                final var distantion = config.getDouble(path + "." + "requirements" + "." + requirement + "." + "distance", 0);

                isNearRequirement.setLocation(
                        new Location(
                                Bukkit.getWorld((String) locationMeta.get(0)),
                                (Double) locationMeta.get(1),
                                (Double) locationMeta.get(2),
                                (Double) locationMeta.get(3)));
                isNearRequirement.setDistance(distantion);

                return isNearRequirement;

            } else if (type == RequirementType.JAVASCRIPT) {

                final var jsRequirement = (JavaScriptRequirement) super.createSimpleRequirement(type, requirement, JavaScriptRequirement.class, optional, denyCommands, successCommands);
                final var expression = config.getString(path + "." + "requirements" + "." + requirement + "." + "expression");
                jsRequirement.setExpression(expression);
                return jsRequirement;

            } else if (type == RequirementType.STRING_EQUALS || type == RequirementType.NOT_STRING_EQUALS) {

                final var stringEqualsRequirement = (StringEqualsRequirement) super.createSimpleRequirement(type, requirement, StringEqualsRequirement.class, optional, denyCommands, successCommands);

                final var input = config.getString(path + "." + "requirements" + "." + requirement + "." + "input");
                final var output = config.getString(path + "." + "requirements" + "." + requirement + "." + "output");

                stringEqualsRequirement.setInput(input);
                stringEqualsRequirement.setOutput(output);

                return stringEqualsRequirement;

            } else if (type == RequirementType.STRING_EQUALS_IGNORE_CASE || type == RequirementType.NOT_STRING_EQUALS_IGNORE_CASE) {

                final var stringEqualsIgnoreCaseRequirement = (StringEqualsIgnoreCaseRequirement) super.createSimpleRequirement(type, requirement, StringEqualsIgnoreCaseRequirement.class, optional, denyCommands, successCommands);

                final var inputIgnoreCase = config.getString(path + "." + "requirements" + "." + requirement + "." + "input");
                final var outputIgnoreCase = config.getString(path + "." + "requirements" + "." + requirement + "." + "output");

                stringEqualsIgnoreCaseRequirement.setInput(inputIgnoreCase);
                stringEqualsIgnoreCaseRequirement.setOutput(outputIgnoreCase);

                return stringEqualsIgnoreCaseRequirement;

            } else if (type == RequirementType.REGEX_MATCHES || type == RequirementType.NOT_REGEX_MATCHES) {

                final var regexRequirement = (RegexRequirement) super.createSimpleRequirement(type, requirement, RegexRequirement.class, optional, denyCommands, successCommands);

                final var inputRegex = config.getString(path + "." + "requirements" + "." + requirement + "." + "input");
                final var regex = config.getString(path + "." + "requirements" + "." + requirement + "." + "regex");

                regexRequirement.setInput(inputRegex);
                regexRequirement.setRegex(regex);

                return regexRequirement;

            } else if (type == RequirementType.STRING_CONTAINS || type == RequirementType.NOT_STRING_CONTAINS) {

                final var stringContainsRequirement = (StringContainsRequirement) super.createSimpleRequirement(type, requirement, StringContainsRequirement.class, optional, denyCommands, successCommands);

                final var input = config.getString(path + "." + "requirements" + "." + requirement + "." + "input");
                final var output = config.getString(path + "." + "requirements" + "." + requirement + "." + "output");

                stringContainsRequirement.setInput(input);
                stringContainsRequirement.setOutput(output);

                return stringContainsRequirement;

            } else if (type == RequirementType.COMPARATORS) {

                final var comparatorsRequirement = (ComparatorsRequirement) super.createSimpleRequirement(type, requirement, ComparatorsRequirement.class, optional, denyCommands, successCommands);

                final var inputComparators = config.get(path + "." + "requirements" + "." + requirement + "." + "input");
                final var outputComparators  = config.get(path + "." + "requirements" + "." + requirement + "." + "output");
                final var operator  = config.getString(path + "." + "requirements" + "." + requirement + "." + "operator");

                comparatorsRequirement.setInput(inputComparators);
                comparatorsRequirement.setOutput(outputComparators);
                comparatorsRequirement.setOperator(operator);

                return comparatorsRequirement;

            }
        } catch (Exception e) {
            super.debugService.debug("&cНе получилось создать требование " + requirement, DebugLevel.LOW);
        }

        return null;
    }

    private RequirementsData createRequirementsData(YamlConfiguration config, String path) {
        final var minimumRequirements = config.getInt(path + "." + "minimum_requirements", 0);
        final var stopAtSuccess = config.getBoolean(path + "." + "stop_at_success", false);
        final var requirements = createRequirements(config, path);
        return new RequirementsData(minimumRequirements, stopAtSuccess, requirements);
    }

}
