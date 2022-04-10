package coinmaterial.coinmaterial.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;

import coinmaterial.coinmaterial.CoinMaterial;
import coinmaterial.coinmaterial.GuildModel;
import coinmaterial.coinmaterial.CoinSerializer.CoinSerializer;
import coinmaterial.coinmaterial.GuildSerializer.GuildSerializer;

/**
 * Implements CoinMaterial wallet command
 * Usage:        /wallet [amount]
 * Requirements: none
 */
public class WalletCommand extends AbstractCommand {
    public WalletCommand() {
        // Simple constructor with super support
        super("wallet");
    }

    public String pluralize(String pluralizable, Integer amount) {
        // pluralize method - return pluralized by I17N standart pluralizable string according to amount
        Integer mod10 = amount % 10;
        Integer mod100 = amount % 100;

        if ((mod10 == 1) && (mod100 != 11)) {
            return pluralizable + getLocal("general", "pluralizeOne");
        } else if ((mod10 >= 2) && (mod10 <= 4) && ((mod100 < 10) || (mod100 >= 20))) {
            return pluralizable + getLocal("general", "pluralizeTwoFour");
        }
        return pluralizable + getLocal("general", "pluralizeMany");
    }
    
    public void executeForPlayer(Player player, Double withdrawAmount) {
    	// executeForPlayer method - executes wallet command for player
    	// Part of Guilded plugin integration
    	
    	if (withdrawAmount < 1.0) {
    		// Check for withdrawing 0 coins
            player.sendMessage(ChatColor.RED + getLocal("msgWallet", "nullWithdraw") + ChatColor.RESET);
            return;
        }
    	
        // Put in player inventory, calculate what does not fit
        Integer didntFit = 0;
        HashMap<Integer, ItemStack> didntFitHashmap = player.getInventory().addItem(new ItemStack(Material.getMaterial(getSettings("currency", "COIN_MATERIAL")), withdrawAmount.intValue()));

        for (ItemStack stack : didntFitHashmap.values()) {
            didntFit += stack.getAmount();
        }

        Integer canWithdraw = withdrawAmount.intValue() - didntFit;

        if (canWithdraw == 0) {
            // All player slots are filled
            player.sendMessage(ChatColor.RED + getLocal("msgWallet", "playerInventoryFull") + ChatColor.RESET);
            return;

        } else if (canWithdraw != withdrawAmount.intValue()) {
            // Deposited amount is not the same as inputed amount
            String msg = ChatColor.BOLD + getLocal("msgWallet", "notEnoughSlots") + ChatColor.RESET;
            player.sendMessage(msg);
        }

        // Remove coin from wallet, save
        BiFunction<Double, Double, Double> bFuncSub = (oldValue, newValue) -> oldValue - newValue;
        CoinSerializer.performCoinOperation(player.getName(), Double.valueOf(canWithdraw), bFuncSub);
        CoinSerializer.SaveCoin();

        // Message the player
        String msgPlayer = getLocal("msgWallet", "correctWithdraw");
        msgPlayer = msgPlayer.replace("{amount}", ChatColor.GREEN + canWithdraw.toString() + ChatColor.RESET);
        msgPlayer = msgPlayer.replace("{materialName}", ChatColor.AQUA + pluralize(getLocal("general", "materialName"), canWithdraw) + ChatColor.RESET);
        msgPlayer = msgPlayer.replace("{currencySymbol}", ChatColor.GOLD + getSettings("currency", "currencySymbol") + ChatColor.RESET);
        player.sendMessage(msgPlayer);
    }
    
    public void executeForGuild(Player player, String guildName, Double withdrawAmount) {
    	// executeForGuild method - executes wallet command for player guild
    	// Part of Guilded plugin integration
    	
    	if (withdrawAmount < 1.0) {
    		// Check for withdrawing 0 coins
            player.sendMessage(ChatColor.RED + getLocal("msgWallet", "nullWithdraw") + ChatColor.RESET);
            return;
        }
    	
        // Put in player inventory, calculate what does not fit
        Integer didntFit = 0;
        HashMap<Integer, ItemStack> didntFitHashmap = player.getInventory().addItem(new ItemStack(Material.getMaterial(getSettings("currency", "COIN_MATERIAL")), withdrawAmount.intValue()));

        for (ItemStack stack : didntFitHashmap.values()) {
            didntFit += stack.getAmount();
        }

        Integer canWithdraw = withdrawAmount.intValue() - didntFit;

        if (canWithdraw == 0) {
            // All player slots are filled
            player.sendMessage(ChatColor.RED + getLocal("msgWallet", "playerInventoryFull") + ChatColor.RESET);
            return;

        } else if (canWithdraw != withdrawAmount.intValue()) {
            // Deposited amount is not the same as inputed amount
            player.sendMessage(ChatColor.BOLD + getLocal("msgWallet", "notEnoughSlots") + ChatColor.RESET);
        }
        
        // Remove coin from wallet, save
        BiFunction<Double, Double, Double> bFuncSub = (oldValue, newValue) -> oldValue - newValue;
        CoinSerializer.performCoinOperation(guildName, Double.valueOf(canWithdraw), bFuncSub);
        CoinSerializer.SaveCoin();

        // Message the player
        String msgPlayer = getLocal("msgWallet", "correctWithdraw");
        msgPlayer = msgPlayer.replace("{amount}", ChatColor.GREEN + canWithdraw.toString() + ChatColor.RESET);
        msgPlayer = msgPlayer.replace("{materialName}", ChatColor.AQUA + pluralize(getLocal("general", "materialName"), canWithdraw) + ChatColor.RESET);
        msgPlayer = msgPlayer.replace("{currencySymbol}", ChatColor.GOLD + getSettings("currency", "currencySymbol") + ChatColor.RESET);
        player.sendMessage(msgPlayer);
    }
    
    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        // Overridden execute method - implements wallet command
    	
