package de.davboecki.multimodworld.exchangeworld;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class ExchangeWorldGenerator extends ChunkGenerator {

	private void addBlock(byte[] result, int x, int y, int z, byte value) {
		result[(x * 16 + z) * 128 + y] = value;
	}

	public static Location getPortalNormal(World world) {
		return new Location(world, 2.5, 65, 10.5);
	}

	public static Location getOtherPortal(World world) {
		return new Location(world, -1.5, 65, 10.5);
	}
	
	@Override
	public byte[] generate(World world, Random random, int cx, int cz) {
		final byte[] result = new byte[32768];
		// if(cx > 30 || cx < -30 || cz > 30 || cz < -30) {
		// return result;
		// }

		// Grundschicht
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				addBlock(result, x, 0, z, (byte) Material.BEDROCK.getId());
				for (int y = 1; y < 64; y++) {
					addBlock(result, x, y, z, (byte) Material.DIRT.getId());
				}
				addBlock(result, x, 64, z, (byte) Material.GRASS.getId());
			}
		}

		if (cx == 0) {
			// Lavakanal
			for (int z = 0; z < 16; z++) {
				addBlock(result, 0, 65, z, (byte) Material.LAVA.getId());
				addBlock(result, 1, 65, z, (byte) Material.GRASS.getId());
			}

			if (cz == 0) {
				// Glas + Lawa Wand
				for (int z = 0; z < 16; z++) {
					for (int y = 65; y < 71; y++) {
						addBlock(result, 0, y, z, (byte) Material.LAVA.getId());
						addBlock(result, 1, y, z, (byte) Material.GLASS.getId());
					}
				}

				// Umrandung
				for (int x = 2; x < 16; x++) {
					addBlock(result, x, 65, 0, (byte) Material.GRASS.getId());
				}
				for (int z = 0; z < 16; z++) {
					addBlock(result, 15, 65, z, (byte) Material.GRASS.getId());
				}

				// Tor
				addBlock(result, 2, 65, 9, (byte) Material.STONE.getId());
				addBlock(result, 2, 66, 9, (byte) Material.STONE.getId());
				addBlock(result, 2, 67, 9, (byte) Material.STONE.getId());
				addBlock(result, 2, 67, 10, (byte) Material.STONE.getId());
				addBlock(result, 2, 67, 11, (byte) Material.STONE.getId());
				addBlock(result, 2, 66, 11, (byte) Material.STONE.getId());
				addBlock(result, 2, 65, 11, (byte) Material.STONE.getId());
			} else if (cz == 1) {
				// Glas + Lawa Wand
				for (int z = 0; z < 6; z++) {
					for (int y = 65; y < 71; y++) {
						addBlock(result, 0, y, z, (byte) Material.LAVA.getId());
						addBlock(result, 1, y, z, (byte) Material.GLASS.getId());
					}
				}

				// Umrandung
				for (int x = 2; x < 16; x++) {
					addBlock(result, x, 65, 5, (byte) Material.GRASS.getId());
				}
				for (int z = 0; z < 6; z++) {
					addBlock(result, 15, 65, z, (byte) Material.GRASS.getId());
				}

			}
		} else if (cx == -1) {
			// Lavakanal
			for (int z = 0; z < 16; z++) {
				addBlock(result, 15, 65, z, (byte) Material.GRASS.getId());
			}

			if (cz == 0) {
				// Glas + Lawa Wand
				for (int z = 0; z < 16; z++) {
					for (int y = 65; y < 71; y++) {
						addBlock(result, 15, y, z, (byte) Material.GLASS.getId());
					}
				}

				// Umrandung
				for (int x = 1; x < 15; x++) {
					addBlock(result, x, 65, 0, (byte) Material.GRASS.getId());
				}
				for (int z = 0; z < 16; z++) {
					addBlock(result, 0, 65, z, (byte) Material.GRASS.getId());
				}

				// Tor
				addBlock(result, 14, 65, 9, (byte) Material.STONE.getId());
				addBlock(result, 14, 66, 9, (byte) Material.STONE.getId());
				addBlock(result, 14, 67, 9, (byte) Material.STONE.getId());
				addBlock(result, 14, 67, 10, (byte) Material.STONE.getId());
				addBlock(result, 14, 67, 11, (byte) Material.STONE.getId());
				addBlock(result, 14, 66, 11, (byte) Material.STONE.getId());
				addBlock(result, 14, 65, 11, (byte) Material.STONE.getId());
			} else if (cz == 1) {
				// Glas + Lawa Wand
				for (int z = 0; z < 6; z++) {
					for (int y = 65; y < 71; y++) {
						addBlock(result, 15, y, z, (byte) Material.GLASS.getId());
					}
				}

				// Umrandung
				for (int x = 1; x < 15; x++) {
					addBlock(result, x, 65, 5, (byte) Material.GRASS.getId());
				}
				for (int z = 0; z < 6; z++) {
					addBlock(result, 0, 65, z, (byte) Material.GRASS.getId());
				}

			}
		}
		return result;
	}

	@Override
	public Location getFixedSpawnLocation(World world, Random random) {
		return new Location(world, 8, 65, 10, 0, 0);
	}
	
	@Override
	public boolean equals(Object object) {
		return object instanceof ExchangeWorldGenerator;
	}
}
