package me.paulvogel.bukkitstats.handlers;

public class MessagesHandler {
	
	public static String noPermission(String permission) {
		final String msg = FilesHandler.messages.getString("NoPermissions").replace("&", "§").replace("%permission%", permission);
		return msg;
	}

	public static String convert(String message) {
		final String msg = FilesHandler.messages.getString(message).replace("&", "§");
		return msg;
	}
	
}
