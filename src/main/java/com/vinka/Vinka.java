package com.vinka;

import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.vinka.blocks.BlockGrow;
import com.vinka.blocks.BlockStrippedWood;
import com.vinka.blocks.Blocks;
import com.vinka.configs.LoadPlayerFiles;
import com.vinka.consumables.Food;
import com.vinka.listeners.AbsorbSoulsListener;
import com.vinka.listeners.CustomPlayerDeathEventListener;
import com.vinka.listeners.CustomPlayerRespawnEventListener;
import com.vinka.mobs.MobDeath;
import com.vinka.mobs.MobInteract;
import com.vinka.mobs.Mobs;
import com.vinka.player.PlayerHandler;
import com.vinka.utils.Utils;
import com.vinkaitems.VinkaItems;
import com.valkutils.modules.ItemModule;

public class Vinka extends JavaPlugin {
	public static Vinka vinka;

	@Override
	public void onEnable() {
		vinka = this;

		Server s = getServer();

		setGameRules(s);

		s.clearRecipes();
		addRecipes();

		registerListeners(s);
		registerCommands();

		handleSpawning(s);
	}

	private void setGameRules(Server s) {
		World w = s.getWorld("world");
		w.setGameRule(GameRule.DO_FIRE_TICK, false);
		w.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
		w.setGameRule(GameRule.COMMAND_BLOCK_OUTPUT, false);
		w.setGameRule(GameRule.DO_MOB_LOOT, false);
		w.setGameRule(GameRule.DO_ENTITY_DROPS, false);
		w.setGameRule(GameRule.DO_TILE_DROPS, false);
		w.setGameRule(GameRule.KEEP_INVENTORY, false);
		w.setGameRule(GameRule.MOB_GRIEFING, false);
	}

	private void registerCommands() {
		// getCommand("test").setExecutor(new CommandTest());
	}

	private void registerListeners(Server s) {
		PluginManager pm = s.getPluginManager();
		pm.registerEvents(new BlockGrow(), this);
		pm.registerEvents(new BlockStrippedWood(), this);
		pm.registerEvents(new Blocks(), this);

		pm.registerEvents(new Food(), this);

		pm.registerEvents(new Mobs(), this);
		pm.registerEvents(new MobDeath(), this);
		pm.registerEvents(new MobInteract(), this);

		pm.registerEvents(new PlayerHandler(), this);

		pm.registerEvents(new LoadPlayerFiles(), this);

		pm.registerEvents(new CustomPlayerRespawnEventListener(), this);
		pm.registerEvents(new CustomPlayerDeathEventListener(), this);
		pm.registerEvents(new AbsorbSoulsListener(), this);
	}

