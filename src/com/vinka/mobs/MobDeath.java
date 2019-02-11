package com.vinka.mobs;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Husk;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import com.valkutils.modules.MobModule;
import com.valkutils.modules.WorldModule;
import com.vinka.utils.Utils;
import com.vinkaitems.VinkaItems;

public class MobDeath implements Listener {
	@EventHandler
	private void entitySplitEvent(SlimeSplitEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	private void entityDeathEvent(EntityDeathEvent e) {
		Entity entity = e.getEntity();
		if (entity instanceof Husk) {
			Location loc = entity.getLocation();
			World w = loc.getWorld();
			
			w.dropItemNaturally(loc, VinkaItems.SUGAR());
			w.playSound(loc, Sound.AMBIENT_CAVE, 1f, 1f);
			
			Utils.spawnParticles(loc, Particle.EXPLOSION_LARGE, 1);
			Utils.spawnParticles(loc, Particle.PORTAL, 10);
			Utils.spawnParticles(loc, Particle.DRAGON_BREATH, 50);
			
			splitMonster(loc, entity, 2);
			Utils.spawnCorpse(loc, (LivingEntity) entity);
		}
	}
	
	private void splitMonster(Location loc, Entity e, int amount) {
		if (WorldModule.day()) return; // Only split during the night.
		if (MobModule.countMobsInWorld(loc.getWorld(), EntityType.HUSK) >= 20) return; // Only split if there are less than this many mobs in the world.
		if (MobModule.countMobsInChunk(loc.getChunk().getEntities(), EntityType.HUSK) > 1) return; // Only split if there are no more than 1 mobs in that chunk.
		if (e.getFireTicks() > 0) return; // Only split if not on fire.
		
		for (int i = 0; i < amount; i++) { // Split into this many.
			Utils.spawnMonster(loc, EntityType.HUSK, null);
		}
	}
}
