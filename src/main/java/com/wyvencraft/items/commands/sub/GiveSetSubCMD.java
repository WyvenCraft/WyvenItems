package com.wyvencraft.items.commands.sub;

import com.wyvencraft.api.commands.SubCommand;
import com.wyvencraft.api.utils.Text;
import com.wyvencraft.items.WyvenItems;
import com.wyvencraft.items.data.ArmorSet;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveSetSubCMD extends SubCommand {
    private final WyvenItems addon;

    public GiveSetSubCMD(WyvenItems addon, String name, String permission, int minArgs, boolean playerCommand) {
        super(addon.getPlugin(), name, permission, minArgs, playerCommand);
        this.addon = addon;
    }

    // /customitems giveset <set> [player]


    @Override
    protected void handleCommand(CommandSender sender, String[] args) {
        Player target = sender instanceof Player ? (Player) sender : null;
        ArmorSet armorSet;

        if (args.length == 0) {
            sender.sendMessage(Text.color("&cUsage: /wi giveset <set> [player]"));
            return;
        }

        if (addon.getItemManager().getArmorSetFromID(args[0]) == null) {
            getPlugin().getLanguageManager().sendMessage(sender, "ITEMS.INVALID_ARMOR_SET", r -> r.replace("{0}", args[0]));
            return;
        }

        armorSet = addon.getItemManager().getArmorSetFromID(args[0]);

        if (args.length >= 2) {
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                getPlugin().getLanguageManager().sendMessage(sender, "INVALID_PLAYER", r -> r.replace("{0}", args[1]));
                return;
            }
        }

        if (target == null) {
            getPlugin().getLanguageManager().sendMessage(sender, "MISSING_TARGET", r -> r.replace("{0}", "(players only)"));
            return;
        }

        addon.getItemManager().giveSet(target, armorSet);
    }
}
