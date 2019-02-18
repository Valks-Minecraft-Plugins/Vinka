package com.vinka.mobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.valkutils.hologram.Hologram;
import com.valkutils.modules.ItemModule;
import com.valkutils.modules.PlayerModule;
import com.valkutils.modules.TextModule;
import com.vinka.Vinka;
import com.vinkaitems.VinkaItems;

public class MobInteract implements Listener {
	@EventHandler
	private void entityDamageEvent(EntityDamageByEntityEvent e) {
		Entity victim = e.getEntity();
		Entity attacker = e.getDamager();

		if (victim instanceof Player) {
			if (attacker instanceof Slime) {
				PlayerModule.addPotionEffect((Player) e.getEntity(), PotionEffectType.POISON, 100, 3);
				return;
			}
		}

		if (!(attacker instanceof Player))
			return;
		if (!(victim instanceof Monster))
			return;
		if (e.getDamage() <= 0)
			return;

		Location loc = victim.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation();
		final Hologram hg = new Hologram(loc.add(0.5d, 0, 0.5d), TextModule.color("&c" + (int) e.getDamage()));
		hg.setVisible(true);
		hg.move();

		World w = loc.getWorld();

		Location newLoc = new Location(w, loc.getX(), loc.getY() + 1.5, loc.getZ());
		w.spawnParticle(Particle.VILLAGER_ANGRY, newLoc, 1);
		w.spawnParticle(Particle.LAVA, newLoc, 5);

		new BukkitRunnable() {
			@Override
			public void run() {
				hg.destroy();
			}
		}.runTaskLater(Vinka.vinka, 60);
	}
	
	@EventHandler
	private void inventoryCloseEvent(InventoryCloseEvent e) {
		if (e.getView().getTitle().toLowerCase().equals("loot")) {
			for (ItemStack item : e.getInventory().getContents()) {
				if (item != null) {
					if (item.getType() != Material.AIR) {
						Player p = (Player) e.getPlayer();
						p.getWorld().dropItemNaturally(p.getLocation(), item);
					}
				}
			}
		}
	}

	@EventHandler
	private void playerInteractAtEntity(PlayerInteractAtEntityEvent e) {
		if (e.getHand() != EquipmentSlot.HAND)
			return;
		Entity entity = e.getRightClicked();
		if (entity == null)
			return;
		if (entity.getType() != EntityType.ARMOR_STAND)
			return;
		
		e.setCancelled(true);
		e.getRightClicked().remove();

		int size = 9;

		Inventory inv = Bukkit.createInventory(null, 9, "Loot");

		inv.setItem(new Random().nextInt(size), VinkaItems.REDSTONE());
		if (Math.random() < 0.35)
			inv.setItem(new Random().nextInt(size), VinkaItems.STICK());
		if (Math.random() < 0.05)
			inv.setItem(new Random().nextInt(size), VinkaItems.SUGAR());
		if (Math.random() < 0.05)
			inv.setItem(new Random().nextInt(size), VinkaItems.IRON_ORE());
		if (Math.random() < 0.03)
			inv.setItem(new Random().nextInt(size), VinkaItems.GOLD_ORE());
		for (int i = 0; i < 5; i++)
			if (Math.random() < 0.05)
				inv.setItem(new Random().nextInt(size),
						VinkaItems.items.get(new Random().nextInt(VinkaItems.items.size())));

		e.getPlayer().openInventory(inv);
	}

	@EventHandler
	private void shear(PlayerShearEntityEvent e) {
		e.setCancelled(true);
		Sheep sheep = (Sheep) e.getEntity();
		sheep.setSheared(true);
		Location loc = sheep.getLocation();
		loc.getWorld().dropItemNaturally(loc, VinkaItems.IRON_ORE());
	}

	@EventHandler
	private void blockNewVillagerTrades(VillagerAcquireTradeEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	private void playerInteractEntity(PlayerInteractEntityEvent e) {
		Entity entity = e.getRightClicked();
		EntityType type = entity.getType();
		Player p = e.getPlayer();
		World w = p.getWorld();
		Location eloc = entity.getLocation();
		EntityEquipment pEquip = p.getEquipment();
		Material holdingItem = pEquip.getItemInMainHand().getType();
		
		if (e.getHand() != EquipmentSlot.HAND)
			return;

		switch (type) {
		case WOLF:
			Wolf wolf = (Wolf) entity;
			if (!wolf.isTamed()) {
				wolf.setTamed(true);
				wolf.setOwner(e.getPlayer());
			}
			return;
		case CHICKEN:
			if (holdingItem != Material.WHEAT_SEEDS) {
				entity.remove();
				w.dropItemNaturally(eloc, VinkaItems.CHICKEN_SPAWN_EGG());
			}
			return;
		case SHEEP:
			if (holdingItem != Material.WHEAT) {
				entity.remove();
				w.dropItemNaturally(eloc, VinkaItems.SHEEP_SPAWN_EGG());
			}
			return;
		default:
			return;
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	private void customMobs(CreatureSpawnEvent e) {
		if (e.getSpawnReason() == SpawnReason.NATURAL) {
			e.setCancelled(true);
		}

		if (e.getSpawnReason() == SpawnReason.SPAWNER_EGG) {
			if (e.getEntityType() == EntityType.WOLF) {
				Wolf wolf = (Wolf) e.getEntity();
				wolf.setAdult();
				wolf.setCollarColor(DyeColor.MAGENTA);
			}

			if (e.getEntityType() == EntityType.CHICKEN) {
				Chicken chick = (Chicken) e.getEntity();
				chick.setBaby();
				chick.setCustomNameVisible(false); // For some reason it's default is visible?
			}

			if (e.getEntityType() == EntityType.SHEEP) {
				Sheep sheep = (Sheep) e.getEntity();
				sheep.setBaby();
				sheep.setColor(DyeColor.LIGHT_GRAY);
			}

			if (e.getEntityType() == EntityType.VILLAGER) {
				Villager villager = (Villager) e.getEntity();
				villager.setProfession(Profession.BLACKSMITH);

				List<MerchantRecipe> merchantRecipes = new ArrayList<MerchantRecipe>();
				merchantRecipes.add(villagerRecipe(VinkaItems.LAPIS(),
						new ItemStack[] { VinkaItems.SUGAR(), VinkaItems.MELON_SEEDS() }));

				ItemStack theExecutor = ItemModule.item("The Executor", "Husks don't come back.",
						Material.DIAMOND_SWORD);
				theExecutor.addEnchantment(Enchantment.FIRE_ASPECT, 1);
				theExecutor.addEnchantment(Enchantment.KNOCKBACK, 1);

				merchantRecipes.add(
						villagerRecipe(theExecutor, new ItemStack[] { VinkaItems.STICK(), VinkaItems.OBSIDIAN() }));

				villager.setRecipes(merchantRecipes);
			}
		}
	}

	private MerchantRecipe villagerRecipe(ItemStack result, ItemStack[] ingredients) {
		MerchantRecipe recipe = new MerchantRecipe(result, 10000);
		recipe.setExperienceReward(false);
		for (ItemStack ingredient : ingredients) {
			recipe.addIngredient(ingredient);
		}
		return recipe;
	}
}
