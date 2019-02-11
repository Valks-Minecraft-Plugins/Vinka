package com.vinka.blocks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;

import com.valkutils.modules.BlockModule;
import com.valkutils.modules.PlayerModule;
import com.vinka.utils.Utils;

public class Blocks implements Listener {
	@EventHandler
	private void blockEvent(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (!PlayerModule.inSurvival(p))
			return;

		if (RestrictedBlocks.restrictedBlock(e)) {
			e.setCancelled(true);
			return;
		}
		
		if (e.getPlayer().getEquipment().getItemInMainHand().getType() == Material.DIAMOND_PICKAXE) {
			if (e.getPlayer().isSneaking())
				Utils.superPickaxe(e.getBlock().getLocation());
		}

		BlockDrops.blockDrops(e, Utils.toolGatherAmount(p));
		BlockModule.treeGravity(e);
		Utils.updateToolDurability(e.getBlock().getType(), p, p.getEquipment().getItemInMainHand());
		e.setExpToDrop(0);
		e.setDropItems(false);
	}

	@EventHandler
	private void blockPlaceEvent(BlockPlaceEvent e) {
		GravityBlocks.gravityPlacedBlocks(e.getBlock(), e.getPlayer());
	}

	@EventHandler
	private void leafDecay(LeavesDecayEvent e) {
		e.setCancelled(true);
		e.getBlock().setType(Material.AIR);
	}
}
