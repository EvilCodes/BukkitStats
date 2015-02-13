package com.evilcodes.citycraft.handlers;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class PermissionsHandler {

	public static boolean hasPermission(CommandSender cs, String permission) {
		if (cs.hasPermission(permission) || cs.isOp() || cs instanceof ConsoleCommandSender) {
			return true;
		} else {
			cs.sendMessage(MessagesHandler.noPermission(permission));
			return false; 
		}
	}
	
}
