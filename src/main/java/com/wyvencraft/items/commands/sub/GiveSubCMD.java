package com.wyvencraft.items.commands.sub;

import com.wyvencraft.api.commands.SubCommand;
import com.wyvencraft.api.utils.MessageUtil;
import com.wyvencraft.items.data.Item;
import com.wyvencraft.items.WyvenItems;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveSubCMD extends SubCommand {
    private final WyvenItems addon;

    public GiveSubCMD(WyvenItems addon, String name, String permission, int minArgs) {
        super(addon.getPlugin(), name, permission, minArgs);
        this.addon = addon;
    }

    // /customitems give <item> [player] [amount]


    @Override
    protected void handleCommand(CommandSender sender, String[] args) {
        Player giveTarget = sender instanceof Player ? (Player) sender : null;
        Item giveItem;
        int amount = 1;

        if (args.length == 0) {
            sender.sendMessage(MessageUtil.color("&cUsage: /wi give <item> [player] [amount]"));
            return;
        }

        if (addon.getItemManager().getCustomItem(args[0]) == null) {
            getPlugin().getLangManager().sendMessage(sender, "ITEMS.INVALID_ITEM", r -> r.replace("{0}", args[0]));
            return;
        }

        giveItem = addon.getItemManager().getCustomItem(args[0]);

        if (args.length >= 2) {
            giveTarget = Bukkit.getPlayer(args[1]);
            if (giveTarget == null) {
                getPlugin().getLangManager().sendMessage(sender, "INVALID_PLAYER", r -> r.replace("{0}", args[1]));
                return;
            }
        }

        if (giveTarget == null) {
            getPlugin().getLangManager().sendMessage(sender, "MISSING_TARGET", r -> r.replace("{0}", "(players only)"));
            return;
        }

        if (args.length >= 3) {
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                getPlugin().getLangManager().sendMessage(sender, "INVALID_NUMBER", r -> r.replace("{0}", args[2]));
                return;
            }

            amount = Math.max(amount, 0);
        }

        addon.getItemManager().giveItem(giveTarget, giveItem, amount);
    }
}
