package com.vinka.mobs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.inventory.ItemStack;
import com.vinkaitems.VinkaItems;

public class Mobs implements Listener {
	@EventHandler
	private void entityDropItemEvent(EntityDropItemEvent e) {
		if (e.getEntityType() != EntityType.CHICKEN)
			return;
		e.setCancelled(true);
		Location loc = e.getEntity().getLocation();
		loc.getWorld().dropItemNaturally(loc, VinkaItems.SUGAR());
	}

	@EventHandler
	private void entityTargetEvent(EntityTargetLivingEntityEvent e) {
		if (e.getEntity() instanceof Monster) {
			if (e.getTarget() instanceof Player) {
				if (e.getReason() == TargetReason.CLOSEST_PLAYER) {
					if (!e.getTarget().isInvulnerable()) {
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
	}
}
