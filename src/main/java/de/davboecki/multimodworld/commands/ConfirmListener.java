package de.davboecki.multimodworld.commands;

import java.util.HashMap;
import java.util.concurrent.Callable;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class ConfirmListener implements Listener {
	
	@SuppressWarnings("rawtypes")
	private static HashMap<String,Callable> Task = new HashMap<String,Callable>();
	private static HashMap<String,MorePageDisplay> MorePageDisplay = new HashMap<String,MorePageDisplay>();
	
    @EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
		if(event.isCancelled()) return;
		if(Task.containsKey(event.getPlayer().getName())){
			event.getPlayer().sendMessage("Don't forget to answer the question!");
			//event.getPlayer().sendMessage("Please answer question first!");
			//event.setCancelled(true);
		}
		if(MorePageDisplay.containsKey(event.getPlayer().getName())){
			if(!MorePageDisplay.get(event.getPlayer().getName()).isTerminated()){
				if(!MorePageDisplay.get(event.getPlayer().getName()).HandleChat("exit",(CommandSender)event.getPlayer())){
					event.getPlayer().sendMessage(ChatColor.RED+"Exit "+ChatColor.AQUA+"PageView"+ChatColor.RED+" first!");
					event.setCancelled(true);
				}
			}
		}
	}

    @EventHandler
	public void onPlayerChat(PlayerChatEvent event) {
		if(Task.containsKey(event.getPlayer().getName())){
			String msg = event.getMessage();
			if(!msg.equalsIgnoreCase("true") && !msg.equalsIgnoreCase("false") && !msg.equalsIgnoreCase("on") && !msg.equalsIgnoreCase("off") && !msg.equalsIgnoreCase("0") && !msg.equalsIgnoreCase("1") && !msg.equalsIgnoreCase("no") && !msg.equalsIgnoreCase("yes")){
				event.getPlayer().sendMessage("Not a valid answer. Please enter <yes/no|true/flase|on/off|1/0>");
			}
			boolean flag = msg.equalsIgnoreCase("true") || msg.equalsIgnoreCase("on") || msg.equalsIgnoreCase("1") || msg.equalsIgnoreCase("yes");
			if(!handleAnswer(flag,event.getPlayer().getName(),event.getPlayer())){
				event.getPlayer().sendMessage(ChatColor.RED + "Error: Could not handle answer.");
			} else {
				//event.getPlayer().sendMessage(ChatColor.GREEN + "Answer handled.");
			}
			event.setCancelled(true);
		} else {
			if(MorePageDisplay.containsKey(event.getPlayer().getName())){
				if(!MorePageDisplay.get(event.getPlayer().getName()).isTerminated()){
					if(MorePageDisplay.get(event.getPlayer().getName()).HandleChat(event.getMessage(),(CommandSender)event.getPlayer())){
						event.setCancelled(true);
					}
				}
			}
		}
	}
	
	public static void register(MorePageDisplay MorePageDisplayinput,String name){
		if(MorePageDisplay.containsKey(name) && !MorePageDisplay.get(name).isTerminated()) return;
		MorePageDisplay.put(name, MorePageDisplayinput);
	}
	
	public boolean handleAnswer(boolean flag,String name, CommandSender sender){
		if(!Task.containsKey(name)) return false;
		if(flag) {
			try {
				Object Erg;
				if((Erg = Task.get(name).call()) != null)
				{
					if(Erg instanceof Boolean){
						boolean Ergflag = (Boolean)Erg;
						if(!Ergflag) {
							return false;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else {
			sender.sendMessage(ChatColor.GREEN + "Answer handled.");
		}
		Task.remove(name);
		return true;
	}

	public boolean ExistTaskFor(String name){
		return Task.containsKey(name);
	}

	public void removeTask(String name){
		Task.remove(name);
	}
	
	@SuppressWarnings("rawtypes")
	public boolean addTask(Callable input,String name){
		if(Task.containsKey(name)) {
			return false;
		} else {
			Task.put(name, input);
			return true;
		}
	}
}
