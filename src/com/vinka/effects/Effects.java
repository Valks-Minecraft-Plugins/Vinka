package com.vinka.effects;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;

public class Effects implements Listener {
	@EventHandler
	private void blockDamageEvent(BlockDamageEvent e) {
		Block b = e.getBlock();
		Location loc = b.getLocation();
		b.getWorld().spawnParticle(Particle.PORTAL, loc, 10);
	}
}
