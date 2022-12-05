package dev.aangepast.feedbackbasecraft.commands;

import dev.aangepast.feedbackbasecraft.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class teleporthereCommand implements CommandExecutor {

    private Main plugin;

    public teleporthereCommand(Main plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player player = (Player) sender;
            if(player.hasPermission("tphere")){
                if(args.length == 1){

                    if(Bukkit.getPlayer(args[0]) != null){
                        Player target = Bukkit.getPlayer(args[0]);
                        target.teleport(player.getLocation());
                        player.sendMessage(ChatColor.GREEN + "Teleported " + ChatColor.YELLOW + target.getName() + ChatColor.GREEN + " to your location.");
                        target.sendMessage(ChatColor.GREEN + "You have been teleported to " + ChatColor.YELLOW + player.getName());
                    } else {
                        player.sendMessage(ChatColor.RED + "Player not found, please check typo's.");
                    }

                } else {
                    player.sendMessage(ChatColor.RED + "Please specify which player we should teleport to you. " + ChatColor.GRAY + "(Syntax: /s <player>)");
                }
            } else {
                player.sendMessage(ChatColor.RED + "You are not permitted to use this command.");
            }
        } else {
            plugin.getLogger().info("You only can execute this command in-game!");
        }



        return true;
    }
}
