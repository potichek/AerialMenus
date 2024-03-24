package bibilmeshka.projects.aerialmenus.requirements;

import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Setter
public class ItemRequirement {

    private Material material;

    private int data;
    private int amount = 1;
    private String name;
    private List<String> lore = new ArrayList<>();
    private boolean nameContains = false;
    private boolean nameIgnoreCase = false;
    private boolean loreContains = false;
    private boolean loreIgnoreCase = false;
    private boolean strict = false;
    private boolean armor = false;
    private boolean offhand = false;

    public ItemRequirement(final Material material, final String name) {
        this.material = material;
        this.name = ChatColor.translateAlternateColorCodes('&', name);
    }

    public void setLore(final List<String> list) {
        this.lore = list;
        for (int i = 0; (list.size() - 1) >= i; i++) {
            lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));
        }
    }

    private boolean equalsLore(final List<String> list, final boolean ignoreCase) {

        if (lore == null || list == null) return false;

        if (lore.size() != list.size()) return false;

        if (loreIgnoreCase) {
            for (int i = 0; (lore.size() - 1) >= i; i++) {

                var strOfLore = lore.get(i).strip().replace(ChatColor.COLOR_CHAR, '&');
                var strOfList = list.get(i).strip().replace(ChatColor.COLOR_CHAR, '&');

                if (strOfList.endsWith("&5")) {
                    strOfList = strOfList.substring(0, strOfList.lastIndexOf("&5"));
                }

                if (!(strOfLore.strip().equalsIgnoreCase(strOfList))) {
                    return false;
                }

            }
        } else {
            for (int i = 0; (lore.size() - 1) >= i; i++) {

                var strOfLore = lore.get(i).strip().replace(ChatColor.COLOR_CHAR, '&');
                var strOfList = list.get(i).strip().replace(ChatColor.COLOR_CHAR, '&');

                if (strOfList.endsWith("&5")) {
                    strOfList = strOfList.substring(0, strOfList.lastIndexOf("&5"));
                }

                if (!(strOfLore.strip().equals(strOfList.strip()))) return false;
            }
        }

        return true;
    }

    private boolean equalsItem(ItemStack item) {

        if (item == null) return false;

        if (strict) {

            if (item.getItemMeta() != null) {

                if (!(item.getItemMeta().getDisplayName().strip().isEmpty())) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Name is NOT empty");
                    return false;
                }

                if (item.getItemMeta().hasCustomModelData()) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Has CMD");
                    return false;
                }

                if (item.getItemMeta().getLore() != null) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Lore is NOT null");
                    return false;
                }
            } else return false;
        }

        if (material != item.getType()) return false;

        if (nameContains) {
            if (nameIgnoreCase) {
                if (!(name.equalsIgnoreCase(item.getItemMeta().getDisplayName()))) return false;
            } else {
                if (!(name.equals(item.getItemMeta().getDisplayName()))) return false;
            }
        }

        if (loreContains) {
            if (loreIgnoreCase) {
                if (!(equalsLore(item.getItemMeta().getLore(), true))) return false;
            } else {
                if (!(equalsLore(item.getItemMeta().getLore(), false))) return false;
            }
        }
        return true;
    }

    public boolean equalsItemWithArmorAndOffhand(Player player) {

        if (armor) {
            if (!(checkArmorSlots(player))) {
                return false;
            } else return true;
        }

        if (offhand) {
            if (!(equalsItem(player.getEquipment().getItemInOffHand()))) return false;
        } else {
            if (!(equalsItem(player.getEquipment().getItemInMainHand()))) return false;
        }

        return true;
    }

    private boolean checkArmorSlots(Player player) {
        for (var armor : player.getEquipment().getArmorContents()) {
            if (equalsItem(armor)) return true;
        }
        return false;
    }

}
