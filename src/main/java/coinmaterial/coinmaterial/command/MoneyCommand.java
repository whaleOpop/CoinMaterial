package coinmaterial.coinmaterial.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;

import coinmaterial.coinmaterial.Hash.Hashmapper;


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

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
		// Overridden execute method - messages balance info to sender
        sender.sendMessage(ChatColor.BOLD + "Ваш баланс: " + Hashmapper.playerCoin.get(sender.getName()).intValue() + ChatColor.GOLD + "ﷻ");
    }
}
