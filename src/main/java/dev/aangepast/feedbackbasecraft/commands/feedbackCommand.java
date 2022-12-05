package dev.aangepast.feedbackbasecraft.commands;

import dev.aangepast.feedbackbasecraft.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import javax.net.ssl.HttpsURLConnection;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

import static org.apache.commons.lang.StringUtils.join;

public class feedbackCommand implements CommandExecutor {
    public HashMap<String, Long> cooldowns = new HashMap<String, Long>();

    private static Main plugin;

    public feedbackCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            int cooldownTime = plugin.getConfig().getInt("cooldown-in-seconds");
            if (cooldowns.containsKey(sender.getName())) {
                long secondsLeft = ((cooldowns.get(sender.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
                if (secondsLeft > 0) {
                    p.sendMessage(ChatColor.RED + "You are still on cooldown, please wait " + ChatColor.YELLOW + secondsLeft + " seconds" + ChatColor.RED + "!");
                    return true;
                } else {
                    cooldowns.remove(sender.getName());
                    p.sendMessage("");
                    p.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Feedback information:");
                    p.sendMessage(ChatColor.DARK_GRAY + "- " + ChatColor.RED + "It's not allowed to send your previous feedback again.");
                    p.sendMessage(ChatColor.DARK_GRAY + "- " + ChatColor.RED + "Make clear what you mean, so we understand what you mean.");
                    p.sendMessage(ChatColor.DARK_GRAY + "- " + ChatColor.RED + "Stick to what this is intended for.");
                    p.sendMessage(ChatColor.AQUA + ChatColor.UNDERLINE.toString() + "Execute this command again to send your feedback");
                    p.sendMessage("");
                    p.playSound(p.getLocation(), "block.note_block.pling", 1, 1);
                }
            } else {
                if (args.length > 2) {
                    cooldowns.put(sender.getName(), System.currentTimeMillis());
                    p.sendMessage(ChatColor.GREEN + "Thank you for your feedback! We will look into it as soon as possible.");
                    p.playSound(p.getLocation(), "entity.player.levelup", 1, 1);
                    p.sendTitle(ChatColor.GREEN + "Thank you!", ChatColor.YELLOW + "We'll look into it", 0, 40, 20);

                    FileConfiguration config = plugin.getConfig();

                    final String webhookURL = config.getString("discord-webhook");

                    if (Objects.equals(webhookURL, "link-here")) {
                        p.sendMessage(ChatColor.RED + "This plugin has not been setup correctly, contact Aangepast#6600 or the staff team." + ChatColor.GRAY + " (Config failed)");
                    } else {
                        main(args, p);
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "Your feedback message must contain more than 3 words!");
                }


            }

        } else {
            plugin.getLogger().info("You can only send this command in-game!");
        }

        return true;
    }

    public static void main(String[] args, Player player) {
        String webhook = plugin.getConfig().getString("discord-webhook");
        if(!Objects.equals(webhook, "link here")){
            String title = "Received new feedback message";
            String message = "**User " + player.getName() + " has sent feedback from basecraft:** `" + join(args, ' ') + "`";
            String jsonBrut = "";
            jsonBrut += "{\"embeds\": [{"
                    + "\"title\": \"" + title + "\","
                    + "\"description\": \"" + message + "\","
                    + "\"color\": 15258703"
                    + "}]}";
            try {
                URL url = new URL(webhook);
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.addRequestProperty("Content-Type", "application/json");
                con.addRequestProperty("User-Agent", "Java-DiscordWebhook-BY-Gelox_");
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                OutputStream stream = con.getOutputStream();
                stream.write(jsonBrut.getBytes());
                stream.flush();
                stream.close();
                con.getInputStream().close();
                con.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            plugin.getLogger().info("Failed to send discord-webhook: configuration is not setup correctly!");
        }
    }
}
