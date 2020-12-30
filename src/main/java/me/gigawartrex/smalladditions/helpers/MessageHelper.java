package me.gigawartrex.smalladditions.helpers;

import me.gigawartrex.smalladditions.main.Constants;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Class for sending messages to entities on the server or interacting with it.
 *
 * @author Paul Ferlitz
 * @version 1.0 2020-12-30 Initial Version
 * @since 1.0
 */
public class MessageHelper
{
    // Class variables
    private final ChatColor pluginNameColor;
    private final String pluginName;
    private final String pluginNameString;

    /**
     * Class constructor
     *
     * @since 1.0
     */
    public MessageHelper()
    {
        this.pluginNameColor = Constants.color;
        this.pluginName = Constants.name;
        this.pluginNameString = pluginNameColor + "[" + pluginName + "]";
    }

    /**
     * Method to send a colored message to the console.
     *
     * @param msg      Message string to be sent.
     * @param msgColor Color of the message sent.
     * @since 1.0
     */
    public void sendConsole(String msg, ChatColor msgColor)
    {
        Constants.console.sendMessage(pluginNameString + " " + msgColor + msg);
    }

    /**
     * Method to send a plain message to the console.
     *
     * @param msg Message string to be sent.
     * @since 1.0
     */
    public void sendConsole(String msg)
    {
        Constants.console.sendMessage(pluginNameString + " " + ChatColor.WHITE + msg);
    }

    /**
     * Method to send a colored message to a player.
     *
     * @param player   The target player.
     * @param msg      The string to be sent.
     * @param msgColor Color of the message sent.
     * @since 1.0
     */
    public void sendPlayer(Player player, String msg, ChatColor msgColor)
    {
        player.sendMessage(pluginNameString + " " + msgColor + msg);
    }

    /**
     * Method to send a plain message to a player.
     *
     * @param player The target player.
     * @param msg    The string to be sent.
     * @since 1.0
     */
    public void sendPlayer(Player player, String msg)
    {
        player.sendMessage(pluginNameString + " " + ChatColor.WHITE + msg);
    }

    /**
     * Method to send a colored message to all online player.
     *
     * @param msg      The string to be sent.
     * @param msgColor Color of the message sent.
     * @since 1.0
     */
    public void sendAllPlayers(String msg, ChatColor msgColor)
    {
        for (Player player : Constants.console.getServer().getOnlinePlayers())
        {
            sendPlayer(player, msg, msgColor);
        }
    }

    /**
     * Method to send a plain message to all online player.
     *
     * @param msg The string to be sent.
     * @since 1.0
     */
    public void sendAllPlayers(String msg)
    {
        for (Player player : Constants.console.getServer().getOnlinePlayers())
        {
            sendPlayer(player, msg, ChatColor.WHITE);
        }
    }

    /**
     * Method to send a colored message to all available recipients. Uses {@link #sendConsole(String, ChatColor)} and
     * {@link #sendAllPlayers(String, ChatColor)}.
     *
     * @param msg      The string to be sent.
     * @param msgColor Color of the message sent.
     * @since 1.0
     */
    public void sendAll(String msg, ChatColor msgColor)
    {
        sendConsole(msg, msgColor);
        sendAllPlayers(msg, msgColor);
    }

    /**
     * Method to send a plain message to all available recipients. Uses {@link #sendConsole(String)} and {@link #sendAllPlayers(String)}.
     *
     * @param msg The string to be sent.
     * @since 1.0
     */
    public void sendAll(String msg)
    {
        sendConsole(msg);
        sendAllPlayers(msg);
    }
}
