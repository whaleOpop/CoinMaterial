package coinmaterial.coinmaterial.command;

import coinmaterial.coinmaterial.Hash.Hashmapper;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoneyCommand extends AbstractCommand {
    public MoneyCommand() {
        super("money");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {

        sender.sendMessage(ChatColor.BOLD +"Твой баланс "+Hashmapper.playerCoin.get(sender.getName())+ChatColor.GOLD+ "ﷻ");
    }
}
