package de.davboecki.multimodworld.exchangeworld.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.WorldLoadEvent;

import de.davboecki.multimodworld.MultiModWorld;

public class WeatherListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onWeatherChange(WeatherChangeEvent event) {
		if (event.toWeatherState() && MultiModWorld.getInstance().getRoomcontroler().isChestWorld(event.getWorld())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onThunderChange(ThunderChangeEvent event) {
		if (event.toThunderState() && MultiModWorld.getInstance().getRoomcontroler().isChestWorld(event.getWorld())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onLightningStrike(LightningStrikeEvent event) {
		if (MultiModWorld.getInstance().getRoomcontroler().isChestWorld(event.getWorld())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockForm(BlockFormEvent event) {
		if(MultiModWorld.getInstance().getRoomcontroler().isChestWorld(event.getBlock().getWorld())) {
			Material mat = event.getNewState().getType();
			
			if (mat == Material.ICE || mat == Material.SNOW) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void WorldLoad(WorldLoadEvent event) {
		if(MultiModWorld.getInstance().getRoomcontroler().isChestWorld(event.getWorld())) {
			event.getWorld().setThundering(false);
			event.getWorld().setStorm(false);
		}
	}
	
}
