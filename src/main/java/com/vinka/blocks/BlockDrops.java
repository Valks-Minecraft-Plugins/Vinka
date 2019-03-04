package com.vinka.blocks;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.valkcore.hologram.Hologram;
import com.valkcore.modules.BlockModule;
import com.valkcore.modules.TextModule;
import com.vinka.Vinka;
import com.vinkaitems.VinkaItem;
import com.vinkaitems.VinkaItems;

public class BlockDrops {
	private static void respawnableBlock(Player p, final Location loc, int amount, final VinkaItem respawnableBlock,
			VinkaItem drop) {
		if (p.isSneaking()) {
			dropItem(loc, respawnableBlock);
		} else {
			drop.getItem().setAmount(amount);
			dropItem(loc, drop);
			new BukkitRunnable() {
				public void run() {
					loc.getBlock().setType(Material.OBSIDIAN);
				}
			}.runTaskLater(Vinka.vinka, 5);
			
			Location hgLoc = new Location(loc.getWorld(), loc.getX() + 0.5, loc.getY() - 0.5, loc.getZ() + 0.5);
			final Hologram hg = new Hologram(hgLoc, "");
			
			final BukkitTask id = new BukkitRunnable() {
				int counter = 15;
				
				public void run() {
					if (!hg.getArmorStand().isVisible())
						hg.setVisible(true);
					counter--;
					hg.updateName("&c" + counter);
				}
			}.runTaskTimer(Vinka.vinka, 20, 20);

			new BukkitRunnable() {
				public void run() {
					Bukkit.getScheduler().cancelTask(id.getTaskId());
					hg.destroy();
					loc.getBlock().setType(respawnableBlock.getItem().getType());
				}
			}.runTaskLater(Vinka.vinka, 20 * 15);
		}
	}

	private static void dropItem(Location loc, VinkaItem item) {
		loc.getWorld().dropItemNaturally(loc, item.getItem());
	}

