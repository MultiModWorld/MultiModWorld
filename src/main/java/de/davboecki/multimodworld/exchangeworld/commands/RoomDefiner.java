package de.davboecki.multimodworld.exchangeworld.commands;

import org.bukkit.command.CommandSender;

import de.davboecki.multimodworld.commands.ICommandHandler;

public class RoomDefiner implements ICommandHandler {

	public boolean isCommand(String command) {
		return command.equalsIgnoreCase("room");
	}

	public boolean onCommand(CommandSender sender, String[] args) {
		if(args != null && args.length > 0) {
			if(args[0].equalsIgnoreCase("define")) { // exchangeworld room define
				
			} else {
				displayhelp(sender);
			}
		} else {
			displayhelp(sender);
		}
		return false;
	}

	public void displayhelp(CommandSender sender) {
		//TODO
	}
	
	public String returnhelp() {
		// TODO Auto-generated method stub
		return null;
	}

}
