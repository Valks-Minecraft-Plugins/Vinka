package com.vinka.utils;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Husk;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import com.valkutils.modules.BlockModule;
import com.valkutils.modules.PlayerModule;
import com.valkutils.modules.TextModule;
import com.vinka.Vinka;
import com.vinkaitems.VinkaItems;

public class Utils {
	public static void spawnParticles(Location loc, Particle type, int amount) {
		loc.getWorld().spawnParticle(type, loc, amount);
	}
	
	public static void superPickaxe(Location loc) {
		loc.setY(loc.getY() + 1);
		loc.setX(loc.getX() - 1);
		loc.setZ(loc.getZ() + 1);
		for (int z = 0; z < 3; z++) {
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 3; x++) {
					if (loc.getBlock().getType().equals(Material.STONE)) {
						loc.getBlock().setType(Material.AIR);
					}
					loc.setX(loc.getX() + 1);
				}
				loc.setX(loc.getX() - 3);
				loc.setY(loc.getY() - 1);
			}
			loc.setY(loc.getY() + 3);
			loc.setZ(loc.getZ() - 1);
		}
	}
	
	/*
	 * Only update health when xp level is an even number
	 * so player will not get half hearts on level up.
	 */
	@SuppressWarnings("deprecation")
	public static void updateHealth(Player p) {
		//if (p.getLevel() % 2 == 0)
		p.setMaxHealth(6 + p.getLevel());
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
	
	public static void spawnMonster(Location loc, EntityType type, Player p) {
		LivingEntity monster = (LivingEntity) loc.getWorld().spawnEntity(loc, type);
		if (p != null) {
			ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta meta = (SkullMeta) skull.getItemMeta();
	        meta.setOwningPlayer(p);
	        skull.setItemMeta(meta);
	        
	        EntityEquipment monsterEquip = monster.getEquipment();
			EntityEquipment playerEquip = p.getEquipment();
			
			monster.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 3));
			
			monster.getEquipment().setHelmet(skull);
			
			if (playerEquip.getChestplate() != null) monsterEquip.setChestplate(playerEquip.getChestplate());
			if (playerEquip.getLeggings() != null) monsterEquip.setLeggings(playerEquip.getLeggings());
			if (playerEquip.getBoots() != null) monsterEquip.setBoots(playerEquip.getBoots());
			if (playerEquip.getItemInMainHand() != null) monsterEquip.setItemInMainHand(playerEquip.getItemInMainHand());
			if (playerEquip.getItemInOffHand() != null) monsterEquip.setItemInOffHand(playerEquip.getItemInOffHand());
		} else {
			monster.getEquipment().setHelmet(new ItemStack(Material.BLACK_WOOL));
			monster.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 1));
		}
		
		if (monster instanceof Husk) {
			((Husk) monster).setBaby(false);
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				monster.remove();
			}
		}.runTaskLater(Vinka.vinka, 600);
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
	
	public static void spawnCorpse(Location loc, LivingEntity entity) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Location newLoc = new Location(loc.getWorld(), loc.getX(), loc.getY() - 1.5, loc.getZ());
				ArmorStand as = (ArmorStand) loc.getWorld().spawn(newLoc, ArmorStand.class);
				as.setVisible(false);
				as.setInvulnerable(true);
				as.setGravity(false);
				as.setAI(false);
				as.setCollidable(false);
				as.setBasePlate(false);
				as.setArms(false);
				as.setMetadata("Type", new FixedMetadataValue(Vinka.vinka, "Lootable"));
				
				int rotation = new Random().nextInt(360);
				
				as.setBodyPose(new EulerAngle(Math.toRadians(-90), rotation + Math.toRadians(10), Math.toRadians(9)));
				as.setHeadPose(new EulerAngle(Math.toRadians(-90), rotation + Math.toRadians(-10), Math.toRadians(30)));
				as.setLeftArmPose(new EulerAngle(Math.toRadians(-90), rotation, 0));
				as.setRightArmPose(new EulerAngle(Math.toRadians(-90), rotation, 0));
				
				ItemStack leather_plate = new ItemStack(Material.LEATHER_CHESTPLATE);
				LeatherArmorMeta im = (LeatherArmorMeta) leather_plate.getItemMeta();
				im.setColor(Color.BLACK);
				leather_plate.setItemMeta(im);
				
				as.setChestplate(leather_plate);
				if (entity.getEquipment().getHelmet().getType() == Material.BLACK_WOOL) {
					as.setHelmet(new ItemStack(Material.ZOMBIE_HEAD));
				} else {
					as.setHelmet(entity.getEquipment().getHelmet());
				}
			}
		}.runTaskLater(Vinka.vinka, 20); // Spawn in a second later or will not spawn in at all.
	}
}
