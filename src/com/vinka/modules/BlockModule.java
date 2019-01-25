package com.vinka.modules;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockModule {
	public static Block[] getAdjacentBlocks(Block center) {
		return new Block[] { center.getRelative(BlockFace.DOWN), center.getRelative(BlockFace.UP),
				center.getRelative(BlockFace.WEST), center.getRelative(BlockFace.EAST),
				center.getRelative(BlockFace.NORTH), center.getRelative(BlockFace.SOUTH) };
	}
	
	public static void superBlockGravity(BlockBreakEvent e) {
		for (Block b : BlockModule.getAdjacentBlocks(e.getBlock())) {
			if (b.getRelative(BlockFace.DOWN).getType() == Material.AIR) {
				b.getWorld().spawnFallingBlock(b.getLocation().add(0.5d, 0, 0.5d), b.getBlockData());
				b.setType(Material.AIR);
			}
		}
	}
	
	public static boolean isLeaves(Material type) {
		switch (type) {
		case ACACIA_LEAVES:
		case BIRCH_LEAVES:
		case DARK_OAK_LEAVES:
		case JUNGLE_LEAVES:
		case OAK_LEAVES:
		case SPRUCE_LEAVES:
			return true;
		default:
			return false;
		}
	}

	public static boolean isFarmable(Material type) {
		switch (type) {
		case WHEAT:
		case POTATOES:
		case BEETROOTS:
		case NETHER_WART:
		case CARROTS:
			return true;
		default:
			return false;
		}
	}

	public static boolean isLog(Material type) {
		switch (type) {
		case ACACIA_LOG:
		case BIRCH_LOG:
		case DARK_OAK_LOG:
		case JUNGLE_LOG:
		case OAK_LOG:
		case SPRUCE_LOG:
			return true;
		default:
			return false;
		}
	}

	public static boolean isOre(Material type) {
		switch (type) {
		case COAL_ORE:
		case DIAMOND_ORE:
		case EMERALD_ORE:
		case GOLD_ORE:
		case IRON_ORE:
		case LAPIS_ORE:
		case REDSTONE_ORE:
			return true;
		default:
			return false;
		}
	}

	public static boolean isSoftBlock(Material type) {
		switch (type) {
		case SAND:
		case GRAVEL:
		case DIRT:
		case COARSE_DIRT:
		case SOUL_SAND:
		case RED_SAND:
		case GRASS_BLOCK:
		case GRASS_PATH:
		case PODZOL:
		case FARMLAND:
		case CLAY:
			return true;
		default:
			return false;
		}
	}

	public static boolean isPlant(Material type) {
		switch (type) {
		case GRASS:
		case TALL_GRASS:
		case SUNFLOWER:
		case FERN:
		case LARGE_FERN:
		case SEAGRASS:
		case DANDELION:
		case DANDELION_YELLOW:
		case POPPY:
		case BLUE_ORCHID:
		case ALLIUM:
		case AZURE_BLUET:
		case RED_TULIP:
		case ORANGE_TULIP:
		case WHITE_TULIP:
		case PINK_TULIP:
		case OXEYE_DAISY:
		case ROSE_BUSH:
		case PEONY:
			return true;
		default:
			return false;
		}
	}

	public static void treeGravity(BlockBreakEvent e) {
		Block b = e.getBlock();

		if (isLog(b.getType())) {
			Location loc = b.getLocation();
			loc.add(0.5d, 0, 0.5d);
			for (int y = 0; y < 5; y++) {
				loc.setY(loc.getY() + 1);
				if (isLog(loc.getBlock().getType())) {
					loc.getBlock().setType(Material.AIR);
					loc.getWorld().spawnFallingBlock(loc, b.getBlockData());
				}
			}
		}
	}
}
