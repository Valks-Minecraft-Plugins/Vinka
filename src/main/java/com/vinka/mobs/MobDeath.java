package com.vinka.mobs;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.inventory.ItemStack;

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
		Location loc = entity.getLocation();
		World w = loc.getWorld();

		w.playSound(loc, Sound.AMBIENT_CAVE, 1f, 1f);

		Utils.spawnParticles(loc, Particle.EXPLOSION_LARGE, 1);
		Utils.spawnParticles(loc, Particle.PORTAL, 10);
		Utils.spawnParticles(loc, Particle.DRAGON_BREATH, 50);

		// Split on death.
		switch (e.getEntityType()) {
		case ZOMBIE:
		case STRAY:
		case HUSK:
			splitMonster(loc, entity, entity.getType(), 2);
			break;
		case SPIDER:
			splitMonster(loc, entity, EntityType.CAVE_SPIDER, 2);
			break;
		default:
			break;
		}

		// Spawn a corpse on death.
		switch (e.getEntityType()) {
		case ZOMBIE:
		case STRAY:
		case HUSK:
			Utils.spawnCorpse(loc, (LivingEntity) entity, false);
			break;
		default:
			Utils.spawnCorpse(loc, (LivingEntity) entity, true);
			break;
		}
		
		// Sugar on Death
		ItemStack sugar = VinkaItems.SUGAR();
		switch (e.getEntityType()) {
		case SLIME:
		case ZOMBIE:
		case STRAY:
			sugar.setAmount(1);
			break;
		case HUSK:
		case WOLF:
		case CAVE_SPIDER:
		case SPIDER:
			sugar.setAmount(2);
			break;
		case PHANTOM:
		case VEX:
			sugar.setAmount(3);
			break;
		case WITHER_SKELETON:
			sugar.setAmount(4);
			break;
		case WITHER:
			sugar.setAmount(25);
			break;
		default:
			break;
		}
		
		w.dropItemNaturally(loc, sugar);
	}

	private void splitMonster(Location loc, Entity e, EntityType splitType, int amount) {
		if (WorldModule.day())
			return; // Only split during the night.
		if (MobModule.countMobsInWorld(loc.getWorld(), e.getType()) >= 20)
			return; // Only split if there are less than this many mobs in the world.
		if (MobModule.countMobsInChunk(loc.getChunk().getEntities(), e.getType()) > 1)
			return; // Only split if there are no more than 1 mobs in that chunk.
		if (e.getFireTicks() > 0)
			return; // Only split if not on fire.

		for (int i = 0; i < amount; i++) { // Split into this many.
			Utils.spawnMonster(loc, splitType);
		}
	}
}
