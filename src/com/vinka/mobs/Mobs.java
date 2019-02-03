package com.vinka.mobs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.valkutils.hologram.Hologram;
import com.valkutils.modules.MobModule;
import com.valkutils.modules.TextModule;
import com.vinka.Vinka;
import com.vinka.configs.PlayerFiles;
import com.vinka.items.VinkaItems;
import com.vinka.utils.Utils;

public class Mobs implements Listener {
	@EventHandler
	private void entityDamageEvent(EntityDamageByEntityEvent e) {
		if (!(e.getDamager() instanceof Player)) return;
		if (!(e.getEntity() instanceof Monster)) return;
		if (e.getDamage() <= 0) return;
		Location loc = e.getEntity().getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation();
		Hologram hg = new Hologram(loc.add(0.5d, 0, 0.5d), TextModule.color("&c" + (int) e.getDamage()));
		hg.setVisible(true);
		hg.move();
		new BukkitRunnable() {
			@Override
			public void run() {
				hg.destroy();
			}
		}.runTaskLater(Vinka.vinka, 60);
	}
	
	@EventHandler
	private void entityTargetEvent(EntityTargetLivingEntityEvent e) {
		if (e.getEntity() instanceof Monster) {
			if (e.getTarget() instanceof Player) {
				if (e.getReason() == TargetReason.CLOSEST_PLAYER) {
					ItemStack helmet = e.getTarget().getEquipment().getHelmet();
					if (helmet != null) {
						if (helmet.getType() == Material.CARVED_PUMPKIN) {
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	private void mobDeathEvent(EntityDeathEvent e) {
		if (e.getEntity() instanceof Monster) {
			Location loc = e.getEntity().getLocation();
			loc.getWorld().dropItemNaturally(loc, VinkaItems.SUGAR());
			loc.getWorld().playSound(loc, Sound.AMBIENT_CAVE, 1f, 1f);
			
			if (MobModule.countMobs(loc.getWorld(), EntityType.HUSK) < 40) {
				for (int i = 0; i < 2; i++) {
					Utils.spawnMonster(loc, EntityType.HUSK);
				}
			}
			
			if (e.getEntity().getKiller() instanceof Player) {
				PlayerFiles cm = PlayerFiles.getConfig(e.getEntity().getKiller());
				FileConfiguration config = cm.getConfig();
				if (!config.isSet("achievement_3")) {
					config.set("achievement_3", true);
					cm.saveConfig();
					Utils.achievement(e.getEntity().getKiller(), "Monster Slayer");
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void customMobs(CreatureSpawnEvent e) {
		if (e.getSpawnReason() == SpawnReason.NATURAL) {
			e.setCancelled(true);
		}
	}
}
