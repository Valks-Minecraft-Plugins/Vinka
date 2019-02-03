package com.vinka;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.vinka.blocks.BlockStrippedWood;
import com.vinka.blocks.Blocks;
import com.vinka.configs.LoadPlayerFiles;
import com.vinka.consumables.Food;
import com.vinka.dungeons.SleepTeleport;
import com.vinka.items.VinkaItems;
import com.vinka.mobs.Mobs;
import com.vinka.player.PlayerHandler;
import com.vinka.utils.Utils;
import com.valkutils.modules.ItemModule;

public class Vinka extends JavaPlugin {
	public static Vinka vinka;

	@Override
	public void onEnable() {
		vinka = this;
		getServer().clearRecipes();
		addRecipes();

		registerListeners(getServer().getPluginManager());
		registerCommands();

		int radius = 30;

		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player p : getServer().getOnlinePlayers()) {
					int count = 0;
					for (Entity entity : p.getNearbyEntities(radius, 5, radius)) {
						if (entity instanceof Monster) {
							count++;
						}
					}

					if (count <= 2) {
						Location loc = p.getLocation();

						for (Location testLoc : Utils.testLocations(loc, radius)) {
							if (Utils.validSpawningLocation(testLoc)) {
								Utils.spawnMonster(testLoc, EntityType.HUSK);
								return;
							}
						}
					}
				}
			}
		}.runTaskTimer(this, 200, 600);
	}
	
	private void registerCommands() {
		//getCommand("test").setExecutor(new CommandTest());
	}

	private void registerListeners(PluginManager pm) {
		pm.registerEvents(new Blocks(), this);
		pm.registerEvents(new Food(), this);
		pm.registerEvents(new Mobs(), this);
		pm.registerEvents(new PlayerHandler(), this);
		pm.registerEvents(new SleepTeleport(), this);
		pm.registerEvents(new LoadPlayerFiles(), this);
		pm.registerEvents(new BlockStrippedWood(), this);
	}

	private void addRecipes() {
		ItemModule.craftedRecipe("IRON_ORE_FROM_LIGHT_GRAY_DYE_ORE", VinkaItems.IRON_ORE(), "xxxxxxxxx", new ItemStack[] {new ItemStack(Material.LIGHT_GRAY_DYE)}, "x");

		// WOOD RECIPES
		ItemModule.handRecipe("SIGN", VinkaItems.SIGN(), "xxss",
				new ItemStack[] { new ItemStack(Material.OAK_SLAB), new ItemStack(Material.STICK) }, "x,s");
		ItemModule.handRecipe("OAK_DOOR", VinkaItems.OAK_DOOR(), "xoxo",
				new ItemStack[] { new ItemStack(Material.OAK_PLANKS) }, "x");
		ItemModule.handRecipe("OAK_SLAB", VinkaItems.OAK_SLAB(), "xxxx",
				new ItemStack[] { new ItemStack(Material.RABBIT_FOOT) }, "x");
		ItemModule.handRecipe("OAK_PLANKS", VinkaItems.OAK_PLANKS(), "xxxx",
				new ItemStack[] { new ItemStack(Material.OAK_SLAB) }, "x");
		ItemModule.handRecipe("CHEST", VinkaItems.CHEST(), "xxxx", new ItemStack[] { new ItemStack(Material.OAK_PLANKS) },
				"x");
		ItemModule.handRecipe("OAK_TRAPDOOR", VinkaItems.OAK_TRAPDOOR(), "ooxx",
				new ItemStack[] { new ItemStack(Material.OAK_SLAB) }, "x");
		ItemModule.handRecipe("OAK_FENCE", VinkaItems.OAK_FENCE(), "ssww",
				new ItemStack[] { new ItemStack(Material.OAK_SLAB), new ItemStack(Material.OAK_PLANKS) }, "s,w");
		ItemModule.handRecipe("OAK_GATE", VinkaItems.OAK_FENCE_GATE(), "swws",
				new ItemStack[] { new ItemStack(Material.STICK), new ItemStack(Material.OAK_SLAB) }, "s,w");
		ItemModule.handRecipe("LADDER", VinkaItems.LADDER(), "xoxo", new ItemStack[] { new ItemStack(Material.STICK) }, "x");
		ItemModule.handRecipe("REDSTONE_TORCH", VinkaItems.REDSTONE_TORCH(), "coso",
				new ItemStack[] { new ItemStack(Material.COAL), new ItemStack(Material.STICK) }, "c,s");
		ItemModule.handRecipe("FISHING_ROD", VinkaItems.FISHING_ROD(), "ocso",
				new ItemStack[] { new ItemStack(Material.STRING), new ItemStack(Material.STICK) }, "c,s");
		ItemModule.handRecipe("OAK_PRESSURE_PLATE", VinkaItems.OAK_PRESSURE_PLATE(), "xoxo",
				new ItemStack[] { new ItemStack(Material.OAK_SLAB) }, "x");
		ItemModule.shapelessRecipe("STICK", VinkaItems.STICK(),
				new ItemStack[] { new ItemStack(Material.RABBIT_FOOT), new ItemStack(Material.RABBIT_FOOT) });
		ItemModule.shapelessRecipe("ITEM_FRAME", VinkaItems.ITEM_FRAME(), new ItemStack[] { new ItemStack(Material.STICK),
				new ItemStack(Material.STICK), new ItemStack(Material.STICK) });
		ItemModule.shapelessRecipe("OAK_STAIRS", VinkaItems.OAK_STAIRS(),
				new ItemStack[] { new ItemStack(Material.OAK_PLANKS), new ItemStack(Material.OAK_PLANKS),
						new ItemStack(Material.OAK_PLANKS) });
		ItemModule.shapelessRecipe("OAK_BUTTON", VinkaItems.OAK_BUTTON(),
				new ItemStack[] { new ItemStack(Material.OAK_PRESSURE_PLATE) });
		ItemModule.shapelessRecipe("BOWL", VinkaItems.BOWL(), new ItemStack[] { new ItemStack(Material.RABBIT_FOOT),
				new ItemStack(Material.RABBIT_FOOT), new ItemStack(Material.RABBIT_FOOT) });
		
		ItemModule.shapelessRecipe("STRING", VinkaItems.STRING(), new ItemStack[] { new ItemStack(Material.INK_SAC),
				new ItemStack(Material.INK_SAC), new ItemStack(Material.INK_SAC), new ItemStack(Material.INK_SAC) });
		ItemModule.shapelessRecipe("WHITE_WOOL", VinkaItems.WHITE_WOOL(), new ItemStack[] { new ItemStack(Material.STRING),
				new ItemStack(Material.STRING), new ItemStack(Material.STRING), new ItemStack(Material.STRING) });
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
		
		ItemModule.handRecipe("COAL", VinkaItems.LIGHT_GRAY_DYE(), "xxxx", new ItemStack[] {new ItemStack(Material.COAL)}, "x");

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

		ItemModule.craftedRecipe("DIRT_BLOCK",  VinkaItems.DIRT_BLOCK(), "xxxxxxxxx", new ItemStack[] {new ItemStack(Material.DRIED_KELP)}, "x");

		// Stone Age
		ItemModule.handRecipe("STONE_SLAB", VinkaItems.STONE_SLAB(), "xxxx",
				new ItemStack[] { new ItemStack(Material.GRAY_DYE) }, "x");
		ItemModule.handRecipe("STONE_BLOCK", VinkaItems.STONE_BLOCK(), "xxxx",
				new ItemStack[] { new ItemStack(Material.STONE_SLAB) }, "x");
		
		ItemModule.handRecipe("BUCKET", VinkaItems.BUCKET(), "xoxo", new ItemStack[] {new ItemStack(Material.STONE_SLAB)}, "x");

		ItemModule.handRecipe("FURNACE", VinkaItems.FURNACE(), "xxxx", new ItemStack[] { new ItemStack(Material.STONE) },
				"x");
		ItemModule.handRecipe("HOPPER", VinkaItems.HOPPER(), "aobo",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT), new ItemStack(Material.CHEST) }, "a,b");

		// Furnace Recipes
		ItemModule.furnaceRecipe("REFINED_IRON_ORE", VinkaItems.REFINED_IRON_ORE(), Material.LIGHT_GRAY_DYE, 5);
		ItemModule.furnaceRecipe("REFINED_GOLD_ORE", VinkaItems.REFINED_GOLD_ORE(), Material.DANDELION_YELLOW, 30);
		ItemModule.furnaceRecipe("REFINED_DIAMOND_ORE", VinkaItems.REFINED_DIAMOND_ORE(), Material.LIGHT_BLUE_DYE, 60);

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

		// Iron Age
		ItemModule.handRecipe("IRON_BLOCK", VinkaItems.IRON_BLOCK(), "xxxx",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT) }, "x");
		ItemModule.handRecipe("CRAFTING_TABLE", VinkaItems.CRAFTING_TABLE(), "abcd",
				new ItemStack[] { new ItemStack(Material.IRON_AXE), new ItemStack(Material.IRON_PICKAXE),
						new ItemStack(Material.IRON_HOE), new ItemStack(Material.IRON_SHOVEL) },
				"a,b,c,d");
		
		ItemModule.handRecipe("GOLD_INGOT", VinkaItems.GOLD_ORE(), "xxxxxxxxx", new ItemStack[] {new ItemStack(Material.IRON_INGOT)}, "x");

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
		
		ItemModule.handRecipe("DIAMOND_BLOCK", VinkaItems.DIAMOND_BLOCK(), "xxxx", new ItemStack[] {new ItemStack(Material.DIAMOND)}, "x");
	}
}
