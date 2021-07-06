package de.max.crates.Commands;

import de.max.crates.api.CratesAPI;
import de.max.crates.utils.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CrateCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player)sender;
            if(args.length == 2){

                String base = args[0];
                String data = args[1];
                if(base.equalsIgnoreCase("give")){
                    if (data.equalsIgnoreCase("chest")) {
                        p.getInventory().addItem(new ItemStack(CratesAPI.getCrateItem()));
                        p.sendMessage(Config.cfg.getString("Prefix")+ "§7Du hast den §cCrate Block §7bekommen!");
                    }
                        if (data.equalsIgnoreCase("key")) {
                            p.getInventory().addItem(new ItemStack(CratesAPI.getKeyItem()));
                            p.sendMessage(Config.cfg.getString("Prefix")+ "§7Du hast eine §cCrate Key §7bekommen!");
                    }
                }
            }
        }
        return false;
    }
}
