package com.vinka.modules;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.vinka.Vinka;

public class MobModule {
	public static void spawnMonster(Location loc, EntityType type) {
		LivingEntity monster = (LivingEntity) loc.getWorld().spawnEntity(loc, type);
		monster.setSilent(true);
		monster.getEquipment().setHelmet(new ItemStack(Material.BLACK_WOOL));
		new BukkitRunnable() {
			@Override
			public void run() {
				monster.remove();
			}
		}.runTaskLater(Vinka.vinka, 1200);
	}
	
	public static boolean validSpawningLocation(Location testLoc) {
		if (testLoc.getBlock().getType() == Material.AIR
				&& testLoc.getBlock().getRelative(BlockFace.UP).getType() == Material.AIR) {
			if (testLoc.getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
				return true;
			}
		}
		return false;
	}
	
	public static Location[] testLocations(Location loc, int radius) {
		World w = loc.getWorld();
		return new Location[]  { new Location(w, loc.getX() + radius, loc.getY(), loc.getZ()),
				new Location(w, loc.getX() - radius, loc.getY(), loc.getZ()),
				new Location(w, loc.getX(), loc.getY(), loc.getZ() + radius),
				new Location(w, loc.getX(), loc.getY(), loc.getZ() - radius) };
	}
}
