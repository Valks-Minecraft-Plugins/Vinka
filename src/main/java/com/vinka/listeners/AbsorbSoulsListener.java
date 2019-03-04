package com.vinka.listeners;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.valkcore.modules.TextModule;
import com.vinka.Vinka;
import com.vinka.events.AbsorbSoulsEvent;
import com.vinka.utils.Utils;

public class AbsorbSoulsListener implements Listener {
	@EventHandler
	private void absorbSoulsEvent(AbsorbSoulsEvent e) {
		final Player p = e.getPlayer();
		int souls = e.getSouls();
		
		Utils.updateHealth(p);
		
		p.giveExp(souls);
		p.sendMessage(TextModule.color("You absorbed &q" + souls + " &wsouls."));
		p.getWorld().playSound(p.getLocation(), Sound.AMBIENT_CAVE, 1.0f, 1.0f);
		
		e.getSoulItem().setAmount(0);
		
		final BukkitTask task = new BukkitRunnable() {
			@Override
			public void run() {
				p.getWorld().spawnParticle(Particle.PORTAL, p.getLocation(), 10);
			}
		}.runTaskTimer(Vinka.vinka, 0, 5);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				task.cancel();
			}
		}.runTaskLater(Vinka.vinka, 100);
	}
}
