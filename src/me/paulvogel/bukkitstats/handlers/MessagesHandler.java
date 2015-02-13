package com.evilcodes.citycraft.handlers;

import com.evilcodes.citycraft.CityCraft;

public class MessagesHandler {
	
	public static String noPermission(String permission) {
		final String msg = CityCraft.messages.getString("NoPermissions").replace("&", "§").replace("%permission%", permission);
		return msg;
	}

	public static String convert(String message) {
		final String msg = CityCraft.messages.getString(message).replace("&", "§");
		return msg;
	}
	
}
