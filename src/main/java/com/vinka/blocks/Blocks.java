package com.vinka.blocks;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.valkcore.modules.BlockModule;
import com.valkcore.modules.PlayerModule;
import com.valkcore.modules.TextModule;
import com.vinka.Vinka;
import com.vinka.utils.Utils;
import com.vinkaitems.VinkaItems;

public class Blocks implements Listener {
	@EventHandler
	private void inventoryOpenEvent(InventoryOpenEvent e) {
		if (e.getInventory().getType() == InventoryType.ANVIL) {
			e.setCancelled(true);
			EntityEquipment equip = e.getPlayer().getEquipment();
			ItemStack item = equip.getItemInMainHand();
			Player p = (Player) e.getPlayer();
			
			if (item.getType() == Material.GOLD_NUGGET) {
				p.sendMessage(TextModule.color("You can't use coins to make coins!"));
			}

			int amount = calcGoldNuggets(item);

			ItemStack goldNugget = VinkaItems.GOLD_NUGGET().getItem();
			goldNugget.setAmount(amount);

			ItemStack heldItem = equip.getItemInMainHand();
			String name = heldItem.getType().name().toLowerCase();
			String info = heldItem.getAmount() + " " + name;
			
			if (amount > 0) {
				p.getWorld().dropItemNaturally(p.getLocation(), goldNugget);
				p.sendMessage(TextModule.color("You used " + info + " and got &q" + amount + " &wcoins."));
			} else {
				p.sendMessage(TextModule.color("You used " + info + " and didn't get anything."));
			}
				
			equip.setItemInMainHand(new ItemStack(Material.AIR));
		}
	}

	private int calcGoldNuggets(ItemStack item) {
		int goldNuggets = 0;

		double chance;
		int multiplier = 1;

		switch (item.getType()) {
		case DIAMOND_AXE:
		case DIAMOND_HOE:
		case DIAMOND_PICKAXE:
		case DIAMOND_SHOVEL:
		case DIAMOND_SWORD:
		case DIAMOND_BOOTS:
		case DIAMOND_LEGGINGS:
		case DIAMOND_CHESTPLATE:
		case DIAMOND_HELMET:
			chance = 0.10;
			multiplier = 6;
			break;
		case GOLDEN_AXE:
		case GOLDEN_HOE:
		case GOLDEN_PICKAXE:
		case GOLDEN_SHOVEL:
		case GOLDEN_SWORD:
		case GOLDEN_BOOTS:
		case GOLDEN_LEGGINGS:
		case GOLDEN_CHESTPLATE:
		case GOLDEN_HELMET:
			chance = 0.10;
			multiplier = 5;
			break;
		case IRON_AXE:
		case IRON_HOE:
		case IRON_PICKAXE:
		case IRON_SHOVEL:
		case IRON_SWORD:
		case IRON_BOOTS:
		case IRON_LEGGINGS:
		case IRON_CHESTPLATE:
		case IRON_HELMET:
			chance = 0.10;
			multiplier = 4;
			break;
		case STONE_AXE:
		case STONE_HOE:
		case STONE_PICKAXE:
		case STONE_SHOVEL:
		case STONE_SWORD:
		case LEATHER_BOOTS:
		case LEATHER_LEGGINGS:
		case LEATHER_CHESTPLATE:
		case LEATHER_HELMET:
			chance = 0.10;
			multiplier = 3;
			break;
		case WOODEN_AXE:
		case WOODEN_HOE:
		case WOODEN_PICKAXE:
		case WOODEN_SHOVEL:
		case WOODEN_SWORD:
			chance = 0.10;
			multiplier = 2;
			break;
		default:
			chance = 0.01;
			multiplier = 1;
			break;
		}

		for (int i = 0; i < item.getAmount(); i++) {
			if (Math.random() < chance) {
				goldNuggets = goldNuggets + 1 * multiplier;
			}
		}

		return goldNuggets;
	}

	@EventHandler
	private void blockEvent(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (!PlayerModule.inSurvival(p))
			return;

		if (RestrictedBlocks.restrictedBlock(e)) {
			e.setCancelled(true);
			return;
		}

		if (p.getEquipment().getItemInMainHand().getType() == Material.DIAMOND_PICKAXE) {
			if (p.isSneaking())
				Utils.superPickaxe(e.getBlock().getLocation());
		}

		BlockDrops.blockDrops(e, Utils.toolGatherAmount(p));
		BlockModule.treeGravity(e);
		moreSapsFromBreakingTrees(e);
		Utils.updateToolDurability(e.getBlock().getType(), p, p.getEquipment().getItemInMainHand());
		e.setExpToDrop(0);
		e.setDropItems(false);
	}

