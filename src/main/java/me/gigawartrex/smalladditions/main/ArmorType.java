package me.gigawartrex.smalladditions.main;

public enum ArmorType
{
    HELMET("helmet"),
    CHESTPLATE("chestplate"),
    LEGGINGS("leggings"),
    BOOTS("boots");

    private final String materialTypeSubstring;

    ArmorType(String materialTypeSubstring)
    {
        this.materialTypeSubstring = materialTypeSubstring;
    }

    public String getMaterialTypeSubstring()
    {
        return materialTypeSubstring;
    }
}
