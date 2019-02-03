package com.vinka.utils;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Husk;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.valkutils.modules.BlockModule;
import com.valkutils.modules.PlayerModule;
import com.valkutils.modules.TextModule;
import com.vinka.Vinka;
import com.vinka.items.VinkaItems;

public class Utils {
	public static void achievement(Player p, String message) {
		p.sendTitle("", message, 20, 60, 20);
		Vinka.vinka.getServer().broadcastMessage(TextModule.color("&f" + p.getPlayer().getName() + " &7just achieved &f" + message + "&7!"));
	}
	
	/*
	 * Only update health when xp level is an even number
	 * so player will not get half hearts on level up.
	 */
	@SuppressWarnings("deprecation")
	public static void updateHealth(Player p) {
		if (p.getLevel() % 2 == 0) {
			//p.setHealth(p.getMaxHealth());
			p.setMaxHealth(6 + p.getLevel());
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void updateToolDurability(Material blocktype, Player p, ItemStack item) {
		if (PlayerModule.isTool(item.getType())) {
			if (BlockModule.isPlant(blocktype)) return;
			item.setDurability((short) (item.getDurability() + 1));
			if (item.getDurability() + 1 > item.getType().getMaxDurability()) {
				ItemStack sticks = VinkaItems.STICK();
				sticks.setAmount(2);
				p.getEquipment().setItemInMainHand(sticks);
				p.sendMessage(TextModule.color("&7Your tool snapped in two."));
			}
		}
	}
	
	public static int toolGatherAmount(Player p) {
		int max = 1;
		int min = 1;
		
		switch (p.getEquipment().getItemInMainHand().getType()) {
		case WOODEN_AXE:
		case WOODEN_HOE:
		case WOODEN_SHOVEL:
		case WOODEN_PICKAXE:
			max = 3;
			break;
		case STONE_AXE:
		case STONE_HOE:
		case STONE_SHOVEL:
		case STONE_PICKAXE:
			max = 4;
			break;
		case IRON_AXE:
		case IRON_HOE:
		case IRON_SHOVEL:
		case IRON_PICKAXE:
			max = 5;
			break;
		case GOLDEN_AXE:
		case GOLDEN_HOE:
		case GOLDEN_SHOVEL:
		case GOLDEN_PICKAXE:
			max = 6;
			break;
		case DIAMOND_AXE:
		case DIAMOND_HOE:
		case DIAMOND_SHOVEL:
		case DIAMOND_PICKAXE:
			max = 7;
			break;
		default:
			break;
		}
		
		return new Random().nextInt(max - min + 1) + min;
	}
	
	public static void spawnMonster(Location loc, EntityType type) {
		LivingEntity monster = (LivingEntity) loc.getWorld().spawnEntity(loc, type);
		monster.setSilent(true);
		monster.getEquipment().setHelmet(new ItemStack(Material.BLACK_WOOL));
		monster.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 1));
		if (monster instanceof Husk) {
			((Husk) monster).setBaby(false);
		}
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
