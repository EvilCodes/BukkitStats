package me.paulvogel.bukkitstats.handlers;

import java.io.File;

import me.paulvogel.bukkitstats.BukkitStats;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FilesHandler {
    
    private static File configFile;
    public static FileConfiguration config;
    private static File messagesFile;
    public static FileConfiguration messages;
		
	public static void CheckForConfigs() {
		configFile = new File(BukkitStats.getInstance().getDataFolder(), "config.yml");
		messagesFile = new File(BukkitStats.getInstance().getDataFolder(), "messages.yml");
		
		if (!configFile.exists()) {
			LogHandler.log("No Configfile found, creating...");
			setConfigs();
		}
		if (!messagesFile.exists()) {
			LogHandler.log("No Messagesfile found, creating...");
			setConfigs();
		}
		LoadFiles();
	}
	
	public static void SaveFiles() {
	    try {
	    	config.save(configFile);
	    	messages.save(messagesFile);
	    }
	    catch (Exception e) {
	    	LogHandler.err("Could not save configfiles!");
	    	LogHandler.stackerr(e);
	    }
	}
	
	public static void LoadFiles() {
	   	try {
	   	   if (config == null) {
	   	    configFile = new File(BukkitStats.getInstance().getDataFolder(), "config.yml");
	   	    }
	   		config = YamlConfiguration.loadConfiguration(configFile);
	   		
		   	if (messages == null) {
			    messagesFile = new File(BukkitStats.getInstance().getDataFolder(), "messages.yml");
			}
	   		messages = YamlConfiguration.loadConfiguration(messagesFile);
		} catch (Exception e) {
	    	LogHandler.err("Could not load configfiles!");
	    	LogHandler.stackerr(e);
		}
	}
		
	public static void setConfigs() {
		config = YamlConfiguration.loadConfiguration(configFile);
	    if (!configFile.exists()) {
	         BukkitStats.getInstance().saveResource("config.yml", false);
	     }
	    
		messages = YamlConfiguration.loadConfiguration(messagesFile);
	    if (!messagesFile.exists()) {
	         BukkitStats.getInstance().saveResource("messages.yml", false);
	     }
	}
		
	
}

