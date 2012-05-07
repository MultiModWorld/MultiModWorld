package de.davboecki.multimodworld.exchangeworld.commands;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.davboecki.multimodworld.commands.ICommandHandler;

public class ExchangeWorldCommands implements ICommandHandler {

	public ArrayList<ICommandHandler> CommandList = new ArrayList<ICommandHandler>();

	public ExchangeWorldCommands() {
		CommandList.add(new RoomDefiner());
	}
	
	public boolean isCommand(String command) {
		return command != null && ( command.equalsIgnoreCase("exchangeworld") || command.equalsIgnoreCase("exchange") || command.equalsIgnoreCase("ew"));
	}

	public boolean onCommand(CommandSender sender, String[] args) {
		for(ICommandHandler handler:CommandList) {
			if(handler.isCommand(args[0])) {
				if(args.length > 1) {
					return handler.onCommand(sender, Arrays.copyOfRange(args, 1, args.length));
				} else {
					return handler.onCommand(sender, null);
				}
			}
		}
		return false;
	}
	
	public String returnhelp() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