	public static void blockDrops(final BlockBreakEvent e, int amount) {
		Location loc = e.getBlock().getLocation();
		World w = loc.getWorld();
		Player p = e.getPlayer();

		switch (e.getBlock().getType()) {
		case SHULKER_BOX:
			dropItem(loc, VinkaItems.SHULKER_BOX());
			break;
		case BLACK_SHULKER_BOX:
			dropItem(loc, VinkaItems.BLACK_SHULKER_BOX());
			break;
		case BLUE_SHULKER_BOX:
			dropItem(loc, VinkaItems.BLUE_SHULKER_BOX());
			break;
		case BROWN_SHULKER_BOX:
			dropItem(loc, VinkaItems.BROWN_SHULKER_BOX());
			break;
		case CYAN_SHULKER_BOX:
			dropItem(loc, VinkaItems.CYAN_SHULKER_BOX());
			break;
		case GRAY_SHULKER_BOX:
			dropItem(loc, VinkaItems.GRAY_SHULKER_BOX());
			break;
		case GREEN_SHULKER_BOX:
			dropItem(loc, VinkaItems.GREEN_SHULKER_BOX());
			break;
		case LIGHT_BLUE_SHULKER_BOX:
			dropItem(loc, VinkaItems.LIGHT_BLUE_SHULKER_BOX());
			break;
		case LIGHT_GRAY_SHULKER_BOX:
			dropItem(loc, VinkaItems.LIGHT_GRAY_SHULKER_BOX());
			break;
		case LIME_SHULKER_BOX:
			dropItem(loc, VinkaItems.LIME_SHULKER_BOX());
			break;
		case MAGENTA_SHULKER_BOX:
			dropItem(loc, VinkaItems.MAGENTA_SHULKER_BOX());
			break;
		case YELLOW_SHULKER_BOX:
			dropItem(loc, VinkaItems.YELLOW_SHULKER_BOX());
			break;
		case WHITE_SHULKER_BOX:
			dropItem(loc, VinkaItems.WHITE_SHULKER_BOX());
			break;
		case RED_SHULKER_BOX:
			dropItem(loc, VinkaItems.RED_SHULKER_BOX());
			break;
		case PURPLE_SHULKER_BOX:
			dropItem(loc, VinkaItems.PURPLE_SHULKER_BOX());
			break;
		case PINK_SHULKER_BOX:
			dropItem(loc, VinkaItems.PINK_SHULKER_BOX());
			break;
		case ORANGE_SHULKER_BOX:
			dropItem(loc, VinkaItems.ORANGE_SHULKER_BOX());
			break;
		case TORCH:
			dropItem(loc, VinkaItems.TORCH());
			break;
		case REDSTONE_TORCH:
			dropItem(loc, VinkaItems.REDSTONE_TORCH());
			break;
		case YELLOW_GLAZED_TERRACOTTA:
			respawnableBlock(p, loc, amount, VinkaItems.YELLOW_GLAZED_TERRACOTTA(), VinkaItems.ENDER_PEARL());
			break;
		case LIGHT_BLUE_GLAZED_TERRACOTTA:
			respawnableBlock(p, loc, amount, VinkaItems.LIGHT_BLUE_GLAZED_TERRACOTTA(), VinkaItems.LIGHT_BLUE_DYE());
			break;
		case MAGENTA_GLAZED_TERRACOTTA:
			respawnableBlock(p, loc, amount, VinkaItems.MAGENTA_GLAZED_TERRACOTTA(), VinkaItems.DANDELION_YELLOW());
			break;
		case ORANGE_GLAZED_TERRACOTTA:
			respawnableBlock(p, loc, amount, VinkaItems.ORANGE_GLAZED_TERRACOTTA(), VinkaItems.LIGHT_GRAY_DYE());
			break;
		case WHITE_GLAZED_TERRACOTTA:
			respawnableBlock(p, loc, amount, VinkaItems.WHITE_GLAZED_TERRACOTTA(), VinkaItems.COAL());
			break;
		case COAL_BLOCK:
			ItemStack coal_block_coal = VinkaItems.COAL().getItem();
			coal_block_coal.setAmount(9);
			w.dropItemNaturally(loc, coal_block_coal);
			break;
		case SUGAR_CANE:
			dropItem(loc, VinkaItems.BONE_MEAL());
			break;
		case SAND:
			if (Math.random() < 0.01) {
				dropItem(loc, VinkaItems.COAL());
			}
			break;
		case NETHER_QUARTZ_ORE:
			VinkaItem[] loot2 = { VinkaItems.SUGAR(), VinkaItems.LIGHT_GRAY_DYE(), VinkaItems.DANDELION_YELLOW(),
					VinkaItems.LIME_DYE(), VinkaItems.LIGHT_BLUE_DYE() };
			dropItem(loc, loot2[new Random().nextInt(loot2.length)]);
			break;
		case IRON_ORE:
			dropItem(loc, VinkaItems.LIGHT_GRAY_DYE());
			break;
		case DEAD_BUSH:
			VinkaItem[] loot = { VinkaItems.SUGAR(), VinkaItems.LIGHT_GRAY_DYE(), VinkaItems.STICK(),
					VinkaItems.OAK_SLAB(), VinkaItems.OAK_PLANKS() };
			dropItem(loc, loot[new Random().nextInt(loot.length)]);
			break;
		case SOUL_SAND:
			ItemStack sugar = VinkaItems.SUGAR().getItem();
			sugar.setAmount(9);
			w.dropItemNaturally(loc, sugar);
			break;
		case REDSTONE_ORE:
			ItemStack redstone_ore = VinkaItems.ROSE_RED().getItem();
			redstone_ore.setAmount(amount);
			w.dropItemNaturally(loc, redstone_ore);
			break;
		case ANDESITE:
			ItemStack cyan_ore = VinkaItems.CYAN_DYE().getItem();
			cyan_ore.setAmount(amount);
			w.dropItemNaturally(loc, cyan_ore);
			break;
		case DIORITE:
			ItemStack diorite_ore = VinkaItems.LIGHT_GRAY_DYE().getItem();
			diorite_ore.setAmount(amount);
			w.dropItemNaturally(loc, diorite_ore);
			break;
		case GRANITE:
			ItemStack orange_ore = VinkaItems.ORANGE_DYE().getItem();
			orange_ore.setAmount(amount);
			w.dropItemNaturally(loc, orange_ore);
			break;
		case COAL_ORE:
			ItemStack item7 = VinkaItems.COAL().getItem();
			item7.setAmount(amount);
			w.dropItemNaturally(loc, item7);
			break;
		case GOLD_ORE:
			ItemStack item5 = VinkaItems.DANDELION_YELLOW().getItem();
			item5.setAmount(amount);
			e.getPlayer().getInventory().addItem(item5);
			e.getPlayer().sendMessage(TextModule.color("Some &qlimonite ore &wwas added to your inventory."));
			e.getBlock().setType(Material.LAVA);
			break;
		case EMERALD_ORE:
			ItemStack emeralds = VinkaItems.LIME_DYE().getItem();
			emeralds.setAmount(amount);
			w.dropItemNaturally(loc, emeralds);
		case DIAMOND_ORE:
			if (w.getName().equals("world")) {
				ItemStack item4 = VinkaItems.LIGHT_BLUE_DYE().getItem();
				w.dropItemNaturally(loc, item4);
			} else {
				ItemStack item4 = VinkaItems.ENDER_PEARL().getItem();
				w.dropItemNaturally(loc, item4);
			}
			e.getBlock().setType(Material.WATER);
			w.playSound(loc, Sound.AMBIENT_CAVE, 1f, 1f);
			break;
		case TNT:
			dropItem(loc, VinkaItems.SUGAR());
			break;
		case OAK_SLAB:
		case SIGN:
			dropItem(loc, VinkaItems.RABBIT_FOOT());
			break;
		case OAK_PLANKS:
		case WHITE_BANNER:
		case OAK_FENCE_GATE:
			dropItem(loc, VinkaItems.STICK());
			break;
		case FURNACE:
			dropItem(loc, VinkaItems.STONE());
			break;
		case OAK_FENCE:
		case CHEST:
		case OAK_DOOR:
			dropItem(loc, VinkaItems.OAK_PLANKS());
			break;
		case IRON_BLOCK:
		case HOPPER:
		case IRON_DOOR:
		case IRON_TRAPDOOR:
			dropItem(loc, VinkaItems.LIGHT_GRAY_DYE());
			break;
		case WHITE_BED:
			dropItem(loc, VinkaItems.WHITE_WOOL());
			dropItem(loc, VinkaItems.OAK_PLANKS());
			break;
		case CRAFTING_TABLE:
			ItemStack some_iron = VinkaItems.IRON_INGOT().getItem();
			some_iron.setAmount(3);
			w.dropItemNaturally(loc, some_iron);
			break;
		case COBBLESTONE:
		case STONE_SLAB:
			dropItem(loc, VinkaItems.GRAY_DYE());
			break;
		case STONE:
		case GRAY_CONCRETE:
			ItemStack item3 = VinkaItems.GRAY_DYE().getItem();
			item3.setAmount(amount);
			w.dropItemNaturally(loc, item3);
			break;
		case GRASS_BLOCK:
		case GRASS_PATH:
		case DIRT:
			ItemStack item8 = VinkaItems.DRIED_KELP().getItem();
			item8.setAmount(amount);
			w.dropItemNaturally(loc, item8);
			break;
		case BEETROOTS:
			Ageable crop = (Ageable) e.getBlock().getBlockData();
			if (crop.getAge() == 3) {
				new BukkitRunnable() {
					@Override
					public void run() {
						e.getBlock().setType(Material.BEETROOTS);
					}
				}.runTaskLater(Vinka.vinka, 20);

				ItemStack item = VinkaItems.BEETROOT().getItem();
				item.setAmount(amount);
				w.dropItemNaturally(loc, item);
			} else {
				dropItem(loc, VinkaItems.BEETROOT_SEEDS());
			}
			break;
		case MELON:
			ItemStack item = VinkaItems.COOKIE().getItem();
			item.setAmount(amount);
			w.dropItemNaturally(loc, item);
			break;
		case WHEAT:
			Ageable crop3 = (Ageable) e.getBlock().getBlockData();
			if (crop3.getAge() == 7) {
				new BukkitRunnable() {
					@Override
					public void run() {
						e.getBlock().setType(Material.WHEAT);
					}
				}.runTaskLater(Vinka.vinka, 20);

				ItemStack item1 = VinkaItems.BAKED_POTATO().getItem();
				item1.setAmount(amount);
				w.dropItemNaturally(loc, item1);
			} else {
				dropItem(loc, VinkaItems.WHEAT_SEEDS());
			}
			break;
		case CARROTS:
			Ageable crop4 = (Ageable) e.getBlock().getBlockData();
			if (crop4.getAge() == 7) {
				new BukkitRunnable() {
					@Override
					public void run() {
						e.getBlock().setType(Material.CARROTS);
					}
				}.runTaskLater(Vinka.vinka, 20);

				ItemStack item1 = VinkaItems.STRING().getItem();
				item1.setAmount(amount);
				w.dropItemNaturally(loc, item1);
			} else {
				dropItem(loc, VinkaItems.CARROT());
			}
			break;
		default:
			if (BlockModule.isPlant(e.getBlock().getType())) {
				if (Math.random() < 0.3) {
					if (Math.random() < 0.5) {
						dropItem(loc, VinkaItems.BEETROOT_SEEDS());
					} else {
						dropItem(loc, VinkaItems.CARROT());
					}
				}
			}

			if (BlockModule.isLog(e.getBlock().getType())) {
				if (e.getPlayer().getEquipment().getItemInMainHand().getType() == Material.AIR) {
					amount++;
				}

				ItemStack item2 = VinkaItems.RABBIT_FOOT().getItem();
				item2.setAmount(amount);
				w.dropItemNaturally(loc, item2);
			}
			break;
		}
	}
}