        if (!(sender instanceof Player)) {
            sender.sendMessage(getLocal("general", "issuerNotPLayer"));
            return;
        }

        if (args.length == 0) {
        	if (CoinMaterial.guildedInstalled) {
        		// Suggest amount or guild keyword
        		sender.sendMessage(ChatColor.BOLD + getLocal("guildedWallet", "promptInputAmount") + ChatColor.RESET);
        	} else {
        		// Suggest passing an amount to the command
        		sender.sendMessage(ChatColor.BOLD + getLocal("general", "promptInputAmount") + getLocal("msgWallet", "promptAdd") + ChatColor.RESET);
        	}
            return;
        }
        
        if (args[0].equalsIgnoreCase("guild")) {
        	// Player tries accessing guild's wallet
        	
        	if (CoinMaterial.guildedInstalled) {
        		// Guilded plugin integration
        		
        		// Testing player's permissions in the guild
        		String playerName = sender.getName();
        		GuildModel gm = GuildSerializer.getGuildByPlayername(playerName);
        		if (gm != null) {
        			// Found player's guild
        			
        			if (gm.testOperatorship(playerName)) {
        				// Player is operator/creator level role
        				
        				if (args.length == 2) {
                			// Player passed withdraw amount to the command
        					
        					if (isNumber(args[1])) {
        	        			// Amount is number
        						
        		        		String guildName = gm.getGuildWalletName();
        						Double withdrawAmount = Double.parseDouble(args[1]);
        		                Double guildCoins = CoinSerializer.getCoin(guildName);
        		                
        		                if (guildCoins >= withdrawAmount) {
        		        			// Execute guild wallet command
        		                	
        		            		executeForGuild((Player) sender, guildName, withdrawAmount);
        		        		} else
        		        			sender.sendMessage(ChatColor.RED + getLocal("guilded", "notEnoughMoney") + ChatColor.RESET);        						
        	        		} else if (args[1].equalsIgnoreCase("all")) {
        	        			// Player can not withdraw all from guild`s wallet
        	        			sender.sendMessage(ChatColor.RED + getLocal("guildedWallet", "withdrawAmountNumberOnly") + ChatColor.RESET);
        	        		} else 
        	        			sender.sendMessage(ChatColor.RED + getLocal("general", "incorrectAmount") + ChatColor.RESET);
        				} else
                			sender.sendMessage(ChatColor.BOLD + getLocal("general", "promptInputAmount") + getLocal("msgWallet", "promptAdd") + ChatColor.RESET);
        			} else
            			sender.sendMessage(ChatColor.BOLD + getLocal("guilded", "playerTooLowRole") + ChatColor.RESET);
        		} else
        			sender.sendMessage(ChatColor.BOLD + getLocal("guilded", "playerNotInGuild") + ChatColor.RESET);
        	} else
        		sender.sendMessage(ChatColor.RED + getLocal("guilded", "incorrectArgument") + ChatColor.RESET);
        	
        } else if (isNumber(args[0]) || args[0].equalsIgnoreCase("all")) {
        	// Simple player wallet withdrawal
        	
            Double withdrawAmount = 0.0;
            Double playerCoins = CoinSerializer.getCoin(sender.getName());
            
            if (args[0].equalsIgnoreCase("all")) {
                // Deposit all coins
                withdrawAmount = playerCoins;
            } else {
                // Deposit amount provided as number
                withdrawAmount = Double.parseDouble(args[0]);

                if (playerCoins < withdrawAmount) {
                    // Player doesn`t have enough coins to deposit
                    sender.sendMessage(ChatColor.RED + getLocal("general", "notEnoughMoney") + ChatColor.RESET);
                    return;
                }
            }
            
            // Execute player wallet command
            executeForPlayer((Player) sender, withdrawAmount);
        } else
        	sender.sendMessage(ChatColor.RED + getLocal("general", "incorrectAmount") + ChatColor.RESET);
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        // Overridden complete method - returns reload as only available command
    	
    	if (CoinMaterial.guildedInstalled) {
    		// Guilded plugin integration support
    		if (args.length == 1)
    			return Lists.newArrayList("<amount>", "all", "guild");
    		
    		
    		if (args.length == 2)
    			if (args[1].equalsIgnoreCase("guild"))
    				return Lists.newArrayList("<amount>");
    		
    	} else if (args.length == 1)
    		return Lists.newArrayList("<amount>", "all");
    	
        return Lists.newArrayList();
    }
}