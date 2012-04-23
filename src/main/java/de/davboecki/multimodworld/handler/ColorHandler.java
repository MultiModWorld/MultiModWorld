package de.davboecki.multimodworld.handler;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum ColorHandler {
	BLACK(ChatColor.BLACK),
    /**
     * Represents dark blue
     */
    DARK_BLUE(ChatColor.DARK_BLUE),
    /**
     * Represents dark green
     */
    DARK_GREEN(ChatColor.DARK_GREEN),
    /**
     * Represents dark blue (aqua)
     */
    DARK_AQUA(ChatColor.DARK_AQUA),
    /**
     * Represents dark red
     */
    DARK_RED(ChatColor.DARK_RED),
    /**
     * Represents dark purple
     */
    DARK_PURPLE(ChatColor.DARK_PURPLE),
    /**
     * Represents gold
     */
    GOLD(ChatColor.GOLD),
    /**
     * Represents gray
     */
    GRAY(ChatColor.GRAY),
    /**
     * Represents dark gray
     */
    DARK_GRAY(ChatColor.DARK_GRAY),
    /**
     * Represents blue
     */
    BLUE(ChatColor.BLUE),
    /**
     * Represents green
     */
    GREEN(ChatColor.GREEN),
    /**
     * Represents aqua
     */
    AQUA(ChatColor.AQUA),
    /**
     * Represents red
     */
    RED(ChatColor.RED),
    /**
     * Represents light purple
     */
    LIGHT_PURPLE(ChatColor.LIGHT_PURPLE),
    /**
     * Represents yellow
     */
    YELLOW(ChatColor.YELLOW),
    /**
     * Represents white
     */
    WHITE(ChatColor.WHITE),
    /**
     * Represents magical characters that change around randomly
     */
    MAGIC(ChatColor.MAGIC),
    /**
     * Makes the text bold.
     */
    BOLD(ChatColor.BOLD),
    /**
     * Makes a line appear through the text.
     */
    STRIKETHROUGH(ChatColor.STRIKETHROUGH),
    /**
     * Makes the text appear underlined.
     */
    UNDERLINE(ChatColor.UNDERLINE),
    /**
     * Makes the text italic.
     */
    ITALIC(ChatColor.ITALIC),
    /**
     * Resets all previous chat colors or formats.
     */
    RESET(ChatColor.RESET);
	
	ColorHandler(ChatColor cColor) {
		Color = cColor;
	}
	
	ChatColor Color;
	
	public String toString() {
		return Color.toString();
	}

	public String toString(CommandSender sender) {
		if(sender instanceof Player) {
			return Color.toString();
		} else {
			return "";
		}
	}
}
