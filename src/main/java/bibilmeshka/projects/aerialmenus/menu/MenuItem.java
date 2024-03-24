package bibilmeshka.projects.aerialmenus.menu;

import bibilmeshka.projects.aerialmenus.nms.NMSConsumer;
import bibilmeshka.projects.aerialmenus.services.cutter.StringCutterService;
import bibilmeshka.projects.aerialmenus.services.debug.DebugLevel;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import bibilmeshka.projects.aerialmenus.services.menu.command.processing.CommandProcessingConsumer;
import bibilmeshka.projects.aerialmenus.services.requirement.check.RequirementCheckService;
import bibilmeshka.projects.aerialmenus.services.skull.MaterialCheckConsumer;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MenuItem {

    private final String name;
    @Getter
    private final MenuItemData data;
    private final CommandProcessingConsumer commandProcessingConsumer;
    private final StringCutterService stringCutterService;
    private final RequirementCheckService requirementCheckService;
    private final MaterialCheckConsumer materialCheckConsumer;
    private final NMSConsumer nmsConsumer;
    private final DebugService debugService;

    public MenuItem(MenuItemData data,
                    CommandProcessingConsumer commandProcessingConsumer,
                    StringCutterService stringCutterService,
                    RequirementCheckService requirementCheckService,
                    MaterialCheckConsumer materialCheckConsumer,
                    NMSConsumer nmsConsumer,
                    DebugService debugService,
                    String name) {
        this.data = data;
        this.commandProcessingConsumer = commandProcessingConsumer;
        this.stringCutterService = stringCutterService;
        this.requirementCheckService = requirementCheckService;
        this.materialCheckConsumer = materialCheckConsumer;
        this.nmsConsumer = nmsConsumer;
        this.debugService = debugService;
        this.name = name;
    }

    public @NotNull ItemStack convertToItemStack(final Player placeholdersOwner, final MenuArgs menuArgs) {
        try {
            var item = this.materialCheckConsumer.create(this.data.getMaterial(), placeholdersOwner);
            final var itemMeta = item.getItemMeta();
            if (itemMeta == null) {
                return item;
            }

            if (itemMeta instanceof Damageable damageable) damageable.setDamage(this.data.getData());
            if (this.data.getModelData() != 0) itemMeta.setCustomModelData(this.data.getModelData());

            if (!this.data.getDynamicAmount().strip().equals("-1")) {
                item.setAmount(Integer.parseInt(PlaceholderAPI.setPlaceholders(placeholdersOwner, this.data.getDynamicAmount())));
            } else {
                item.setAmount(this.data.getAmount());
            }

            if (itemMeta instanceof BannerMeta banner) {

                for (final var meta : this.data.getBannerMeta()) {
                    banner.addPattern(new Pattern(meta.getDyeColor(), meta.getPatternType()));
                }

            } else if (itemMeta instanceof PotionMeta potion) {

                potion.setColor(this.data.getColor().toColor());
                for (final var effect : this.data.getPotionEffects()) {
                    potion.addCustomEffect(new PotionEffect(effect.getEffectType(), effect.getDuration(), effect.getAmplifier()), true);
                }
                if (this.data.isHideEffects()) itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

            }
            for (final var flag : this.data.getItemFlags()) {
                itemMeta.addItemFlags(flag);
            }

            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes(
                    '&',
                    PlaceholderAPI.setPlaceholders(placeholdersOwner,
                            menuArgs.setHolders(this.data.getDisplayName()))));

            if (!this.data.getLore().isEmpty()) {
                final var lore = new ArrayList<String>();
                for (final var str : this.data.getLore()) {
                    lore.add(ChatColor.translateAlternateColorCodes(
                            '&',
                            PlaceholderAPI.setPlaceholders(placeholdersOwner, menuArgs.setHolders(str))));
                }
                itemMeta.setLore(lore);
            }
            for (final var enchantment : this.data.getEnchantments()) {
                itemMeta.addEnchant(enchantment.getEnchantment(), enchantment.getLevel(), true);
            }
            if (this.data.isHideEnchantments()) itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            if (this.data.isHideAttributes()) itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            if (this.data.isHideUnbreakable()) itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            itemMeta.setUnbreakable(this.data.isUnbreakable());

            item.setItemMeta(itemMeta);

            final var nbtStringKey = this.data.getNbtStringKey();
            if ((nbtStringKey != null & !(nbtStringKey.isEmpty()))) {
                if (!(this.nmsConsumer.hasTag(item, nbtStringKey))) {
                    item = this.nmsConsumer.setTag(item, nbtStringKey, this.data.getNbtStringValue());
                }
            }

            final var nbtIntKey = this.data.getNbtIntKey();
            if ((nbtIntKey != null & !(nbtIntKey.isEmpty()))) {
                if (!(this.nmsConsumer.hasTag(item, nbtIntKey))) {
                    item = this.nmsConsumer.setTag(item, nbtIntKey, this.data.getNbtIntValue());
                }
            }

            for (final var stringKey : this.data.getNbtStrings().keySet()) {
                if (!(this.nmsConsumer.hasTag(item, stringKey))) {
                    item = this.nmsConsumer.setTag(item, stringKey, this.data.getNbtStrings().get(stringKey));
                }
            }

            for (final var intKey : this.data.getNbtInts().keySet()) {
                if (!(this.nmsConsumer.hasTag(item, intKey))) {
                    item = this.nmsConsumer.setTag(item, intKey, this.data.getNbtInts().get(intKey));
                }
            }
            return item;
        } catch (Exception e) {
            debugService.debug("&cНе получилось создать педмет " + this.name, DebugLevel.MEDIUM);
        }
        return new ItemStack(Material.STONE);
    }

    public void executeClickCommands(final  Player player) {
        final var clickCommands = this.data.getClickCommands();
        if (clickCommands.isEmpty()) return;
        if (!(this.requirementCheckService.check(this.data.getClickCommandsRequirement(), player))) return;
        this.commandProcessingConsumer.executeСommands(clickCommands, player);
    }

    public void executeLeftClickCommands(final Player player) {
        final var leftClickCommands = this.data.getLeftClickCommands();
        if (leftClickCommands.isEmpty()) return;
        if (!(this.requirementCheckService.check(this.data.getLeftClickCommandsRequirement(), player))) return;
        this.commandProcessingConsumer.executeСommands(leftClickCommands, player);
    }

    public void executeRightClickCommands(final Player player) {
        final var rightClickCommands = this.data.getRightClickCommands();
        if (rightClickCommands.isEmpty()) return;
        if (!(this.requirementCheckService.check(this.data.getRightClickCommandsRequirement(), player))) return;
        this.commandProcessingConsumer.executeСommands(rightClickCommands, player);
    }

    public void executeMiddleClickCommands(final Player player) {
        final var middleClickCommands = this.data.getMiddleClickCommands();
        if (middleClickCommands.isEmpty()) return;
        if (!(this.requirementCheckService.check(this.data.getMiddleClickCommandsRequirement(), player))) return;
        this.commandProcessingConsumer.executeСommands(middleClickCommands, player);
    }

    public void executeShiftLeftClickCommands(final Player player) {
        final var shiftLeftClickCommands = this.data.getShiftLeftClickCommands();
        if (shiftLeftClickCommands.isEmpty()) return;
        if (!(this.requirementCheckService.check(this.data.getShiftLeftClickCommandsRequirement(), player))) return;
        this.commandProcessingConsumer.executeСommands(shiftLeftClickCommands, player);
    }

    public void executeShiftRightClickCommands(final Player player) {
        final var shiftRightClickCommands = this.data.getShiftRightClickCommands();
        if (shiftRightClickCommands.isEmpty()) return;
        if (!(this.requirementCheckService.check(this.data.getRightClickCommandsRequirement(), player))) return;
        this.commandProcessingConsumer.executeСommands(shiftRightClickCommands, player);
    }

    public boolean canPick(final Player player) {
        if (!(this.data.isPickable())) return false;
        return this.requirementCheckService.check(this.data.getPickRequirement(), player);
    }

    public boolean canPut(final Player player) {
        if (!(this.data.isPutable())) return false;
        return this.requirementCheckService.check(this.data.getPutRequirement(), player);
    }

    public boolean isAir() {
        return this.data.isAir();
    }

    public boolean isUpdate() {
        return this.data.isUpdate();
    }

}
