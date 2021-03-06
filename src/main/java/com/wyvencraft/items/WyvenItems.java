package com.wyvencraft.items;

import com.wyvencraft.api.addon.Addon;
import com.wyvencraft.api.integration.WyvenAPI;
import com.wyvencraft.items.commands.ItemsCMD;
import com.wyvencraft.items.commands.ItemsTabCompleter;
import com.wyvencraft.items.listeners.OrbListener;
import com.wyvencraft.items.managers.ItemManager;
import com.wyvencraft.items.menus.ItemsMenu;
import com.wyvencraft.items.menus.ItemsMenuProvider;
import com.wyvencraft.items.utils.Message;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;

public class WyvenItems extends Addon {

    public static WyvenItems instance;
    private final ItemManager itemManager;

    public static NamespacedKey WYVEN_ITEM;
    public static NamespacedKey ITEM_TYPE;

    private ItemsMenu itemsMenu;

    public WyvenItems(WyvenAPI plugin) {
        super(plugin);
        instance = this;
        WYVEN_ITEM = new NamespacedKey(this.getPlugin().getPlugin(), "wyvenitems");
        ITEM_TYPE = new NamespacedKey(this.getPlugin().getPlugin(), "type");
        itemManager = new ItemManager(this);
    }

    @Override
    public void onLoad() {
        saveDefaultConfig("items.yml");
        loadMessages();

        itemManager.loadItems();
    }

