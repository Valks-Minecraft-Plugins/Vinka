package com.vinka.modules;

import org.bukkit.block.Biome;

import com.vinka.Vinka;

public class WorldModule {
	public static boolean day() {
		long time = Vinka.vinka.getServer().getWorld("world").getTime();
		return time < 12300 || time > 23850;
	}

	public static boolean isBirchBiome(Biome biome) {
		switch (biome) {
		case BIRCH_FOREST:
		case BIRCH_FOREST_HILLS:
		case TALL_BIRCH_FOREST:
		case TALL_BIRCH_HILLS:
			return true;
		default:
			return false;
		}
	}

	public static boolean isForestBiome(Biome biome) {
		switch (biome) {
		case FOREST:
		case BIRCH_FOREST:
		case BIRCH_FOREST_HILLS:
		case DARK_FOREST:
		case DARK_FOREST_HILLS:
		case FLOWER_FOREST:
		case TALL_BIRCH_FOREST:
			return true;
		default:
			return false;
		}
	}

	public static boolean isOceanBiome(Biome biome) {
		switch (biome) {
		case OCEAN:
		case COLD_OCEAN:
		case DEEP_COLD_OCEAN:
		case DEEP_FROZEN_OCEAN:
		case DEEP_LUKEWARM_OCEAN:
		case DEEP_OCEAN:
		case DEEP_WARM_OCEAN:
		case FROZEN_OCEAN:
		case LUKEWARM_OCEAN:
		case WARM_OCEAN:
			return true;
		default:
			return false;
		}
	}

	public static boolean isSnowyBiome(Biome biome) {
		switch (biome) {
		case SNOWY_BEACH:
		case SNOWY_MOUNTAINS:
		case SNOWY_TAIGA:
		case SNOWY_TAIGA_HILLS:
		case SNOWY_TAIGA_MOUNTAINS:
		case SNOWY_TUNDRA:
			return true;
		default:
			return false;
		}
	}
}
