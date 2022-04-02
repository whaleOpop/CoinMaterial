package coinmaterial.coinmaterial.command;

import coinmaterial.coinmaterial.Hash.Hashmapper;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiFunction;

public class WalletCommand extends AbstractCommand {
    public WalletCommand() {
        super("wallet");
    }

    public boolean isNumber(String str) {
        if (str == null || str.isEmpty()) return false;
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) return false;
        }
        return true;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.BOLD + "Введите колличество Арабских лигатур Джаллаялалоуху которые хотите получить");
            return;
        }


        if (isNumber(args[0]) == true) {

            if (!(Double.parseDouble(args[0]) > Double.parseDouble(String.valueOf(Hashmapper.playerCoin.get(Bukkit.getPlayer(sender.getName()).getName()))) )) {

                BiFunction<Double, Double, Double> bFunc = (oldValue, newValue) -> oldValue - newValue;

                System.out.println("число");
                sender.sendMessage(ChatColor.BOLD + "Вы получили " + args[0] + " ");
                Bukkit.getPlayer(sender.getName()).getInventory().addItem(new ItemStack(Material.EMERALD, Integer.valueOf(args[0])));
                Hashmapper.playerCoin.put(Bukkit.getPlayer(sender.getName()).getName(), Hashmapper.playerCoin.merge(Bukkit.getPlayer(sender.getName()).getName(), Double.valueOf(args[0]), bFunc));

                Hashmapper.SaveCoin();
                return;
            } else {
                sender.sendMessage(ChatColor.BOLD+"Чел ты бомж");
                return;
            }
        } else {
            sender.sendMessage(ChatColor.BOLD + "Это не похоже на кол-во Арабских лигатур Джаллаялалоуху");
            return;

        }
    }
}
