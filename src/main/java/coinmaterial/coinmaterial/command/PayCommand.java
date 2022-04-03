package coinmaterial.coinmaterial.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.net.DatagramPacket;
import java.util.function.BiFunction;

import coinmaterial.coinmaterial.Hash.Hashmapper;


/**
 * Implements CoinMaterial pay command
 * Usage:        /pay
 * Requirements: none
*/
public class PayCommand extends AbstractCommand {
    public PayCommand() {
		// Simple constructor with super support
        super("pay");
    }
	
	public boolean enoughCoins(CommandSender sender, Double paymentValue){
		// enoughCoins method - tests if sender user has enough coins in their wallet
		return paymentValue <= Double.parseDouble(String.valueOf(Hashmapper.playerCoin.get(Bukkit.getPlayer(sender.getName()).getName())));
		
	}
	
    @Override
    public void execute(CommandSender sender, String label, String[] args) {
		// Overridden execute method - implements pay command
        label="s";
		
        if (args.length == 0) {
            sender.sendMessage("Введите количество" + ChatColor.BOLD + "" + ChatColor.GOLD + "Арабских лигатур Джаллаялалоуху" + ChatColor.RESET + ",которое хотите перевести игроку.");
            return;
        }
		
        if (isNumber(args[0]) == true) {
			// Payment value provided is a number
			
            if (enoughCoins(sender, Double.parseDouble(args[0]))) {
				// Player has no less than a payment value coins in wallet
				
				Bukkit.getServer().getOnlinePlayers()
				
				//TODO:fix only for online players
				if(args[1] != null) {
					// Player provided payment receiver player who is online
					
					if(!args[1].equals(sender.getName())) {
						// payment Sender is not Receiver
						
						// Messages two players about outgoing and ingoing payment
						sender.sendMessage("Вы перевели игроку " + ChatColor.BOLD + args[1] + ChatColor.RESET + " " + args[0] + CharColor.GOLD + "ﷻ");
						Bukkit.getPlayer(args[1]).sendMessage("Вы получили " + args[0] + ChatColor.GOLD + "ﷻ" + ChatColor.RESET + " от " + ChatColor.BOLD + sender.getName());
						
						// Plays villager_trade sound to both players
						Bukkit.getPlayer(sender.getName()).playSound(Bukkit.getPlayer(sender.getName()).getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1.0f, 1.0f);
						Bukkit.getPlayer(args[1]).playSound(Bukkit.getPlayer(args[1]).getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1.0f, 1.0f);
						
						// Transfers the money
						BiFunction<Double, Double, Double> bFuncSub = (oldValue, newValue) -> oldValue - newValue;
						BiFunction<Double, Double, Double> bFuncAdd = (oldValue, newValue) -> oldValue + newValue;
						
						Hashmapper.playerCoin.merge(Bukkit.getPlayer(sender.getName()).getName(), Double.valueOf(args[0]), bFuncSub);
						Hashmapper.playerCoin.merge(args[1], Double.valueOf(args[0]), bFuncAdd);
						
						// Saves state
						Hashmapper.SaveCoin();
						
					} else {
						sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Вы " + "أنت شخص غبي" + ", зачем самому себе переводить лигатуры?");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Введите имя игрока на сервере!");
				}
            } else {
                sender.sendMessage(ChatColor.RED + "Вы " + "الوغد" + ", не достаточно лигатур!");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Введите корректную сумму перевода числом!");
        }
    }
}
