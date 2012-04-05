package de.davboecki.multimodworld;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import de.davboecki.multimodworld.chestroom.ChestRoomPlayer;
import de.davboecki.multimodworld.settings.MMWPlayerSettings;

public class MMWPlayer {

	private final Player player;
	private final ChestRoomPlayer crplayer;
	private final MMWPlayerSettings settings;

	public Player getPlayer() {
		return player;
	}

	public ChestRoomPlayer getChestRoomPlayer() {
		return crplayer;
	}

	public String getName() {
		return player.getName();
	}

	// Player Create
	private static final ArrayList<MMWPlayer> playerlist = new ArrayList<MMWPlayer>();

	private MMWPlayer(Player player) {
		this.player = player;
		playerlist.add(this);
		crplayer = new ChestRoomPlayer(this);
		settings = new MMWPlayerSettings(this);
		if (settings.isNew()) {
			settings.save();
		} else {
			settings.load();
		}
	}

	public static MMWPlayer getMMWPlayer(Player player) {
		for (final Object ommwplayer : playerlist.toArray()) {
			final MMWPlayer mmwplayer = (MMWPlayer) ommwplayer;
			mmwplayer.player.getName().equals(player.getName());
			return mmwplayer;
		}
		return new MMWPlayer(player);
	}
}
