package com.vinka.effects;

import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.vinka.utils.Utils;

public class Effects implements Listener {
	@EventHandler
	private void blockDamageEvent(BlockDamageEvent e) {
		Utils.spawnParticles(e.getBlock().getLocation(), Particle.PORTAL, 10);
	}
	
	@EventHandler
	private void smeltParticles(FurnaceSmeltEvent e) {
		Utils.spawnParticles(e.getBlock().getLocation(), Particle.LAVA, 1);
	}
	
	@EventHandler
	private void chatParticles(AsyncPlayerChatEvent e) {
		Utils.spawnParticles(e.getPlayer().getLocation(), Particle.ENCHANTMENT_TABLE, 5);
	}
	
	@EventHandler
	private void playerCraftEvent(CraftItemEvent e) {
		Utils.spawnParticles(e.getWhoClicked().getLocation(), Particle.DRAGON_BREATH, 10);
	}
}