	private void moreSapsFromBreakingTrees(BlockBreakEvent e) {
		if (BlockModule.isLog(e.getBlock().getType())) {
			int length = 4 + new Random().nextInt(5);
			Location loc = e.getBlock().getLocation();
			World w = loc.getWorld();
			Location testLoc1 = new Location(w, loc.getX() + length, loc.getY(), loc.getZ());
			Location testLoc2 = new Location(w, loc.getX(), loc.getY(), loc.getZ() + length);
			Location testLoc3 = new Location(w, loc.getX() - length, loc.getY(), loc.getZ());
			Location testLoc4 = new Location(w, loc.getX(), loc.getY(), loc.getZ() - length);
			Location[] locs = { testLoc1, testLoc2, testLoc3, testLoc4 };
			Block spawnBlock = w.getHighestBlockAt(locs[new Random().nextInt(locs.length)]).getRelative(BlockFace.DOWN);
			if (Math.random() < 0.20) {
				if (spawnBlock.getType() == Material.GRASS_BLOCK) {
					if (spawnBlock.getRelative(BlockFace.UP).getType() == Material.AIR) {
						spawnBlock.getRelative(BlockFace.UP).setType(Material.OAK_SAPLING);
					}
				}
			}
		}
	}

	@EventHandler
	private void blockPlaceEvent(BlockPlaceEvent e) {
		if (e.getBlock() == null)
			return;
		Player p = e.getPlayer();
		GravityBlocks.gravityPlacedBlocks(e.getItemInHand(), e.getBlock(), p);

		if (isBanner(e.getBlock().getType())) {
			PlayerModule.addPotionEffect(p, PotionEffectType.FAST_DIGGING, 20 * 60 * 5, 0);
			PlayerModule.addPotionEffect(p, PotionEffectType.INCREASE_DAMAGE, 20 * 60 * 5, 0);
			PlayerModule.addPotionEffect(p, PotionEffectType.REGENERATION, 20 * 60 * 5, 0);
			PlayerModule.addPotionEffect(p, PotionEffectType.SPEED, 20 * 60 * 5, 0);
			PlayerModule.addPotionEffect(p, PotionEffectType.JUMP, 20 * 60 * 5, 0);
		}

		if (e.getBlock().getType() == Material.RED_SAND) { // Is placed block red sand?
			turnBlockToBlock(Material.SAND, Material.RED_SAND, Material.SANDSTONE, Material.RED_SANDSTONE, e.getBlock(),
					1.0);
		}

		if (e.getBlock().getType() == Material.SAND) {
			turnBlockToBlock(Material.RED_SAND, Material.SAND, Material.RED_SANDSTONE, Material.SANDSTONE, e.getBlock(),
					1.0);
		}
	}

	private void turnBlockToBlock(final Material blockA1, final Material blockA2, final Material blockB1, final Material blockB2, final Block b,
			final double chance) {
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Block block : BlockModule.getAdjacentBlocks(b)) { // Get adjacent blocks around it.
					if (Math.random() < chance) {
						if (block.getType() == blockA1) { // If adjacent block is sand?
							block.setType(blockA2); // Turn to red sand.
							turnBlockToBlock(blockA1, blockA2, blockB1, blockB2, block, chance - 0.02);
						}

						if (block.getType() == blockB1) {
							block.setType(blockB2);
							turnBlockToBlock(blockA1, blockA2, blockB1, blockB2, block, chance - 0.02);
						}
					}
				}
			}
		}.runTaskLater(Vinka.vinka, 5);
	}

	@EventHandler
	private void leafDecay(LeavesDecayEvent e) {
		e.setCancelled(true);
		e.getBlock().setType(Material.AIR);
	}

	private boolean isBanner(Material type) {
		switch (type) {
		case WHITE_BANNER:
		case BLACK_BANNER:
		case BLACK_WALL_BANNER:
		case BLUE_BANNER:
		case BLUE_WALL_BANNER:
		case BROWN_BANNER:
		case BROWN_WALL_BANNER:
		case CYAN_BANNER:
		case CYAN_WALL_BANNER:
		case GRAY_BANNER:
		case GRAY_WALL_BANNER:
		case GREEN_BANNER:
		case GREEN_WALL_BANNER:
		case LIGHT_BLUE_BANNER:
		case LIGHT_BLUE_WALL_BANNER:
		case LIGHT_GRAY_BANNER:
		case LIGHT_GRAY_WALL_BANNER:
		case LIME_BANNER:
		case LIME_WALL_BANNER:
		case MAGENTA_BANNER:
		case MAGENTA_WALL_BANNER:
		case ORANGE_BANNER:
		case ORANGE_WALL_BANNER:
		case PINK_BANNER:
		case PINK_WALL_BANNER:
		case PURPLE_BANNER:
		case PURPLE_WALL_BANNER:
		case RED_BANNER:
		case RED_WALL_BANNER:
		case WHITE_WALL_BANNER:
		case YELLOW_BANNER:
		case YELLOW_WALL_BANNER:
			return true;
		default:
			return false;
		}
	}
}
