package com.vinka.blocks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import com.vinka.modules.BlockModule;
import com.vinka.modules.PlayerModule;

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

		BlockDrops.blockDrops(e, PlayerModule.toolGatherAmount(p));
		BlockModule.treeGravity(e);
		PlayerModule.updateToolDurability(p, p.getEquipment().getItemInMainHand());
		e.setExpToDrop(0);
		e.setDropItems(false);
	}

	@EventHandler
	private void blockPlaceEvent(BlockPlaceEvent e) {
		GravityBlocks.gravityPlacedBlocks(e.getBlock(), e.getPlayer());
	}

	@EventHandler
	private void leafDecay(LeavesDecayEvent e) {
		e.getBlock().getDrops().clear();
	}
}
