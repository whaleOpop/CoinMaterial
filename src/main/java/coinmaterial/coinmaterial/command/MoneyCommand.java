package coinmaterial.coinmaterial.command;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import coinmaterial.coinmaterial.CoinMaterial;
import coinmaterial.coinmaterial.GuildModel;
import coinmaterial.coinmaterial.CoinSerializer.CoinSerializer;
import coinmaterial.coinmaterial.GuildSerializer.GuildSerializer;

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
        msg = msg.replace("{amount}", Integer.toString(CoinSerializer.getCoin(sender.getName()).intValue()));
        msg = msg.replace("{currencySymbol}", ChatColor.GOLD + getSettings("currency", "currencySymbol") + ChatColor.RESET);
        sender.sendMessage(msg);
    }
    
    public void executeForGuild(CommandSender sender) {
    	// executeForGuild method - executes pay command for player guild
    	// Part of Guilded plugin integration
    	
    	// Testing player permissions in guild
		GuildModel gm = GuildSerializer.getGuildByPlayername(sender.getName());
		if (gm != null) {
			// If guild exists
			if (gm.testMembership(sender.getName())) {
				// If issuer is guild member
				String msg = ChatColor.BOLD + getLocal("guildedMoney", "infoMoney");
		        msg = msg.replace("{amount}", Integer.toString(CoinSerializer.getCoin(gm.getGuildWalletName()).intValue()));
		        msg = msg.replace("{currencySymbol}", ChatColor.GOLD + getSettings("currency", "currencySymbol") + ChatColor.RESET);
		        sender.sendMessage(msg);
			} else {
				// Player is too low of a level to access in a guild this command
				sender.sendMessage(ChatColor.RED + getLocal("guilded", "playerNotAdded") + ChatColor.RESET);
			}
		} else {
			// Player is not in a guild
			sender.sendMessage(ChatColor.RED + getLocal("guilded", "playerNotInGuild") + ChatColor.RESET);
		}
    }
    
    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        // Overridden execute method - messages balance info to sender
        if (!(sender instanceof Player)) {
        	sender.sendMessage(getLocal("general", "issuerNotPLayer"));
            return;
        }
        
        if (args.length == 1) {
            // Player passed an argument
            
        	if (CoinMaterial.guildedInstalled) {
        		// CoinMaterial integration
        		
        		if (args[0].equalsIgnoreCase("guild")) {
        			// Argument is a guild string
        			// Executing for guild
        			executeForGuild(sender);
        		} else {
        			// Too many arguments for money command
        			sender.sendMessage(ChatColor.RED + getLocal("general", "tooManyArguments") + ChatColor.RESET);
        		}
        	} else {
        		// Guilded not installed
        		if (args[0].equalsIgnoreCase("guild")) {
        			// Yet player tried to access guild's wallet
        			sender.sendMessage(ChatColor.RED + getLocal("guilded", "incorrectArgument") + ChatColor.RESET);
        		} else {
        			// Too many arguments for money command
        			sender.sendMessage(ChatColor.RED + getLocal("general", "tooManyArguments") + ChatColor.RESET);
        		}
        	}
        } else {
        	// Player issued a simple money command
        	// Get player wallet
        	executeForPlayer(sender);
        }
    }
    
    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        // Overridden complete method - returns reload as only available command
    	
    	// TODO: test if empty string is needed
    	// Guilded plugin integration support
        if ((args.length == 1) && CoinMaterial.guildedInstalled)
        		return Lists.newArrayList("guild");
        
        return Lists.newArrayList();
    }
}
