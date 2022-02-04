package me.gigawartrex.smalladditions.helpers;

import me.gigawartrex.smalladditions.io.Config;

/**
 * Abstract template class for {@link IconMenu}.
 *
 * @author Paul Ferlitz
 */
public abstract class MenuTemplate
{
    // Class variables
    private MessageHelper msghelp;
    private Config config;

    /**
     * Class constructor.
     */
    public MenuTemplate()
    {
        msghelp = new MessageHelper();
        config = new Config();
    }

    /**
     * Get the books MessageHelper.
     *
     * @return the {@link MessageHelper}.
     */
    public MessageHelper getMessageHelper()
    {
        return msghelp;
    }

    /**
     * Get the books Config.
     *
     * @return the {@link Config}
     */
    public Config getConfig()
    {
        return config;
    }

    /**
     * Method to generate and return a custom IconMenu.
     *
     * @return the custom {@link IconMenu}
     */
    public abstract IconMenu generateMenu();
}
