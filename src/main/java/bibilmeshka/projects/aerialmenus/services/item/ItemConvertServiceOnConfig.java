package bibilmeshka.projects.aerialmenus.services.item;

import bibilmeshka.projects.aerialmenus.menu.MenuItem;
import bibilmeshka.projects.aerialmenus.menu.MenuItemData;
import bibilmeshka.projects.aerialmenus.menu.items.MenuItemBannerMeta;
import bibilmeshka.projects.aerialmenus.menu.items.MenuItemClickType;
import bibilmeshka.projects.aerialmenus.menu.items.MenuItemEnchantment;
import bibilmeshka.projects.aerialmenus.menu.items.MenuItemPotionEffect;
import bibilmeshka.projects.aerialmenus.nms.NMSConsumer;
import bibilmeshka.projects.aerialmenus.services.cutter.StringCutterService;
import bibilmeshka.projects.aerialmenus.services.debug.DebugLevel;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import bibilmeshka.projects.aerialmenus.services.item.convert.ItemMetaConvertConsumer;
import bibilmeshka.projects.aerialmenus.services.menu.command.processing.CommandProcessingConsumer;
import bibilmeshka.projects.aerialmenus.services.requirement.check.RequirementCheckService;
import bibilmeshka.projects.aerialmenus.services.requirement.convert.RequirementConvertConsumer;
import bibilmeshka.projects.aerialmenus.services.skull.MaterialCheckConsumer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;

public class ItemConvertServiceOnConfig implements ItemConvertService<YamlConfiguration> {

    private final RequirementConvertConsumer requirementConsumer;
    private final ItemMetaConvertConsumer itemMetaConvertConsumer;
    private final CommandProcessingConsumer commandProcessingConsumer;
    private final StringCutterService stringCutterService;
    private final RequirementCheckService requirementCheckService;
    private final MaterialCheckConsumer materialCheckConsumer;
    private final NMSConsumer nmsConsumer;
    private final DebugService debugService;

    public ItemConvertServiceOnConfig(
            RequirementConvertConsumer requirementConsumer,
            ItemMetaConvertConsumer itemMetaConvertConsumer,
            CommandProcessingConsumer commandProcessingConsumer,
            StringCutterService stringCutterService,
            RequirementCheckService requirementCheckService,
            MaterialCheckConsumer materialCheckConsumer,
            NMSConsumer nmsConsumer,
            DebugService debugService) {

        this.stringCutterService = stringCutterService;
        this.requirementConsumer = requirementConsumer;
        this.itemMetaConvertConsumer = itemMetaConvertConsumer;
        this.commandProcessingConsumer = commandProcessingConsumer;
        this.requirementCheckService = requirementCheckService;
        this.materialCheckConsumer = materialCheckConsumer;
        this.nmsConsumer = nmsConsumer;
        this.debugService = debugService;
    }

