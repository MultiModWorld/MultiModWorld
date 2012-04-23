package de.davboecki.multimodworld.commands;

import org.bukkit.command.CommandSender;

public interface ICommandHandler {
	public boolean isCommand(String command);
	public boolean onCommand(CommandSender sender, String[] args);
	public String returnhelp();
}
