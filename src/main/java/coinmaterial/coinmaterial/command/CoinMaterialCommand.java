package coinmaterial.coinmaterial.command;

import coinmaterial.coinmaterial.CoinMaterial;
import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CoinMaterialCommand extends AbstractCommand {

    public CoinMaterialCommand() {
        super("CoinMaterial");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Reload plugin: /CoinMaterial reload");
            return;
        }
        if (args[0].equalsIgnoreCase("reload")) {


            if (!sender.hasPermission("CoinMaterial.reload")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission");
                return;
            }
            CoinMaterial.getInstance().reloadConfig();
            sender.sendMessage(ChatColor.GREEN+"CoinMaterial Reloaded");
            return ;
        }
        sender.sendMessage(ChatColor.RED+"Unknown command:"+args[0]);
        }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if(args.length==1)return Lists.newArrayList("reload");
        return Lists.newArrayList();
    }
}


