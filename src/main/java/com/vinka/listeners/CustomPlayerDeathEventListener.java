package com.vinka.listeners;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.valkcore.modules.PlayerModule;
import com.valkcore.modules.TextModule;
import com.vinka.Vinka;
import com.vinka.events.CustomPlayerDeathEvent;
import com.vinka.events.CustomPlayerRespawnEvent;
import com.vinka.utils.Utils;

public class CustomPlayerDeathEventListener implements Listener {
	private String randomMessage(String[] array) {
		return array[new Random().nextInt(array.length)];
	}

	@EventHandler
	private void customPlayerDeathEvent(CustomPlayerDeathEvent e) {
		Player p = e.getPlayer();

		p.getWorld().playSound(p.getLocation(), Sound.AMBIENT_CAVE, 1f, 1f);

		Vinka.vinka.getServer().broadcastMessage(TextModule.color(deathMessage(e.getDamager().getType(), p)));

		p.sendTitle("", "You Died", 20, 80, 0);

		resetStats(p);
		dropItemsOnDeath(p);
		deathAnimation(p);

		Utils.spawnMonsterPlayer(p.getLocation(), EntityType.HUSK, p);
	}
	
	private String deathMessage(EntityType type, Player p) {
		String deathMessage = "";
		switch (type) {
		case SLIME:
			String[] slime = { "&q" + p.getName() + " &wgot into a sticky situation..",
					"&q" + p.getName() + " &wwas squashed into a ball of goo!",
					"&wSlime finally made a friend! Say hi to &q" + p.getName() + " &wthe slime!",
					"&q" + p.getName() + " &wdidn't made it past the slimes!", "&q" + p.getName() + " &wwas slimed to death.",
					"&q" + p.getName() + " &wthought they could be friends with a slime.",
					"&q" + p.getName() + " &wwas engulfed by the jelly monster." };
			deathMessage = randomMessage(slime);
			break;
		case ZOMBIE:
			String[] zombie = { "&q" + p.getName() + " &wbecame the undead!",
					"&q" + p.getName() + " &wthought zombies were their friends.", "&q" + p.getName() + " &wwas left 4 dead." };
			deathMessage = randomMessage(zombie);
			break;
		case HUSK:
			String[] husk = { "&q" + p.getName() + " &wexperienced immense pain.", "&q" + p.getName() + " &wdied a hero.",
					"&q" + p.getName() + " &wdecided to stand still in front of a monster." };
			deathMessage = randomMessage(husk);
			break;
		case SPIDER:
		case CAVE_SPIDER:
			String[] spider = { "&q" + p.getName() + " &wentered spider territory and that didn't go so well.",
					"&q" + p.getName() + " &wpeace offer didn't go so well with the spiders.", "The spiders ate &q" + p.getName(),
					"&q" + p.getName() + "&w's spider sense failed them." };
			deathMessage = randomMessage(spider);
			break;
		case PHANTOM:
			String[] phantom = { "&q" + p.getName() + " &wdidn't look up soon enough.", "&q" + p.getName() + " &wwent bird hunting!",
					"&q" + p.getName() + " &wfound out that birds are mean.",
					"&q" + p.getName() + " &wrealized that bird hunting isn't their thing." };
			deathMessage = randomMessage(phantom);
			break;
		case WITHER_SKELETON:
		case WITHER:
			String[] wither = { "&q" + p.getName() + " &wgot boned.", "&q" + p.getName() + " &wmessed with the wrong monster." };
			deathMessage = randomMessage(wither);
			break;
		default:
			String[] other = { "&q" + p.getName() + " &wwas sliced in half.", "&q" + p.getName() + " &wwas torn to pieces.",
					"&q" + p.getName() + "&w's limbs were smashed.", "&q" + p.getName() + " &wwas eviscerated.",
					"&q" + p.getName() + "&w's face was torn off.", "&q" + p.getName() + "&w's skull was crushed.",
					"&q" + p.getName() + "&w was brutally dissected.", "&q" + p.getName() + "&w's vital organs were ruptured.",
					"&q" + p.getName() + "&w was turned into a pile of flesh." };
			deathMessage = randomMessage(other);
			break;
		}
		
		return deathMessage;
	}

	private void resetStats(Player p) {
		PlayerModule.removeAllPotionEffects(p);
		PlayerModule.fillPlayerHealth(p);
		PlayerModule.fillPlayerFood(p);
		p.setExp(0);
		p.setLevel(p.getLevel() / 2);
	}

	private void dropItemsOnDeath(Player p) {
		Inventory inv = p.getInventory();
		for (int i = 0; i < inv.getSize(); i++) {
			if (inv.getItem(i) == null)
				continue;
			if (inv.getItem(i).getType() == Material.BLACK_STAINED_GLASS_PANE)
				continue;
			p.getWorld().dropItemNaturally(p.getLocation(), inv.getItem(i));
			inv.setItem(i, new ItemStack(Material.AIR));
		}
	}

	private void deathAnimation(final Player p) {
		p.setGameMode(GameMode.SPECTATOR);
		p.setFlySpeed(0.0f);

		final int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Vinka.vinka, new Runnable() {
			@Override
			public void run() {
				p.setVelocity(new Vector(0, 0.03, 0));
			}
		}, 0L, 1L);

		new BukkitRunnable() {
			@Override
			public void run() {
				Bukkit.getScheduler().cancelTask(task);
				PlayerModule.addPotionEffect(p, PotionEffectType.BLINDNESS, 40, 1);
			}
		}.runTaskLater(Vinka.vinka, 80);

		new BukkitRunnable() {
			@Override
			public void run() {
				if (p.getBedSpawnLocation() != null) {
					p.teleport(p.getBedSpawnLocation());
				} else {
					World world = Bukkit.getWorld("world");
					p.teleport(world.getSpawnLocation());
				}
				p.setGameMode(GameMode.SURVIVAL);
				p.setFlySpeed(0.1f);

				p.getWorld().playSound(p.getLocation(), Sound.MUSIC_DISC_13, 1f, 1f);

				// Call the custom player respawn event since the vanilla respawn event is never
				// called.
				Bukkit.getServer().getPluginManager().callEvent(new CustomPlayerRespawnEvent(p));
			}
		}.runTaskLater(Vinka.vinka, 100);
	}
}
