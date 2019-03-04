package com.vinka.blocks;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.valkcore.modules.PlayerModule;

public class GravityBlocks {
	private static boolean isGravityBlock(ItemStack item) {
		ItemMeta im = item.getItemMeta();
		List<String> lore = im.getLore();
		if (lore != null) {
			for (String element : lore) {
				if (element.contains("Gravity")) {
					boolean gravity = Boolean.parseBoolean(element.substring("&7Gravity: &f".length()));
					return gravity;
				}
			}
		}
		return false;
	}
	
	public static void gravityPlacedBlocks(ItemStack item, Block b, Player p) {
		if (isGravityBlock(item)) {
			if (PlayerModule.inSurvival(p)) {
				if (b.getRelative(BlockFace.DOWN).getType() == Material.AIR) {
					b.getWorld().spawnFallingBlock(b.getLocation().add(0.5d, 0, 0.5d), b.getBlockData());
					b.setType(Material.AIR);
				}
			}
		}
	}
}
