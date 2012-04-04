package de.davboecki.multimodworld.chestroom.listener;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import de.davboecki.multimodworld.MMWPlayer;
import de.davboecki.multimodworld.MultiModWorld;
import de.davboecki.multimodworld.chestroom.ChestRoomPlayer;

public class PlayerMoveListener implements Listener {

	MultiModWorld plugin;

	public PlayerMoveListener(MultiModWorld instance) {
		plugin = instance;
	}

	@EventHandler
	public void PlayerMove(PlayerMoveEvent event) {
		final MMWPlayer mPlayer = MMWPlayer.getMMWPlayer(event.getPlayer());
		if (mPlayer.getChestRoomPlayer().getWorldPos() != null
				&& !mPlayer.getChestRoomPlayer().getWorldPos().equals(mPlayer.getChestRoomPlayer().getPlayerPos())) {
			if (mPlayer.getChestRoomPlayer().getPlayerPos().equals(ChestRoomPlayer.WorldPos.LavaWall)) {
				if (mPlayer.getChestRoomPlayer().getWorldPos().equals(ChestRoomPlayer.WorldPos.Normal)) {
					final Location loc = mPlayer.getPlayer().getLocation();
					loc.setX(loc.getX() + 3);
					mPlayer.getPlayer().teleport(loc);
				} else if (mPlayer.getChestRoomPlayer().getWorldPos().equals(ChestRoomPlayer.WorldPos.Mod)) {
					final Location loc = mPlayer.getPlayer().getLocation();
					loc.setX(loc.getX() - 3);
					mPlayer.getPlayer().teleport(loc);
				}
			} else if (mPlayer.getChestRoomPlayer().getPlayerPos().equals(ChestRoomPlayer.WorldPos.Underground)) {
				final Location loc = mPlayer.getPlayer().getWorld().getSpawnLocation();
				mPlayer.getPlayer().teleport(loc);
			} else {
				event.setCancelled(true);
			}
		}
	}
}
