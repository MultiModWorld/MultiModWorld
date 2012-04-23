package de.davboecki.multimodworld.commands;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.davboecki.multimodworld.commands.commandhandler.PlayerModInfo;

public class CommandHandler implements CommandExecutor {
	
	public ArrayList<ICommandHandler> CommandList = new ArrayList<ICommandHandler>();
	
	public CommandHandler() {
		CommandList.add(new PlayerModInfo());
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length < 1) {
			displayhelp(sender);
			return true;
		}
		for(ICommandHandler handler:CommandList) {
			if(handler.isCommand(args[0])) {
				if(args.length > 1) {
					return handler.onCommand(sender, Arrays.copyOfRange(args, 1, args.length));
				} else {
					return handler.onCommand(sender, null);
				}
			}
		}
		displayhelp(sender);
		return true;
	}
	
	public void displayhelp(CommandSender sender) {
		
	}

}
