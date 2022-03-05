# SmallAdditions 1.2.5
<p align="center">
  <img src="https://img.shields.io/badge/version-1.2.5-blue">
  <img src="https://img.shields.io/badge/bukkit-1.18.1-green">
  <img src="https://img.shields.io/badge/paper-1.18.1-green">
</p>

SmallAdditions is a Bukkit/Paper server plugin which aims to improve vanilla gameplay just a smidge.
The features are designed to be unintrusive and can be deactivated and configured in the given YAML file.  
New ideas and issues can be be directly submitted to this repository.


### Active features:
- Vein Mining/Breaking - Breaking specific blocks, of same type, next to each other (Warning: Read more in Passive features - Broken tool refill)
  - Ore vein mining
  - Whole tree felling
    - Auto-replanting possible
  - Gravel vein digging
- Item magnet
- Right clicking wheat harvests it
  - Auto-replanting possible
### Passive features:
- x% in world have to sleep for next day
- Broken tool refill
  - Ignores Silk Touch and Fortune tools
  - When veining (breaking all of same type): Will use all tools in inventory, applicable for the material, until job done
- Empty ItemStack refill
- Broken Armor refill
- Deathboxes
- First time joined book
- New crafting / smelting recipes:
  - Raw ore block to smelted ore block - (Raw iron ore block + 8x Coal = Iron block)
  - Colored wool + dye = recoloring - (Red wool + Blue dye = Blue wool)
  - Budding amethyst - (Amethyst block + 16x Coal block = Budding amethyst)
### Admin features:
- Inventory see of players
- Enderchest inventory see of players
- Ninjajoin (no "joined server" message when player joins server)
- Hide (player can't be seen anymore)
### Ideas and WIP:
- BedHandler should also handle thunder effectively
- More minor admin features
- New and better logger + log levels

Documentation: https://paul-eff.github.io/SmallAdditions/  
Bukkit:&nbsp;https://www.spigotmc.org/wiki/buildtools/#compile-craftbukkit  
Paper:&nbsp;&nbsp;https://papermc.io/downloads
