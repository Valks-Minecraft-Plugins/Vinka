package com.vinka.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import com.vinka.Vinka;

public class Utils {
	public static void achievement(Player p, String message) {
		p.sendTitle("", message, 20, 60, 20);
		Vinka.vinka.getServer().broadcastMessage(Utils.color("&f" + p.getPlayer().getName() + " &7just achieved &f" + message + "&7!"));
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
	
	public static void craftedRecipe(String key, ItemStack item, String rows, ItemStack[] ingredients, String ids) {
		NamespacedKey namespacedKey = new NamespacedKey(Vinka.vinka, key);
		ShapedRecipe recipe = new ShapedRecipe(namespacedKey, item);
		String row1 = rows.substring(0, 3);
		String row2 = rows.substring(3, 6);
		String row3 = rows.substring(6, 9);
		recipe.shape(row1, row2, row3);
		
		String[] the_ids = ids.split(",");
		
		for (int i = 0; i < ingredients.length; i++) {
			recipe.setIngredient(the_ids[i].charAt(0), ingredients[i].getType());
		}
		Vinka.vinka.getServer().addRecipe(recipe);
		Vinka.vinka.recipes.add(new NamespacedKey(Vinka.vinka, key));
	}
	
	public static void handRecipe(String key, ItemStack item, String rows, ItemStack[] ingredients, String ids) {
		NamespacedKey namespacedKey = new NamespacedKey(Vinka.vinka, key);
		ShapedRecipe recipe = new ShapedRecipe(namespacedKey, item);
		String row1 = rows.substring(0, 2);
		String row2 = rows.substring(2, 4);
		recipe.shape(row1, row2);
		
		String[] the_ids = ids.split(",");
		
		for (int i = 0; i < ingredients.length; i++) {
			recipe.setIngredient(the_ids[i].charAt(0), ingredients[i].getType());
		}
		Vinka.vinka.getServer().addRecipe(recipe);
		Vinka.vinka.recipes.add(new NamespacedKey(Vinka.vinka, key));
	}
	
	/*
	 * Time is in seconds.
	 */
	public static void furnaceRecipe(String key, ItemStack result, Material required, int time) {
		NamespacedKey nameSpacedKey = new NamespacedKey(Vinka.vinka, key);
		FurnaceRecipe recipe = new FurnaceRecipe(nameSpacedKey, result, required, 0f, time * 20);
		Vinka.vinka.getServer().addRecipe(recipe);
		Vinka.vinka.recipes.add(new NamespacedKey(Vinka.vinka, key));
	}
	
	public static void shapelessRecipe(String key, ItemStack item, ItemStack[] ingredients) {
		NamespacedKey namespacedKey = new NamespacedKey(Vinka.vinka, key);
		ShapelessRecipe recipe = new ShapelessRecipe(namespacedKey, item);
		for (ItemStack ingredient : ingredients) {
			recipe.addIngredient(ingredient.getType());
		}
		Vinka.vinka.getServer().addRecipe(recipe);
	}
	
	public static ItemStack item(String name, String lore, Material material) {
		ItemStack item = new ItemStack(material);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(ChatColor.WHITE + color(name));
		im.addItemFlags(ItemFlag.values());
		List<String> list = new ArrayList<String>();
		for (String element : lore.split("\n")) {
			list.add(ChatColor.GRAY + color(element));
		}
		im.setLore(list);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack tool(String name, String lore, Material material) {
		ItemStack item = new ItemStack(material, 1);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(ChatColor.WHITE + color(name));
		im.addItemFlags(ItemFlag.values());
		List<String> list = new ArrayList<String>();
		for (String element : lore.split("\n")) {
			list.add(ChatColor.GRAY + color(element));
		}
		im.setLore(list);
		item.setItemMeta(im);
		return item;
	}
	
	public static String color(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
}
