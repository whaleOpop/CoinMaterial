package coinmaterial.coinmaterial.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;
import com.google.common.collect.Lists;

import coinmaterial.coinmaterial.CoinMaterial;


/**
 * Implements handler for CoinMaterial plugin reload
 * Usage:        /CoinMaterial reload
 * Requirements: CoinMaterial.reload permission
*/
public class CoinMaterialCommand extends AbstractCommand {

    public CoinMaterialCommand() {
		// Simple constructor with super support
        super("CoinMaterial");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
		// Overridden execute method - handles plugin reload command
        if (args.length == 0) {
            sender.sendMessage("Reload plugin: /CoinMaterial reload");
            return;
        }
		
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("CoinMaterial.reload")) {
                sender.sendMessage(ChatColor.RED + "You don't have the permission required to reload plugin!");
                return;
            }
            CoinMaterial.getInstance().reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "CoinMaterial Reloaded");
            return;
        }
        
		sender.sendMessage(ChatColor.RED + "Unknown command: " + args[0]);
        }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
		// Overridden complete method - returns reload as only avialiable command
        if(args.length == 1)
			return Lists.newArrayList("reload");
        return Lists.newArrayList();
    }
}


