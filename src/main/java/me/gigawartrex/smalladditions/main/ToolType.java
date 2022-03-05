package me.gigawartrex.smalladditions.main;

public enum ToolType
{
    PICKAXE("pickaxe"),
    SHOVEL("shovel"),
    SWORD("sword"),
    AXE("axe"),
    HOE("hoe"),
    FLINT_AND_STEEL("flint_and_steel"),
    FISHING_ROD("fishing_rod"),
    SHEARS("shears");

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
