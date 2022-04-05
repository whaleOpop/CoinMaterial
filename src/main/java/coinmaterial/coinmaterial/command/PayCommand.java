package coinmaterial.coinmaterial.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.List;
import java.util.function.BiFunction;

import coinmaterial.coinmaterial.Hash.Hashmapper;

/**
 * Implements CoinMaterial pay command
 * Usage:        /pay [amount] [receiver]
 * Requirements: none
 */
public class PayCommand extends AbstractCommand {
    public PayCommand() {
        // Simple constructor with super support
        super("pay");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        // Overridden execute method - implements pay command
        if (!(sender instanceof Player)) {
            sender.sendMessage(readConfig("error", "issuerNotPLayer"));
            return;
        }
        
        if (args.length == 0) {
            sender.sendMessage(ChatColor.BOLD + readConfig("msg", "promptInputMoney") + readConfig("msgPay", "promptAdd"));
            return;
        }

    	String playerName = sender.getName();
        
        if (isNumber(args[0]) == true) {
            // Payment value provided is a number
        	Double transferAmount = Double.parseDouble(args[0]);
        	Double playerCoins = Hashmapper.getPlayerCoin(playerName);
        	
            if (transferAmount > 0.0) {
                // Payment value is greater than zero

                if (transferAmount <= playerCoins) {
                    // Player has no less than a payment value coins in wallet

                    if (args.length == 2) {
                        // Player provided payment receiver nickname
                    	
                        if (!args[1].equals(playerName)) {
                            // Payment Sender is not Receiver
                        	
                            if (Hashmapper.playerExists(args[1])) {
                                // Payment Receiver is a Player and has a wallet
                                Player player = (Player) sender;
                                Player receiver = Bukkit.getPlayerExact(args[1]);

                                // Messages sender and receiver about payment, play villager_trade sound to both
                                String msg = readConfig("msgPay", "senderMessage");
                                msg = msg.replace("{receiver}", ChatColor.BOLD + args[1] + ChatColor.RESET);
                                msg = msg.replace("{amount}", args[0]);
                                msg = msg.replace("{coinSymbol}", ChatColor.GOLD + readConfig("coin", "coinSymbol") + ChatColor.RESET);
                                sender.sendMessage(msg);
                                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1.0f, 1.0f);
                                
                                if (receiver != null) {
                                	// Payment receiver might be offline, if so - don`t message him
	                                msg = readConfig("msgPay", "receiverMessage");
	                                msg = msg.replace("{sender}", ChatColor.BOLD + playerName + ChatColor.RESET);
	                                msg = msg.replace("{amount}", args[0]);
	                                msg = msg.replace("{coinSymbol}", ChatColor.GOLD + readConfig("coin", "coinSymbol") + ChatColor.RESET);
	                                receiver.sendMessage(msg);
	                                receiver.playSound(receiver.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1.0f, 1.0f);
                                }
                                
                                // Transfers the money
                                BiFunction<Double, Double, Double> bFuncSub = (oldValue, newValue) -> oldValue - newValue;
                                BiFunction<Double, Double, Double> bFuncAdd = (oldValue, newValue) -> oldValue + newValue;

                                Hashmapper.performCoinOperation(playerName, transferAmount, bFuncSub);
                                Hashmapper.performCoinOperation(args[1], transferAmount, bFuncAdd);
                                
                                // Saves state
                                Hashmapper.SaveCoin();

                            } else {
                                sender.sendMessage(ChatColor.RED + readConfig("msgPay", "incorrectPlayer") + ChatColor.RESET);
                            }
                        } else {
                            sender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + readConfig("msgPay", "transferToSelf") + ChatColor.RESET);
                        }
                    } else {
                        sender.sendMessage(ChatColor.BOLD + readConfig("msgPay", "nullPlayerName") + ChatColor.RESET);
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + readConfig("error", "notEnoughMoney") + ChatColor.RESET);
                }
            } else {
                sender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + readConfig("msgPay", "nullTransfer") + ChatColor.RESET);
            }
        } else {
            sender.sendMessage(ChatColor.BOLD + readConfig("error", "incorrectNumber") + ChatColor.RESET);
        }
    }
    
    @Override
	public List<String> complete(CommandSender sender, String[] args) {
		// Overridden complete method - returns reload as only available command
    	if (args.length == 2) {
    		return Hashmapper.getAllPlayers();
    	}
		return Lists.newArrayList("<amount> <playerName>");
	}
}
