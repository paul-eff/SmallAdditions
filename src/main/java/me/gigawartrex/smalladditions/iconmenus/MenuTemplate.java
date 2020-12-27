package me.gigawartrex.smalladditions.iconmenus;

import me.gigawartrex.smalladditions.helpers.IconMenu;
import me.gigawartrex.smalladditions.helpers.MessageHelper;
import me.gigawartrex.smalladditions.io.Config;

public abstract class MenuTemplate
{
    private MessageHelper msghelp;
    private Config config;

    public MenuTemplate()
    {
        msghelp = new MessageHelper();
        config = new Config();
    }

    public MessageHelper getMessageHelper()
    {
        return msghelp;
    }

    public Config getConfig()
    {
        return config;
    }

    public abstract IconMenu generateMenu();
}
