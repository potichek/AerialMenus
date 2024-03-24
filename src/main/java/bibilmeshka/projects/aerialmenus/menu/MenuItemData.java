package bibilmeshka.projects.aerialmenus.menu;

import bibilmeshka.projects.aerialmenus.menu.items.MenuItemBannerMeta;
import bibilmeshka.projects.aerialmenus.menu.items.MenuItemColor;
import bibilmeshka.projects.aerialmenus.menu.items.MenuItemEnchantment;
import bibilmeshka.projects.aerialmenus.menu.items.MenuItemPotionEffect;
import bibilmeshka.projects.aerialmenus.requirements.Requirement;
import bibilmeshka.projects.aerialmenus.requirements.RequirementsData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Setter
@Getter
public class MenuItemData {

    private String material;
    private int data;
    private int amount;
    private String dynamicAmount;
    private int modelData;
    private List<MenuItemBannerMeta> bannerMeta = new ArrayList<>();
    private DyeColor baseColor;
    private List<ItemFlag> itemFlags = new ArrayList<>();
    private List<MenuItemPotionEffect> potionEffects = new ArrayList<>();
    private EntityType entityType;
    private MenuItemColor color;
    private String displayName;
    private String nbtStringKey = "";
    private String nbtStringValue;
    private String nbtIntKey = "";
    private Integer nbtIntValue;
    private List<String> lore = new ArrayList<>();
    private int slot;
    private List<Integer> slots = new ArrayList<>();
    private int priority;
    private RequirementsData viewRequirement;
    private List<MenuItemEnchantment> enchantments = new ArrayList<>();
    private boolean isAir;
    private boolean update;
    private boolean isHideEnchantments;
    private boolean isHideAttributes;
    private boolean isHideEffects;
    private boolean isHideUnbreakable;
    private boolean isUnbreakable;
    private boolean isPickable;
    private boolean isPutable;
    private HashMap<String, String> nbtStrings = new HashMap<>();
    private HashMap<String, Integer> nbtInts = new HashMap<>();
    private List<String> clickCommands = new ArrayList<>();
    private List<String> leftClickCommands = new ArrayList<>();
    private List<String> rightClickCommands = new ArrayList<>();
    private List<String> middleClickCommands = new ArrayList<>();
    private List<String> shiftLeftClickCommands = new ArrayList<>();
    private List<String> shiftRightClickCommands = new ArrayList<>();
    private RequirementsData clickCommandsRequirement;
    private RequirementsData leftClickCommandsRequirement;
    private RequirementsData rightClickCommandsRequirement;
    private RequirementsData middleClickCommandsRequirement;
    private RequirementsData shiftLeftClickCommandsRequirement;
    private RequirementsData shiftRightClickCommandsRequirement;
    private RequirementsData pickRequirement;
    private RequirementsData putRequirement;

}