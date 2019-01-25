package com.vinka;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.vinka.blocks.Blocks;
import com.vinka.commands.CommandTest;
import com.vinka.configs.LoadPlayerFiles;
import com.vinka.consumables.Food;
import com.vinka.dungeons.SleepTeleport;
import com.vinka.items.VinkaItems;
import com.vinka.mobs.Mobs;
import com.vinka.modules.MobModule;
import com.vinka.player.PlayerHandler;
import com.vinka.utils.Utils;

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

						for (Location testLoc : MobModule.testLocations(loc, radius)) {
							if (MobModule.validSpawningLocation(testLoc)) {
								MobModule.spawnMonster(testLoc, EntityType.HUSK);
								return;
							}
						}
					}
				}
			}
		}.runTaskTimer(this, 200, 600);
	}
	
	private void registerCommands() {
		getCommand("test").setExecutor(new CommandTest());
	}

	private void registerListeners(PluginManager pm) {
		pm.registerEvents(new Blocks(), this);
		pm.registerEvents(new Food(), this);
		pm.registerEvents(new Mobs(), this);
		pm.registerEvents(new PlayerHandler(), this);
		pm.registerEvents(new SleepTeleport(), this);
		pm.registerEvents(new LoadPlayerFiles(), this);
	}

	public List<NamespacedKey> recipes = new ArrayList<NamespacedKey>();

	private void addRecipes() {
		Utils.craftedRecipe("IRON_ORE_FROM_LIGHT_GRAY_DYE_ORE", VinkaItems.IRON_ORE(), "xxxxxxxxx", new ItemStack[] {new ItemStack(Material.LIGHT_GRAY_DYE)}, "x");

		// Primitive Age
		Utils.handRecipe("SIGN", VinkaItems.SIGN(), "xxss",
				new ItemStack[] { new ItemStack(Material.OAK_SLAB), new ItemStack(Material.STICK) }, "x,s");
		Utils.handRecipe("OAK_DOOR", VinkaItems.OAK_DOOR(), "xoxo",
				new ItemStack[] { new ItemStack(Material.OAK_PLANKS) }, "x");
		Utils.handRecipe("OAK_SLAB", VinkaItems.OAK_SLAB(), "xxxx",
				new ItemStack[] { new ItemStack(Material.RABBIT_FOOT) }, "x");
		Utils.handRecipe("OAK_PLANKS", VinkaItems.OAK_PLANKS(), "xxxx",
				new ItemStack[] { new ItemStack(Material.OAK_SLAB) }, "x");
		Utils.handRecipe("CHEST", VinkaItems.CHEST(), "xxxx", new ItemStack[] { new ItemStack(Material.OAK_PLANKS) },
				"x");
		Utils.handRecipe("OAK_TRAPDOOR", VinkaItems.OAK_TRAPDOOR(), "ooxx",
				new ItemStack[] { new ItemStack(Material.OAK_SLAB) }, "x");

		Utils.handRecipe("WHITE_BED", VinkaItems.WHITE_BED(), "wwxx",
				new ItemStack[] { new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.OAK_SLAB) }, "w,x");
		Utils.handRecipe("OAK_FENCE", VinkaItems.OAK_FENCE(), "ssww",
				new ItemStack[] { new ItemStack(Material.OAK_SLAB), new ItemStack(Material.OAK_PLANKS) }, "s,w");
		Utils.handRecipe("OAK_GATE", VinkaItems.OAK_FENCE_GATE(), "swws",
				new ItemStack[] { new ItemStack(Material.STICK), new ItemStack(Material.OAK_SLAB) }, "s,w");
		Utils.handRecipe("WHITE_BANNER", VinkaItems.WHITE_BANNER(), "woso",
				new ItemStack[] { new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.STICK) }, "w,s");
		Utils.handRecipe("LADDER", VinkaItems.LADDER(), "xoxo", new ItemStack[] { new ItemStack(Material.STICK) }, "x");
		Utils.handRecipe("REDSTONE_TORCH", VinkaItems.REDSTONE_TORCH(), "coso",
				new ItemStack[] { new ItemStack(Material.COAL), new ItemStack(Material.STICK) }, "c,s");
		Utils.handRecipe("FISHING_ROD", VinkaItems.FISHING_ROD(), "ocso",
				new ItemStack[] { new ItemStack(Material.STRING), new ItemStack(Material.STICK) }, "c,s");
		Utils.handRecipe("OAK_PRESSURE_PLATE", VinkaItems.OAK_PRESSURE_PLATE(), "xoxo",
				new ItemStack[] { new ItemStack(Material.OAK_SLAB) }, "x");
		Utils.shapelessRecipe("STICK", VinkaItems.STICK(),
				new ItemStack[] { new ItemStack(Material.RABBIT_FOOT), new ItemStack(Material.RABBIT_FOOT) });
		Utils.shapelessRecipe("ITEM_FRAME", VinkaItems.ITEM_FRAME(), new ItemStack[] { new ItemStack(Material.STICK),
				new ItemStack(Material.STICK), new ItemStack(Material.STICK) });
		Utils.shapelessRecipe("OAK_STAIRS", VinkaItems.OAK_STAIRS(),
				new ItemStack[] { new ItemStack(Material.OAK_PLANKS), new ItemStack(Material.OAK_PLANKS),
						new ItemStack(Material.OAK_PLANKS) });
		Utils.shapelessRecipe("OAK_BUTTON", VinkaItems.OAK_BUTTON(),
				new ItemStack[] { new ItemStack(Material.OAK_PRESSURE_PLATE) });
		Utils.shapelessRecipe("BOWL", VinkaItems.BOWL(), new ItemStack[] { new ItemStack(Material.RABBIT_FOOT),
				new ItemStack(Material.RABBIT_FOOT), new ItemStack(Material.RABBIT_FOOT) });
		Utils.shapelessRecipe("STRING", VinkaItems.STRING(), new ItemStack[] { new ItemStack(Material.INK_SAC),
				new ItemStack(Material.INK_SAC), new ItemStack(Material.INK_SAC), new ItemStack(Material.INK_SAC) });
		Utils.shapelessRecipe("WHITE_WOOL", VinkaItems.WHITE_WOOL(), new ItemStack[] { new ItemStack(Material.STRING),
				new ItemStack(Material.STRING), new ItemStack(Material.STRING), new ItemStack(Material.STRING) });

		// Reverse Recipes
		Utils.shapelessRecipe("OAK_SLAB_FROM_OAK_PLANKS", VinkaItems.OAK_SLAB(),
				new ItemStack[] { new ItemStack(Material.OAK_PLANKS) });
		Utils.shapelessRecipe("RABBIT_FOOT_FROM_OAK_SLAB", VinkaItems.RABBIT_FOOT(),
				new ItemStack[] { new ItemStack(Material.OAK_SLAB) });
		Utils.shapelessRecipe("OAK_PLANKS_FROM_OAK_DOOR", VinkaItems.OAK_PLANKS(),
				new ItemStack[] { new ItemStack(Material.OAK_DOOR) });
		Utils.shapelessRecipe("OAK_PLANKS_FROM_OAK_STAIRS", VinkaItems.OAK_PLANKS(),
				new ItemStack[] { new ItemStack(Material.OAK_STAIRS) });

		Utils.furnaceRecipe("MELON_SEEDS", VinkaItems.MELON_SEEDS(), Material.BEETROOT_SEEDS, 5);
		Utils.craftedRecipe("SEEDS", VinkaItems.WHEAT_SEEDS(), "xxxxxxxxx",
				new ItemStack[] { new ItemStack(Material.MELON_SEEDS) }, "x");

		// Tools
		Utils.handRecipe("WOODEN_PICK", VinkaItems.WOODEN_PICKAXE(), "xxsx",
				new ItemStack[] { new ItemStack(Material.OAK_SLAB), new ItemStack(Material.STICK) }, "x,s");
		Utils.handRecipe("WOODEN_SHOVEL", VinkaItems.WOODEN_SHOVEL(), "xoso",
				new ItemStack[] { new ItemStack(Material.OAK_SLAB), new ItemStack(Material.STICK) }, "x,s");
		Utils.handRecipe("WOODEN_HOE", VinkaItems.WOODEN_HOE(), "xxso",
				new ItemStack[] { new ItemStack(Material.OAK_SLAB), new ItemStack(Material.STICK) }, "x,s");
		Utils.handRecipe("WOODEN_AXE", VinkaItems.WOODEN_AXE(), "sxox",
				new ItemStack[] { new ItemStack(Material.OAK_SLAB), new ItemStack(Material.STICK) }, "x,s");
		Utils.handRecipe("WOODEN_SWORD", VinkaItems.WOODEN_SWORD(), "oxsx",
				new ItemStack[] { new ItemStack(Material.OAK_SLAB), new ItemStack(Material.STICK) }, "x,s");
		Utils.handRecipe("BOW", VinkaItems.BOW(), "sxsx",
				new ItemStack[] { new ItemStack(Material.STRING), new ItemStack(Material.STICK) }, "s,x");
		Utils.handRecipe("ARROW", VinkaItems.ARROW(), "aobo",
				new ItemStack[] { new ItemStack(Material.RABBIT_FOOT), new ItemStack(Material.STICK) }, "a,b");

		Utils.handRecipe("DIRT_BLOCK", VinkaItems.DIRT_BLOCK(), "xxxx",
				new ItemStack[] { new ItemStack(Material.OAK_SAPLING) }, "x");

		// Stone Age
		Utils.handRecipe("STONE_SLAB", VinkaItems.STONE_SLAB(), "xxxx",
				new ItemStack[] { new ItemStack(Material.GRAY_DYE) }, "x");
		Utils.handRecipe("STONE_BLOCK", VinkaItems.STONE_BLOCK(), "xxxx",
				new ItemStack[] { new ItemStack(Material.STONE_SLAB) }, "x");

		Utils.handRecipe("FURNACE", VinkaItems.FURNACE(), "xxxx", new ItemStack[] { new ItemStack(Material.STONE) },
				"x");
		Utils.handRecipe("HOPPER", VinkaItems.HOPPER(), "aobo",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT), new ItemStack(Material.CHEST) }, "a,b");

		// Furnace Recipes
		Utils.furnaceRecipe("REFINED_IRON_ORE", VinkaItems.REFINED_IRON_ORE(), Material.LIGHT_GRAY_DYE, 5);
		Utils.furnaceRecipe("REFINED_GOLD_ORE", VinkaItems.REFINED_GOLD_ORE(), Material.DANDELION_YELLOW, 30);
		Utils.furnaceRecipe("REFINED_DIAMOND_ORE", VinkaItems.REFINED_DIAMOND_ORE(), Material.LIGHT_BLUE_DYE, 60);

		// Tools
		Utils.handRecipe("STONE_PICK", VinkaItems.STONE_PICKAXE(), "xxsx",
				new ItemStack[] { new ItemStack(Material.STONE_SLAB), new ItemStack(Material.STICK) }, "x,s");
		Utils.handRecipe("STONE_SHOVEL", VinkaItems.STONE_SHOVEL(), "xoso",
				new ItemStack[] { new ItemStack(Material.STONE_SLAB), new ItemStack(Material.STICK) }, "x,s");
		Utils.handRecipe("STONE_HOE", VinkaItems.STONE_HOE(), "xxso",
				new ItemStack[] { new ItemStack(Material.STONE_SLAB), new ItemStack(Material.STICK) }, "x,s");
		Utils.handRecipe("STONE_AXE", VinkaItems.STONE_AXE(), "sxox",
				new ItemStack[] { new ItemStack(Material.STONE_SLAB), new ItemStack(Material.STICK) }, "x,s");
		Utils.handRecipe("STONE_SWORD", VinkaItems.STONE_SWORD(), "oxsx",
				new ItemStack[] { new ItemStack(Material.STONE_SLAB), new ItemStack(Material.STICK) }, "x,s");

		Utils.handRecipe("TNT", VinkaItems.TNT(), "xxxx", new ItemStack[] { new ItemStack(Material.SUGAR) }, "x");

		Utils.craftedRecipe("RAILS", VinkaItems.RAILS(), "oooabaooo",
				new ItemStack[] { new ItemStack(Material.STONE_SLAB), new ItemStack(Material.STICK) }, "a,b");
		Utils.craftedRecipe("POWERED_RAILS", VinkaItems.POWERED_RAILS(), "aoaabaooo",
				new ItemStack[] { new ItemStack(Material.STONE_SLAB), new ItemStack(Material.STICK) }, "a,b");
		Utils.craftedRecipe("DETECTOR_RAILS", VinkaItems.DETECTOR_RAILS(), "oooabaaoa",
				new ItemStack[] { new ItemStack(Material.STONE_SLAB), new ItemStack(Material.STICK) }, "a,b");
		Utils.craftedRecipe("ACTIVATOR_RAILS", VinkaItems.ACTIVATOR_RAILS(), "aoaabaaoa",
				new ItemStack[] { new ItemStack(Material.STONE_SLAB), new ItemStack(Material.STICK) }, "a,b");

		Utils.craftedRecipe("MINECART", VinkaItems.MINECART(), "oooaoaaaa",
				new ItemStack[] { new ItemStack(Material.STONE_SLAB) }, "a");

		Utils.craftedRecipe("CARVED_PUMPKIN", VinkaItems.CARVED_PUMPKIN(), "xxxxxxxxx",
				new ItemStack[] { new ItemStack(Material.WHEAT_SEEDS) }, "x");

		Utils.handRecipe("LEVER", VinkaItems.LEVER(), "aobo",
				new ItemStack[] { new ItemStack(Material.STONE_SLAB), new ItemStack(Material.STICK) }, "a,b");

		Utils.handRecipe("SHIELD", VinkaItems.SHIELD(), "aobo",
				new ItemStack[] { new ItemStack(Material.OAK_PLANKS), new ItemStack(Material.OAK_SLAB) }, "a,b");

		Utils.handRecipe("IRON_TRAPDOOR", VinkaItems.IRON_TRAPDOOR(), "xxoo",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT) }, "x");
		Utils.handRecipe("IRON_DOOR", VinkaItems.IRON_DOOR(), "xoxo",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT) }, "x");
		Utils.shapelessRecipe("STONE_BUTTON", VinkaItems.STONE_BUTTON(),
				new ItemStack[] { new ItemStack(Material.GRAY_DYE) });
		Utils.shapelessRecipe("STONE_PRESSUREPLATE", VinkaItems.STONE_PRESSUREPLATE(),
				new ItemStack[] { new ItemStack(Material.STONE_SLAB) });

		Utils.craftedRecipe("WOOD_BOOTS", VinkaItems.LEATHER_BOOTS(), "ooooooxox",
				new ItemStack[] { new ItemStack(Material.OAK_PLANKS) }, "x");
		Utils.craftedRecipe("WOOD_LEGGINGS", VinkaItems.LEATHER_LEGGINGS(), "oooxoxxox",
				new ItemStack[] { new ItemStack(Material.OAK_PLANKS) }, "x");
		Utils.craftedRecipe("WOOD_CHESTPLATE", VinkaItems.LEATHER_CHESTPLATE(), "oooxxxxxx",
				new ItemStack[] { new ItemStack(Material.OAK_PLANKS) }, "x");
		Utils.craftedRecipe("WOOD_HELMET", VinkaItems.LEATHER_HELMET(), "xxxoooooo",
				new ItemStack[] { new ItemStack(Material.OAK_PLANKS) }, "x");

		Utils.craftedRecipe("IRON_BOOTS", VinkaItems.IRON_BOOTS(), "ooooooxox",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT) }, "x");
		Utils.craftedRecipe("IRON_LEGGINGS", VinkaItems.IRON_LEGGINGS(), "oooxoxxox",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT) }, "x");
		Utils.craftedRecipe("IRON_CHESTPLATE", VinkaItems.IRON_CHESTPLATE(), "oooxxxxxx",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT) }, "x");
		Utils.craftedRecipe("IRON_HELMET", VinkaItems.IRON_HELMET(), "xxxoooooo",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT) }, "x");

		Utils.craftedRecipe("GOLDEN_BOOTS", VinkaItems.GOLDEN_BOOTS(), "ooooooxox",
				new ItemStack[] { new ItemStack(Material.GOLD_INGOT) }, "x");
		Utils.craftedRecipe("GOLDEN_LEGGINGS", VinkaItems.GOLDEN_LEGGINGS(), "oooxoxxox",
				new ItemStack[] { new ItemStack(Material.GOLD_INGOT) }, "x");
		Utils.craftedRecipe("GOLDEN_CHESTPLATE", VinkaItems.GOLDEN_CHESTPLATE(), "oooxxxxxx",
				new ItemStack[] { new ItemStack(Material.GOLD_INGOT) }, "x");
		Utils.craftedRecipe("GOLDEN_HELMET", VinkaItems.GOLDEN_HELMET(), "xxxoooooo",
				new ItemStack[] { new ItemStack(Material.GOLD_INGOT) }, "x");

		Utils.craftedRecipe("DIAMOND_BOOTS", VinkaItems.DIAMOND_BOOTS(), "ooooooxox",
				new ItemStack[] { new ItemStack(Material.DIAMOND) }, "x");
		Utils.craftedRecipe("DIAMOND_LEGGINGS", VinkaItems.DIAMOND_LEGGINGS(), "oooxoxxox",
				new ItemStack[] { new ItemStack(Material.DIAMOND) }, "x");
		Utils.craftedRecipe("DIAMOND_CHESTPLATE", VinkaItems.DIAMOND_CHESTPLATE(), "oooxxxxxx",
				new ItemStack[] { new ItemStack(Material.DIAMOND) }, "x");
		Utils.craftedRecipe("DIAMOND_HELMET", VinkaItems.DIAMOND_HELMET(), "xxxoooooo",
				new ItemStack[] { new ItemStack(Material.DIAMOND) }, "x");

		// Iron Age
		Utils.handRecipe("IRON_BLOCK", VinkaItems.IRON_BLOCK(), "xxxx",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT) }, "x");
		Utils.handRecipe("CRAFTING_TABLE", VinkaItems.CRAFTING_TABLE(), "abcd",
				new ItemStack[] { new ItemStack(Material.IRON_AXE), new ItemStack(Material.IRON_PICKAXE),
						new ItemStack(Material.IRON_HOE), new ItemStack(Material.IRON_SHOVEL) },
				"a,b,c,d");

		// Tools
		Utils.handRecipe("IRON_PICK", VinkaItems.IRON_PICKAXE(), "xxsx",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT), new ItemStack(Material.STICK) }, "x,s");
		Utils.handRecipe("IRON_SHOVEL", VinkaItems.IRON_SHOVEL(), "xoso",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT), new ItemStack(Material.STICK) }, "x,s");
		Utils.handRecipe("IRON_HOE", VinkaItems.IRON_HOE(), "xxso",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT), new ItemStack(Material.STICK) }, "x,s");
		Utils.handRecipe("IRON_AXE", VinkaItems.IRON_AXE(), "sxox",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT), new ItemStack(Material.STICK) }, "x,s");
		Utils.handRecipe("IRON_SWORD", VinkaItems.IRON_SWORD(), "oxsx",
				new ItemStack[] { new ItemStack(Material.IRON_INGOT), new ItemStack(Material.STICK) }, "x,s");

		// Gold Age

		// Tools
		Utils.handRecipe("GOLD_PICK", VinkaItems.GOLD_PICKAXE(), "xxsx",
				new ItemStack[] { new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.STICK) }, "x,s");
		Utils.handRecipe("GOLD_SHOVEL", VinkaItems.GOLD_SHOVEL(), "xoso",
				new ItemStack[] { new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.STICK) }, "x,s");
		Utils.handRecipe("GOLD_HOE", VinkaItems.GOLD_HOE(), "xxso",
				new ItemStack[] { new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.STICK) }, "x,s");
		Utils.handRecipe("GOLD_AXE", VinkaItems.GOLD_AXE(), "sxox",
				new ItemStack[] { new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.STICK) }, "x,s");
		Utils.handRecipe("GOLD_SWORD", VinkaItems.GOLD_SWORD(), "oxsx",
				new ItemStack[] { new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.STICK) }, "x,s");

		// Diamond Age

		// Tools
		Utils.handRecipe("DIAMOND_PICK", VinkaItems.DIAMOND_PICKAXE(), "xxsx",
				new ItemStack[] { new ItemStack(Material.DIAMOND), new ItemStack(Material.STICK) }, "x,s");
		Utils.handRecipe("DIAMOND_SHOVEL", VinkaItems.DIAMOND_SHOVEL(), "xoso",
				new ItemStack[] { new ItemStack(Material.DIAMOND), new ItemStack(Material.STICK) }, "x,s");
		Utils.handRecipe("DIAMOND_HOE", VinkaItems.DIAMOND_HOE(), "xxso",
				new ItemStack[] { new ItemStack(Material.DIAMOND), new ItemStack(Material.STICK) }, "x,s");
		Utils.handRecipe("DIAMOND_AXE", VinkaItems.DIAMOND_AXE(), "sxox",
				new ItemStack[] { new ItemStack(Material.DIAMOND), new ItemStack(Material.STICK) }, "x,s");
		Utils.handRecipe("DIAMOND_SWORD", VinkaItems.DIAMOND_SWORD(), "oxsx",
				new ItemStack[] { new ItemStack(Material.DIAMOND), new ItemStack(Material.STICK) }, "x,s");
	}
}