    private MenuItem getItem(final YamlConfiguration config, final String itemName) {
        try {
            final var menuItemData = new MenuItemData();

            menuItemData.setAir(config.getString("items" + "." + itemName + "." + "material").equalsIgnoreCase("air"));
            menuItemData.setMaterial(config.getString("items" + "." + itemName + "." + "material"));
            menuItemData.setData(config.getInt("items" + "." + itemName + "." +  "data", 0));
            menuItemData.setAmount(config.getInt("items" + "." + itemName + "." +  "amount", 1));
            menuItemData.setDynamicAmount(config.getString("items" + "." + itemName + "." +  "dynamic_amount", "-1"));
            menuItemData.setModelData(config.getInt("items" + "." + itemName + "." +  "model_data", 0));

            final var nbtStringMeta = this.stringCutterService
                    .cutArgumentsFromString(config.getString("items" + "." + itemName + "." +  "nbt_string", ""), ':');
            if (!(nbtStringMeta.isEmpty())) {
                menuItemData.setNbtStringKey(nbtStringMeta.get(0));
                menuItemData.setNbtStringValue(nbtStringMeta.get(1));
            }

            final var nbtIntMeta = this.stringCutterService
                    .cutArgumentsFromString(config.getString("items" + "." + itemName + "." +  "nbt_int", ""), ':', Integer.class);
            if (!(nbtIntMeta.isEmpty())) {
                menuItemData.setNbtIntKey((String) nbtIntMeta.get(0));
                menuItemData.setNbtIntValue((Integer) nbtIntMeta.get(1));
            }

            for (final var nbtString : config.getStringList("items" + "." + itemName + "." +  "nbt_strings")) {
                final var nbtStringsMeta = this.stringCutterService.cutArgumentsFromString(nbtString, ':');
                menuItemData.getNbtStrings().put(nbtStringsMeta.get(0), nbtStringsMeta.get(1));
            }

            for (final var nbtInts : config.getStringList("items" + "." + itemName + "." +  "nbt_ints")) {
                final var nbtStringsMeta = this.stringCutterService.cutArgumentsFromString(nbtInts, ':', Integer.class);
                menuItemData.getNbtInts().put((String) nbtStringsMeta.get(0), (Integer) nbtStringsMeta.get(1));
            }

            final var bannerMetaList = new ArrayList<MenuItemBannerMeta>();
            for (final var str : config.getStringList("items" + "." + itemName + "." +  "banner_meta")) {
                bannerMetaList.add(this.itemMetaConvertConsumer.createBannerMeta(config, itemName));
            }
            menuItemData.setBannerMeta(bannerMetaList);

            menuItemData.setBaseColor(DyeColor.valueOf(config.getString("items" + "." + itemName + "." +  "base_color", "WHITE")));

            final var flags = new ArrayList<ItemFlag>();
            for (final var flag : config.getStringList("items" + "." + itemName + "." +  "item_flags")) {
                flags.add(ItemFlag.valueOf(flag));
            }
            menuItemData.setItemFlags(flags);

            final var potionEffects = new ArrayList<MenuItemPotionEffect>();
            for (final var potion : config.getStringList("items" + "." + itemName + "." +  "potion_effects")) {
                potionEffects.add(this.itemMetaConvertConsumer.createItemPotionEffect(config, itemName));
            }
            menuItemData.setPotionEffects(potionEffects);

            menuItemData.setEntityType(EntityType.valueOf(config.getString("items" + "." + itemName + "." + "entity_type", "ZOMBIE")));
            menuItemData.setColor(this.itemMetaConvertConsumer.createItemColor(config, itemName));
            menuItemData.setDisplayName(config.getString("items" + "." + itemName + "." +  "display_name", "Error"));

            menuItemData.setLore(config.getStringList("items" + "." + itemName + "." +  "lore"));

            menuItemData.setSlot(config.getInt("items" + "." + itemName + "." +  "slot", 0));
            menuItemData.setSlots(config.getIntegerList("items" + "." + itemName + "." +  "slots"));
            menuItemData.setPriority(config.getInt("items" + "." + itemName + "." + "priority", 1));
            menuItemData.setViewRequirement(requirementConsumer.getItemViewRequirements(config, itemName));
            menuItemData.setUpdate(config.getBoolean("items" + "." + itemName + "." + "update", true));

            final var enchantments = new ArrayList<MenuItemEnchantment>();
            for (final var enchantment : config.getStringList("items" + "." + itemName + "." + "enchantments")) {
                enchantments.add(this.itemMetaConvertConsumer.createItemEnchantment(config, itemName));
            }
            menuItemData.setEnchantments(enchantments);

            menuItemData.setHideEnchantments(config.getBoolean("items" + "." + itemName + "." + "hide_enchantments", false));
            menuItemData.setHideAttributes(config.getBoolean("items" + "." + itemName + "." + "hide_attributes", false));
            menuItemData.setHideEffects(config.getBoolean("items" + "." + itemName + "." + "hide_effects", false));
            menuItemData.setHideUnbreakable(config.getBoolean("items" + "." + itemName + "." + "hide_unbreakable", false));

            menuItemData.setClickCommands(config.getStringList("items" + "." + itemName + "." + "click_commands"));
            menuItemData.setLeftClickCommands(config.getStringList("items" + "." + itemName + "." + "left_click_commands"));
            menuItemData.setRightClickCommands(config.getStringList("items" + "." + itemName + "." + "right_click_commands"));
            menuItemData.setMiddleClickCommands(config.getStringList("items" + "." + itemName + "." + "middle_click_commands"));
            menuItemData.setShiftLeftClickCommands(config.getStringList("items" + "." + itemName + "." + "shift_left_click_commands"));
            menuItemData.setShiftRightClickCommands(config.getStringList("items" + "." + itemName + "." + "shift_right_click_commands"));

            menuItemData.setPickable(config.getBoolean("items" + "." + itemName + "." + "pickable"));
            menuItemData.setPutable(config.getBoolean("items" + "." + itemName + "." + "putable"));

            menuItemData.setClickCommandsRequirement(this.requirementConsumer.getItemClickRequirements(config, itemName, MenuItemClickType.CLICK_REQUIREMENT));
            menuItemData.setLeftClickCommandsRequirement(this.requirementConsumer.getItemClickRequirements(config, itemName, MenuItemClickType.LEFT_CLICK_REQUIREMENT));
            menuItemData.setRightClickCommandsRequirement(this.requirementConsumer.getItemClickRequirements(config, itemName, MenuItemClickType.RIGHT_CLICK_REQUIREMENT));
            menuItemData.setMiddleClickCommandsRequirement(this.requirementConsumer.getItemClickRequirements(config, itemName, MenuItemClickType.MIDDLE_CLICK_REQUIREMENT));
            menuItemData.setShiftLeftClickCommandsRequirement(this.requirementConsumer.getItemClickRequirements(config, itemName, MenuItemClickType.SHIFT_LEFT_CLICK_REQUIREMENT));
            menuItemData.setShiftRightClickCommandsRequirement(this.requirementConsumer.getItemClickRequirements(config, itemName, MenuItemClickType.SHIFT_RIGHT_CLICK_REQUIREMENT));

            menuItemData.setPutRequirement(this.requirementConsumer.getItemPutRequirements(config, itemName));
            menuItemData.setPickRequirement(this.requirementConsumer.getItemPickRequirements(config, itemName));

            return new MenuItem(menuItemData,
                    this.commandProcessingConsumer,
                    this.stringCutterService, this.requirementCheckService,
                    this.materialCheckConsumer,
                    this.nmsConsumer,
                    this.debugService,
                    itemName);
        } catch (Exception e) {
            this.debugService.debug("&cНе получилось создать предмет " + itemName + " для меню", DebugLevel.LOW);
        }
        return null;
    }

    @Override
    public List<MenuItem> getItems(final YamlConfiguration resource) {
        final var menuItems = new ArrayList<MenuItem>();
        try {
            if (resource.getConfigurationSection("items") == null) return menuItems;
            for (final var itemName : resource.getConfigurationSection("items").getKeys(false)) {
                menuItems.add(getItem(resource, itemName));
            }
        } catch (Exception e) {
            this.debugService.debug("&cНе получилось получить предметы для меню", DebugLevel.LOW);
        }
        return menuItems;
    }
}