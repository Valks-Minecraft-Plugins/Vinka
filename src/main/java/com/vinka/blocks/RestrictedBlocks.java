package com.vinka.blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import com.valkcore.modules.BlockModule;
import com.valkcore.modules.PlayerModule;
import com.valkcore.modules.TextModule;

public class RestrictedBlocks {
	public static boolean restrictedBlock(BlockBreakEvent e) {
		Player p = e.getPlayer();

		// Restricted blocks should only effect survival players.
		if (!PlayerModule.inSurvival(p))
			return false;

		Block b = e.getBlock();
		Material blocktype = b.getType();

		Material tool = p.getEquipment().getItemInMainHand().getType();
		
		// Players need a pickaxe to mine hard blocks.
		if (BlockModule.isOre(blocktype)) {
			if (!PlayerModule.isPickaxe(tool)) {
				p.sendMessage(TextModule.color(
						"You need a &qpickaxe &wto mine " + e.getBlock().getType().name().toLowerCase() + "."));
				return true;
			}
		}

		// Players need a shovel to mine soft blocks.
		if (BlockModule.isSoftBlock(blocktype)) {
			if (!PlayerModule.isShovel(tool)) {
				p.sendMessage(TextModule.color(
						"You need a &qshovel &wto excavate " + e.getBlock().getType().name().toLowerCase() + "."));
				return true;
			}
		}
		
		// Restrict tools based on y level.
		//if (yLevelRestrictedTools(p, b)) return true;
		
		// UNUSED
		/*if (b.getType() == Material.DIAMOND_ORE) {
			if (tool != Material.GOLDEN_PICKAXE || tool != Material.DIAMOND_PICKAXE) {
				p.sendMessage("&7You need a stronger pickaxe to mine that.");
				return true;
			}
		}*/

		return false;
	}
	
	@SuppressWarnings("unused")
	private static boolean yLevelRestrictedTools(Player p, Block b) {
		switch (p.getEquipment().getItemInMainHand().getType()) {
		case WOODEN_PICKAXE:
			if (yLevelRestriction(p, b, 40)) return true;
		case STONE_PICKAXE:
			if (yLevelRestriction(p, b, 30)) return true;
		case IRON_PICKAXE:
			if (yLevelRestriction(p, b, 20)) return true;
		case GOLDEN_PICKAXE:
			if (yLevelRestriction(p, b, 10)) return true;
		default:
			return false;
		}
	}
	
	private static boolean yLevelRestriction(Player p, Block b, int yLevel) {
		if (b.getY() <= yLevel) {
			p.sendMessage(TextModule.color("You need a stronger pickaxe to mine deeper."));
			return true;
		}
		return false;
	}
}
