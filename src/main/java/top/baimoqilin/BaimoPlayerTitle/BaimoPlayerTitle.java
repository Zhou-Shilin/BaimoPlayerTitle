package top.baimoqilin.BaimoPlayerTitle;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

import java.util.HashMap;

public class BaimoPlayerTitle extends PluginBase implements Listener {

    private Config config;
    private HashMap<String, String> playerTitles = new HashMap<>();

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.config = this.getConfig();
        this.playerTitles = (HashMap<String, String>) this.config.get("playerTitles", new HashMap<String, String>());
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        this.config.set("playerTitles", this.playerTitles);
        this.config.save();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("pt")) {
            if (args.length == 0) {
                sender.sendMessage("Usage: /pt add <playertitle> <titleid>");
                sender.sendMessage("Usage: /pt give <titleid> <player>");
                sender.sendMessage("Usage: /pt delete <titleid>");
                sender.sendMessage("Usage: /pt strip <player>");
                sender.sendMessage("Usage: /pt switch <titleid>");
                sender.sendMessage("Usage: /pt remove <playertitle>");
                sender.sendMessage("Usage: /pt deprive <playertitle> <player>");
                sender.sendMessage("Usage: /pt list");
                return true;
            }
            if (args[0].equalsIgnoreCase("add")) {
                if (args.length != 3) {
                    sender.sendMessage("Usage: /pt add <playertitle> <titleid>");
                    return true;
                }
                if (sender.isOp()) {
                    String playerTitle = args[1];
                    String titleId = args[2];
                    this.playerTitles.put(titleId, playerTitle);
                    sender.sendMessage("Player title added successfully.");
                } else {
                    sender.sendMessage("You do not have permission to use this command.");
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("give")) {
                if (args.length != 3) {
                    sender.sendMessage("Usage: /pt give <titleid> <player>");
                    return true;
                }
                if (sender.isOp()) {
                    String titleId = args[1];
                    String playerName = args[2];
                    Player player = this.getServer().getPlayer(playerName);
                    if (player != null) {
                        String playerTitle = this.playerTitles.get(titleId);
                        if (playerTitle != null) {
                            player.setDisplayName(player.getName() + " " + playerTitle);
                            sender.sendMessage("Player title given successfully.");
                        } else {
                            sender.sendMessage("Invalid title ID.");
                        }
                    } else {
                        sender.sendMessage("Player not found.");
                    }
                } else {
                    sender.sendMessage("You do not have permission to use this command.");
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("delete")) {
                if (args.length != 2) {
                    sender.sendMessage("Usage: /pt delete <titleid>");
                    return true;
                }
                if (sender.isOp()) {
                    String titleId = args[1];
                    if (this.playerTitles.remove(titleId) != null) {
                        sender.sendMessage("Player title deleted successfully.");
                    } else {
                        sender.sendMessage("Invalid title ID.");
                    }
                } else {
                    sender.sendMessage("You do not have permission to use this command.");
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("strip")) {
                if (args.length != 2) {
                    sender.sendMessage("Usage: /pt strip <player>");
                    return true;
                }
                if (sender.isOp()) {
                    String playerName = args[1];
                    Player player = this.getServer().getPlayer(playerName);
                    if (player != null) {
                        player.setDisplayName(player.getName());
                        sender.sendMessage("Player title stripped successfully.");
                    } else {
                        sender.sendMessage("Player not found.");
                    }
                } else {
                    sender.sendMessage("You do not have permission to use this command.");
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("switch")) {
                if (args.length != 2) {
                    sender.sendMessage("Usage: /pt switch <titleid>");
                    return true;
                }
                String titleId = args[1];
                String playerTitle = this.playerTitles.get(titleId);
                if (playerTitle != null) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        player.setDisplayName(player.getName() + " " + playerTitle);
                        sender.sendMessage("Player title switched successfully.");
                    } else {
                        sender.sendMessage("You must be a player to use this command.");
                    }
                } else {
                    sender.sendMessage("Invalid title ID.");
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("remove")) {
                if (args.length != 2) {
                    sender.sendMessage("Usage: /pt remove <playertitle>");
                    return true;
                }
                if (sender.isOp()) {
                    String playerTitle = args[1];
                    String titleId = null;
                    for (String key : this.playerTitles.keySet()) {
                        if (this.playerTitles.get(key).equals(playerTitle)) {
                            titleId = key;
                            break;
                        }
                    }
                    if (titleId != null) {
                        this.playerTitles.remove(titleId);
                        sender.sendMessage("Player title removed successfully.");
                    } else {
                        sender.sendMessage("Player title not found.");
                    }
                } else {
                    sender.sendMessage("You do not have permission to use this command.");
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("deprive")) {
                if (args.length != 3) {
                    sender.sendMessage("Usage: /pt deprive <playertitle> <player>");
                    return true;
                }
                if (sender.isOp()) {
                    String playerTitle = args[1];
                    String playerName = args[2];
                    Player player = this.getServer().getPlayer(playerName);
                    if (player != null) {
                        if (player.getDisplayName().contains(playerTitle)) {
                            player.setDisplayName(player.getName());
                            sender.sendMessage("Player title deprived successfully.");
                        } else {
                            sender.sendMessage("Player does not have this title.");
                        }
                    } else {
                        sender.sendMessage("Player not found.");
                    }
                } else {
                    sender.sendMessage("You do not have permission to use this command.");
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("list")) {
                if (args.length != 1) {
                    sender.sendMessage("Usage: /pt list");
                    return true;
                }
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    String playerTitles = "";
                    for (String key : this.playerTitles.keySet()) {
                        if (player.getDisplayName().contains(this.playerTitles.get(key))) {
                            playerTitles += key + ", ";
                        }
                    }
                    if (playerTitles.length() > 0) {
                        playerTitles = playerTitles.substring(0, playerTitles.length() - 2);
                    }
                    sender.sendMessage("Your titles: " + playerTitles);
                } else {
                    sender.sendMessage("You must be a player to use this command.");
                }
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        String playerTitle = this.playerTitles.get(player.getName());
        if (playerTitle != null) {
            event.setFormat("<" + player.getName() + " " + playerTitle + "> " + event.getMessage());
        }
    }
}
