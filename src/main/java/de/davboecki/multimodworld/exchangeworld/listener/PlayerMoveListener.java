package de.davboecki.multimodworld.exchangeworld.listener;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import de.davboecki.multimodworld.MMWPlayer;
import de.davboecki.multimodworld.MultiModWorld;
import de.davboecki.multimodworld.exchangeworld.ExchangeWorldGenerator;
import de.davboecki.multimodworld.exchangeworld.ExchangeWorldPlayer;
import de.davboecki.multimodworld.exchangeworld.ExchangeWorldPlayer.WorldPos;

public class PlayerMoveListener implements Listener {

	private MultiModWorld plugin;

	public PlayerMoveListener() {
		plugin = MultiModWorld.getInstance();
	}

	@EventHandler
	public void PlayerMove(PlayerMoveEvent event) {
		if(plugin.getRoomcontroler().isChestWorld(event.getPlayer().getWorld())) {
			ExchangeWorldMoveValid(event);
			ExchangeWorldMovePortal(event);
		}
	}

	private void ExchangeWorldMovePortal(PlayerMoveEvent event) {
		final MMWPlayer mPlayer = MMWPlayer.getMMWPlayer(event.getPlayer());
		if(ExchangeWorldGenerator.getPortalNormal(event.getPlayer().getWorld()).distance(event.getPlayer().getLocation()) < 0.75) {
			if(!mPlayer.needTeleportcooldown()) {
				ExchangeWorldPortalNormal(event,mPlayer);
			}
		} else if(ExchangeWorldGenerator.getOtherPortal(event.getPlayer().getWorld()).distance(event.getPlayer().getLocation()) < 0.75) {
			if(!mPlayer.needTeleportcooldown()) {
				ExchangeWorldOtherPortal(event,mPlayer);
			}
		} else if(mPlayer.waitTeleportcooldown()) {
			mPlayer.setTeleportcooldownNow();
		}
	}

	private void ExchangeWorldPortalNormal(PlayerMoveEvent event, MMWPlayer mPlayer) {
		// TODO Now Teleport to Room
		// TODO Remove this is debug code
		mPlayer.teleport(ExchangeWorldGenerator.getOtherPortal(event.getPlayer().getWorld()));
		mPlayer.getChestRoomPlayer().setWorldPos(WorldPos.Mod);
		mPlayer.setWaitTeleportcooldown();
	}

	private void ExchangeWorldOtherPortal(PlayerMoveEvent event, MMWPlayer mPlayer) {
		// TODO Now Teleport to Room
		// TODO Remove this is debug code
		mPlayer.teleport(ExchangeWorldGenerator.getPortalNormal(event.getPlayer().getWorld()));
		mPlayer.getChestRoomPlayer().setWorldPos(WorldPos.Normal);
		mPlayer.setWaitTeleportcooldown();
	}

	private void ExchangeWorldMoveValid(PlayerMoveEvent event) {
		// TODO Add messages to TeleportEvents
		final MMWPlayer mPlayer = MMWPlayer.getMMWPlayer(event.getPlayer());
		if (mPlayer.getChestRoomPlayer().getWorldPos() != null
				&& !mPlayer.getChestRoomPlayer().getWorldPos().equals(mPlayer.getChestRoomPlayer().getPlayerPos())) {
			if (mPlayer.getChestRoomPlayer().getPlayerPos().equals(ExchangeWorldPlayer.WorldPos.LavaWall)) {
				if (mPlayer.getChestRoomPlayer().getWorldPos().equals(ExchangeWorldPlayer.WorldPos.Normal)) {
					final Location loc = mPlayer.getPlayer().getLocation();
					loc.setX(loc.getX() + 3);
					mPlayer.getPlayer().teleport(loc);
				} else if (mPlayer.getChestRoomPlayer().getWorldPos().equals(ExchangeWorldPlayer.WorldPos.Mod)) {
					final Location loc = mPlayer.getPlayer().getLocation();
					loc.setX(loc.getX() - 3);
					mPlayer.getPlayer().teleport(loc);
				}
			} else if (mPlayer.getChestRoomPlayer().getPlayerPos().equals(ExchangeWorldPlayer.WorldPos.Underground)) {
				final Location loc = mPlayer.getPlayer().getWorld().getSpawnLocation();
				mPlayer.getPlayer().teleport(loc);
			} else {
				if (mPlayer.getChestRoomPlayer().getWorldPos().equals(ExchangeWorldPlayer.WorldPos.Normal)) {
					final Location loc = ExchangeWorldGenerator.getPortalNormal(mPlayer.getPlayer().getWorld());
					mPlayer.teleport(loc);
					mPlayer.setWaitTeleportcooldown();
				} else if (mPlayer.getChestRoomPlayer().getWorldPos().equals(ExchangeWorldPlayer.WorldPos.Mod)) {
					final Location loc = ExchangeWorldGenerator.getOtherPortal(mPlayer.getPlayer().getWorld());
					mPlayer.teleport(loc);
					mPlayer.setWaitTeleportcooldown();
				}
			}
		}
	}
}
