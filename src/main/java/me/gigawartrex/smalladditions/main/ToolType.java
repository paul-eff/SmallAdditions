package me.gigawartrex.smalladditions.main;

public enum ToolType
{
    PICKAXE("pickaxe"),
    SHOVEL("shovel"),
    SWORD("sword"),
    AXE("axe"),
    HOE("hoe");

    private final String materialTypeSubstring;

    ToolType(String materialTypeSubstring)
    {
        this.materialTypeSubstring = materialTypeSubstring;
    }

    public String getMaterialTypeSubstring()
    {
        return materialTypeSubstring;
    }
}
