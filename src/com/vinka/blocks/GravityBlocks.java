package com.vinka.blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.vinka.modules.PlayerModule;

public class GravityBlocks {
	public static void gravityPlacedBlocks(Block b, Player p) {
		switch (b.getType()) {
		case OAK_SLAB:
		case OAK_PLANKS:
		case STONE_SLAB:
		case STONE:
		case CHEST:
		case LADDER:
		case CRAFTING_TABLE:
		case OAK_FENCE:
		case OAK_FENCE_GATE:
		case REDSTONE_TORCH:
		case REDSTONE_WALL_TORCH:
		case WHITE_WOOL:
		case OAK_STAIRS:
			if (PlayerModule.inSurvival(p)) {
				b.getWorld().spawnFallingBlock(b.getLocation().add(0.5d, 0, 0.5d), b.getBlockData());
				b.setType(Material.AIR);
			}
			break;
		default:
			break;
		}
	}
}
