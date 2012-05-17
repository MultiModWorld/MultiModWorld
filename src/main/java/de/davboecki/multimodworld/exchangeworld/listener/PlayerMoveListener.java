package de.davboecki.multimodworld.exchangeworld.listener;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import de.davboecki.multimodworld.MultiModWorld;
import de.davboecki.multimodworld.exchangeworld.ExchangeWorldGenerator;
import de.davboecki.multimodworld.exchangeworld.ExchangeWorldPlayer;
import de.davboecki.multimodworld.handler.ColorHandler;
import de.davboecki.multimodworld.handler.LanguageHandler;
import de.davboecki.multimodworld.utils.MMWExchangeWorld;
import de.davboecki.multimodworld.utils.MMWPlayer;
import de.davboecki.multimodworld.utils.MMWWorld;

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
		if(ExchangeWorldGenerator.getPortalNormal(event.getPlayer().getWorld()).distance(event.getPlayer().getLocation()) < 0.4) {
			mPlayer.getChestRoomPlayer().teleport((MMWExchangeWorld) MMWWorld.getMMWWorld(event.getPlayer().getWorld()), MMWPlayer.exchangeworldposition.normal_RoomPortal);
		} else if(ExchangeWorldGenerator.getOtherPortal(event.getPlayer().getWorld()).distance(event.getPlayer().getLocation()) < 0.4) {
			mPlayer.getChestRoomPlayer().teleport((MMWExchangeWorld) MMWWorld.getMMWWorld(event.getPlayer().getWorld()), MMWPlayer.exchangeworldposition.other_RoomPortal);
		} else if(mPlayer.getChestRoomPlayer().getRealPos() == ExchangeWorldPlayer.WorldPos.Room) {
			if(mPlayer.getChestRoomPlayer().getRoomSettingsForWorld((MMWExchangeWorld) MMWWorld.getMMWWorld(event.getPlayer().getWorld())) != null) {
				if(mPlayer.getChestRoomPlayer().getRoomSettingsForWorld((MMWExchangeWorld) MMWWorld.getMMWWorld(event.getPlayer().getWorld())).getNormalPortal().getBukkitLocation(MMWWorld.getMMWWorld(event.getPlayer().getWorld())).distance(event.getTo()) < 0.3) {
					mPlayer.getChestRoomPlayer().teleport((MMWExchangeWorld) MMWWorld.getMMWWorld(event.getPlayer().getWorld()), MMWPlayer.exchangeworldposition.normal_Area);
				} else if(mPlayer.getChestRoomPlayer().getRoomSettingsForWorld((MMWExchangeWorld) MMWWorld.getMMWWorld(event.getPlayer().getWorld())).getOtherPortal().getBukkitLocation(MMWWorld.getMMWWorld(event.getPlayer().getWorld())).distance(event.getTo()) < 0.3) {
					mPlayer.getChestRoomPlayer().teleport((MMWExchangeWorld) MMWWorld.getMMWWorld(event.getPlayer().getWorld()), MMWPlayer.exchangeworldposition.other_Area);
				} else if(mPlayer.waitTeleportcooldown()) {
					mPlayer.setTeleportcooldownNow();
				}
			} else if(mPlayer.waitTeleportcooldown()) {
				mPlayer.setTeleportcooldownNow();
			}
		} else if(mPlayer.waitTeleportcooldown()) {
			mPlayer.setTeleportcooldownNow();
		}
	}

	private void ExchangeWorldMoveValid(PlayerMoveEvent event) {
		final MMWPlayer mPlayer = MMWPlayer.getMMWPlayer(event.getPlayer());
		if (mPlayer.getChestRoomPlayer().getShouldBePos() != null
				&& !mPlayer.getChestRoomPlayer().getShouldBePos().equals(mPlayer.getChestRoomPlayer().getRealPos())) {
			if (mPlayer.getChestRoomPlayer().getRealPos().equals(ExchangeWorldPlayer.WorldPos.LavaWall)) {
				if (mPlayer.getChestRoomPlayer().getShouldBePos().equals(ExchangeWorldPlayer.WorldPos.Normal)) {
					final Location loc = mPlayer.getPlayer().getLocation();
					loc.setX(loc.getX() + 3);
					mPlayer.getPlayer().teleport(loc);
				} else if (mPlayer.getChestRoomPlayer().getShouldBePos().equals(ExchangeWorldPlayer.WorldPos.Mod)) {
					final Location loc = mPlayer.getPlayer().getLocation();
					loc.setX(loc.getX() - 3);
					mPlayer.getPlayer().teleport(loc);
				}
				event.getPlayer().sendMessage(ColorHandler.RED.toString()+LanguageHandler.Walk_On_Lava_Wall.toString());
			} else if (mPlayer.getChestRoomPlayer().getRealPos().equals(ExchangeWorldPlayer.WorldPos.Underground)) {
				if(mPlayer.getChestRoomPlayer().getShouldBePos().equals(ExchangeWorldPlayer.WorldPos.Room)) {
					final Location loc = mPlayer.getChestRoomPlayer().getRoomSettingsForWorld((MMWExchangeWorld)MMWWorld.getMMWWorld(event.getPlayer().getWorld())).getNormalPortal().getBukkitLocation(MMWWorld.getMMWWorld(event.getPlayer().getWorld()));
					mPlayer.getPlayer().teleport(loc);
					mPlayer.setWaitTeleportcooldown();
					event.getPlayer().sendMessage(ColorHandler.RED.toString()+LanguageHandler.Walk_Underground.toString());
				} else {
					final Location loc = mPlayer.getPlayer().getWorld().getSpawnLocation();
					mPlayer.getPlayer().teleport(loc);
					event.getPlayer().sendMessage(ColorHandler.RED.toString()+LanguageHandler.Walk_Underground.toString());
				}
			} else {
				if (mPlayer.getChestRoomPlayer().getShouldBePos().equals(ExchangeWorldPlayer.WorldPos.Normal)) {
					final Location loc = ExchangeWorldGenerator.getPortalNormal(mPlayer.getPlayer().getWorld());
					mPlayer.teleport(loc);
					mPlayer.setWaitTeleportcooldown();
				} else if (mPlayer.getChestRoomPlayer().getShouldBePos().equals(ExchangeWorldPlayer.WorldPos.Mod)) {
					final Location loc = ExchangeWorldGenerator.getOtherPortal(mPlayer.getPlayer().getWorld());
					mPlayer.teleport(loc);
					mPlayer.setWaitTeleportcooldown();
				} else if (mPlayer.getChestRoomPlayer().getShouldBePos().equals(ExchangeWorldPlayer.WorldPos.Room)) {
					final Location loc = mPlayer.getChestRoomPlayer().getRoomSettingsForWorld((MMWExchangeWorld)MMWWorld.getMMWWorld(event.getPlayer().getWorld())).getNormalPortal().getBukkitLocation(MMWWorld.getMMWWorld(event.getPlayer().getWorld()));
					mPlayer.teleport(loc);
					mPlayer.setWaitTeleportcooldown();
				}
				event.getPlayer().sendMessage(ColorHandler.RED.toString()+LanguageHandler.Please_Use_Portals.toString());
			}
		}
	}
}
