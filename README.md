# SmallAdditions 1.2.10
<p align="center">
  <img src="https://img.shields.io/badge/version-1.2.10-blue">
  <img src="https://img.shields.io/badge/minecraft-1.19.3-green">
  <img src="https://img.shields.io/badge/java-17-yellow">
</p>

SmallAdditions is a Bukkit/Paper/Spigot server plugin which aims to improve vanilla gameplay just a smidge.
The features are designed to be unintrusive and can be deactivated and configured in the given YAML file.  
New ideas and issues can be directly submitted to this repository (e.g. via Issues).


### Active features:
- Vein Mining/Breaking - Breaking specific blocks, of same type, next to each other (Warning: Read more in Passive features - Broken tool refill)
  - Ore vein mining
  - Whole tree felling
    - Auto-replanting possible
  - Gravel vein digging
- Item magnet
- Right-clicking wheat harvests it
  - Auto-replanting possible
### Passive features:
- x% in world have to sleep for next day
- Broken tool refill
  - Ignores Silk Touch and Fortune tools
  - When veining (breaking all of the same type): Will use all tools in inventory, applicable for the material, until job done
- Empty ItemStack refill
- Broken Armor refill
- Deathboxes
- First time joined book
- Smelting recipes:
  - Raw ore block smelting - e.g. raw iron ore block + 1x coal = iron block
  - Budding amethyst - amethyst block + 16x coal block = budding amethyst
- Crafting recipes:
  - Wool recoloring - e.g. red wool + blue dye = blue wool
  - Light block - torch/glowstone/redstone lamp surrounded by glass = light block (with light level 5/10/15)
### Admin features:
- Inventory see of players
- Enderchest inventory see of players
- Ninjajoin (no "joined server" message when player joins server)
- Hide (player can't be seen anymore)
### Ideas and WIP:
- BedHandler should also handle thunder effectively
- More minor admin features
- New and better logger + log levels
- Rework config to support changes between versions dynamically (no resetall)

Documentation: https://paul-eff.github.io/SmallAdditions/  
Bukkit:&nbsp;https://www.spigotmc.org/wiki/buildtools/#compile-craftbukkit  
Paper:&nbsp;&nbsp;https://papermc.io/downloads
