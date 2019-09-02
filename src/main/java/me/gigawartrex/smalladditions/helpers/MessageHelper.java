package me.gigawartrex.smalladditions.helpers;

import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageHelper {

  private ChatColor pluginNameColor;
  private String pluginName;
  private String pluginNameString;

  public MessageHelper() {
    this.pluginNameColor = Constants.color;
    this.pluginName = Constants.name;
    this.pluginNameString = pluginNameColor + "[" + pluginName + "]";
  }

  public void sendConsole(String msg, ChatColor msgColor) {
    Constants.console.sendMessage(pluginNameString + " " + msgColor + msg);
  }

  public void sendPlayer(Player player, String msg, ChatColor msgColor) {
    player.sendMessage(pluginNameString + " " + msgColor + msg);
  }

  public void sendAllPlayers(String msg, ChatColor msgColor) {
    for (Player player : Constants.console.getServer().getOnlinePlayers()) {
      sendPlayer(player, msg, msgColor);
    }
  }

  public void sendAll(String msg, ChatColor msgColor) {
    sendConsole(msg, msgColor);
    sendAllPlayers(msg, msgColor);
  }
}
