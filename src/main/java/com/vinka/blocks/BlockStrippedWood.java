package com.vinka.blocks;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;

import com.valkcore.modules.BlockModule;
import com.valkcore.modules.PlayerModule;

public class BlockStrippedWood implements Listener {
	@EventHandler
	private void strippedWood(PlayerInteractEvent e) {
		EntityEquipment equip = e.getPlayer().getEquipment();
		
		if (equip.getItemInMainHand() == null) return;
		if (!PlayerModule.isAxe(equip.getItemInMainHand().getType())) return;
		if (e.getClickedBlock() == null) return;
		if (!BlockModule.isLog(e.getClickedBlock().getType())) return;
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		
		e.setCancelled(true);
	}
}
