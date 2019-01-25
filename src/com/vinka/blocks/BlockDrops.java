package com.vinka.blocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.data.Ageable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.vinka.Vinka;
import com.vinka.configs.PlayerFiles;
import com.vinka.items.VinkaItems;
import com.vinka.modules.BlockModule;
import com.vinka.utils.Utils;

public class BlockDrops {
	public static void blockDrops(BlockBreakEvent e, int amount) {
		Location loc = e.getBlock().getLocation();
		World w = loc.getWorld();

		switch (e.getBlock().getType()) {
		case REDSTONE_ORE:
			ItemStack redstone_ore = VinkaItems.ROSE_RED_DYE();
			redstone_ore.setAmount(amount);
			w.dropItemNaturally(loc, redstone_ore);
			break;
		case ANDESITE:
			ItemStack cyan_ore = VinkaItems.CYAN_DYE();
			cyan_ore.setAmount(amount);
			w.dropItemNaturally(loc, cyan_ore);
			break;
		case DIORITE:
			ItemStack diorite_ore = VinkaItems.LIGHT_GRAY_DYE();
			diorite_ore.setAmount(amount);
			w.dropItemNaturally(loc, diorite_ore);
			break;
		case GRANITE:
			ItemStack orange_ore = VinkaItems.ORANGE_DYE();
			orange_ore.setAmount(amount);
			w.dropItemNaturally(loc, orange_ore);
			break;
		case COAL_ORE:
			ItemStack item7 = VinkaItems.COAL();
			item7.setAmount(amount);
			w.dropItemNaturally(loc, item7);
			break;
		case IRON_ORE:
			ItemStack item6 = VinkaItems.IRON_ORE();
			item6.setAmount(amount);
			w.dropItemNaturally(loc, item6);
			break;
		case GOLD_ORE:
			ItemStack item5 = VinkaItems.GOLD_ORE();
			item5.setAmount(amount);
			e.getPlayer().getInventory().addItem(item5);
			e.getPlayer().sendMessage(Utils.color("&7Some &flimonite ore &7was added to your inventory."));
			e.getBlock().setType(Material.LAVA);
			break;
		case DIAMOND_ORE:
			ItemStack item4 = VinkaItems.DIAMOND_ORE();
			w.dropItemNaturally(loc, item4);
			e.getBlock().setType(Material.WATER);
			w.playSound(loc, Sound.AMBIENT_CAVE, 1f, 1f);
			break;
		case STONE:
			ItemStack item3 = VinkaItems.GRAY_DYE();
			item3.setAmount(amount);
			w.dropItemNaturally(loc, item3);
			break;
		case DIRT:
			new BukkitRunnable() {
				@Override
				public void run() {
					e.getBlock().setType(Material.COARSE_DIRT);
				}
			}.runTaskLater(Vinka.vinka, 2);
			break;
		case COARSE_DIRT:
			ItemStack item8 = VinkaItems.DRIED_KELP();
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

				ItemStack item = VinkaItems.BEETROOT();
				item.setAmount(amount);
				w.dropItemNaturally(loc, item);
			} else {
				w.dropItemNaturally(loc, VinkaItems.BEETROOT_SEEDS());
			}
			break;
		case MELON:
			ItemStack item = VinkaItems.COOKIE();
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
				
				ItemStack item1 = VinkaItems.BAKED_POTATO();
				item1.setAmount(amount);
				w.dropItemNaturally(loc, item1);
			} else {
				w.dropItemNaturally(loc, VinkaItems.WHEAT_SEEDS());
			}
			break;
		default:
			if (BlockModule.isPlant(e.getBlock().getType())) {
				if (Math.random() < 0.3) {
					w.dropItemNaturally(loc, VinkaItems.BEETROOT_SEEDS());
				}
			}

			if (BlockModule.isLog(e.getBlock().getType())) {
				PlayerFiles cm = PlayerFiles.getConfig(e.getPlayer());
				FileConfiguration config = cm.getConfig();
				
				if (!config.getBoolean("achievement_1")) {
					config.set("achievement_1", true);
					Utils.achievement(e.getPlayer(), "Just Getting Started");
					cm.saveConfig();
				}
				
				ItemStack item2 = VinkaItems.RABBIT_FOOT();
				item2.setAmount(amount);
				w.dropItemNaturally(loc, item2);
			}
			break;
		}
	}
}
