package com.vinka.blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

public class BlockGrow implements Listener {
	@EventHandler
	private void blockGrowEvent(BlockGrowEvent e) {
		Material cropType = e.getBlock().getType();
		
		if (cropType == Material.MELON_STEM || cropType == Material.PUMPKIN_STEM)
			return;
		
		Block bottom = e.getBlock().getRelative(BlockFace.DOWN);
		Block[] adjacentBlocks = { bottom.getRelative(BlockFace.NORTH), bottom.getRelative(BlockFace.SOUTH),
				bottom.getRelative(BlockFace.WEST), bottom.getRelative(BlockFace.EAST) };

		for (Block b : adjacentBlocks) {
			if (b.getType() != Material.GRASS_BLOCK && b.getType() != Material.DIRT) continue;
			if (b.getRelative(BlockFace.UP).getType() != Material.AIR) continue;
			if (Math.random() > 0.33) continue;
			b.setType(Material.FARMLAND);
			b.getRelative(BlockFace.UP).setType(cropType);
		}
	}
}