    @Override
    public void onEnable() {
        ItemsCMD cmd = new ItemsCMD(this);
        getPlugin().registerCommand("wyvenitems", cmd, new ItemsTabCompleter(this), "Main command to accessing and giving custom items", "/wyvenitems <argument>", "witem", "witems", "wi");

        getPlugin().getAddonHandler().registerListener(this, new OrbListener(itemManager));

        itemsMenu = new ItemsMenu(getPlugin().getSmartInventory(), new ItemsMenuProvider());
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void reloadConfig() {
        reloadConfig("items.yml");
    }

    private void loadMessages() {
        final FileConfiguration langFile = getPlugin().getConfig(getPlugin().getLangManager().getLanguage() + ".yml");

        for (Message msg : Message.values()) {
            if (!langFile.isSet(msg.getPath()))
                if (msg.getDefaultMessage().length > 1) langFile.set(msg.getPath(), msg.getDefaultMessage());
                else langFile.set(msg.getPath(), msg.getDefaultMessage()[0]);
        }

        getPlugin().saveConfig(getPlugin().getLangManager().getLanguage() + ".yml");
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public ItemsMenu getItemsMenu() {
        return itemsMenu;
    }
/*
    public static void fullSetBonus(PlayerStats ps, ArmorSet set, boolean debuff) {
        for (String line : set.getFullsetActions()) {
            String[] action = line.split("] ", 2);
            switch (action[0].toLowerCase()) {
//                case "[ability":
//                    Ability ability = plugin.getAbility(action[1]);
//                    if (ability == null) {
//                        plugin.getLogger().severe("could not find ability: " + action[1]);
//                        return;
//                    }
//
//                    if (debuff) ability.revoke(ps);
//                    else ability.apply(ps);
//
//                    break;
                case "[potion":
                    String[] args = action[1].split(";", 2);
                    PotionEffectType potionType;

                    potionType = PotionEffectType.getByName(args[0]);
                    if (potionType == null) {
                        plugin.getLogger().severe("could not find potioneffect: " + args[0]);
                        return;
                    }

                    int amplifier = args.length < 2 ? 1 : Methods.getInteger(args[1]);

                    PotionEffect potion = new PotionEffect(potionType, Integer.MAX_VALUE, amplifier, false, false);

                    if (debuff) ps.getPlayer().removePotionEffect(potionType);
                    else ps.getPlayer().addPotionEffect(potion);

                    break;
                case "[triple":
                    if (action[1].equalsIgnoreCase("attributes")) {
                        if (debuff)
                            ps.getBaseAttributes().forEach((attr, level) -> ps.getBaseAttributes().put(attr, level / 3));
                        else
                            ps.getBaseAttributes().forEach((attr, level) -> ps.getBaseAttributes().put(attr, level * 3));
                    } else if (action[1].equalsIgnoreCase("bonus_attributes")) {
                        if (debuff)
                            ps.getBonusAttributes().forEach((attr, level) -> ps.getBonusAttributes().put(attr, level / 3));
                        else
                            ps.getBonusAttributes().forEach((attr, level) -> ps.getBonusAttributes().put(attr, level * 3));
                    } else {
                        plugin.getLogger().severe("for \"[triple]\" action you can only use \"attributes\" or \"bonus_attributes\". " + action[1] + " is not a valid option");
                        return;
                    }
                    break;
                case "[double":
                    if (action[1].equalsIgnoreCase("attributes")) {
                        if (debuff)
                            ps.getBaseAttributes().forEach((attr, level) -> ps.getBaseAttributes().put(attr, level / 2));
                        else
                            ps.getBaseAttributes().forEach((attr, level) -> ps.getBaseAttributes().put(attr, level * 2));
                    } else if (action[1].equalsIgnoreCase("bonus_attributes")) {
                        if (debuff)
                            ps.getBonusAttributes().forEach((attr, level) -> ps.getBonusAttributes().put(attr, level / 2));
                        else
                            ps.getBonusAttributes().forEach((attr, level) -> ps.getBonusAttributes().put(attr, level * 2));
                    } else {
                        plugin.getLogger().severe("for \"[double]\" action you can only use \"attributes\" or \"bonus_attributes\". " + action[1] + " is not a valid option");
                        return;
                    }
                    break;
                case "[strength":
                    int str = Methods.getInteger(action[1]);
                    if (debuff) AttributesHandler.instance.take(ps, Attribute.STRENGTH, str, true);
                    else AttributesHandler.instance.add(ps, Attribute.STRENGTH, str, true);
                    break;
                case "[defense":
                    int def = Methods.getInteger(action[1]);
                    if (debuff) AttributesHandler.instance.take(ps, Attribute.DEFENSE, def, true);
                    else AttributesHandler.instance.add(ps, Attribute.DEFENSE, def, true);
                    break;
                case "[health":
                    int hp = Methods.getInteger(action[1]);
                    if (debuff) AttributesHandler.instance.take(ps, Attribute.HEALTH, hp, true);
                    else AttributesHandler.instance.add(ps, Attribute.HEALTH, hp, true);
                    break;
                case "[speed":
                    int speed = Methods.getInteger(action[1]);
                    if (debuff) AttributesHandler.instance.take(ps, Attribute.SPEED, speed, true);
                    else AttributesHandler.instance.add(ps, Attribute.SPEED, speed, true);
                    break;
                case "[critchance":
                    int cchance = Methods.getInteger(action[1]);
                    if (debuff) AttributesHandler.instance.take(ps, Attribute.CRIT_CHANCE, cchance, true);
                    else AttributesHandler.instance.add(ps, Attribute.CRIT_CHANCE, cchance, true);
                    break;
                case "[critdamage":
                    int cdmg = Methods.getInteger(action[1]);
                    if (debuff) AttributesHandler.instance.take(ps, Attribute.CRIT_DAMAGE, cdmg, true);
                    else AttributesHandler.instance.add(ps, Attribute.CRIT_DAMAGE, cdmg, true);
                    break;
                case "[intelligence":
                    int in = Methods.getInteger(action[1]);
                    if (debuff) AttributesHandler.instance.take(ps, Attribute.INTELLIGENCE, in, true);
                    else AttributesHandler.instance.add(ps, Attribute.INTELLIGENCE, in, true);
                    break;
                case "[attackspeed":
                    int atkspeed = Methods.getInteger(action[1]);
                    if (debuff) AttributesHandler.instance.take(ps, Attribute.ATTACK_SPEED, atkspeed, true);
                    else AttributesHandler.instance.add(ps, Attribute.ATTACK_SPEED, atkspeed, true);
                    break;
                case "[addperm":
                    if (debuff)
                        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), plugin.getSettings().takePermission
                                .replace("{player}", ps.getPlayer().getName())
                                .replace("{permission}", action[1]));
                    else
                        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), plugin.getSettings().addPermission
                                .replace("{player}", ps.getPlayer().getName())
                                .replace("{permission}", action[1]));
                case "[takeperm":
                    if (debuff)
                        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), plugin.getSettings().addPermission
                                .replace("{player}", ps.getPlayer().getName())
                                .replace("{permission}", action[1]));
                    else
                        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), plugin.getSettings().takePermission
                                .replace("{player}", ps.getPlayer().getName())
                                .replace("{permission}", action[1]));

            }
        }
    }*/
}
