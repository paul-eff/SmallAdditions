package me.gigawartrex.smalladditions.helpers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

/**
 * Class to instantiate an interactive inventory menu.
 * This class was taken from bukkit.org as documented below (27. December 2020).
 *
 * @author nisovin
 * @see <a href="https://bukkit.org/members/nisovin.2980/">nisovin's profile</a>
 * @see <a href="https://bukkit.org/threads/icon-menu.108342/">original bukkit.org post</a>
 */
public class IconMenu implements Listener
{
    // Class variables
    private final String name;
    private final int size;
    private OptionClickEventHandler handler;
    private Plugin plugin;
    private String[] optionNames;
    private ItemStack[] optionIcons;

    /**
     * Class constructor.
     *
     * @param name    the name of the menu
     * @param size    the size of the menu, must be a multiple of 9
     * @param handler the {@link OptionClickEventHandler} handler to detect item clicks
     * @param plugin  the reference to the bukkit plugin
     */
    public IconMenu(String name, int size, OptionClickEventHandler handler, Plugin plugin)
    {
        this.name = name;
        this.size = size;
        this.handler = handler;
        this.plugin = plugin;
        this.optionNames = new String[size];
        this.optionIcons = new ItemStack[size];
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Method to set a book's name and lore.
     *
     * @param item the item to be edited
     * @param name the new name of the item
     * @param lore the popup text when hovering over the item
     * @return the {@code ItemStack} with new set name and lore
     */
    private ItemStack setItemNameAndLore(ItemStack item, String name, String[] lore)
    {
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(name);
        im.setLore(Arrays.asList(lore));
        item.setItemMeta(im);
        return item;
    }

    /**
     * Method to set an inventory slot to a specific item with information.
     *
     * @param position the position in the inventory, starting with 0
     * @param icon     the {@code ItemStack} to be displayed
     * @param name     the name of the option
     * @param info     the popup information when hovering over the icon
     * @return the resulting {@code IconMenu}
     */
    public IconMenu setOption(int position, ItemStack icon, String name, String... info)
    {
        optionNames[position] = name;
        optionIcons[position] = setItemNameAndLore(icon, name, info);
        return this;
    }

    /**
     * Method to open the inventory for the specified player.
     *
     * @param player the target {@code Player}
     */
    public void open(Player player)
    {
        // Build menu
        Inventory inventory = Bukkit.createInventory(player, size, name);
        for (int i = 0; i < optionIcons.length; i++)
        {
            if (optionIcons[i] != null)
            {
                inventory.setItem(i, optionIcons[i]);
            }
        }
        // Open menu
        player.openInventory(inventory);
    }

    /**
     * Method to destroy the current IconMenu object.
     */
    public void destroy()
    {
        // Unregister all handlers
        HandlerList.unregisterAll(this);

        // Set all other variables to null
        handler = null;
        plugin = null;
        optionNames = null;
        optionIcons = null;
    }

    /**
     * EventHandler to handle clicking on options in the IconMenu.
     *
     * @param event the event generated through inventory clicks
     */
    @EventHandler(priority = EventPriority.MONITOR)
    void onInventoryClick(InventoryClickEvent event)
    {
        // Test if the inventory name is equals to this object
        if (event.getView().getTitle().equals(name))
        {
            // Cancel item "pickup" on click
            event.setCancelled(true);
            int slot = event.getRawSlot();
            // Check option in slot
            if (slot >= 0 && slot < size && optionNames[slot] != null)
            {
                Plugin plugin = this.plugin;
                OptionClickEvent e = new OptionClickEvent((Player) event.getWhoClicked(), slot, optionNames[slot]);
                handler.onOptionClick(e);
                // Close inventory on click
                if (e.willClose())
                {
                    final Player p = (Player) event.getWhoClicked();
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> p.closeInventory(), 1);
                }
                // Destroy inventory if specified
                if (e.willDestroy())
                {
                    destroy();
                }
            }
        }
    }

    /**
     * Interface for the {@link OptionClickEvent} class.
     */
    public interface OptionClickEventHandler
    {
        void onOptionClick(OptionClickEvent event);
    }

    /**
     * Class representing an option click event.
     */
    public class OptionClickEvent
    {
        // Class variables
        private final Player player;
        private final int position;
        private final String name;
        private boolean close;
        private boolean destroy;

        /**
         * Class constructor.
         *
         * @param player   the event {@code Player}
         * @param position the clicked position in the inventory
         * @param name     the name of the inventory
         */
        public OptionClickEvent(Player player, int position, String name)
        {
            this.player = player;
            this.position = position;
            this.name = name;
            this.close = true;
            this.destroy = false;
        }

        /**
         * Method to get the player that clicked an option.
         *
         * @return the {@code Player} that clicked an option
         */
        public Player getPlayer()
        {
            return player;
        }

        /**
         * Method to get the click position in the menu.
         *
         * @return the click position starting at 0
         */
        public int getPosition()
        {
            return position;
        }

        /**
         * Method to get the menu name.
         *
         * @return the name of the menu
         */
        public String getName()
        {
            return name;
        }

        /**
         * Method to get if the menu will close after this event.
         *
         * @return {@code True} if the menu will close
         */
        public boolean willClose()
        {
            return close;
        }

        /**
         * Method to get if the menu will destroy after this event.
         *
         * @return {@code True} if the menu will destroy
         */
        public boolean willDestroy()
        {
            return destroy;
        }

        /**
         * Method to set if the menu will close after this event.
         *
         * @param close if the menu should close
         */
        public void setWillClose(boolean close)
        {
            this.close = close;
        }

        /**
         * Method to set if the menu will destroy after this event.
         *
         * @param destroy if the menu should destroy
         */
        public void setWillDestroy(boolean destroy)
        {
            this.destroy = destroy;
        }
    }
}