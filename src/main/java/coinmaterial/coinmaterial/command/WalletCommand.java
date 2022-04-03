package coinmaterial.coinmaterial.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiFunction;
import org.apache.commons.lang.math.NumberUtils;

import coinmaterial.coinmaterial.Hash.Hashmapper;


/**
 * Implements CoinMaterial wallet command
 * Usage:        /CoinMaterial wallet
 * Requirements: none
*/
public class WalletCommand extends AbstractCommand {
    public WalletCommand() {
		// Simple constructor with super support
        super("wallet");
    }
	
	public boolean enoughCoins(CommandSender sender, Double paymentValue) {
		// enoughCoins method - tests if sender user has enough coins in their wallet
		return paymentValue < Double.parseDouble(String.valueOf(Hashmapper.playerCoin.get(Bukkit.getPlayer(sender.getName()).getName())));
		
	}
	
	public String pluralize(String pluralizable, Double amount) {
		// pluralize method - return pluralized pluralizable string according to amount in russian
		Integer mod10 = (int)amount % 10;
		Integer mod100 = (int)amount % 100;
		
		if((mod10 == 1) && (mod100 != 11)){
			return pluralizable + "ь";
		} else if ((mod10 >= 2) && (mod10 <= 4) && ((mod100 < 10) || (mod100 >= 20))) {
			return pluralizable + "я";
		} else {
			return pluralizable + "ей";
		}
	}
	
    @Override
    public void execute(CommandSender sender, String label, String[] args) {
		// Overridden execute method - implements wallet command
        if (args.length == 0) {
            sender.sendMessage(ChatColor.BOLD + "Введите колличество Арабских лигатур Джаллаялалоуху, которое хотите вывести.");
            return;
        }

        if (isNumber(args[0]) == true) {
			// Deposit value proveded as number
            if (enoughCoins(sender, Double.parseDouble(args[0]))) {
				// Player has enough coins to deposit
				sender.sendMessage(ChatColor.BOLD + "Вы получили " + args[0] + " " + pluralize("смарагд", Double.parseDouble(args[0])) + " по курсу 1 к 1 с " + ChatColor.GOLD + "ﷻ");
				
                
				// Send emeralds to inventory, remove from wallet
				BiFunction<Double, Double, Double> bFuncSub = (oldValue, newValue) -> oldValue - newValue;
                
                Bukkit.getPlayer(sender.getName()).getInventory().addItem(new ItemStack(Material.EMERALD, Integer.valueOf(args[0])));
                Hashmapper.playerCoin.put(Bukkit.getPlayer(sender.getName()).getName(), Hashmapper.playerCoin.merge(Bukkit.getPlayer(sender.getName()).getName(), Double.valueOf(args[0]), bFuncSub));

                Hashmapper.SaveCoin();
                return;
				
            } else {
                sender.sendMessage(ChatColor.BOLD + "الوغد, у вас не достаточно лигатур!");
                return;
            }
        } else {
            sender.sendMessage(ChatColor.BOLD + "Введите корректную сумму вывода числом!");
            return;

        }
    }
}
