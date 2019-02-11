package com.vinka.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.valkutils.modules.PlayerModule;
import com.valkutils.modules.TextModule;
import com.vinka.Vinka;
import com.vinka.events.CustomPlayerDeathEvent;
import com.vinka.events.CustomPlayerRespawnEvent;
import com.vinka.utils.Utils;

public class CustomPlayerDeathEventListener implements Listener {
	@EventHandler
	private void customPlayerDeathEvent(CustomPlayerDeathEvent e) {
		Player p = e.getPlayer();
		
		p.getWorld().playSound(p.getLocation(), Sound.AMBIENT_CAVE, 1f, 1f);
		
		Vinka.vinka.getServer().broadcastMessage(TextModule.color("&f" + p.getDisplayName() + " &7died from " + e.getCause().name().toLowerCase() + " damage."));
		
		p.sendTitle("", "You Died", 20, 80, 0);
		
		resetStats(p);
		dropItemsOnDeath(p);
		deathAnimation(p);
		
		Utils.spawnMonsterPlayer(p.getLocation(), EntityType.HUSK, p);
	}
	
	private void resetStats(Player p) {
		PlayerModule.removeAllPotionEffects(p);
		PlayerModule.fillPlayerHealth(p);
		PlayerModule.fillPlayerFood(p);
		p.setLevel(0);
		p.setExp(0);
	}
	
	private void dropItemsOnDeath(Player p) {
		Inventory inv = p.getInventory();
		for (int i = 0; i < inv.getSize(); i++) {
			if (inv.getItem(i) == null) continue;
			if (inv.getItem(i).getType() == Material.BLACK_STAINED_GLASS_PANE) continue;
			p.getWorld().dropItemNaturally(p.getLocation(), inv.getItem(i));
			inv.setItem(i, new ItemStack(Material.AIR));
		}
	}
	
	private void deathAnimation(final Player p) {
		p.setGameMode(GameMode.SPECTATOR);
		p.setFlySpeed(0.0f);
		
		final int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Vinka.vinka, new Runnable() {
            @Override
            public void run() {
            	p.setVelocity(new Vector(0, 0.03, 0));
            }
        }, 0L, 1L);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				Bukkit.getScheduler().cancelTask(task);
				PlayerModule.addPotionEffect(p, PotionEffectType.BLINDNESS, 40, 1);
			}
		}.runTaskLater(Vinka.vinka, 80);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				if (p.getBedSpawnLocation() != null) {
					p.teleport(p.getBedSpawnLocation());
				} else {
					p.teleport(p.getWorld().getSpawnLocation());
				}
				p.setGameMode(GameMode.SURVIVAL);
				p.setFlySpeed(0.1f);
				
				p.getWorld().playSound(p.getLocation(), Sound.MUSIC_DISC_13, 1f, 1f);
				
				// Call the custom player respawn event since the vanilla respawn event is never called.
				Bukkit.getServer().getPluginManager().callEvent(new CustomPlayerRespawnEvent(p));
			}
		}.runTaskLater(Vinka.vinka, 100);
	}
}
