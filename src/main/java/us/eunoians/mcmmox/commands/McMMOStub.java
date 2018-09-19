package us.eunoians.mcmmox.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.eunoians.mcmmox.Mcmmox;
import us.eunoians.mcmmox.api.util.Methods;
import us.eunoians.mcmmox.gui.GUI;
import us.eunoians.mcmmox.gui.GUITracker;
import us.eunoians.mcmmox.gui.HomeGUI;
import us.eunoians.mcmmox.players.PlayerManager;

public class McMMOStub implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Mcmmox plugin = Mcmmox.getInstance();
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(args.length == 0){
                GUI gui = new HomeGUI(PlayerManager.getPlayer(p.getUniqueId()));
                p.openInventory(gui.getGui().getInv());
                GUITracker.trackPlayer(p, gui);
                return true;
            }
            else if(args.length == 1){
                if(args[0].equalsIgnoreCase("reload")) {
                    if(p.hasPermission("mcmmo.*") || p.hasPermission("mcmmo.admin.*") || p.hasPermission("mcmmo.admin.reload")){
                        Mcmmox.getInstance().getFileManager().reloadFiles();
                        p.sendMessage(Methods.color(plugin.getPluginPrefix() + plugin.getLangFile().getString("Messages.Commands.ReloadFiles")));
                        PlayerManager.startSave(plugin);
                        return true;
                    }
                    else{
                        p.sendMessage(Methods.color(plugin.getPluginPrefix() + plugin.getLangFile().getString("Messages.Commands.Utility.NoPerms")));
                        return true;
                    }
                }
            }
            else{
                p.sendMessage("Not added");
                return true;
            }
        }
        else{
            if(args.length == 0){
                sender.sendMessage(Methods.color(plugin.getPluginPrefix() + "&cConsole can not run this command"));
                return true;
            }
            else if(args.length == 1){
                if(args[0].equalsIgnoreCase("reload")){
                    Mcmmox.getInstance().getFileManager().reloadFiles();
                    sender.sendMessage(Methods.color(plugin.getPluginPrefix() + plugin.getLangFile().getString("Messages.Commands.ReloadFiles")));
                    PlayerManager.startSave(plugin);
                    return true;
                }
                else{
                    sender.sendMessage("Not added");
                    return true;
                }
            }
            else{
                sender.sendMessage("Not added");
                return true;
            }
        }
        return false;
    }
}