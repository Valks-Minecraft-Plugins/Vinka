package com.vinka;

import java.util.Random;

import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.valkcore.modules.ItemModule;
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
import com.vinkaitems.VinkaItem;
import com.vinkaitems.VinkaItems;

public class Vinka extends JavaPlugin {
	public static Vinka vinka;

	@Override
	public void onEnable() {
		vinka = this;

		Server s = getServer();

		setGameRules(s);

		s.clearRecipes();
		addRecipes();

		ItemModule.handRecipeHomeInv();
		ItemModule.furnaceRecipeHomeInv();
		ItemModule.craftingRecipeHomeInv();
		ItemModule.shapelessRecipeHomeInv();

		registerListeners(s);
		registerCommands();

		handleSpawning(s);
	}

	private void setGameRules(Server s) {
		for (World w : s.getWorlds()) {
			w.setGameRule(GameRule.DO_FIRE_TICK, false);
			w.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
			w.setGameRule(GameRule.COMMAND_BLOCK_OUTPUT, false);
			w.setGameRule(GameRule.DO_MOB_LOOT, false);
			w.setGameRule(GameRule.DO_ENTITY_DROPS, false);
			w.setGameRule(GameRule.DO_TILE_DROPS, false);
			w.setGameRule(GameRule.KEEP_INVENTORY, false);
			w.setGameRule(GameRule.MOB_GRIEFING, true);
			w.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
		}
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
		final int radius = 20;

		BukkitScheduler scheduler = getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				// Do something
				for (Player p : s.getOnlinePlayers()) {
					Location playerLocation = p.getLocation();

					Location[] testLocs = Utils.testLocations(playerLocation, radius);
					Location testLoc = testLocs[new Random().nextInt(testLocs.length)];
					Location loc = playerLocation.getWorld().getHighestBlockAt(testLoc).getLocation();

					if (loc.getWorld().getName().equals("world")) {
						int dist = (int) playerLocation.distance(playerLocation.getWorld().getSpawnLocation());

						for (int i = 0; i < 10; i++) {
							if (!spawnRange(dist, 1000 * i, 1000 * (i + 1)))
								continue;
							switch (i) {
							case 0:
							case 1:
							case 2:
								Utils.spawnMonster(loc, EntityType.SLIME);
								break;
							case 3:
								Utils.spawnMonster(loc, EntityType.PHANTOM);
								break;
							case 4:
								Utils.spawnMonster(loc, EntityType.ZOMBIE);
								break;
							case 5:
								Utils.spawnMonster(loc, EntityType.HUSK);
								break;
							case 6:
							case 7:
								Utils.spawnMonster(loc, EntityType.WITHER_SKELETON);
								break;
							default:
								Utils.spawnMonster(loc, EntityType.WITHER_SKELETON);
								break;
							}
						}
					} else {
						Location higherLoc = new Location(loc.getWorld(), loc.getX(), loc.getY() + 30, loc.getZ());
						Utils.spawnMonster(higherLoc, EntityType.GHAST);
					}

					return;

				}
			}
		}, 0L, 20L * 10L);
	}

	private boolean spawnRange(int dist, int minDist, int maxDist) {
		return dist >= minDist && dist <= maxDist;
	}

	private void addRecipes() {
		addHandRecipes();
		addShapelessRecipes();
		addFurnaceRecipes();
		addCraftingRecipes();
	}

	private void addHandRecipes() {
		ItemModule.handRecipe("OAK_TRAPDOOR", VinkaItems.OAK_TRAPDOOR(), "xxxx", new VinkaItem[] { VinkaItems.RABBIT_FOOT() },
				"x");
		ItemModule.handRecipe("OAK_PLANKS", VinkaItems.OAK_PLANKS(), "xxxx", new VinkaItem[] { VinkaItems.OAK_TRAPDOOR() },
				"x");
		ItemModule.handRecipe("CHEST", VinkaItems.CHEST(), "xxxx", new VinkaItem[] { VinkaItems.OAK_PLANKS() }, "x");
		ItemModule.handRecipe("WOODEN_PICK", VinkaItems.WOODEN_PICKAXE(), "xxsx",
				new VinkaItem[] { VinkaItems.OAK_TRAPDOOR(), VinkaItems.STICK() }, "x,s");
		ItemModule.handRecipe("WOODEN_SHOVEL", VinkaItems.WOODEN_SHOVEL(), "xoso",
				new VinkaItem[] { VinkaItems.OAK_TRAPDOOR(), VinkaItems.STICK() }, "x,s");
		ItemModule.handRecipe("WOODEN_HOE", VinkaItems.WOODEN_HOE(), "xxso",
				new VinkaItem[] { VinkaItems.OAK_TRAPDOOR(), VinkaItems.STICK() }, "x,s");
		ItemModule.handRecipe("WOODEN_AXE", VinkaItems.WOODEN_AXE(), "sxox",
				new VinkaItem[] { VinkaItems.OAK_TRAPDOOR(), VinkaItems.STICK() }, "x,s");
		ItemModule.handRecipe("WOODEN_SWORD", VinkaItems.WOODEN_SWORD(), "oxsx",
				new VinkaItem[] { VinkaItems.OAK_TRAPDOOR(), VinkaItems.STICK() }, "x,s");
		ItemModule.handRecipe("BOW", VinkaItems.BOW(), "sxsx",
				new VinkaItem[] { VinkaItems.STRING(), VinkaItems.STICK() }, "s,x");
		ItemModule.handRecipe("ARROW", VinkaItems.ARROW(), "aobo",
				new VinkaItem[] { VinkaItems.RABBIT_FOOT(), VinkaItems.STICK() }, "a,b");

		VinkaItem moreRedstoneTorches = VinkaItems.REDSTONE_TORCH();
		moreRedstoneTorches.getItem().setAmount(2);

		ItemModule.handRecipe("REDSTONE_TORCH", moreRedstoneTorches, "coso",
				new VinkaItem[] { VinkaItems.COAL(), VinkaItems.STICK() }, "c,s");

		ItemModule.handRecipe("LADDER", VinkaItems.LADDER(), "xoxo", new VinkaItem[] { VinkaItems.STICK() }, "x");

		ItemModule.handRecipe("SIGN", VinkaItems.SIGN(), "xxss",
				new VinkaItem[] { VinkaItems.OAK_SLAB(), VinkaItems.STICK() }, "x,s");
		ItemModule.handRecipe("OAK_DOOR", VinkaItems.OAK_DOOR(), "xoxo", new VinkaItem[] { VinkaItems.OAK_PLANKS() },
				"x");

		ItemModule.handRecipe("OAK_SLAB", VinkaItems.OAK_SLAB(), "ooxx",
				new VinkaItem[] { VinkaItems.OAK_SLAB() }, "x");
		ItemModule.handRecipe("OAK_FENCE", VinkaItems.OAK_FENCE(), "ssww",
				new VinkaItem[] { VinkaItems.OAK_SLAB(), VinkaItems.OAK_PLANKS() }, "s,w");
		ItemModule.handRecipe("OAK_GATE", VinkaItems.OAK_FENCE_GATE(), "swws",
				new VinkaItem[] { VinkaItems.STICK(), VinkaItems.OAK_SLAB() }, "s,w");

		ItemModule.handRecipe("FISHING_ROD", VinkaItems.FISHING_ROD(), "ocso",
				new VinkaItem[] { VinkaItems.STRING(), VinkaItems.STICK() }, "c,s");
		ItemModule.handRecipe("OAK_PRESSURE_PLATE", VinkaItems.OAK_PRESSURE_PLATE(), "xoxo",
				new VinkaItem[] { VinkaItems.OAK_SLAB() }, "x");
		ItemModule.handRecipe("WHITE_BED", VinkaItems.WHITE_BED(), "wwxx",
				new VinkaItem[] { VinkaItems.WHITE_WOOL(), VinkaItems.OAK_SLAB() }, "w,x");
		ItemModule.handRecipe("WHITE_BANNER", VinkaItems.WHITE_BANNER(), "woso",
				new VinkaItem[] { VinkaItems.WHITE_WOOL(), VinkaItems.STICK() }, "w,s");

		ItemModule.handRecipe("STONE_SLAB", VinkaItems.STONE_SLAB(), "xxxx", new VinkaItem[] { VinkaItems.GRAY_DYE() },
				"x");

		ItemModule.handRecipe("STONE_PICK", VinkaItems.STONE_PICKAXE(), "xxsx",
				new VinkaItem[] { VinkaItems.STONE_SLAB(), VinkaItems.STICK() }, "x,s");
		ItemModule.handRecipe("STONE_SHOVEL", VinkaItems.STONE_SHOVEL(), "xoso",
				new VinkaItem[] { VinkaItems.STONE_SLAB(), VinkaItems.STICK() }, "x,s");
		ItemModule.handRecipe("STONE_HOE", VinkaItems.STONE_HOE(), "xxso",
				new VinkaItem[] { VinkaItems.STONE_SLAB(), VinkaItems.STICK() }, "x,s");
		ItemModule.handRecipe("STONE_AXE", VinkaItems.STONE_AXE(), "sxox",
				new VinkaItem[] { VinkaItems.STONE_SLAB(), VinkaItems.STICK() }, "x,s");
		ItemModule.handRecipe("STONE_SWORD", VinkaItems.STONE_SWORD(), "oxsx",
				new VinkaItem[] { VinkaItems.STONE_SLAB(), VinkaItems.STICK() }, "x,s");

		ItemModule.handRecipe("STONE", VinkaItems.STONE(), "xxxx", new VinkaItem[] { VinkaItems.STONE_SLAB() }, "x");
		ItemModule.handRecipe("FURNACE", VinkaItems.FURNACE(), "xxxx", new VinkaItem[] { VinkaItems.STONE() }, "x");

		ItemModule.handRecipe("IRON_SLAB", VinkaItems.IRON_TRAPDOOR(), "xxoo",
				new VinkaItem[] { VinkaItems.IRON_INGOT() }, "x");
		ItemModule.handRecipe("IRON_DOOR", VinkaItems.IRON_DOOR(), "xoxo", new VinkaItem[] { VinkaItems.IRON_INGOT() },
				"x");
		ItemModule.handRecipe("IRON_BLOCK", VinkaItems.IRON_BLOCK(), "xxxx",
				new VinkaItem[] { VinkaItems.IRON_INGOT() }, "x");
		ItemModule.handRecipe("IRON_PICK", VinkaItems.IRON_PICKAXE(), "xxsx",
				new VinkaItem[] { VinkaItems.IRON_INGOT(), VinkaItems.STICK() }, "x,s");
		ItemModule.handRecipe("IRON_SHOVEL", VinkaItems.IRON_SHOVEL(), "xoso",
				new VinkaItem[] { VinkaItems.IRON_INGOT(), VinkaItems.STICK() }, "x,s");
		ItemModule.handRecipe("IRON_HOE", VinkaItems.IRON_HOE(), "xxso",
				new VinkaItem[] { VinkaItems.IRON_INGOT(), VinkaItems.STICK() }, "x,s");
		ItemModule.handRecipe("IRON_AXE", VinkaItems.IRON_AXE(), "sxox",
				new VinkaItem[] { VinkaItems.IRON_INGOT(), VinkaItems.STICK() }, "x,s");
		ItemModule.handRecipe("IRON_SWORD", VinkaItems.IRON_SWORD(), "oxsx",
				new VinkaItem[] { VinkaItems.IRON_INGOT(), VinkaItems.STICK() }, "x,s");

		ItemModule.handRecipe("FLINT_AND_STEEL", VinkaItems.FLINT_AND_STEEL(), "xoox",
				new VinkaItem[] { VinkaItems.IRON_INGOT() }, "x");
		ItemModule.handRecipe("BUCKET", VinkaItems.BUCKET(), "xoxo", new VinkaItem[] { VinkaItems.STONE_SLAB() }, "x");

		ItemModule.handRecipe("HAND_CHISELED_SANDSTONE", VinkaItems.CHISELED_SANDSTONE(), "xxxx",
				new VinkaItem[] { VinkaItems.SMOOTH_SANDSTONE() }, "x");
		ItemModule.handRecipe("HAND_SMOOTH_SANDSTONE", VinkaItems.SMOOTH_SANDSTONE(), "xxxx",
				new VinkaItem[] { VinkaItems.SANDSTONE() }, "x");
		ItemModule.handRecipe("HAND_SANDSTONE", VinkaItems.SANDSTONE(), "xxxx", new VinkaItem[] { VinkaItems.SAND() },
				"x");
		ItemModule.handRecipe("HAND_MAP", VinkaItems.MAP(), "xxxx", new VinkaItem[] { VinkaItems.PAPER() }, "x");
		ItemModule.handRecipe("HAND_OAK_STAIRS", VinkaItems.OAK_STAIRS(), "xoxx",
				new VinkaItem[] { VinkaItems.OAK_SLAB() }, "x");
		ItemModule.handRecipe("HAND_POTION_THICK", VinkaItems.POTION_THICK(), "xxxx",
				new VinkaItem[] { VinkaItems.DRIED_KELP() }, "x");
		ItemModule.handRecipe("BONE_MEAL", VinkaItems.BONE_MEAL(), "xxoo", new VinkaItem[] { VinkaItems.SUGAR() }, "x");
		ItemModule.handRecipe("SHEARS", VinkaItems.SHEARS(), "oxxx", new VinkaItem[] { VinkaItems.IRON_INGOT() }, "x");

		ItemModule.handRecipe("GLOWSTONE", VinkaItems.GLOWSTONE(), "xxxx", new VinkaItem[] { VinkaItems.TORCH() }, "x");
		ItemModule.handRecipe("TORCH", VinkaItems.TORCH(), "xxxx", new VinkaItem[] { VinkaItems.REDSTONE_TORCH() },
				"x");
		ItemModule.handRecipe("LEAD", VinkaItems.LEAD(), "osso", new VinkaItem[] { VinkaItems.STICK() }, "s");
		ItemModule.handRecipe("OBSIDIAN", VinkaItems.OBSIDIAN(), "xxxx", new VinkaItem[] { VinkaItems.DIAMOND() }, "x");
		ItemModule.handRecipe("ENCHANTMENT_TABLE", VinkaItems.ENCHANTING_TABLE(), "xxxx",
				new VinkaItem[] { VinkaItems.OBSIDIAN() }, "x");
		ItemModule.handRecipe("BREWER", VinkaItems.BREWING_STAND(), "xxxx",
				new VinkaItem[] { VinkaItems.WHEAT_SEEDS() }, "x");
		ItemModule.handRecipe("CAULDRON", VinkaItems.CAULDRON(), "bbaa",
				new VinkaItem[] { VinkaItems.STONE(), VinkaItems.STONE_SLAB() }, "a,b");
		ItemModule.handRecipe("ELYTRA", VinkaItems.ELYTRA(), "xxxx", new VinkaItem[] { VinkaItems.EMERALD() }, "x");
		ItemModule.handRecipe("REDSTONE", VinkaItems.REDSTONE(), "xxxx", new VinkaItem[] { VinkaItems.BEETROOT() },
				"x");
		ItemModule.handRecipe("PISTON", VinkaItems.PISTON(), "abab",
				new VinkaItem[] { VinkaItems.REDSTONE(), VinkaItems.OAK_PLANKS() }, "a,b");
		ItemModule.handRecipe("DISPENSER", VinkaItems.DISPENSER(), "xxxx", new VinkaItem[] { VinkaItems.REDSTONE() },
				"x");

		ItemModule.handRecipe("COAL", VinkaItems.LIGHT_GRAY_DYE(), "xxxx", new VinkaItem[] { VinkaItems.COAL() }, "x");
		ItemModule.handRecipe("HOPPER", VinkaItems.HOPPER(), "aobo",
				new VinkaItem[] { VinkaItems.IRON_INGOT(), VinkaItems.CHEST() }, "a,b");

		ItemModule.handRecipe("TNT", VinkaItems.TNT(), "xxxx", new VinkaItem[] { VinkaItems.SUGAR() }, "x");
		ItemModule.handRecipe("LEVER", VinkaItems.LEVER(), "aobo",
				new VinkaItem[] { VinkaItems.STONE_BUTTON(), VinkaItems.STICK() }, "a,b");
		ItemModule.handRecipe("SHIELD", VinkaItems.SHIELD(), "aobo",
				new VinkaItem[] { VinkaItems.OAK_PLANKS(), VinkaItems.OAK_TRAPDOOR() }, "a,b");

		ItemModule.handRecipe("GOLD_PICK", VinkaItems.GOLDEN_PICKAXE(), "xxsx",
				new VinkaItem[] { VinkaItems.DANDELION_YELLOW(), VinkaItems.STICK() }, "x,s");
		ItemModule.handRecipe("GOLD_SHOVEL", VinkaItems.GOLDEN_SHOVEL(), "xoso",
				new VinkaItem[] { VinkaItems.DANDELION_YELLOW(), VinkaItems.STICK() }, "x,s");
		ItemModule.handRecipe("GOLD_HOE", VinkaItems.GOLDEN_HOE(), "xxso",
				new VinkaItem[] { VinkaItems.DANDELION_YELLOW(), VinkaItems.STICK() }, "x,s");
		ItemModule.handRecipe("GOLD_AXE", VinkaItems.GOLDEN_AXE(), "sxox",
				new VinkaItem[] { VinkaItems.DANDELION_YELLOW(), VinkaItems.STICK() }, "x,s");
		ItemModule.handRecipe("GOLD_SWORD", VinkaItems.GOLDEN_SWORD(), "oxsx",
				new VinkaItem[] { VinkaItems.DANDELION_YELLOW(), VinkaItems.STICK() }, "x,s");
		ItemModule.handRecipe("DIAMOND_PICK", VinkaItems.DIAMOND_PICKAXE(), "xxsx",
				new VinkaItem[] { VinkaItems.DIAMOND(), VinkaItems.STICK() }, "x,s");
		ItemModule.handRecipe("DIAMOND_SHOVEL", VinkaItems.DIAMOND_SHOVEL(), "xoso",
				new VinkaItem[] { VinkaItems.DIAMOND(), VinkaItems.STICK() }, "x,s");
		ItemModule.handRecipe("DIAMOND_HOE", VinkaItems.DIAMOND_HOE(), "xxso",
				new VinkaItem[] { VinkaItems.DIAMOND(), VinkaItems.STICK() }, "x,s");
		ItemModule.handRecipe("DIAMOND_AXE", VinkaItems.DIAMOND_AXE(), "sxox",
				new VinkaItem[] { VinkaItems.DIAMOND(), VinkaItems.STICK() }, "x,s");
		ItemModule.handRecipe("DIAMOND_SWORD", VinkaItems.DIAMOND_SWORD(), "oxsx",
				new VinkaItem[] { VinkaItems.DIAMOND(), VinkaItems.STICK() }, "x,s");
		ItemModule.handRecipe("ENHANCED_PICK", VinkaItems.ENHANCED_PICKAXE(), "xxsx",
				new VinkaItem[] { VinkaItems.ENDER_PEARL(), VinkaItems.STICK() }, "x,s");
		ItemModule.handRecipe("ENHANCED_SHOVEL", VinkaItems.ENHANCED_SHOVEL(), "xoso",
				new VinkaItem[] { VinkaItems.ENDER_PEARL(), VinkaItems.STICK() }, "x,s");
		ItemModule.handRecipe("ENHANCED_HOE", VinkaItems.ENHANCED_HOE(), "xxso",
				new VinkaItem[] { VinkaItems.ENDER_PEARL(), VinkaItems.STICK() }, "x,s");
		ItemModule.handRecipe("ENHANCED_AXE", VinkaItems.ENHANCED_AXE(), "sxox",
				new VinkaItem[] { VinkaItems.ENDER_PEARL(), VinkaItems.STICK() }, "x,s");
		ItemModule.handRecipe("ENHANCED_SWORD", VinkaItems.ENHANCED_SWORD(), "oxsx",
				new VinkaItem[] { VinkaItems.ENDER_PEARL(), VinkaItems.STICK() }, "x,s");
	}

	private void addShapelessRecipes() {
		ItemModule.shapelessRecipe("STICK", VinkaItems.STICK(),
				new VinkaItem[] { VinkaItems.RABBIT_FOOT(), VinkaItems.RABBIT_FOOT() });

		ItemModule.shapelessRecipe("CRAFTING_TABLE", VinkaItems.CRAFTING_TABLE(), new VinkaItem[] {
				VinkaItems.IRON_AXE(), VinkaItems.IRON_PICKAXE(), VinkaItems.IRON_HOE(), VinkaItems.IRON_SHOVEL() });

		ItemModule.shapelessRecipe("SHAPELESS_DROPPER", VinkaItems.DROPPER(),
				new VinkaItem[] { VinkaItems.DISPENSER() });
		ItemModule.shapelessRecipe("SHAPELESS_PAPER", VinkaItems.PAPER(), new VinkaItem[] { VinkaItems.BONE_MEAL() });
		ItemModule.shapelessRecipe("SHAPELESS_ENDERCHEST", VinkaItems.ENDER_CHEST(),
				new VinkaItem[] { VinkaItems.CHEST(), VinkaItems.ENDER_PEARL() });
		ItemModule.shapelessRecipe("SHAPELESS_GLASS_PANE", VinkaItems.GLASS_PANE(),
				new VinkaItem[] { VinkaItems.GLASS() });

		VinkaItem sugar = VinkaItems.SUGAR();
		sugar.getItem().setAmount(9);
		ItemModule.shapelessRecipe("SOULS_FROM_SOUL_SAND", sugar, new VinkaItem[] { VinkaItems.SOUL_SAND() });
		ItemModule.shapelessRecipe("STICKY_PISTON", VinkaItems.STICKY_PISTON(),
				new VinkaItem[] { VinkaItems.PISTON() });
		ItemModule.shapelessRecipe("SEA_LANTERN", VinkaItems.SEA_LANTERN(), new VinkaItem[] { VinkaItems.GLOWSTONE() });
		ItemModule.shapelessRecipe("OAK_SAPLING", VinkaItems.OAK_SAPLING(), new VinkaItem[] { VinkaItems.SUGAR() });
		ItemModule.shapelessRecipe("MULE_SPAWN_EGG", VinkaItems.MULE_SPAWN_EGG(),
				new VinkaItem[] { VinkaItems.LLAMA_SPAWN_EGG() });
		ItemModule.shapelessRecipe("LLAMA_SPAWN_EGG", VinkaItems.LLAMA_SPAWN_EGG(),
				new VinkaItem[] { VinkaItems.DONKEY_SPAWN_EGG() });
		ItemModule.shapelessRecipe("DONKEY_SPAWN_EGG", VinkaItems.DONKEY_SPAWN_EGG(),
				new VinkaItem[] { VinkaItems.HORSE_SPAWN_EGG() });
		ItemModule.shapelessRecipe("ITEM_FRAME", VinkaItems.ITEM_FRAME(),
				new VinkaItem[] { VinkaItems.STICK(), VinkaItems.STICK(), VinkaItems.STICK() });
		ItemModule.shapelessRecipe("OAK_STAIRS", VinkaItems.OAK_STAIRS(),
				new VinkaItem[] { VinkaItems.OAK_PLANKS(), VinkaItems.OAK_PLANKS(), VinkaItems.OAK_PLANKS() });
		ItemModule.shapelessRecipe("OAK_BUTTON", VinkaItems.OAK_BUTTON(),
				new VinkaItem[] { VinkaItems.OAK_PRESSURE_PLATE() });
		ItemModule.shapelessRecipe("BOWL", VinkaItems.BOWL(),
				new VinkaItem[] { VinkaItems.RABBIT_FOOT(), VinkaItems.RABBIT_FOOT(), VinkaItems.RABBIT_FOOT() });
		ItemModule.shapelessRecipe("STRING", VinkaItems.STRING(), new VinkaItem[] { new VinkaItem(Material.INK_SAC),
				new VinkaItem(Material.INK_SAC), new VinkaItem(Material.INK_SAC), new VinkaItem(Material.INK_SAC) });
		ItemModule.shapelessRecipe("WHITE_WOOL", VinkaItems.WHITE_WOOL(),
				new VinkaItem[] { VinkaItems.STRING(), VinkaItems.STRING(), VinkaItems.STRING(), VinkaItems.STRING() });
		ItemModule.shapelessRecipe("OAK_SLAB_FROM_OAK_PLANKS", VinkaItems.OAK_SLAB(),
				new VinkaItem[] { VinkaItems.OAK_PLANKS() });
		ItemModule.shapelessRecipe("RABBIT_FOOT_FROM_OAK_SLAB", VinkaItems.RABBIT_FOOT(),
				new VinkaItem[] { VinkaItems.OAK_SLAB() });
		ItemModule.shapelessRecipe("OAK_PLANKS_FROM_OAK_DOOR", VinkaItems.OAK_PLANKS(),
				new VinkaItem[] { VinkaItems.OAK_DOOR() });
		ItemModule.shapelessRecipe("OAK_PLANKS_FROM_OAK_STAIRS", VinkaItems.OAK_PLANKS(),
				new VinkaItem[] { VinkaItems.OAK_STAIRS() });
		ItemModule.shapelessRecipe("STONE_BUTTON", VinkaItems.STONE_BUTTON(),
				new VinkaItem[] { VinkaItems.GRAY_DYE() });
		ItemModule.shapelessRecipe("STONE_PRESSUREPLATE", VinkaItems.STONE_PRESSURE_PLATE(),
				new VinkaItem[] { VinkaItems.STONE_SLAB() });

		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_ELYTRA", VinkaItems.ELYTRA(),
				new VinkaItem[] { VinkaItems.ELYTRA(), VinkaItems.ENDER_PEARL() });

		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_WOOD_PICKAXE", VinkaItems.WOODEN_PICKAXE(),
				new VinkaItem[] { VinkaItems.WOODEN_PICKAXE(), VinkaItems.OAK_SLAB() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_STONE_PICKAXE", VinkaItems.STONE_PICKAXE(),
				new VinkaItem[] { VinkaItems.STONE_PICKAXE(), VinkaItems.STONE_SLAB() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_IRON_PICKAXE", VinkaItems.IRON_PICKAXE(),
				new VinkaItem[] { VinkaItems.IRON_PICKAXE(), VinkaItems.IRON_INGOT() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_GOLD_PICKAXE", VinkaItems.GOLDEN_PICKAXE(),
				new VinkaItem[] { VinkaItems.GOLDEN_PICKAXE(), VinkaItems.DANDELION_YELLOW() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_DIAMOND_PICKAXE", VinkaItems.DIAMOND_PICKAXE(),
				new VinkaItem[] { VinkaItems.DIAMOND_PICKAXE(), VinkaItems.DIAMOND() });

		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_WOOD_AXE", VinkaItems.WOODEN_AXE(),
				new VinkaItem[] { VinkaItems.WOODEN_AXE(), VinkaItems.OAK_SLAB() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_STONE_AXE", VinkaItems.STONE_AXE(),
				new VinkaItem[] { VinkaItems.STONE_AXE(), VinkaItems.STONE_SLAB() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_IRON_AXE", VinkaItems.IRON_AXE(),
				new VinkaItem[] { VinkaItems.IRON_AXE(), VinkaItems.IRON_INGOT() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_GOLD_AXE", VinkaItems.GOLDEN_AXE(),
				new VinkaItem[] { VinkaItems.GOLDEN_AXE(), VinkaItems.DANDELION_YELLOW() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_DIAMOND_AXE", VinkaItems.DIAMOND_AXE(),
				new VinkaItem[] { VinkaItems.DIAMOND_AXE(), VinkaItems.DIAMOND() });

		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_WOOD_SHOVEL", VinkaItems.WOODEN_SHOVEL(),
				new VinkaItem[] { VinkaItems.WOODEN_SHOVEL(), VinkaItems.OAK_SLAB() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_STONE_SHOVEL", VinkaItems.STONE_SHOVEL(),
				new VinkaItem[] { VinkaItems.STONE_SHOVEL(), VinkaItems.STONE_SLAB() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_IRON_SHOVEL", VinkaItems.IRON_SHOVEL(),
				new VinkaItem[] { VinkaItems.IRON_SHOVEL(), VinkaItems.IRON_INGOT() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_GOLD_SHOVEL", VinkaItems.GOLDEN_SHOVEL(),
				new VinkaItem[] { VinkaItems.GOLDEN_SHOVEL(), VinkaItems.DANDELION_YELLOW() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_DIAMOND_SHOVEL", VinkaItems.DIAMOND_SHOVEL(),
				new VinkaItem[] { VinkaItems.DIAMOND_SHOVEL(), VinkaItems.DIAMOND() });

		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_WOOD_HOE", VinkaItems.WOODEN_HOE(),
				new VinkaItem[] { VinkaItems.WOODEN_HOE(), VinkaItems.OAK_SLAB() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_STONE_HOE", VinkaItems.STONE_HOE(),
				new VinkaItem[] { VinkaItems.STONE_HOE(), VinkaItems.STONE_SLAB() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_IRON_HOE", VinkaItems.IRON_HOE(),
				new VinkaItem[] { VinkaItems.IRON_HOE(), VinkaItems.IRON_INGOT() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_GOLD_HOE", VinkaItems.GOLDEN_HOE(),
				new VinkaItem[] { VinkaItems.GOLDEN_HOE(), VinkaItems.DANDELION_YELLOW() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_DIAMOND_HOE", VinkaItems.DIAMOND_HOE(),
				new VinkaItem[] { VinkaItems.DIAMOND_HOE(), VinkaItems.DIAMOND() });

		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_WOOD_SWORD", VinkaItems.WOODEN_SWORD(),
				new VinkaItem[] { VinkaItems.WOODEN_SWORD(), VinkaItems.OAK_SLAB() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_STONE_SWORD", VinkaItems.STONE_SWORD(),
				new VinkaItem[] { VinkaItems.STONE_SWORD(), VinkaItems.STONE_SLAB() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_IRON_SWORD", VinkaItems.IRON_SWORD(),
				new VinkaItem[] { VinkaItems.IRON_SWORD(), VinkaItems.IRON_INGOT() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_GOLD_SWORD", VinkaItems.GOLDEN_SWORD(),
				new VinkaItem[] { VinkaItems.GOLDEN_SWORD(), VinkaItems.DANDELION_YELLOW() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_DIAMOND_SWORD", VinkaItems.DIAMOND_SWORD(),
				new VinkaItem[] { VinkaItems.DIAMOND_SWORD(), VinkaItems.DIAMOND() });

		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_BOW", VinkaItems.BOW(),
				new VinkaItem[] { VinkaItems.BOW(), VinkaItems.OAK_TRAPDOOR() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_FLINT_AND_STEEL", VinkaItems.FLINT_AND_STEEL(),
				new VinkaItem[] { VinkaItems.FLINT_AND_STEEL(), VinkaItems.IRON_INGOT() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_SHEARS", VinkaItems.SHEARS(),
				new VinkaItem[] { VinkaItems.SHEARS(), VinkaItems.IRON_INGOT() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_FISHING_ROD", VinkaItems.FISHING_ROD(),
				new VinkaItem[] { VinkaItems.FISHING_ROD(), VinkaItems.OAK_TRAPDOOR() });

		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_LEATHER_HELMET", VinkaItems.LEATHER_HELMET(),
				new VinkaItem[] { VinkaItems.LEATHER_HELMET(), VinkaItems.OAK_TRAPDOOR() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_LEATHER_CHESTPLATE", VinkaItems.LEATHER_CHESTPLATE(),
				new VinkaItem[] { VinkaItems.LEATHER_CHESTPLATE(), VinkaItems.OAK_TRAPDOOR() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_LEATHER_LEGGINGS", VinkaItems.LEATHER_LEGGINGS(),
				new VinkaItem[] { VinkaItems.LEATHER_LEGGINGS(), VinkaItems.OAK_TRAPDOOR() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_LEATHER_BOOTS", VinkaItems.LEATHER_BOOTS(),
				new VinkaItem[] { VinkaItems.LEATHER_BOOTS(), VinkaItems.OAK_TRAPDOOR() });

		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_IRON_HELMET", VinkaItems.IRON_HELMET(),
				new VinkaItem[] { VinkaItems.IRON_HELMET(), VinkaItems.IRON_INGOT() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_IRON_CHESTPLATE", VinkaItems.IRON_CHESTPLATE(),
				new VinkaItem[] { VinkaItems.IRON_CHESTPLATE(), VinkaItems.IRON_INGOT() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_IRON_LEGGINGS", VinkaItems.IRON_LEGGINGS(),
				new VinkaItem[] { VinkaItems.IRON_LEGGINGS(), VinkaItems.IRON_INGOT() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_IRON_BOOTS", VinkaItems.IRON_BOOTS(),
				new VinkaItem[] { VinkaItems.IRON_BOOTS(), VinkaItems.IRON_INGOT() });

		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_GOLD_HELMET", VinkaItems.GOLDEN_HELMET(),
				new VinkaItem[] { VinkaItems.GOLDEN_HELMET(), VinkaItems.DANDELION_YELLOW() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_GOLD_CHESTPLATE", VinkaItems.GOLDEN_CHESTPLATE(),
				new VinkaItem[] { VinkaItems.GOLDEN_CHESTPLATE(), VinkaItems.DANDELION_YELLOW() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_GOLD_LEGGINGS", VinkaItems.GOLDEN_LEGGINGS(),
				new VinkaItem[] { VinkaItems.GOLDEN_LEGGINGS(), VinkaItems.DANDELION_YELLOW() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_GOLD_BOOTS", VinkaItems.GOLDEN_BOOTS(),
				new VinkaItem[] { VinkaItems.GOLDEN_BOOTS(), VinkaItems.DANDELION_YELLOW() });

		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_DIAMOND_HELMET", VinkaItems.DIAMOND_HELMET(),
				new VinkaItem[] { VinkaItems.DIAMOND_HELMET(), VinkaItems.DIAMOND() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_DIAMOND_CHESTPLATE", VinkaItems.DIAMOND_CHESTPLATE(),
				new VinkaItem[] { VinkaItems.DIAMOND_CHESTPLATE(), VinkaItems.DIAMOND() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_DIAMOND_LEGGINGS", VinkaItems.DIAMOND_LEGGINGS(),
				new VinkaItem[] { VinkaItems.DIAMOND_LEGGINGS(), VinkaItems.DIAMOND() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_DIAMOND_BOOTS", VinkaItems.DIAMOND_BOOTS(),
				new VinkaItem[] { VinkaItems.DIAMOND_BOOTS(), VinkaItems.DIAMOND() });

		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_ENHANCED_HELMET", VinkaItems.ENHANCED_HELMET(),
				new VinkaItem[] { VinkaItems.ENHANCED_HELMET(), VinkaItems.ENDER_PEARL() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_ENHANCED_CHESTPLATE", VinkaItems.ENHANCED_CHESTPLATE(),
				new VinkaItem[] { VinkaItems.ENHANCED_CHESTPLATE(), VinkaItems.ENDER_PEARL() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_ENHANCED_LEGGINGS", VinkaItems.ENHANCED_LEGGINGS(),
				new VinkaItem[] { VinkaItems.ENHANCED_LEGGINGS(), VinkaItems.ENDER_PEARL() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_ENHANCED_BOOTS", VinkaItems.ENHANCED_BOOTS(),
				new VinkaItem[] { VinkaItems.ENHANCED_BOOTS(), VinkaItems.ENDER_PEARL() });

		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_ENHANCED_PICKAXE", VinkaItems.ENHANCED_PICKAXE(),
				new VinkaItem[] { VinkaItems.ENHANCED_PICKAXE(), VinkaItems.ENDER_PEARL() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_ENHANCED_AXE", VinkaItems.ENHANCED_AXE(),
				new VinkaItem[] { VinkaItems.ENHANCED_AXE(), VinkaItems.ENDER_PEARL() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_ENHANCED_SHOVEL", VinkaItems.ENHANCED_SHOVEL(),
				new VinkaItem[] { VinkaItems.ENHANCED_SHOVEL(), VinkaItems.ENDER_PEARL() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_ENHANCED_HOE", VinkaItems.ENHANCED_HOE(),
				new VinkaItem[] { VinkaItems.ENHANCED_HOE(), VinkaItems.ENDER_PEARL() });
		ItemModule.shapelessRecipe("SHAPELESS_REPAIR_ENHANCED_SWORD", VinkaItems.ENHANCED_SWORD(),
				new VinkaItem[] { VinkaItems.ENHANCED_SWORD(), VinkaItems.ENDER_PEARL() });

		ItemModule.shapelessRecipe("SHAPELESS_POTION_SPEED", VinkaItems.POTION_SPEED(),
				new VinkaItem[] { VinkaItems.POTION_THICK(), VinkaItems.BEETROOT() });
		ItemModule.shapelessRecipe("SHAPELESS_POTION_NIGHT_VISION", VinkaItems.POTION_NIGHT_VISION(),
				new VinkaItem[] { VinkaItems.POTION_THICK(), VinkaItems.CARROT() });
		ItemModule.shapelessRecipe("SHAPELESS_POTION_STRENGTH", VinkaItems.POTION_STRENGTH(),
				new VinkaItem[] { VinkaItems.POTION_THICK(), VinkaItems.BAKED_POTATO() });
		ItemModule.shapelessRecipe("SHAPELESS_POTION_INSTANT_HEAL", VinkaItems.POTION_INSTANT_HEAL(),
				new VinkaItem[] { VinkaItems.POTION_THICK(), VinkaItems.COOKIE() });
		ItemModule.shapelessRecipe("SHAPELESS_POTION_INVISIBILITY", VinkaItems.POTION_INVISIBILITY(),
				new VinkaItem[] { VinkaItems.POTION_THICK(), VinkaItems.STONE() });
		ItemModule.shapelessRecipe("SHAPELESS_POTION_JUMP", VinkaItems.POTION_JUMP(),
				new VinkaItem[] { VinkaItems.POTION_THICK(), VinkaItems.RABBIT_FOOT() });
		ItemModule.shapelessRecipe("SHAPELESS_POTION_FIRE_RESITANCE", VinkaItems.POTION_FIRE_RESISTANCE(),
				new VinkaItem[] { VinkaItems.POTION_THICK(), VinkaItems.DANDELION_YELLOW() });
		ItemModule.shapelessRecipe("SHAPELESS_POTION_INSTANT_DAMAGE", VinkaItems.POTION_INSTANT_DAMAGE(),
				new VinkaItem[] { VinkaItems.POTION_THICK(), VinkaItems.LIGHT_GRAY_DYE() });
		ItemModule.shapelessRecipe("SHAPELESS_POTION_LUCK", VinkaItems.POTION_LUCK(),
				new VinkaItem[] { VinkaItems.POTION_THICK(), VinkaItems.BLUE_WOOL() });
		ItemModule.shapelessRecipe("SHAPELESS_POTION_POISON", VinkaItems.POTION_POISON(),
				new VinkaItem[] { VinkaItems.POTION_THICK(), VinkaItems.REDSTONE() });
		ItemModule.shapelessRecipe("SHAPELESS_POTION_REGEN", VinkaItems.POTION_REGEN(), new VinkaItem[] {
				VinkaItems.POTION_THICK(), VinkaItems.COOKIE(), VinkaItems.COOKIE(), VinkaItems.COOKIE() });
		ItemModule.shapelessRecipe("SHAPELESS_POTION_SLOW_FALLING", VinkaItems.POTION_SLOW_FALLING(),
				new VinkaItem[] { VinkaItems.POTION_THICK(), VinkaItems.STRING() });
		ItemModule.shapelessRecipe("SHAPELESS_POTION_SLOWNESS", VinkaItems.POTION_SLOWNESS(),
				new VinkaItem[] { VinkaItems.POTION_THICK(), VinkaItems.COAL() });
		ItemModule.shapelessRecipe("SHAPELESS_POTION_WEAKNESS", VinkaItems.POTION_WEAKNESS(),
				new VinkaItem[] { VinkaItems.POTION_THICK(), VinkaItems.IRON_INGOT() });
		ItemModule.shapelessRecipe("SHAPELESS_POTION_WATER_BREATHING", VinkaItems.POTION_WATER_BREATHING(),
				new VinkaItem[] { VinkaItems.POTION_THICK(), VinkaItems.DANDELION_YELLOW() });
	}

	private void addCraftingRecipes() {
		ItemModule.craftedRecipe("CRAFTED_UNREFINED_GOLD", VinkaItems.DANDELION_YELLOW(), "xxxxxxxxx",
				new VinkaItem[] { VinkaItems.IRON_INGOT() }, "x");
		ItemModule.craftedRecipe("CRAFTED_SEEDS", VinkaItems.WHEAT_SEEDS(), "xxxxxxxxx",
				new VinkaItem[] { VinkaItems.MELON_SEEDS() }, "x");
		ItemModule.craftedRecipe("CRAFTED_CARVED_PUMPKIN", VinkaItems.CARVED_PUMPKIN(), "xxxxxxxxx",
				new VinkaItem[] { VinkaItems.WHEAT_SEEDS() }, "x");
		ItemModule.craftedRecipe("CRAFTED_ANVIL", VinkaItems.ANVIL(), "xxxxoxxxx", new VinkaItem[] { VinkaItems.IRON_INGOT() },
				"x");
		ItemModule.craftedRecipe("CRAFTED_BOOK_SHOP", VinkaItems.BOOK_SHOP(), "aaaabaaaa", new VinkaItem[] {VinkaItems.PAPER(), VinkaItems.GOLD_NUGGET()}, "a,b");
		ItemModule.craftedRecipe("CRAFTED_BOOK", VinkaItems.BOOK(), "xxxxxxxxx", new VinkaItem[] {VinkaItems.PAPER()}, "x");
		ItemModule.craftedRecipe("CRAFTED_BOOKSHELF", VinkaItems.BOOKSHELF(), "aaaabaaaa", new VinkaItem[] {VinkaItems.OAK_SLAB(), VinkaItems.BOOK()}, "a,b");

		ItemModule.craftedRecipe("CRAFTED_WOOD_BOOTS", VinkaItems.LEATHER_BOOTS(), "ooooooxox",
				new VinkaItem[] { VinkaItems.OAK_PLANKS() }, "x");
		ItemModule.craftedRecipe("CRAFTED_WOOD_LEGGINGS", VinkaItems.LEATHER_LEGGINGS(), "oooxoxxox",
				new VinkaItem[] { VinkaItems.OAK_PLANKS() }, "x");
		ItemModule.craftedRecipe("CRAFTED_WOOD_CHESTPLATE", VinkaItems.LEATHER_CHESTPLATE(), "oooxxxxxx",
				new VinkaItem[] { VinkaItems.OAK_PLANKS() }, "x");
		ItemModule.craftedRecipe("CRAFTED_WOOD_HELMET", VinkaItems.LEATHER_HELMET(), "xxxoooooo",
				new VinkaItem[] { VinkaItems.OAK_PLANKS() }, "x");

		ItemModule.craftedRecipe("CRAFTED_WOLF_SPAWN_EGG", VinkaItems.WOLF_SPAWN_EGG(), "xxxxaxxxx",
				new VinkaItem[] { VinkaItems.SUGAR(), VinkaItems.BEETROOT_SEEDS() }, "x,a");
		ItemModule.craftedRecipe("CRAFTED_HORSE_SPAWN_EGG", VinkaItems.HORSE_SPAWN_EGG(), "xxxxaxxxx",
				new VinkaItem[] { VinkaItems.SUGAR(), VinkaItems.WHEAT_SEEDS() }, "x,a");
		ItemModule.craftedRecipe("CRAFTED_SHEEP_SPAWN_EGG", VinkaItems.SHEEP_SPAWN_EGG(), "xxxxaxxxx",
				new VinkaItem[] { VinkaItems.SUGAR(), VinkaItems.IRON_INGOT() }, "x,a");
		ItemModule.craftedRecipe("CRAFTED_VILLAGER_SPAWN_EGG", VinkaItems.VILLAGER_SPAWN_EGG(), "xxxxaxxxx",
				new VinkaItem[] { VinkaItems.SUGAR(), VinkaItems.EMERALD() }, "x,a");
		ItemModule.craftedRecipe("CRAFTED_CHICKEN_SPAWN_EGG", VinkaItems.CHICKEN_SPAWN_EGG(), "xxxxaxxxx",
				new VinkaItem[] { VinkaItems.SUGAR(), VinkaItems.DIAMOND() }, "x,a");

		ItemModule.craftedRecipe("CRAFTED_GLAZED_TERRACOTTA_WHITE", VinkaItems.WHITE_GLAZED_TERRACOTTA(), "aaaabaaaa",
				new VinkaItem[] { VinkaItems.STONE(), VinkaItems.LAPIS_BLOCK() }, "a,b");
		ItemModule.craftedRecipe("CRAFTED_ORANGE_GLAZED_TERRACOTTA", VinkaItems.ORANGE_GLAZED_TERRACOTTA(), "oooexeobo",
				new VinkaItem[] { VinkaItems.ENDER_PEARL(), VinkaItems.LAPIS_BLOCK(),
						VinkaItems.WHITE_GLAZED_TERRACOTTA() },
				"e,b,x");
		ItemModule.craftedRecipe("CRAFTED_MAGENTA_GLAZED_TERRACOTTA", VinkaItems.MAGENTA_GLAZED_TERRACOTTA(),
				"oaoexeobo", new VinkaItem[] { VinkaItems.GOLDEN_APPLE(), VinkaItems.WHITE_GLAZED_TERRACOTTA(),
						VinkaItems.ORANGE_GLAZED_TERRACOTTA(), VinkaItems.SOUL_SAND() },
				"e,b,x,a");
		ItemModule.craftedRecipe("CRAFTED_LIGHT_BLUE_GLAZED_TERRACOTTA", VinkaItems.LIGHT_BLUE_GLAZED_TERRACOTTA(),
				"cacexeobo",
				new VinkaItem[] { VinkaItems.LIGHT_BLUE_DYE(), VinkaItems.ORANGE_GLAZED_TERRACOTTA(),
						VinkaItems.MAGENTA_GLAZED_TERRACOTTA(), VinkaItems.CARVED_PUMPKIN(), VinkaItems.SOUL_SAND() },
				"e,b,x,a,c");
		ItemModule.craftedRecipe("CRAFTED_YELLOW_GLAZED_TERRACOTTA", VinkaItems.YELLOW_GLAZED_TERRACOTTA(), "ababxbaba",
				new VinkaItem[] { VinkaItems.ENDER_PEARL(), VinkaItems.ENDER_EYE(),
						VinkaItems.LIGHT_BLUE_GLAZED_TERRACOTTA() },
				"a,b,x");

		ItemModule.craftedRecipe("CRAFTED_EYE_OF_ENDER", VinkaItems.ENDER_EYE(), "xxxxxxxxx",
				new VinkaItem[] { VinkaItems.ENDER_PEARL() }, "x");
		ItemModule.craftedRecipe("LAPIS_BLOCK", VinkaItems.LAPIS_BLOCK(), "xxxxxxxxx",
				new VinkaItem[] { VinkaItems.DIAMOND() }, "x");

		ItemModule.craftedRecipe("CRAFTED_SAND_BLOCK", VinkaItems.SAND(), "xxxxoxxxx",
				new VinkaItem[] { VinkaItems.DRIED_KELP() }, "x");
		ItemModule.craftedRecipe("CRAFTED_RED_SAND", VinkaItems.RED_SAND(), "xxxxaxxxx",
				new VinkaItem[] { VinkaItems.REDSTONE(), VinkaItems.SAND() }, "x,a");
		ItemModule.craftedRecipe("CRAFTED_WHITE_WOOL", VinkaItems.WHITE_WOOL(), "xxxxxxxxx",
				new VinkaItem[] { VinkaItems.STRING() }, "x");

		ItemModule.craftedRecipe("CRAFTED_SUGAR_CANE", VinkaItems.SUGAR_CANE(), "xooxooxoo",
				new VinkaItem[] { VinkaItems.BEETROOT() }, "x");
		ItemModule.craftedRecipe("CRAFTED_DRAGON_HEAD", VinkaItems.DRAGON_HEAD(), "xxxxoxxxx",
				new VinkaItem[] { VinkaItems.ENDER_PEARL() }, "x");
		ItemModule.craftedRecipe("CRAFTED_GOLDEN_APPLE", VinkaItems.GOLDEN_APPLE(), "xxxxxxxxx",
				new VinkaItem[] { VinkaItems.BAKED_POTATO() }, "x");
		ItemModule.craftedRecipe("CRAFTED_WHITE_CONCRETE", VinkaItems.WHITE_CONCRETE(), "oaoabaoao",
				new VinkaItem[] { VinkaItems.STONE_SLAB(), VinkaItems.STONE() }, "a,b");
		ItemModule.craftedRecipe("CRAFTED_WHITE_SHULKER_BOX", VinkaItems.WHITE_SHULKER_BOX(), "aaaabaaaa",
				new VinkaItem[] { VinkaItems.GRAY_DYE(), VinkaItems.CHEST() }, "a,b");
		ItemModule.craftedRecipe("CRAFTED_WHITE_TERRACOTTA", VinkaItems.WHITE_TERRACOTTA(), "aaaabaaaa",
				new VinkaItem[] { VinkaItems.DRIED_KELP(), VinkaItems.STONE() }, "a,b");
		ItemModule.craftedRecipe("CRAFTED_WHITE_CARPET", VinkaItems.WHITE_CARPET(), "ooooooxxx",
				new VinkaItem[] { VinkaItems.STRING() }, "x");
		ItemModule.craftedRecipe("CRAFTED_OAK_LEAVES", VinkaItems.OAK_LEAVES(), "xoxoxoxox",
				new VinkaItem[] { VinkaItems.DRIED_KELP() }, "x");
		ItemModule.craftedRecipe("CRAFTED_OAK_LOG", VinkaItems.OAK_LOG(), "xoxoooxox",
				new VinkaItem[] { VinkaItems.OAK_SLAB() }, "x");

		ItemModule.craftedRecipe("COAL_BLOCK", VinkaItems.COAL_BLOCK(), "xxxxxxxxx",
				new VinkaItem[] { VinkaItems.COAL() }, "x");
		ItemModule.craftedRecipe("OAK_BOAT", VinkaItems.OAK_BOAT(), "oooaoaaaa",
				new VinkaItem[] { VinkaItems.OAK_SLAB() }, "a");
		ItemModule.craftedRecipe("BRICK_BLOCK", VinkaItems.BRICKS(), "obobabobo",
				new VinkaItem[] { VinkaItems.STONE(), VinkaItems.DIRT() }, "a,b");
		ItemModule.craftedRecipe("STONE_BRICKS", VinkaItems.STONE(), "babababab",
				new VinkaItem[] { VinkaItems.STONE(), VinkaItems.STONE_SLAB() }, "a,b");
		ItemModule.craftedRecipe("ANDESITE", VinkaItems.ANDESITE(), "obobabobo",
				new VinkaItem[] { VinkaItems.STONE(), VinkaItems.STONE_SLAB() }, "a,b");

		ItemModule.craftedRecipe("CRAFTED_DIAMOND_HORSE_ARMOR", VinkaItems.DIAMOND_HORSE_ARMOR(), "ooooooxxx",
				new VinkaItem[] { VinkaItems.DIAMOND() }, "x");
		ItemModule.craftedRecipe("CRAFTED_GOLDEN_HORSE_ARMOR", VinkaItems.GOLDEN_HORSE_ARMOR(), "ooooooxxx",
				new VinkaItem[] { VinkaItems.GOLD_INGOT() }, "x");
		ItemModule.craftedRecipe("CRAFTED_IRON_HORSE_ARMOR", VinkaItems.IRON_HORSE_ARMOR(), "ooooooxxx",
				new VinkaItem[] { VinkaItems.IRON_INGOT() }, "x");

		ItemModule.craftedRecipe("CRAFTED_SADDLE", VinkaItems.SADDLE(), "xoxxaxxxx",
				new VinkaItem[] { VinkaItems.WHEAT_SEEDS(), VinkaItems.IRON_INGOT() }, "x,a");
		ItemModule.craftedRecipe("CRAFTED_DIRT_BLOCK", VinkaItems.DIRT(), "xxxxxxxxx",
				new VinkaItem[] { VinkaItems.DRIED_KELP() }, "x");
		ItemModule.craftedRecipe("CRAFTED_RAILS", VinkaItems.RAIL(), "oooabaooo",
				new VinkaItem[] { VinkaItems.STONE_SLAB(), VinkaItems.STICK() }, "a,b");
		ItemModule.craftedRecipe("CRAFTED_POWERED_RAILS", VinkaItems.POWERED_RAIL(), "aoaabaooo",
				new VinkaItem[] { VinkaItems.STONE_SLAB(), VinkaItems.STICK() }, "a,b");
		ItemModule.craftedRecipe("CRAFTED_DETECTOR_RAILS", VinkaItems.DETECTOR_RAIL(), "oooabaaoa",
				new VinkaItem[] { VinkaItems.STONE_SLAB(), VinkaItems.STICK() }, "a,b");
		ItemModule.craftedRecipe("CRAFTED_ACTIVATOR_RAILS", VinkaItems.ACTIVATOR_RAIL(), "aoaabaaoa",
				new VinkaItem[] { VinkaItems.STONE_SLAB(), VinkaItems.STICK() }, "a,b");
		ItemModule.craftedRecipe("CRAFTED_MINECART", VinkaItems.MINECART(), "oooaoaaaa",
				new VinkaItem[] { VinkaItems.STONE_SLAB() }, "a");

		ItemModule.craftedRecipe("CRAFTED_IRON_BOOTS", VinkaItems.IRON_BOOTS(), "ooooooxox",
				new VinkaItem[] { VinkaItems.IRON_INGOT() }, "x");
		ItemModule.craftedRecipe("CRAFTED_IRON_LEGGINGS", VinkaItems.IRON_LEGGINGS(), "oooxoxxox",
				new VinkaItem[] { VinkaItems.IRON_INGOT() }, "x");
		ItemModule.craftedRecipe("CRAFTED_IRON_CHESTPLATE", VinkaItems.IRON_CHESTPLATE(), "oooxxxxxx",
				new VinkaItem[] { VinkaItems.IRON_INGOT() }, "x");
		ItemModule.craftedRecipe("CRAFTED_IRON_HELMET", VinkaItems.IRON_HELMET(), "xxxoooooo",
				new VinkaItem[] { VinkaItems.IRON_INGOT() }, "x");
		ItemModule.craftedRecipe("CRAFTED_GOLDEN_BOOTS", VinkaItems.GOLDEN_BOOTS(), "ooooooxox",
				new VinkaItem[] { VinkaItems.DANDELION_YELLOW() }, "x");
		ItemModule.craftedRecipe("CRAFTED_GOLDEN_LEGGINGS", VinkaItems.GOLDEN_LEGGINGS(), "oooxoxxox",
				new VinkaItem[] { VinkaItems.DANDELION_YELLOW() }, "x");
		ItemModule.craftedRecipe("CRAFTED_GOLDEN_CHESTPLATE", VinkaItems.GOLDEN_CHESTPLATE(), "oooxxxxxx",
				new VinkaItem[] { VinkaItems.DANDELION_YELLOW() }, "x");
		ItemModule.craftedRecipe("CRAFTED_GOLDEN_HELMET", VinkaItems.GOLDEN_HELMET(), "xxxoooooo",
				new VinkaItem[] { VinkaItems.DANDELION_YELLOW() }, "x");
		ItemModule.craftedRecipe("CRAFTED_DIAMOND_BOOTS", VinkaItems.DIAMOND_BOOTS(), "ooooooxox",
				new VinkaItem[] { VinkaItems.DIAMOND() }, "x");
		ItemModule.craftedRecipe("CRAFTED_DIAMOND_LEGGINGS", VinkaItems.DIAMOND_LEGGINGS(), "oooxoxxox",
				new VinkaItem[] { VinkaItems.DIAMOND() }, "x");
		ItemModule.craftedRecipe("CRAFTED_DIAMOND_CHESTPLATE", VinkaItems.DIAMOND_CHESTPLATE(), "oooxxxxxx",
				new VinkaItem[] { VinkaItems.DIAMOND() }, "x");
		ItemModule.craftedRecipe("CRAFTED_DIAMOND_HELMET", VinkaItems.DIAMOND_HELMET(), "xxxoooooo",
				new VinkaItem[] { VinkaItems.DIAMOND() }, "x");
		ItemModule.craftedRecipe("CRAFTED_ENHANCED_BOOTS", VinkaItems.ENHANCED_BOOTS(), "ooooooxox",
				new VinkaItem[] { VinkaItems.ENDER_PEARL() }, "x");
		ItemModule.craftedRecipe("CRAFTED_ENHANCED_LEGGINGS", VinkaItems.ENHANCED_LEGGINGS(), "oooxoxxox",
				new VinkaItem[] { VinkaItems.ENDER_PEARL() }, "x");
		ItemModule.craftedRecipe("CRAFTED_ENHANCED_CHESTPLATE", VinkaItems.ENHANCED_CHESTPLATE(), "oooxxxxxx",
				new VinkaItem[] { VinkaItems.ENDER_PEARL() }, "x");
		ItemModule.craftedRecipe("CRAFTED_ENHANCED_HELMET", VinkaItems.ENHANCED_HELMET(), "xxxoooooo",
				new VinkaItem[] { VinkaItems.ENDER_PEARL() }, "x");
		ItemModule.craftedRecipe("CRAFTED_WHEAT", VinkaItems.WHEAT(), "xxxxoxxxx",
				new VinkaItem[] { VinkaItems.WHEAT_SEEDS() }, "x");
		ItemModule.craftedRecipe("CRAFTED_SOUL_SAND", VinkaItems.SOUL_SAND(), "xxxxxxxxx",
				new VinkaItem[] { VinkaItems.SUGAR() }, "x");
	}

	private void addFurnaceRecipes() {
		ItemModule.furnaceRecipe("FURNACE_MELON_SEEDS", VinkaItems.MELON_SEEDS(), Material.BEETROOT_SEEDS, 5);
		ItemModule.furnaceRecipe("FURNACE_IRON_INGOT", VinkaItems.IRON_INGOT(), Material.LIGHT_GRAY_DYE, 5);
		ItemModule.furnaceRecipe("FURNACE_DANDELION_YELLOW", VinkaItems.GOLD_INGOT(), Material.DANDELION_YELLOW, 30);
		ItemModule.furnaceRecipe("FURNACE_DIAMOND", VinkaItems.DIAMOND(), Material.LIGHT_BLUE_DYE, 20);
		ItemModule.furnaceRecipe("FURNACE_REFINED_EMERALD", VinkaItems.EMERALD(), Material.LIME_DYE, 20);

		ItemModule.furnaceRecipe("FURNACE_BLAZE_POWDER", VinkaItems.BLAZE_POWDER(), Material.BLACK_CONCRETE, 30);
		ItemModule.furnaceRecipe("FURNACE_GUNPOWDER", VinkaItems.GUNPOWDER(), Material.SUGAR, 1);
		ItemModule.furnaceRecipe("FURNACE_WHITE_CONCRETE_POWDER", VinkaItems.WHITE_CONCRETE_POWDER(),
				Material.WHITE_CONCRETE, 5);
		ItemModule.furnaceRecipe("FURNACE_GLASS", VinkaItems.GLASS(), Material.SAND, 60);

		ItemModule.furnaceRecipe("FURNACE_POLISHED_ANDESITE", VinkaItems.POLISHED_ANDESITE(), Material.ANDESITE, 10);
		ItemModule.furnaceRecipe("FURNACE_POLISHED_DIORITE", VinkaItems.POLISHED_DIORITE(), Material.DIORITE, 10);
		ItemModule.furnaceRecipe("FURNACE_POLISHED_GRANITE", VinkaItems.POLISHED_GRANITE(), Material.GRANITE, 10);
		ItemModule.furnaceRecipe("FURNACE_GLASS_PANE", VinkaItems.GLASS_PANE(), Material.RED_SAND, 10);

		ItemModule.furnaceRecipe("FURNACE_BIRCH_PLANKS", VinkaItems.BIRCH_PLANKS(), Material.OAK_PLANKS, 3);
		ItemModule.furnaceRecipe("FURNACE_SPRUCE_PLANKS", VinkaItems.SPRUCE_PLANKS(), Material.BIRCH_PLANKS, 3);
		ItemModule.furnaceRecipe("FURNACE_JUNGLE_PLANKS", VinkaItems.JUNGLE_PLANKS(), Material.SPRUCE_PLANKS, 3);
		ItemModule.furnaceRecipe("FURNACE_ACACIA_PLANKS", VinkaItems.ACACIA_PLANKS(), Material.JUNGLE_PLANKS, 3);
		ItemModule.furnaceRecipe("FURNACE_DARK_OAK_PLANKS", VinkaItems.DARK_OAK_PLANKS(), Material.ACACIA_PLANKS, 3);

		ItemModule.furnaceRecipe("FURNACE_BIRCH_STAIRS", VinkaItems.BIRCH_STAIRS(), Material.OAK_STAIRS, 3);
		ItemModule.furnaceRecipe("FURNACE_SPRUCE_STAIRS", VinkaItems.SPRUCE_STAIRS(), Material.BIRCH_STAIRS, 3);
		ItemModule.furnaceRecipe("FURNACE_JUNGLE_STAIRS", VinkaItems.JUNGLE_STAIRS(), Material.SPRUCE_STAIRS, 3);
		ItemModule.furnaceRecipe("FURNACE_ACACIA_STAIRS", VinkaItems.ACACIA_STAIRS(), Material.JUNGLE_STAIRS, 3);
		ItemModule.furnaceRecipe("FURNACE_DARK_OAK_STAIRS", VinkaItems.DARK_OAK_STAIRS(), Material.ACACIA_STAIRS, 3);

		ItemModule.furnaceRecipe("FURNACE_BIRCH_FENCE_GATE", VinkaItems.BIRCH_FENCE_GATE(), Material.OAK_FENCE_GATE, 3);
		ItemModule.furnaceRecipe("FURNACE_SPRUCE_FENCE_GATE", VinkaItems.SPRUCE_FENCE_GATE(), Material.BIRCH_FENCE_GATE,
				3);
		ItemModule.furnaceRecipe("FURNACE_JUNGLE_FENCE_GATE", VinkaItems.JUNGLE_FENCE_GATE(),
				Material.SPRUCE_FENCE_GATE, 3);
		ItemModule.furnaceRecipe("FURNACE_ACACIA_FENCE_GATE", VinkaItems.ACACIA_FENCE_GATE(),
				Material.JUNGLE_FENCE_GATE, 3);
		ItemModule.furnaceRecipe("FURNACE_DARK_OAK_FENCE_GATE", VinkaItems.DARK_OAK_FENCE_GATE(),
				Material.ACACIA_FENCE_GATE, 3);

		ItemModule.furnaceRecipe("FURNACE_BIRCH_FENCE", VinkaItems.BIRCH_FENCE(), Material.OAK_FENCE, 3);
		ItemModule.furnaceRecipe("FURNACE_SPRUCE_FENCE", VinkaItems.SPRUCE_FENCE(), Material.BIRCH_FENCE, 3);
		ItemModule.furnaceRecipe("FURNACE_JUNGLE_FENCE", VinkaItems.JUNGLE_FENCE(), Material.SPRUCE_FENCE, 3);
		ItemModule.furnaceRecipe("FURNACE_ACACIA_FENCE", VinkaItems.ACACIA_FENCE(), Material.JUNGLE_FENCE, 3);
		ItemModule.furnaceRecipe("FURNACE_DARK_OAK_FENCE", VinkaItems.DARK_OAK_FENCE(), Material.ACACIA_FENCE, 3);

		ItemModule.furnaceRecipe("FURNACE_BIRCH_SLAB", VinkaItems.BIRCH_SLAB(), Material.OAK_SLAB, 3);
		ItemModule.furnaceRecipe("FURNACE_SPRUCE_SLAB", VinkaItems.SPRUCE_SLAB(), Material.BIRCH_SLAB, 3);
		ItemModule.furnaceRecipe("FURNACE_JUNGLE_SLAB", VinkaItems.JUNGLE_SLAB(), Material.SPRUCE_SLAB, 3);
		ItemModule.furnaceRecipe("FURNACE_ACACIA_SLAB", VinkaItems.ACACIA_SLAB(), Material.JUNGLE_SLAB, 3);
		ItemModule.furnaceRecipe("FURNACE_DARK_OAK_SLAB", VinkaItems.DARK_OAK_SLAB(), Material.ACACIA_SLAB, 3);
		
		ItemModule.furnaceRecipe("FURNACE_BIRCH_TRAPDOOR", VinkaItems.BIRCH_TRAPDOOR(), Material.OAK_TRAPDOOR, 3);
		ItemModule.furnaceRecipe("FURNACE_SPRUCE_TRAPDOOR", VinkaItems.SPRUCE_TRAPDOOR(), Material.BIRCH_TRAPDOOR, 3);
		ItemModule.furnaceRecipe("FURNACE_JUNGLE_TRAPDOOR", VinkaItems.JUNGLE_TRAPDOOR(), Material.SPRUCE_TRAPDOOR, 3);
		ItemModule.furnaceRecipe("FURNACE_ACACIA_TRAPDOOR", VinkaItems.ACACIA_TRAPDOOR(), Material.JUNGLE_TRAPDOOR, 3);
		ItemModule.furnaceRecipe("FURNACE_DARK_OAK_TRAPDOOR", VinkaItems.DARK_OAK_TRAPDOOR(), Material.ACACIA_TRAPDOOR, 3);

		ItemModule.furnaceRecipe("FURNACE_BIRCH_DOOR", VinkaItems.BIRCH_DOOR(), Material.OAK_DOOR, 3);
		ItemModule.furnaceRecipe("FURNACE_SPRUCE_DOOR", VinkaItems.SPRUCE_DOOR(), Material.BIRCH_DOOR, 3);
		ItemModule.furnaceRecipe("FURNACE_JUNGLE_DOOR", VinkaItems.JUNGLE_DOOR(), Material.SPRUCE_DOOR, 3);
		ItemModule.furnaceRecipe("FURNACE_ACACIA_DOOR", VinkaItems.ACACIA_DOOR(), Material.JUNGLE_DOOR, 3);
		ItemModule.furnaceRecipe("FURNACE_DARK_OAK_DOOR", VinkaItems.DARK_OAK_DOOR(), Material.ACACIA_DOOR, 3);

		ItemModule.furnaceRecipe("FURNACE_BIRCH_LOG", VinkaItems.BIRCH_LOG(), Material.OAK_LOG, 3);
		ItemModule.furnaceRecipe("FURNACE_SPRUCE_LOG", VinkaItems.SPRUCE_LOG(), Material.BIRCH_LOG, 3);
		ItemModule.furnaceRecipe("FURNACE_JUNGLE_LOG", VinkaItems.JUNGLE_LOG(), Material.SPRUCE_LOG, 3);
		ItemModule.furnaceRecipe("FURNACE_ACACIA_LOG", VinkaItems.ACACIA_LOG(), Material.JUNGLE_LOG, 3);
		ItemModule.furnaceRecipe("FURNACE_DARK_OAK_LOG", VinkaItems.DARK_OAK_LOG(), Material.ACACIA_LOG, 3);

		ItemModule.furnaceRecipe("FURNACE_BIRCH_LEAVES", VinkaItems.BIRCH_LEAVES(), Material.OAK_LEAVES, 3);
		ItemModule.furnaceRecipe("FURNACE_SPRUCE_LEAVES", VinkaItems.SPRUCE_LEAVES(), Material.BIRCH_LEAVES, 3);
		ItemModule.furnaceRecipe("FURNACE_JUNGLE_LEAVES", VinkaItems.JUNGLE_LEAVES(), Material.SPRUCE_LEAVES, 3);
		ItemModule.furnaceRecipe("FURNACE_ACACIA_LEAVES", VinkaItems.ACACIA_LEAVES(), Material.JUNGLE_LEAVES, 3);
		ItemModule.furnaceRecipe("FURNACE_DARK_OAK_LEAVES", VinkaItems.DARK_OAK_LEAVES(), Material.ACACIA_LEAVES, 3);

		ItemModule.furnaceRecipe("FURNACE_BIRCH_BOAT", VinkaItems.BIRCH_BOAT(), Material.OAK_BOAT, 3);
		ItemModule.furnaceRecipe("FURNACE_SPRUCE_BOAT", VinkaItems.SPRUCE_BOAT(), Material.BIRCH_BOAT, 3);
		ItemModule.furnaceRecipe("FURNACE_JUNGLE_BOAT", VinkaItems.JUNGLE_BOAT(), Material.SPRUCE_BOAT, 3);
		ItemModule.furnaceRecipe("FURNACE_ACACIA_BOAT", VinkaItems.ACACIA_BOAT(), Material.JUNGLE_BOAT, 3);
		ItemModule.furnaceRecipe("FURNACE_DARK_OAK_BOAT", VinkaItems.DARK_OAK_BOAT(), Material.ACACIA_BOAT, 3);

		ItemModule.furnaceRecipe("FURNACE_WHITE_GLASS", VinkaItems.WHITE_STAINED_GLASS(), Material.GLASS, 3);
		ItemModule.furnaceRecipe("FURNACE_ORANGE_GLASS", VinkaItems.ORANGE_STAINED_GLASS(),
				Material.WHITE_STAINED_GLASS, 3);
		ItemModule.furnaceRecipe("FURNACE_MAGENTA_GLASS", VinkaItems.MAGENTA_STAINED_GLASS(),
				Material.ORANGE_STAINED_GLASS, 3);
		ItemModule.furnaceRecipe("FURNACE_LIGHT_BLUE_GLASS", VinkaItems.LIGHT_BLUE_STAINED_GLASS(),
				Material.MAGENTA_STAINED_GLASS, 3);
		ItemModule.furnaceRecipe("FURNACE_YELLOW_GLASS", VinkaItems.YELLOW_STAINED_GLASS(),
				Material.LIGHT_BLUE_STAINED_GLASS, 3);
		ItemModule.furnaceRecipe("FURNACE_LIME_GLASS", VinkaItems.LIME_STAINED_GLASS(), Material.YELLOW_STAINED_GLASS,
				3);
		ItemModule.furnaceRecipe("FURNACE_PINK_GLASS", VinkaItems.PINK_STAINED_GLASS(), Material.LIME_STAINED_GLASS, 3);
		ItemModule.furnaceRecipe("FURNACE_GRAY_GLASS", VinkaItems.GRAY_STAINED_GLASS(), Material.PINK_STAINED_GLASS, 3);
		ItemModule.furnaceRecipe("FURNACE_LIGHT_GRAY_GLASS", VinkaItems.LIGHT_GRAY_STAINED_GLASS(),
				Material.GRAY_STAINED_GLASS, 3);
		ItemModule.furnaceRecipe("FURNACE_CYAN_GLASS", VinkaItems.CYAN_STAINED_GLASS(),
				Material.LIGHT_GRAY_STAINED_GLASS, 3);
		ItemModule.furnaceRecipe("FURNACE_PURPLE_GLASS", VinkaItems.PURPLE_STAINED_GLASS(), Material.CYAN_STAINED_GLASS,
				3);
		ItemModule.furnaceRecipe("FURNACE_BLUE_GLASS", VinkaItems.BLUE_STAINED_GLASS(), Material.PURPLE_STAINED_GLASS,
				3);
		ItemModule.furnaceRecipe("FURNACE_BROWN_GLASS", VinkaItems.BROWN_STAINED_GLASS(), Material.BLUE_STAINED_GLASS,
				3);
		ItemModule.furnaceRecipe("FURNACE_GREEN_GLASS", VinkaItems.GREEN_STAINED_GLASS(), Material.BROWN_STAINED_GLASS,
				3);
		ItemModule.furnaceRecipe("FURNACE_RED_GLASS", VinkaItems.RED_STAINED_GLASS(), Material.GREEN_STAINED_GLASS, 3);
		ItemModule.furnaceRecipe("FURNACE_BLACK_GLASS", VinkaItems.BLACK_STAINED_GLASS(), Material.RED_STAINED_GLASS,
				3);

		ItemModule.furnaceRecipe("FURNACE_WHITE_GLASS_PANE", VinkaItems.WHITE_STAINED_GLASS_PANE(), Material.GLASS_PANE,
				3);
		ItemModule.furnaceRecipe("FURNACE_ORANGE_GLASS_PANE", VinkaItems.ORANGE_STAINED_GLASS_PANE(),
				Material.WHITE_STAINED_GLASS_PANE, 3);
		ItemModule.furnaceRecipe("FURNACE_MAGENTA_GLASS_PANE", VinkaItems.MAGENTA_STAINED_GLASS_PANE(),
				Material.ORANGE_STAINED_GLASS_PANE, 3);
		ItemModule.furnaceRecipe("FURNACE_LIGHT_BLUE_GLASS_PANE", VinkaItems.LIGHT_BLUE_STAINED_GLASS_PANE(),
				Material.MAGENTA_STAINED_GLASS_PANE, 3);
		ItemModule.furnaceRecipe("FURNACE_YELLOW_GLASS_PANE", VinkaItems.YELLOW_STAINED_GLASS_PANE(),
				Material.LIGHT_BLUE_STAINED_GLASS_PANE, 3);
		ItemModule.furnaceRecipe("FURNACE_LIME_GLASS_PANE", VinkaItems.LIME_STAINED_GLASS_PANE(),
				Material.YELLOW_STAINED_GLASS_PANE, 3);
		ItemModule.furnaceRecipe("FURNACE_PINK_GLASS_PANE", VinkaItems.PINK_STAINED_GLASS_PANE(),
				Material.LIME_STAINED_GLASS_PANE, 3);
		ItemModule.furnaceRecipe("FURNACE_GRAY_GLASS_PANE", VinkaItems.GRAY_STAINED_GLASS_PANE(),
				Material.PINK_STAINED_GLASS_PANE, 3);
		ItemModule.furnaceRecipe("FURNACE_LIGHT_GRAY_GLASS_PANE", VinkaItems.LIGHT_GRAY_STAINED_GLASS_PANE(),
				Material.GRAY_STAINED_GLASS_PANE, 3);
		ItemModule.furnaceRecipe("FURNACE_CYAN_GLASS_PANE", VinkaItems.CYAN_STAINED_GLASS_PANE(),
				Material.LIGHT_GRAY_STAINED_GLASS_PANE, 3);
		ItemModule.furnaceRecipe("FURNACE_PURPLE_GLASS_PANE", VinkaItems.PURPLE_STAINED_GLASS_PANE(),
				Material.CYAN_STAINED_GLASS_PANE, 3);
		ItemModule.furnaceRecipe("FURNACE_BLUE_GLASS_PANE", VinkaItems.BLUE_STAINED_GLASS_PANE(),
				Material.PURPLE_STAINED_GLASS_PANE, 3);
		ItemModule.furnaceRecipe("FURNACE_BROWN_GLASS_PANE", VinkaItems.BROWN_STAINED_GLASS_PANE(),
				Material.BLUE_STAINED_GLASS_PANE, 3);
		ItemModule.furnaceRecipe("FURNACE_GREEN_GLASS_PANE", VinkaItems.GREEN_STAINED_GLASS_PANE(),
				Material.BROWN_STAINED_GLASS_PANE, 3);
		ItemModule.furnaceRecipe("FURNACE_RED_GLASS_PANE", VinkaItems.RED_STAINED_GLASS_PANE(),
				Material.GREEN_STAINED_GLASS_PANE, 3);
		ItemModule.furnaceRecipe("FURNACE_BLACK_GLASS_PANE", VinkaItems.BLACK_STAINED_GLASS_PANE(),
				Material.RED_STAINED_GLASS_PANE, 3);

		ItemModule.furnaceRecipe("FURNACE_ORANGE_CARPET", VinkaItems.ORANGE_CARPET(), Material.WHITE_CARPET, 3);
		ItemModule.furnaceRecipe("FURNACE_MAGENTA_CARPET", VinkaItems.MAGENTA_CARPET(), Material.ORANGE_CARPET, 3);
		ItemModule.furnaceRecipe("FURNACE_LIGHT_BLUE_CARPET", VinkaItems.LIGHT_BLUE_CARPET(), Material.MAGENTA_CARPET,
				3);
		ItemModule.furnaceRecipe("FURNACE_YELLOW_CARPET", VinkaItems.YELLOW_CARPET(), Material.LIGHT_BLUE_CARPET, 3);
		ItemModule.furnaceRecipe("FURNACE_LIME_CARPET", VinkaItems.LIME_CARPET(), Material.YELLOW_CARPET, 3);
		ItemModule.furnaceRecipe("FURNACE_PINK_CARPET", VinkaItems.PINK_CARPET(), Material.LIME_CARPET, 3);
		ItemModule.furnaceRecipe("FURNACE_GRAY_CARPET", VinkaItems.GRAY_CARPET(), Material.PINK_CARPET, 3);
		ItemModule.furnaceRecipe("FURNACE_LIGHT_GRAY_CARPET", VinkaItems.LIGHT_GRAY_CARPET(), Material.GRAY_CARPET, 3);
		ItemModule.furnaceRecipe("FURNACE_CYAN_CARPET", VinkaItems.CYAN_CARPET(), Material.LIGHT_GRAY_CARPET, 3);
		ItemModule.furnaceRecipe("FURNACE_PURPLE_CARPET", VinkaItems.PURPLE_CARPET(), Material.CYAN_CARPET, 3);
		ItemModule.furnaceRecipe("FURNACE_BLUE_CARPET", VinkaItems.BLUE_CARPET(), Material.PURPLE_CARPET, 3);
		ItemModule.furnaceRecipe("FURNACE_BROWN_CARPET", VinkaItems.BROWN_CARPET(), Material.BLUE_CARPET, 3);
		ItemModule.furnaceRecipe("FURNACE_GREEN_CARPET", VinkaItems.GREEN_CARPET(), Material.BROWN_CARPET, 3);
		ItemModule.furnaceRecipe("FURNACE_RED_CARPET", VinkaItems.RED_CARPET(), Material.GREEN_CARPET, 3);
		ItemModule.furnaceRecipe("FURNACE_BLACK_CARPET", VinkaItems.BLACK_CARPET(), Material.RED_CARPET, 3);

		ItemModule.furnaceRecipe("FURNACE_ORANGE_TERRACOTTA", VinkaItems.ORANGE_TERRACOTTA(), Material.WHITE_TERRACOTTA,
				3);
		ItemModule.furnaceRecipe("FURNACE_MAGENTA_TERRACOTTA", VinkaItems.MAGENTA_TERRACOTTA(),
				Material.ORANGE_TERRACOTTA, 3);
		ItemModule.furnaceRecipe("FURNACE_LIGHT_BLUE_TERRACOTTA", VinkaItems.LIGHT_BLUE_TERRACOTTA(),
				Material.MAGENTA_TERRACOTTA, 3);
		ItemModule.furnaceRecipe("FURNACE_YELLOW_TERRACOTTA", VinkaItems.YELLOW_TERRACOTTA(),
				Material.LIGHT_BLUE_TERRACOTTA, 3);
		ItemModule.furnaceRecipe("FURNACE_LIME_TERRACOTTA", VinkaItems.LIME_TERRACOTTA(), Material.YELLOW_TERRACOTTA,
				3);
		ItemModule.furnaceRecipe("FURNACE_PINK_TERRACOTTA", VinkaItems.PINK_TERRACOTTA(), Material.LIME_TERRACOTTA, 3);
		ItemModule.furnaceRecipe("FURNACE_GRAY_TERRACOTTA", VinkaItems.GRAY_TERRACOTTA(), Material.PINK_TERRACOTTA, 3);
		ItemModule.furnaceRecipe("FURNACE_LIGHT_GRAY_TERRACOTTA", VinkaItems.LIGHT_GRAY_TERRACOTTA(),
				Material.GRAY_TERRACOTTA, 3);
		ItemModule.furnaceRecipe("FURNACE_CYAN_TERRACOTTA", VinkaItems.CYAN_TERRACOTTA(),
				Material.LIGHT_GRAY_TERRACOTTA, 3);
		ItemModule.furnaceRecipe("FURNACE_PURPLE_TERRACOTTA", VinkaItems.PURPLE_TERRACOTTA(), Material.CYAN_TERRACOTTA,
				3);
		ItemModule.furnaceRecipe("FURNACE_BLUE_TERRACOTTA", VinkaItems.BLUE_TERRACOTTA(), Material.PURPLE_TERRACOTTA,
				3);
		ItemModule.furnaceRecipe("FURNACE_BROWN_TERRACOTTA", VinkaItems.BROWN_TERRACOTTA(), Material.BLUE_TERRACOTTA,
				3);
		ItemModule.furnaceRecipe("FURNACE_GREEN_TERRACOTTA", VinkaItems.GREEN_TERRACOTTA(), Material.BROWN_TERRACOTTA,
				3);
		ItemModule.furnaceRecipe("FURNACE_RED_TERRACOTTA", VinkaItems.RED_TERRACOTTA(), Material.GREEN_TERRACOTTA, 3);
		ItemModule.furnaceRecipe("FURNACE_BLACK_TERRACOTTA", VinkaItems.BLACK_TERRACOTTA(), Material.RED_TERRACOTTA, 3);

		ItemModule.furnaceRecipe("FURNACE_ORANGE_WOOL", VinkaItems.ORANGE_WOOL(), Material.WHITE_WOOL, 3);
		ItemModule.furnaceRecipe("FURNACE_MAGENTA_WOOL", VinkaItems.MAGENTA_WOOL(), Material.ORANGE_WOOL, 3);
		ItemModule.furnaceRecipe("FURNACE_LIGHT_BLUE_WOOL", VinkaItems.LIGHT_BLUE_WOOL(), Material.MAGENTA_WOOL, 3);
		ItemModule.furnaceRecipe("FURNACE_YELLOW_WOOL", VinkaItems.YELLOW_WOOL(), Material.LIGHT_BLUE_WOOL, 3);
		ItemModule.furnaceRecipe("FURNACE_LIME_WOOL", VinkaItems.LIME_WOOL(), Material.YELLOW_WOOL, 3);
		ItemModule.furnaceRecipe("FURNACE_PINK_WOOL", VinkaItems.PINK_WOOL(), Material.LIME_WOOL, 3);
		ItemModule.furnaceRecipe("FURNACE_GRAY_WOOL", VinkaItems.GRAY_WOOL(), Material.PINK_WOOL, 3);
		ItemModule.furnaceRecipe("FURNACE_LIGHT_GRAY_WOOL", VinkaItems.LIGHT_GRAY_WOOL(), Material.GRAY_WOOL, 3);
		ItemModule.furnaceRecipe("FURNACE_CYAN_WOOL", VinkaItems.CYAN_WOOL(), Material.LIGHT_GRAY_WOOL, 3);
		ItemModule.furnaceRecipe("FURNACE_PURPLE_WOOL", VinkaItems.PURPLE_WOOL(), Material.CYAN_WOOL, 3);
		ItemModule.furnaceRecipe("FURNACE_BLUE_WOOL", VinkaItems.BLUE_WOOL(), Material.PURPLE_WOOL, 3);
		ItemModule.furnaceRecipe("FURNACE_BROWN_WOOL", VinkaItems.BROWN_WOOL(), Material.BLUE_WOOL, 3);
		ItemModule.furnaceRecipe("FURNACE_GREEN_WOOL", VinkaItems.GREEN_WOOL(), Material.BROWN_WOOL, 3);
		ItemModule.furnaceRecipe("FURNACE_RED_WOOL", VinkaItems.RED_WOOL(), Material.GREEN_WOOL, 3);
		ItemModule.furnaceRecipe("FURNACE_BLACK_WOOL", VinkaItems.BLACK_WOOL(), Material.RED_WOOL, 3);

		ItemModule.furnaceRecipe("FURNACE_ORANGE_BED", VinkaItems.ORANGE_BED(), Material.WHITE_BED, 3);
		ItemModule.furnaceRecipe("FURNACE_MAGENTA_BED", VinkaItems.MAGENTA_BED(), Material.ORANGE_BED, 3);
		ItemModule.furnaceRecipe("FURNACE_LIGHT_BLUE_BED", VinkaItems.LIGHT_BLUE_BED(), Material.MAGENTA_BED, 3);
		ItemModule.furnaceRecipe("FURNACE_YELLOW_BED", VinkaItems.YELLOW_BED(), Material.LIGHT_BLUE_BED, 3);
		ItemModule.furnaceRecipe("FURNACE_LIME_BED", VinkaItems.LIME_BED(), Material.YELLOW_BED, 3);
		ItemModule.furnaceRecipe("FURNACE_PINK_BED", VinkaItems.PINK_BED(), Material.LIME_BED, 3);
		ItemModule.furnaceRecipe("FURNACE_GRAY_BED", VinkaItems.GRAY_BED(), Material.PINK_BED, 3);
		ItemModule.furnaceRecipe("FURNACE_LIGHT_GRAY_BED", VinkaItems.LIGHT_GRAY_BED(), Material.GRAY_BED, 3);
		ItemModule.furnaceRecipe("FURNACE_CYAN_BED", VinkaItems.CYAN_BED(), Material.LIGHT_GRAY_BED, 3);
		ItemModule.furnaceRecipe("FURNACE_PURPLE_BED", VinkaItems.PURPLE_BED(), Material.CYAN_BED, 3);
		ItemModule.furnaceRecipe("FURNACE_BLUE_BED", VinkaItems.BLUE_BED(), Material.PURPLE_BED, 3);
		ItemModule.furnaceRecipe("FURNACE_BROWN_BED", VinkaItems.BROWN_BED(), Material.BLUE_BED, 3);
		ItemModule.furnaceRecipe("FURNACE_GREEN_BED", VinkaItems.GREEN_BED(), Material.BROWN_BED, 3);
		ItemModule.furnaceRecipe("FURNACE_RED_BED", VinkaItems.RED_BED(), Material.GREEN_BED, 3);
		ItemModule.furnaceRecipe("FURNACE_BLACK_BED", VinkaItems.BLACK_BED(), Material.RED_BED, 3);

		ItemModule.furnaceRecipe("FURNACE_ORANGE_SHULKER_BOX", VinkaItems.ORANGE_SHULKER_BOX(),
				Material.WHITE_SHULKER_BOX, 3);
		ItemModule.furnaceRecipe("FURNACE_MAGENTA_SHULKER_BOX", VinkaItems.MAGENTA_SHULKER_BOX(),
				Material.ORANGE_SHULKER_BOX, 3);
		ItemModule.furnaceRecipe("FURNACE_LIGHT_BLUE_SHULKER_BOX", VinkaItems.LIGHT_BLUE_SHULKER_BOX(),
				Material.MAGENTA_SHULKER_BOX, 3);
		ItemModule.furnaceRecipe("FURNACE_YELLOW_SHULKER_BOX", VinkaItems.YELLOW_SHULKER_BOX(),
				Material.LIGHT_BLUE_SHULKER_BOX, 3);
		ItemModule.furnaceRecipe("FURNACE_LIME_SHULKER_BOX", VinkaItems.LIME_SHULKER_BOX(), Material.YELLOW_SHULKER_BOX,
				3);
		ItemModule.furnaceRecipe("FURNACE_PINK_SHULKER_BOX", VinkaItems.PINK_SHULKER_BOX(), Material.LIME_SHULKER_BOX,
				3);
		ItemModule.furnaceRecipe("FURNACE_GRAY_SHULKER_BOX", VinkaItems.GRAY_SHULKER_BOX(), Material.PINK_SHULKER_BOX,
				3);
		ItemModule.furnaceRecipe("FURNACE_LIGHT_GRAY_SHULKER_BOX", VinkaItems.LIGHT_GRAY_SHULKER_BOX(),
				Material.GRAY_SHULKER_BOX, 3);
		ItemModule.furnaceRecipe("FURNACE_CYAN_SHULKER_BOX", VinkaItems.CYAN_SHULKER_BOX(),
				Material.LIGHT_GRAY_SHULKER_BOX, 3);
		ItemModule.furnaceRecipe("FURNACE_PURPLE_SHULKER_BOX", VinkaItems.PURPLE_SHULKER_BOX(),
				Material.CYAN_SHULKER_BOX, 3);
		ItemModule.furnaceRecipe("FURNACE_BLUE_SHULKER_BOX", VinkaItems.BLUE_SHULKER_BOX(), Material.PURPLE_SHULKER_BOX,
				3);
		ItemModule.furnaceRecipe("FURNACE_BROWN_SHULKER_BOX", VinkaItems.BROWN_SHULKER_BOX(), Material.BLUE_SHULKER_BOX,
				3);
		ItemModule.furnaceRecipe("FURNACE_GREEN_SHULKER_BOX", VinkaItems.GREEN_SHULKER_BOX(),
				Material.BROWN_SHULKER_BOX, 3);
		ItemModule.furnaceRecipe("FURNACE_RED_SHULKER_BOX", VinkaItems.RED_SHULKER_BOX(), Material.GREEN_SHULKER_BOX,
				3);
		ItemModule.furnaceRecipe("FURNACE_BLACK_SHULKER_BOX", VinkaItems.BLACK_SHULKER_BOX(), Material.RED_SHULKER_BOX,
				3);

		ItemModule.furnaceRecipe("FURNACE_ORANGE_CONCRETE", VinkaItems.ORANGE_CONCRETE(), Material.WHITE_CONCRETE, 3);
		ItemModule.furnaceRecipe("FURNACE_MAGENTA_CONCRETE", VinkaItems.MAGENTA_CONCRETE(), Material.ORANGE_CONCRETE,
				3);
		ItemModule.furnaceRecipe("FURNACE_LIGHT_BLUE_CONCRETE", VinkaItems.LIGHT_BLUE_CONCRETE(),
				Material.MAGENTA_CONCRETE, 3);
		ItemModule.furnaceRecipe("FURNACE_YELLOW_CONCRETE", VinkaItems.YELLOW_CONCRETE(), Material.LIGHT_BLUE_CONCRETE,
				3);
		ItemModule.furnaceRecipe("FURNACE_LIME_CONCRETE", VinkaItems.LIME_CONCRETE(), Material.YELLOW_CONCRETE, 3);
		ItemModule.furnaceRecipe("FURNACE_PINK_CONCRETE", VinkaItems.PINK_CONCRETE(), Material.LIME_CONCRETE, 3);
		ItemModule.furnaceRecipe("FURNACE_GRAY_CONCRETE", VinkaItems.GRAY_CONCRETE(), Material.PINK_CONCRETE, 3);
		ItemModule.furnaceRecipe("FURNACE_LIGHT_GRAY_CONCRETE", VinkaItems.LIGHT_GRAY_CONCRETE(),
				Material.GRAY_CONCRETE, 3);
		ItemModule.furnaceRecipe("FURNACE_CYAN_CONCRETE", VinkaItems.CYAN_CONCRETE(), Material.LIGHT_GRAY_CONCRETE, 3);
		ItemModule.furnaceRecipe("FURNACE_PURPLE_CONCRETE", VinkaItems.PURPLE_CONCRETE(), Material.CYAN_CONCRETE, 3);
		ItemModule.furnaceRecipe("FURNACE_BLUE_CONCRETE", VinkaItems.BLUE_CONCRETE(), Material.PURPLE_CONCRETE, 3);
		ItemModule.furnaceRecipe("FURNACE_BROWN_CONCRETE", VinkaItems.BROWN_CONCRETE(), Material.BLUE_CONCRETE, 3);
		ItemModule.furnaceRecipe("FURNACE_GREEN_CONCRETE", VinkaItems.GREEN_CONCRETE(), Material.BROWN_CONCRETE, 3);
		ItemModule.furnaceRecipe("FURNACE_RED_CONCRETE", VinkaItems.RED_CONCRETE(), Material.GREEN_CONCRETE, 3);
		ItemModule.furnaceRecipe("FURNACE_BLACK_CONCRETE", VinkaItems.BLACK_CONCRETE(), Material.RED_CONCRETE, 3);

		ItemModule.furnaceRecipe("FURNACE_ORANGE_CONCRETE_POWDER", VinkaItems.ORANGE_CONCRETE_POWDER(),
				Material.WHITE_CONCRETE_POWDER, 3);
		ItemModule.furnaceRecipe("FURNACE_MAGENTA_CONCRETE_POWDER", VinkaItems.MAGENTA_CONCRETE_POWDER(),
				Material.ORANGE_CONCRETE_POWDER, 3);
		ItemModule.furnaceRecipe("FURNACE_LIGHT_BLUE_CONCRETE_POWDER", VinkaItems.LIGHT_BLUE_CONCRETE_POWDER(),
				Material.MAGENTA_CONCRETE_POWDER, 3);
		ItemModule.furnaceRecipe("FURNACE_YELLOW_CONCRETE_POWDER", VinkaItems.YELLOW_CONCRETE_POWDER(),
				Material.LIGHT_BLUE_CONCRETE_POWDER, 3);
		ItemModule.furnaceRecipe("FURNACE_LIME_CONCRETE_POWDER", VinkaItems.LIME_CONCRETE_POWDER(),
				Material.YELLOW_CONCRETE_POWDER, 3);
		ItemModule.furnaceRecipe("FURNACE_PINK_CONCRETE_POWDER", VinkaItems.PINK_CONCRETE_POWDER(),
				Material.LIME_CONCRETE_POWDER, 3);
		ItemModule.furnaceRecipe("FURNACE_GRAY_CONCRETE_POWDER", VinkaItems.GRAY_CONCRETE_POWDER(),
				Material.PINK_CONCRETE_POWDER, 3);
		ItemModule.furnaceRecipe("FURNACE_LIGHT_GRAY_CONCRETE_POWDER", VinkaItems.LIGHT_GRAY_CONCRETE_POWDER(),
				Material.GRAY_CONCRETE_POWDER, 3);
		ItemModule.furnaceRecipe("FURNACE_CYAN_CONCRETE_POWDER", VinkaItems.CYAN_CONCRETE_POWDER(),
				Material.LIGHT_GRAY_CONCRETE_POWDER, 3);
		ItemModule.furnaceRecipe("FURNACE_PURPLE_CONCRETE_POWDER", VinkaItems.PURPLE_CONCRETE_POWDER(),
				Material.CYAN_CONCRETE_POWDER, 3);
		ItemModule.furnaceRecipe("FURNACE_BLUE_CONCRETE_POWDER", VinkaItems.BLUE_CONCRETE_POWDER(),
				Material.PURPLE_CONCRETE_POWDER, 3);
		ItemModule.furnaceRecipe("FURNACE_BROWN_CONCRETE_POWDER", VinkaItems.BROWN_CONCRETE_POWDER(),
				Material.BLUE_CONCRETE_POWDER, 3);
		ItemModule.furnaceRecipe("FURNACE_GREEN_CONCRETE_POWDER", VinkaItems.GREEN_CONCRETE_POWDER(),
				Material.BROWN_CONCRETE_POWDER, 3);
		ItemModule.furnaceRecipe("FURNACE_RED_CONCRETE_POWDER", VinkaItems.RED_CONCRETE_POWDER(),
				Material.GREEN_CONCRETE_POWDER, 3);
		ItemModule.furnaceRecipe("FURNACE_BLACK_CONCRETE_POWDER", VinkaItems.BLACK_CONCRETE_POWDER(),
				Material.RED_CONCRETE_POWDER, 3);

		ItemModule.furnaceRecipe("FURNACE_LEATHER_HELMET", VinkaItems.OAK_PLANKS(), Material.LEATHER_HELMET, 10);
		ItemModule.furnaceRecipe("FURNACE_LEATHER_CHESTPLATE", VinkaItems.OAK_PLANKS(), Material.LEATHER_CHESTPLATE,
				10);
		ItemModule.furnaceRecipe("FURNACE_LEATHER_LEGGINGS", VinkaItems.OAK_PLANKS(), Material.LEATHER_LEGGINGS, 10);
		ItemModule.furnaceRecipe("FURNACE_LEATHER_BOOTS", VinkaItems.OAK_PLANKS(), Material.LEATHER_BOOTS, 10);

		ItemModule.furnaceRecipe("FURNACE_IRON_HELMET", VinkaItems.IRON_INGOT(), Material.IRON_HELMET, 10);
		ItemModule.furnaceRecipe("FURNACE_IRON_CHESTPLATE", VinkaItems.IRON_INGOT(), Material.IRON_CHESTPLATE, 10);
		ItemModule.furnaceRecipe("FURNACE_IRON_LEGGINGS", VinkaItems.IRON_INGOT(), Material.IRON_LEGGINGS, 10);
		ItemModule.furnaceRecipe("FURNACE_IRON_BOOTS", VinkaItems.IRON_INGOT(), Material.IRON_BOOTS, 10);

		ItemModule.furnaceRecipe("FURNACE_GOLDEN_HELMET", VinkaItems.GOLD_INGOT(), Material.GOLDEN_HELMET, 10);
		ItemModule.furnaceRecipe("FURNACE_GOLDEN_CHESTPLATE", VinkaItems.GOLD_INGOT(), Material.GOLDEN_CHESTPLATE, 10);
		ItemModule.furnaceRecipe("FURNACE_GOLDEN_LEGGINGS", VinkaItems.GOLD_INGOT(), Material.GOLDEN_LEGGINGS, 10);
		ItemModule.furnaceRecipe("FURNACE_GOLDEN_BOOTS", VinkaItems.GOLD_INGOT(), Material.GOLDEN_BOOTS, 10);

		ItemModule.furnaceRecipe("FURNACE_DIAMOND_HELMET", VinkaItems.DIAMOND(), Material.DIAMOND_HELMET, 10);
		ItemModule.furnaceRecipe("FURNACE_DIAMOND_CHESTPLATE", VinkaItems.DIAMOND(), Material.DIAMOND_CHESTPLATE, 10);
		ItemModule.furnaceRecipe("FURNACE_DIAMOND_LEGGINGS", VinkaItems.DIAMOND(), Material.DIAMOND_LEGGINGS, 10);
		ItemModule.furnaceRecipe("FURNACE_DIAMOND_BOOTS", VinkaItems.DIAMOND(), Material.DIAMOND_BOOTS, 10);

		ItemModule.furnaceRecipe("FURNACE_IRON_SHOVEL", VinkaItems.IRON_INGOT(), Material.IRON_SHOVEL, 10);
		ItemModule.furnaceRecipe("FURNACE_IRON_PICKAXE", VinkaItems.IRON_INGOT(), Material.IRON_PICKAXE, 10);
		ItemModule.furnaceRecipe("FURNACE_IRON_HOE", VinkaItems.IRON_INGOT(), Material.IRON_HOE, 10);
		ItemModule.furnaceRecipe("FURNACE_IRON_SWORD", VinkaItems.IRON_INGOT(), Material.IRON_SWORD, 10);

		ItemModule.furnaceRecipe("FURNACE_GOLDEN_SHOVEL", VinkaItems.GOLD_INGOT(), Material.GOLDEN_SHOVEL, 10);
		ItemModule.furnaceRecipe("FURNACE_GOLDEN_PICKAXE", VinkaItems.GOLD_INGOT(), Material.GOLDEN_PICKAXE, 10);
		ItemModule.furnaceRecipe("FURNACE_GOLDEN_HOE", VinkaItems.GOLD_INGOT(), Material.GOLDEN_HOE, 10);
		ItemModule.furnaceRecipe("FURNACE_GOLDEN_SWORD", VinkaItems.GOLD_INGOT(), Material.GOLDEN_SWORD, 10);

		ItemModule.furnaceRecipe("FURNACE_DIAMOND_SHOVEL", VinkaItems.DIAMOND(), Material.DIAMOND_SHOVEL, 10);
		ItemModule.furnaceRecipe("FURNACE_DIAMOND_PICKAXE", VinkaItems.DIAMOND(), Material.DIAMOND_PICKAXE, 10);
		ItemModule.furnaceRecipe("FURNACE_DIAMOND_HOE", VinkaItems.DIAMOND(), Material.DIAMOND_HOE, 10);
		ItemModule.furnaceRecipe("FURNACE_DIAMOND_SWORD", VinkaItems.DIAMOND(), Material.DIAMOND_SWORD, 10);
	}
}
