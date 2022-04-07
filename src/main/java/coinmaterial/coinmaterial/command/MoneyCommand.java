package coinmaterial.coinmaterial.command;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import coinmaterial.coinmaterial.CoinMaterial;
import coinmaterial.coinmaterial.CoinSerializer.CoinSerializer;

/**
 * Implements CoinMaterial money command (get balance)
 * Usage:        /money
 * Requirements: none
 */
public class MoneyCommand extends AbstractCommand {
    public MoneyCommand() {
        // Simple constructor with super support
        super("money");
    }

    public void executeForPlayer(CommandSender sender) {
    	// executeForPlayer method - executes pay command for player
    	// Part of Guilded plugin integration
    	String msg = ChatColor.BOLD + getLocal("msgMoney", "infoMoney");
        msg = msg.replace("{amount}", Integer.toString(CoinSerializer.getPlayerCoin(sender.getName()).intValue()));
        msg = msg.replace("{currencySymbol}", ChatColor.GOLD + getSettings("currency", "currencySymbol") + ChatColor.RESET);
        sender.sendMessage(msg);
    }
    
    public void executeForGuild(CommandSender sender) {
    	// executeForGuild method - executes pay command for player guild
    	// Part of Guilded plugin integration
    	sender.sendMessage("Not implemented yet, sorry!");
    	return;
    }
    
    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        // Overridden execute method - messages balance info to sender
        if (!(sender instanceof Player)) {
        	sender.sendMessage(getLocal("general", "issuerNotPLayer"));
            return;
        }
        
        if (args.length == 1) {
        	
        	// Guilded plugin integration support
            if (CoinMaterial.guildedInstalled) {
            	
            	if (args[0].equalsIgnoreCase("guild")) {
            		
            		// Get player Guild
            		// ...
            		executeForGuild(sender);
            		return;
            	} else {
            		String msg = ChatColor.RED + getLocal("guilded", "incorrectArgument") + ChatColor.RESET;
            		sender.sendMessage(msg);
            		return;
            	}
            } else {
            	String msg = ChatColor.RED + getLocal("msgMoney", "tooManyArguments") + ChatColor.RESET;
        		sender.sendMessage(msg);
        		return;
            }
        }
        
        executeForPlayer(sender);
    }
    
    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        // Overridden complete method - returns reload as only available command
    	
    	// TODO: test if empty string is needed
    	// Guilded plugin integration support
        if ((args.length == 0) && CoinMaterial.guildedInstalled)
        		return Lists.newArrayList("guild");
        
        return Lists.newArrayList();
    }
}
