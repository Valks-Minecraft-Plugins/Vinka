package com.vinka.modules;

import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.vinka.items.VinkaItems;
import com.vinka.utils.Utils;

public class PlayerModule {
	@SuppressWarnings("deprecation")
	public static void updateToolDurability(Player p, ItemStack item) {
		if (PlayerModule.isTool(item.getType())) {
			item.setDurability((short) (item.getDurability() + 1));
			if (item.getDurability() + 1 > item.getType().getMaxDurability()) {
				ItemStack sticks = VinkaItems.STICK();
				sticks.setAmount(2);
				p.getEquipment().setItemInMainHand(sticks);
				p.sendMessage(Utils.color("&7Your tool snapped in two."));
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
			max = 1;
			break;
		case STONE_AXE:
		case STONE_HOE:
		case STONE_SHOVEL:
		case STONE_PICKAXE:
			max = 2;
			break;
		case IRON_AXE:
		case IRON_HOE:
		case IRON_SHOVEL:
		case IRON_PICKAXE:
			max = 3;
			break;
		case GOLDEN_AXE:
		case GOLDEN_HOE:
		case GOLDEN_SHOVEL:
		case GOLDEN_PICKAXE:
			max = 4;
			break;
		case DIAMOND_AXE:
		case DIAMOND_HOE:
		case DIAMOND_SHOVEL:
		case DIAMOND_PICKAXE:
			max = 5;
			break;
		default:
			break;
		}
		
		return new Random().nextInt(max - min + 1) + min;
	}
	
	public static void addPotionEffect(Player p, PotionEffectType type, int duration, int level) {
		if (!p.hasPotionEffect(type)) {
			p.addPotionEffect(new PotionEffect(type, duration, level));
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void fillPlayerHealth(Player p) {
		p.setHealth(p.getMaxHealth());
	}
	
	public static void fillPlayerFood(Player p) {
		p.setFoodLevel(20);
	}

	public static boolean inSurvival(Player p) {
		return p.getGameMode() == GameMode.SURVIVAL;
	}
	
	public static void removeAllPotionEffects(Player p) {
		for (PotionEffect effect : p.getActivePotionEffects()) {
			p.removePotionEffect(effect.getType());
		}
	}

	public static boolean isAxe(Material tool) {
		switch (tool) {
		case WOODEN_AXE:
		case STONE_AXE:
		case IRON_AXE:
		case GOLDEN_AXE:
		case DIAMOND_AXE:
			return true;
		default:
			return false;
		}
	}

	public static boolean isPickaxe(Material tool) {
		switch (tool) {
		case WOODEN_PICKAXE:
		case STONE_PICKAXE:
		case IRON_PICKAXE:
		case GOLDEN_PICKAXE:
		case DIAMOND_PICKAXE:
			return true;
		default:
			return false;
		}
	}

	public static boolean isHoe(Material tool) {
		switch (tool) {
		case WOODEN_HOE:
		case STONE_HOE:
		case IRON_HOE:
		case GOLDEN_HOE:
		case DIAMOND_HOE:
			return true;
		default:
			return false;
		}
	}

	public static boolean isShovel(Material tool) {
		switch (tool) {
		case WOODEN_SHOVEL:
		case STONE_SHOVEL:
		case IRON_SHOVEL:
		case GOLDEN_SHOVEL:
		case DIAMOND_SHOVEL:
			return true;
		default:
			return false;
		}
	}

	public static boolean isSword(Material tool) {
		switch (tool) {
		case WOODEN_SWORD:
		case STONE_SWORD:
		case IRON_SWORD:
		case GOLDEN_SWORD:
		case DIAMOND_SWORD:
			return true;
		default:
			return false;
		}
	}
	
	public static boolean isTool(Material tool) {
		return isPickaxe(tool) || isAxe(tool) || isHoe(tool) || isShovel(tool);
	}
	
	public static boolean isWeapon(Material tool) {
		return isSword(tool) || tool == Material.BOW;
	}
}
