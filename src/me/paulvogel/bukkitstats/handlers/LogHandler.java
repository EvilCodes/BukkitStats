package me.paulvogel.bukkitstats.handlers;

import me.paulvogel.bukkitstats.BukkitStats;
import org.bukkit.Bukkit;

import java.util.logging.Logger;

public class LogHandler {
    
    public static Logger bukkitlogger;

	public static void log(String msg) {
        getLogger().info(BukkitStats.logprefix + msg);
	}
	
	public static void err(String msg) {
		getLogger().severe(BukkitStats.logprefix + msg);
	}
	
	public static void stackerr(Exception msg) {
		if (BukkitStats.debug) {
			getLogger().severe(BukkitStats.logprefix + msg);
		}
	}
    
    public static Logger getLogger() {
        if (bukkitlogger != null) {
            bukkitlogger = Bukkit.getLogger();
        }
        return bukkitlogger;
    }
}
