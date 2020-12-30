package me.gigawartrex.smalladditions.iconmenus;

import me.gigawartrex.smalladditions.helpers.IconMenu;
import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.io.Config;

/**
 * Abstract template class for {@link IconMenu}.
 *
 * @author Paul Ferlitz
 * @version 1.0 2020-12-30 Initial Version
 * @since 1.0
 */
public abstract class MenuTemplate
{
    // Class variables
    private MessageHelper msghelp;
    private Config config;

    /**
     * Class constructor.
     *
     * @since 1.0
     */
    public MenuTemplate()
    {
        msghelp = new MessageHelper();
        config = new Config();
    }

    /**
     * Get the books {@link MessageHelper}.
     *
     * @return The {@link MessageHelper}.
     * @since 1.0
     */
    public MessageHelper getMessageHelper()
    {
        return msghelp;
    }

    /**
     * Get the books {@link Config}.
     *
     * @return The {@link Config}.
     * @since 1.0
     */
    public Config getConfig()
    {
        return config;
    }

    /**
     * Method to gerenate and return a custom {@link IconMenu}
     *
     * @return The custom {@link IconMenu}
     */
    public abstract IconMenu generateMenu();
}
