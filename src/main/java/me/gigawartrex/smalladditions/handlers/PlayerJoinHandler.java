package me.gigawartrex.smalladditions.handlers;

import me.gigawartrex.smalladditions.main.Constants;
import me.gigawartrex.smalladditions.helpers.Book;
import me.gigawartrex.smalladditions.files.Config;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class PlayerJoinHandler implements Listener {

    private Config config;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        event.getPlayer().setSleepingIgnored(false);

        config = new Config();

        if (config.read("Config.Players." + event.getPlayer().getUniqueId()).equals("")) {

            config.write("Config.Players." + event.getPlayer().getUniqueId() + ".Book received?", "" + false);
        }

        boolean add = true;
        boolean freeSlot = false;
        Book refBook = new Book("SmallAdditions Guide", "GigaWarTr3x", "Rules");

        for(ItemStack stack : event.getPlayer().getInventory().getStorageContents()) {
            if(stack != null) {
                if(stack.getType().equals(Material.WRITTEN_BOOK)) {

                    BookMeta meta = (BookMeta)stack.getItemMeta();

                    if(meta.getTitle().equals(refBook.getMeta().getTitle())) {
                        if(meta.getAuthor().equals(refBook.getMeta().getAuthor())) {
                            if(meta.getPages().equals(refBook.getMeta().getPages())) {
                                add = false;
                            }
                        }
                    }
                }
            }else if(!freeSlot){
                freeSlot = true;
            }
        }

        boolean receivedBook = Boolean.parseBoolean(config.read("Config.Players." + event.getPlayer().getUniqueId() + ".Book received?"));

        if(add && freeSlot && !receivedBook) {
            event.getPlayer().getInventory().addItem(refBook);
            config.write("Config.Players." + event.getPlayer().getUniqueId() + ".Book received?", "" + true);
        }
    }
}