	private void handleSpawning(final Server s) {
		final int radius = 30;

		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player p : s.getOnlinePlayers()) {
					int count = 0;
					for (Entity entity : p.getNearbyEntities(radius, 5, radius)) {
						if (entity instanceof Monster) {
							count++;
						}
					}

					if (count <= 2) {
						Location playerLocation = p.getLocation();

						for (Location testLoc : Utils.testLocations(playerLocation, radius)) {
							Location loc = playerLocation.getWorld().getHighestBlockAt(testLoc).getLocation();
							if (Utils.validSpawningLocation(loc)) {

								if (loc.getWorld().getName().equals("world")) {
									int dist = (int) playerLocation.distance(playerLocation.getWorld().getSpawnLocation());

									for (int i = 0; i < 10; i++) {
										if (!spawnRange(dist, 100 * i, 100 * (i + 1)))
											continue;
										switch (i) {
										case 0:
											Utils.spawnMonster(loc, EntityType.SLIME);
											break;
										case 1:
											Utils.spawnMonster(loc, EntityType.ZOMBIE);
											break;
										case 2:
											Utils.spawnMonster(loc, EntityType.STRAY);
											break;
										case 3:
											Utils.spawnMonster(loc, EntityType.HUSK);
											break;
										case 4:
											Utils.spawnMonster(loc, EntityType.WOLF);
											break;
										case 5:
											Utils.spawnMonster(loc, EntityType.CAVE_SPIDER);
											break;
										case 6:
											Utils.spawnMonster(loc, EntityType.SPIDER);
											break;
										case 7:
											Utils.spawnMonster(loc, EntityType.PHANTOM);
											break;
										case 8:
											Utils.spawnMonster(loc, EntityType.VEX);
											break;
										case 9:
											Utils.spawnMonster(loc, EntityType.WITHER_SKELETON);
											break;
										case 10:
											Utils.spawnMonster(loc, EntityType.WITHER);
											break;
										default:
											break;
										}
									}
								} else {
									Utils.spawnMonster(loc, EntityType.WITHER_SKELETON);
								}
								
								return;
							}
						}
					}
				}
			}
		}.runTaskTimer(this, 200, 300);
	}

	private boolean spawnRange(int dist, int minDist, int maxDist) {
		return dist >= minDist && dist <= maxDist;
	}

	private void addRecipes() {
		ItemModule.handRecipe("SHEARS", VinkaItems.SHEARS(), "oxxo", new ItemStack[] {new ItemStack(Material.IRON_INGOT)}, "x");
		ItemModule.handRecipe("FLINT_AND_STEEL", VinkaItems.FLINT_AND_STEEL(), "xoox", new ItemStack[] {new ItemStack(Material.IRON_INGOT)}, "x");
		
		ItemModule.shapelessRecipe("SEA_LANTERN", VinkaItems.SEA_LANTERN(),
				new ItemStack[] { new ItemStack(Material.GLOWSTONE) });

		ItemModule.handRecipe("GLOWSTONE_BLOCK", VinkaItems.GLOWSTONE_BLOCK(), "xxxx",
				new ItemStack[] { new ItemStack(Material.TORCH) }, "x");
		ItemModule.handRecipe("TORCH", VinkaItems.TORCH(), "xxxx",
				new ItemStack[] { new ItemStack(Material.REDSTONE_TORCH) }, "x");
		ItemModule.shapelessRecipe("OAK_SAPLING", VinkaItems.OAK_SAPLING(),
				new ItemStack[] { new ItemStack(Material.SUGAR) });

		ItemModule.craftedRecipe("DIAMOND_HORSE_ARMOR", VinkaItems.DIAMOND_HORSE_ARMOR(), "ooooooxxx",
				new ItemStack[] { new ItemStack(Material.DIAMOND) }, "x");
		ItemModule.craftedRecipe("GOLDEN_HORSE_ARMOR", VinkaItems.GOLD_HORSE_ARMOR(), "ooooooxxx",
				new ItemStack[] { new ItemStack(Material.GOLD_INGOT) }, "x");
		ItemModule.craftedRecipe("IRON_HORSE_ARMOR", VinkaItems.IRON_HORSE_ARMOR(), "ooooooxxx",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT) }, "x");

		ItemModule.shapelessRecipe("MULE_SPAWN_EGG", VinkaItems.MULE_SPAWN_EGG(),
				new ItemStack[] { new ItemStack(Material.LLAMA_SPAWN_EGG) });
		ItemModule.shapelessRecipe("LLAMA_SPAWN_EGG", VinkaItems.LLAMA_SPAWN_EGG(),
				new ItemStack[] { new ItemStack(Material.DONKEY_SPAWN_EGG) });
		ItemModule.shapelessRecipe("DONKEY_SPAWN_EGG", VinkaItems.DONKEY_SPAWN_EGG(),
				new ItemStack[] { new ItemStack(Material.HORSE_SPAWN_EGG) });
		ItemModule.craftedRecipe("WOLF_SPAWN_EGG", VinkaItems.WOLF_SPAWN_EGG(), "xxxxaxxxx",
				new ItemStack[] { new ItemStack(Material.SUGAR), new ItemStack(Material.BEETROOT_SEEDS) }, "x,a");
		ItemModule.craftedRecipe("HORSE_SPAWN_EGG", VinkaItems.HORSE_SPAWN_EGG(), "xxxxaxxxx",
				new ItemStack[] { new ItemStack(Material.SUGAR), new ItemStack(Material.WHEAT_SEEDS) }, "x,a");
		ItemModule.craftedRecipe("SHEEP_SPAWN_EGG", VinkaItems.SHEEP_SPAWN_EGG(), "xxxxaxxxx",
				new ItemStack[] { new ItemStack(Material.SUGAR), new ItemStack(Material.IRON_INGOT) }, "x,a");
		ItemModule.craftedRecipe("VILLAGER_SPAWN_EGG", VinkaItems.VILLAGER_SPAWN_EGG(), "xxxxaxxxx",
				new ItemStack[] { new ItemStack(Material.SUGAR), new ItemStack(Material.EMERALD) }, "x,a");
		ItemModule.craftedRecipe("CHICKEN_SPAWN_EGG", VinkaItems.CHICKEN_SPAWN_EGG(), "xxxxaxxxx",
				new ItemStack[] { new ItemStack(Material.SUGAR), new ItemStack(Material.DIAMOND) }, "x,a");

		ItemModule.craftedRecipe("IRON_ORE_FROM_LIGHT_GRAY_DYE_ORE", VinkaItems.IRON_ORE(), "xxxxxxxxx",
				new ItemStack[] { new ItemStack(Material.LIGHT_GRAY_DYE) }, "x");

		ItemModule.handRecipe("LEAD", VinkaItems.LEAD(), "osso", new ItemStack[] { new ItemStack(Material.STICK) },
				"s");
		ItemModule.craftedRecipe("SADDLE", VinkaItems.SADDLE(), "xoxxaxxxx",
				new ItemStack[] { new ItemStack(Material.WHEAT_SEEDS), new ItemStack(Material.IRON_INGOT) }, "x,a");
		ItemModule.handRecipe("OBSIDIAN", VinkaItems.OBSIDIAN(), "xxxx",
				new ItemStack[] { new ItemStack(Material.DIAMOND) }, "x");
		ItemModule.handRecipe("ENCHANTMENT_TABLE", VinkaItems.ENCHANTMENT_TABLE(), "xxxx",
				new ItemStack[] { new ItemStack(Material.OBSIDIAN) }, "x");
		ItemModule.handRecipe("BREWER", VinkaItems.BREWING_STAND(), "xxxx",
				new ItemStack[] { new ItemStack(Material.WHEAT_SEEDS) }, "x");
		ItemModule.handRecipe("CAULDRON", VinkaItems.CAULDRON(), "bbaa",
				new ItemStack[] { new ItemStack(Material.STONE), new ItemStack(Material.STONE_SLAB) }, "a,b");
		ItemModule.handRecipe("ELYTRA", VinkaItems.ELYTRA(), "xxxx",
				new ItemStack[] { new ItemStack(Material.EMERALD) }, "x");
		ItemModule.handRecipe("REDSTONE", VinkaItems.REDSTONE(), "xxxx",
				new ItemStack[] { new ItemStack(Material.BEETROOT) }, "x");
		ItemModule.handRecipe("PISTON", VinkaItems.PISTON(), "abab",
				new ItemStack[] { new ItemStack(Material.REDSTONE), new ItemStack(Material.OAK_PLANKS) }, "a,b");
		ItemModule.shapelessRecipe("STICKY_PISTON", VinkaItems.STICKY_PISTON(),
				new ItemStack[] { new ItemStack(Material.PISTON) });
		ItemModule.handRecipe("DISPENSER", VinkaItems.DISPENSER(), "xxxx",
				new ItemStack[] { new ItemStack(Material.REDSTONE) }, "x");

		// WOOD RECIPES
		ItemModule.handRecipe("SIGN", VinkaItems.SIGN(), "xxss",
				new ItemStack[] { new ItemStack(Material.OAK_SLAB), new ItemStack(Material.STICK) }, "x,s");
		ItemModule.handRecipe("OAK_DOOR", VinkaItems.OAK_DOOR(), "xoxo",
				new ItemStack[] { new ItemStack(Material.OAK_PLANKS) }, "x");
		ItemModule.handRecipe("OAK_SLAB", VinkaItems.OAK_SLAB(), "xxxx",
				new ItemStack[] { new ItemStack(Material.RABBIT_FOOT) }, "x");
		ItemModule.handRecipe("OAK_PLANKS", VinkaItems.OAK_PLANKS(), "xxxx",
				new ItemStack[] { new ItemStack(Material.OAK_SLAB) }, "x");
		ItemModule.handRecipe("CHEST", VinkaItems.CHEST(), "xxxx",
				new ItemStack[] { new ItemStack(Material.OAK_PLANKS) }, "x");
		ItemModule.handRecipe("OAK_TRAPDOOR", VinkaItems.OAK_TRAPDOOR(), "ooxx",
				new ItemStack[] { new ItemStack(Material.OAK_SLAB) }, "x");
		ItemModule.handRecipe("OAK_FENCE", VinkaItems.OAK_FENCE(), "ssww",
				new ItemStack[] { new ItemStack(Material.OAK_SLAB), new ItemStack(Material.OAK_PLANKS) }, "s,w");
		ItemModule.handRecipe("OAK_GATE", VinkaItems.OAK_FENCE_GATE(), "swws",
				new ItemStack[] { new ItemStack(Material.STICK), new ItemStack(Material.OAK_SLAB) }, "s,w");
		ItemModule.handRecipe("LADDER", VinkaItems.LADDER(), "xoxo", new ItemStack[] { new ItemStack(Material.STICK) },
				"x");

		ItemStack moreRedstoneTorches = VinkaItems.REDSTONE_TORCH();
		moreRedstoneTorches.setAmount(2);

		ItemModule.handRecipe("REDSTONE_TORCH", moreRedstoneTorches, "coso",
				new ItemStack[] { new ItemStack(Material.COAL), new ItemStack(Material.STICK) }, "c,s");

		ItemModule.handRecipe("FISHING_ROD", VinkaItems.FISHING_ROD(), "ocso",
				new ItemStack[] { new ItemStack(Material.STRING), new ItemStack(Material.STICK) }, "c,s");
		ItemModule.handRecipe("OAK_PRESSURE_PLATE", VinkaItems.OAK_PRESSURE_PLATE(), "xoxo",
				new ItemStack[] { new ItemStack(Material.OAK_SLAB) }, "x");
		ItemModule.shapelessRecipe("STICK", VinkaItems.STICK(),
				new ItemStack[] { new ItemStack(Material.RABBIT_FOOT), new ItemStack(Material.RABBIT_FOOT) });
		ItemModule.shapelessRecipe("ITEM_FRAME", VinkaItems.ITEM_FRAME(), new ItemStack[] {
				new ItemStack(Material.STICK), new ItemStack(Material.STICK), new ItemStack(Material.STICK) });
		ItemModule.shapelessRecipe("OAK_STAIRS", VinkaItems.OAK_STAIRS(),
				new ItemStack[] { new ItemStack(Material.OAK_PLANKS), new ItemStack(Material.OAK_PLANKS),
						new ItemStack(Material.OAK_PLANKS) });
		ItemModule.shapelessRecipe("OAK_BUTTON", VinkaItems.OAK_BUTTON(),
				new ItemStack[] { new ItemStack(Material.OAK_PRESSURE_PLATE) });
		ItemModule.shapelessRecipe("BOWL", VinkaItems.BOWL(), new ItemStack[] { new ItemStack(Material.RABBIT_FOOT),
				new ItemStack(Material.RABBIT_FOOT), new ItemStack(Material.RABBIT_FOOT) });

		ItemModule.shapelessRecipe("STRING", VinkaItems.STRING(), new ItemStack[] { new ItemStack(Material.INK_SAC),
				new ItemStack(Material.INK_SAC), new ItemStack(Material.INK_SAC), new ItemStack(Material.INK_SAC) });
		ItemModule.shapelessRecipe("WHITE_WOOL", VinkaItems.WHITE_WOOL(),
				new ItemStack[] { new ItemStack(Material.STRING), new ItemStack(Material.STRING),
						new ItemStack(Material.STRING), new ItemStack(Material.STRING) });
		ItemModule.handRecipe("WHITE_BED", VinkaItems.WHITE_BED(), "wwxx",
				new ItemStack[] { new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.OAK_SLAB) }, "w,x");
		ItemModule.handRecipe("WHITE_BANNER", VinkaItems.WHITE_BANNER(), "woso",
				new ItemStack[] { new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.STICK) }, "w,s");

		// Reverse Recipes
		ItemModule.shapelessRecipe("OAK_SLAB_FROM_OAK_PLANKS", VinkaItems.OAK_SLAB(),
				new ItemStack[] { new ItemStack(Material.OAK_PLANKS) });
		ItemModule.shapelessRecipe("RABBIT_FOOT_FROM_OAK_SLAB", VinkaItems.RABBIT_FOOT(),
				new ItemStack[] { new ItemStack(Material.OAK_SLAB) });
		ItemModule.shapelessRecipe("OAK_PLANKS_FROM_OAK_DOOR", VinkaItems.OAK_PLANKS(),
				new ItemStack[] { new ItemStack(Material.OAK_DOOR) });
		ItemModule.shapelessRecipe("OAK_PLANKS_FROM_OAK_STAIRS", VinkaItems.OAK_PLANKS(),
				new ItemStack[] { new ItemStack(Material.OAK_STAIRS) });

		ItemModule.furnaceRecipe("MELON_SEEDS", VinkaItems.MELON_SEEDS(), Material.BEETROOT_SEEDS, 5);
		ItemModule.craftedRecipe("SEEDS", VinkaItems.WHEAT_SEEDS(), "xxxxxxxxx",
				new ItemStack[] { new ItemStack(Material.MELON_SEEDS) }, "x");

		ItemModule.handRecipe("COAL", VinkaItems.LIGHT_GRAY_DYE(), "xxxx",
				new ItemStack[] { new ItemStack(Material.COAL) }, "x");

		// Tools
		ItemModule.handRecipe("WOODEN_PICK", VinkaItems.WOODEN_PICKAXE(), "xxsx",
				new ItemStack[] { new ItemStack(Material.OAK_SLAB), new ItemStack(Material.STICK) }, "x,s");
		ItemModule.handRecipe("WOODEN_SHOVEL", VinkaItems.WOODEN_SHOVEL(), "xoso",
				new ItemStack[] { new ItemStack(Material.OAK_SLAB), new ItemStack(Material.STICK) }, "x,s");
		ItemModule.handRecipe("WOODEN_HOE", VinkaItems.WOODEN_HOE(), "xxso",
				new ItemStack[] { new ItemStack(Material.OAK_SLAB), new ItemStack(Material.STICK) }, "x,s");
		ItemModule.handRecipe("WOODEN_AXE", VinkaItems.WOODEN_AXE(), "sxox",
				new ItemStack[] { new ItemStack(Material.OAK_SLAB), new ItemStack(Material.STICK) }, "x,s");
		ItemModule.handRecipe("WOODEN_SWORD", VinkaItems.WOODEN_SWORD(), "oxsx",
				new ItemStack[] { new ItemStack(Material.OAK_SLAB), new ItemStack(Material.STICK) }, "x,s");
		ItemModule.handRecipe("BOW", VinkaItems.BOW(), "sxsx",
				new ItemStack[] { new ItemStack(Material.STRING), new ItemStack(Material.STICK) }, "s,x");
		ItemModule.handRecipe("ARROW", VinkaItems.ARROW(), "aobo",
				new ItemStack[] { new ItemStack(Material.RABBIT_FOOT), new ItemStack(Material.STICK) }, "a,b");

		ItemModule.craftedRecipe("DIRT_BLOCK", VinkaItems.DIRT_BLOCK(), "xxxxxxxxx",
				new ItemStack[] { new ItemStack(Material.DRIED_KELP) }, "x");

		// Stone Age
		ItemModule.handRecipe("STONE_SLAB", VinkaItems.STONE_SLAB(), "xxxx",
				new ItemStack[] { new ItemStack(Material.GRAY_DYE) }, "x");
		ItemModule.handRecipe("STONE_BLOCK", VinkaItems.STONE_BLOCK(), "xxxx",
				new ItemStack[] { new ItemStack(Material.STONE_SLAB) }, "x");

		ItemModule.handRecipe("BUCKET", VinkaItems.BUCKET(), "xoxo",
				new ItemStack[] { new ItemStack(Material.STONE_SLAB) }, "x");

		ItemModule.handRecipe("FURNACE", VinkaItems.FURNACE(), "xxxx",
				new ItemStack[] { new ItemStack(Material.STONE) }, "x");
		ItemModule.handRecipe("HOPPER", VinkaItems.HOPPER(), "aobo",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT), new ItemStack(Material.CHEST) }, "a,b");

		// Furnace Recipes
		ItemModule.furnaceRecipe("REFINED_IRON_ORE", VinkaItems.REFINED_IRON_ORE(), Material.LIGHT_GRAY_DYE, 5);
		ItemModule.furnaceRecipe("REFINED_GOLD_ORE", VinkaItems.REFINED_GOLD_ORE(), Material.DANDELION_YELLOW, 30);
		ItemModule.furnaceRecipe("REFINED_DIAMOND_ORE", VinkaItems.REFINED_DIAMOND_ORE(), Material.LIGHT_BLUE_DYE, 60);
		ItemModule.furnaceRecipe("REFINED_EMERALD", VinkaItems.REFINED_EMERALD(), Material.LIME_DYE, 60);

		// Tools
		ItemModule.handRecipe("STONE_PICK", VinkaItems.STONE_PICKAXE(), "xxsx",
				new ItemStack[] { new ItemStack(Material.STONE_SLAB), new ItemStack(Material.STICK) }, "x,s");
		ItemModule.handRecipe("STONE_SHOVEL", VinkaItems.STONE_SHOVEL(), "xoso",
				new ItemStack[] { new ItemStack(Material.STONE_SLAB), new ItemStack(Material.STICK) }, "x,s");
		ItemModule.handRecipe("STONE_HOE", VinkaItems.STONE_HOE(), "xxso",
				new ItemStack[] { new ItemStack(Material.STONE_SLAB), new ItemStack(Material.STICK) }, "x,s");
		ItemModule.handRecipe("STONE_AXE", VinkaItems.STONE_AXE(), "sxox",
				new ItemStack[] { new ItemStack(Material.STONE_SLAB), new ItemStack(Material.STICK) }, "x,s");
		ItemModule.handRecipe("STONE_SWORD", VinkaItems.STONE_SWORD(), "oxsx",
				new ItemStack[] { new ItemStack(Material.STONE_SLAB), new ItemStack(Material.STICK) }, "x,s");

		ItemModule.handRecipe("TNT", VinkaItems.TNT(), "xxxx", new ItemStack[] { new ItemStack(Material.SUGAR) }, "x");

		ItemModule.craftedRecipe("RAILS", VinkaItems.RAILS(), "oooabaooo",
				new ItemStack[] { new ItemStack(Material.STONE_SLAB), new ItemStack(Material.STICK) }, "a,b");
		ItemModule.craftedRecipe("POWERED_RAILS", VinkaItems.POWERED_RAILS(), "aoaabaooo",
				new ItemStack[] { new ItemStack(Material.STONE_SLAB), new ItemStack(Material.STICK) }, "a,b");
		ItemModule.craftedRecipe("DETECTOR_RAILS", VinkaItems.DETECTOR_RAILS(), "oooabaaoa",
				new ItemStack[] { new ItemStack(Material.STONE_SLAB), new ItemStack(Material.STICK) }, "a,b");
		ItemModule.craftedRecipe("ACTIVATOR_RAILS", VinkaItems.ACTIVATOR_RAILS(), "aoaabaaoa",
				new ItemStack[] { new ItemStack(Material.STONE_SLAB), new ItemStack(Material.STICK) }, "a,b");

		ItemModule.craftedRecipe("MINECART", VinkaItems.MINECART(), "oooaoaaaa",
				new ItemStack[] { new ItemStack(Material.STONE_SLAB) }, "a");

		ItemModule.craftedRecipe("CARVED_PUMPKIN", VinkaItems.CARVED_PUMPKIN(), "xxxxxxxxx",
				new ItemStack[] { new ItemStack(Material.WHEAT_SEEDS) }, "x");

		ItemModule.handRecipe("LEVER", VinkaItems.LEVER(), "aobo",
				new ItemStack[] { new ItemStack(Material.STONE_SLAB), new ItemStack(Material.STICK) }, "a,b");

		ItemModule.handRecipe("SHIELD", VinkaItems.SHIELD(), "aobo",
				new ItemStack[] { new ItemStack(Material.OAK_PLANKS), new ItemStack(Material.OAK_SLAB) }, "a,b");

		ItemModule.handRecipe("IRON_TRAPDOOR", VinkaItems.IRON_TRAPDOOR(), "xxoo",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT) }, "x");
		ItemModule.handRecipe("IRON_DOOR", VinkaItems.IRON_DOOR(), "xoxo",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT) }, "x");
		ItemModule.shapelessRecipe("STONE_BUTTON", VinkaItems.STONE_BUTTON(),
				new ItemStack[] { new ItemStack(Material.GRAY_DYE) });
		ItemModule.shapelessRecipe("STONE_PRESSUREPLATE", VinkaItems.STONE_PRESSUREPLATE(),
				new ItemStack[] { new ItemStack(Material.STONE_SLAB) });

		ItemModule.craftedRecipe("WOOD_BOOTS", VinkaItems.LEATHER_BOOTS(), "ooooooxox",
				new ItemStack[] { new ItemStack(Material.OAK_PLANKS) }, "x");
		ItemModule.craftedRecipe("WOOD_LEGGINGS", VinkaItems.LEATHER_LEGGINGS(), "oooxoxxox",
				new ItemStack[] { new ItemStack(Material.OAK_PLANKS) }, "x");
		ItemModule.craftedRecipe("WOOD_CHESTPLATE", VinkaItems.LEATHER_CHESTPLATE(), "oooxxxxxx",
				new ItemStack[] { new ItemStack(Material.OAK_PLANKS) }, "x");
		ItemModule.craftedRecipe("WOOD_HELMET", VinkaItems.LEATHER_HELMET(), "xxxoooooo",
				new ItemStack[] { new ItemStack(Material.OAK_PLANKS) }, "x");

		ItemModule.craftedRecipe("IRON_BOOTS", VinkaItems.IRON_BOOTS(), "ooooooxox",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT) }, "x");
		ItemModule.craftedRecipe("IRON_LEGGINGS", VinkaItems.IRON_LEGGINGS(), "oooxoxxox",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT) }, "x");
		ItemModule.craftedRecipe("IRON_CHESTPLATE", VinkaItems.IRON_CHESTPLATE(), "oooxxxxxx",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT) }, "x");
		ItemModule.craftedRecipe("IRON_HELMET", VinkaItems.IRON_HELMET(), "xxxoooooo",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT) }, "x");

		ItemModule.craftedRecipe("GOLDEN_BOOTS", VinkaItems.GOLDEN_BOOTS(), "ooooooxox",
				new ItemStack[] { new ItemStack(Material.GOLD_INGOT) }, "x");
		ItemModule.craftedRecipe("GOLDEN_LEGGINGS", VinkaItems.GOLDEN_LEGGINGS(), "oooxoxxox",
				new ItemStack[] { new ItemStack(Material.GOLD_INGOT) }, "x");
		ItemModule.craftedRecipe("GOLDEN_CHESTPLATE", VinkaItems.GOLDEN_CHESTPLATE(), "oooxxxxxx",
				new ItemStack[] { new ItemStack(Material.GOLD_INGOT) }, "x");
		ItemModule.craftedRecipe("GOLDEN_HELMET", VinkaItems.GOLDEN_HELMET(), "xxxoooooo",
				new ItemStack[] { new ItemStack(Material.GOLD_INGOT) }, "x");

		ItemModule.craftedRecipe("DIAMOND_BOOTS", VinkaItems.DIAMOND_BOOTS(), "ooooooxox",
				new ItemStack[] { new ItemStack(Material.DIAMOND) }, "x");
		ItemModule.craftedRecipe("DIAMOND_LEGGINGS", VinkaItems.DIAMOND_LEGGINGS(), "oooxoxxox",
				new ItemStack[] { new ItemStack(Material.DIAMOND) }, "x");
		ItemModule.craftedRecipe("DIAMOND_CHESTPLATE", VinkaItems.DIAMOND_CHESTPLATE(), "oooxxxxxx",
				new ItemStack[] { new ItemStack(Material.DIAMOND) }, "x");
		ItemModule.craftedRecipe("DIAMOND_HELMET", VinkaItems.DIAMOND_HELMET(), "xxxoooooo",
				new ItemStack[] { new ItemStack(Material.DIAMOND) }, "x");
		
		ItemModule.craftedRecipe("ENHANCED_BOOTS", VinkaItems.ENHANCED_BOOTS(), "ooooooxox",
				new ItemStack[] { new ItemStack(Material.ENDER_PEARL) }, "x");
		ItemModule.craftedRecipe("ENHANCED_LEGGINGS", VinkaItems.ENHANCED_LEGGINGS(), "oooxoxxox",
				new ItemStack[] { new ItemStack(Material.ENDER_PEARL) }, "x");
		ItemModule.craftedRecipe("ENHANCED_CHESTPLATE", VinkaItems.ENHANCED_CHESTPLATE(), "oooxxxxxx",
				new ItemStack[] { new ItemStack(Material.ENDER_PEARL) }, "x");
		ItemModule.craftedRecipe("ENHANCED_HELMET", VinkaItems.ENHANCED_HELMET(), "xxxoooooo",
				new ItemStack[] { new ItemStack(Material.ENDER_PEARL) }, "x");

		// Iron Age
		ItemModule.handRecipe("IRON_BLOCK", VinkaItems.IRON_BLOCK(), "xxxx",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT) }, "x");

		ItemModule.shapelessRecipe("CRAFTING_TABLE", VinkaItems.CRAFTING_TABLE(),
				new ItemStack[] { new ItemStack(Material.IRON_AXE), new ItemStack(Material.IRON_PICKAXE),
						new ItemStack(Material.IRON_HOE), new ItemStack(Material.IRON_SHOVEL) });

		ItemModule.handRecipe("UNREFINED_GOLD", VinkaItems.GOLD_ORE(), "xxxx",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT) }, "x");

		// Tools
		ItemModule.handRecipe("IRON_PICK", VinkaItems.IRON_PICKAXE(), "xxsx",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT), new ItemStack(Material.STICK) }, "x,s");
		ItemModule.handRecipe("IRON_SHOVEL", VinkaItems.IRON_SHOVEL(), "xoso",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT), new ItemStack(Material.STICK) }, "x,s");
		ItemModule.handRecipe("IRON_HOE", VinkaItems.IRON_HOE(), "xxso",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT), new ItemStack(Material.STICK) }, "x,s");
		ItemModule.handRecipe("IRON_AXE", VinkaItems.IRON_AXE(), "sxox",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT), new ItemStack(Material.STICK) }, "x,s");
		ItemModule.handRecipe("IRON_SWORD", VinkaItems.IRON_SWORD(), "oxsx",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT), new ItemStack(Material.STICK) }, "x,s");

		// Gold Age

		// Tools
		ItemModule.handRecipe("GOLD_PICK", VinkaItems.GOLD_PICKAXE(), "xxsx",
				new ItemStack[] { new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.STICK) }, "x,s");
		ItemModule.handRecipe("GOLD_SHOVEL", VinkaItems.GOLD_SHOVEL(), "xoso",
				new ItemStack[] { new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.STICK) }, "x,s");
		ItemModule.handRecipe("GOLD_HOE", VinkaItems.GOLD_HOE(), "xxso",
				new ItemStack[] { new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.STICK) }, "x,s");
		ItemModule.handRecipe("GOLD_AXE", VinkaItems.GOLD_AXE(), "sxox",
				new ItemStack[] { new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.STICK) }, "x,s");
		ItemModule.handRecipe("GOLD_SWORD", VinkaItems.GOLD_SWORD(), "oxsx",
				new ItemStack[] { new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.STICK) }, "x,s");

		// Diamond Age

		// Tools
		ItemModule.handRecipe("DIAMOND_PICK", VinkaItems.DIAMOND_PICKAXE(), "xxsx",
				new ItemStack[] { new ItemStack(Material.DIAMOND), new ItemStack(Material.STICK) }, "x,s");
		ItemModule.handRecipe("DIAMOND_SHOVEL", VinkaItems.DIAMOND_SHOVEL(), "xoso",
				new ItemStack[] { new ItemStack(Material.DIAMOND), new ItemStack(Material.STICK) }, "x,s");
		ItemModule.handRecipe("DIAMOND_HOE", VinkaItems.DIAMOND_HOE(), "xxso",
				new ItemStack[] { new ItemStack(Material.DIAMOND), new ItemStack(Material.STICK) }, "x,s");
		ItemModule.handRecipe("DIAMOND_AXE", VinkaItems.DIAMOND_AXE(), "sxox",
				new ItemStack[] { new ItemStack(Material.DIAMOND), new ItemStack(Material.STICK) }, "x,s");
		ItemModule.handRecipe("DIAMOND_SWORD", VinkaItems.DIAMOND_SWORD(), "oxsx",
				new ItemStack[] { new ItemStack(Material.DIAMOND), new ItemStack(Material.STICK) }, "x,s");
		
		ItemModule.handRecipe("ENHANCED_PICK", VinkaItems.ENHANCED_PICKAXE(), "xxsx",
				new ItemStack[] { new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.STICK) }, "x,s");
		ItemModule.handRecipe("ENHANCED_SHOVEL", VinkaItems.ENHANCED_SHOVEL(), "xoso",
				new ItemStack[] { new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.STICK) }, "x,s");
		ItemModule.handRecipe("ENHANCED_HOE", VinkaItems.ENHANCED_HOE(), "xxso",
				new ItemStack[] { new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.STICK) }, "x,s");
		ItemModule.handRecipe("ENHANCED_AXE", VinkaItems.ENHANCED_AXE(), "sxox",
				new ItemStack[] { new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.STICK) }, "x,s");
		ItemModule.handRecipe("ENHANCED_SWORD", VinkaItems.ENHANCED_SWORD(), "oxsx",
				new ItemStack[] { new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.STICK) }, "x,s");
	}
}
