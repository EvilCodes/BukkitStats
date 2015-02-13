package me.paulvogel.bukkitstats.handlers;

import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import me.paulvogel.bukkitstats.BukkitStats;
import me.paulvogel.bukkitstats.utils.CustomDate;
import me.paulvogel.bukkitstats.utils.DBCore;
import me.paulvogel.bukkitstats.utils.DatabaseType;
import me.paulvogel.bukkitstats.utils.MySQLCore;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class DBHandler {
    
    public static DatabaseType type;

    private static String dbhost;
    private static String dbuser;
    private static String dbpass;
    private static String dbname;
    private static String dbprefix;
    
    private static DBCore databaseInstance;
    
    //Table structures:
    //GENERAL - id, uuid, username (latest), first_login, last_login, times_logged_in, last_ip_address, 
    
    public static DBCore getDatabaseInstance() {
        return databaseInstance;
    }

    public static void init() {
        if (FilesHandler.config.getString("Database.Type").equalsIgnoreCase("mysql")) 
            type = DatabaseType.MySQL;
        else if (FilesHandler.config.getString("Database.Type").equalsIgnoreCase("mongodb"))
            type = DatabaseType.MongoDB;
        else
            type = DatabaseType.unknown;
        
        if (type == DatabaseType.unknown) {
            //Plugin needs a database to work. If none is given (or not correct configured) the plugin shuts down.
            LogHandler.err("Could not set database. Type " + FilesHandler.config.getString("Database.Type") + " is not supported. \n" +
                    "Please try mysql or mongodb as database type. \nThe plugin will disable itself now.");
            Bukkit.getPluginManager().disablePlugin(BukkitStats.getInstance());
            return;
        }
        if (type == DatabaseType.MySQL) 
            databaseInstance = new MySQLCore(dbhost, dbname, dbuser, dbpass);
        else if (type == DatabaseType.MongoDB) 
            //TODO Add support for mongodb
        ;
        
    }
    
	public static String getString(final String table, final String UUID, final String columnName) {
		final String query = "SELECT * FROM `" + dbprefix + table + "` WHERE `uuid` = '" + UUID + "';";
		String result = "";
		if (MySQLCore.mysqlExists(query)) {
			final ResultSet res = getDatabaseInstance().select(query);
		    if (res != null) {
			      try {
			        while (res.next()) {
			          try {
			        	  if (res.getString(columnName) != null || res.getString(columnName) != "" || res.getString(columnName) != " ") {
			        		  result = res.getString(columnName);
			        	  }
			          } catch (Exception ex) {
			        	  LogHandler.err("Could not reach MySQLDatabase!");
			        	  LogHandler.stackerr(ex);
			          }
			        }
			      } catch (SQLException ex) {
			    	  LogHandler.err(String.format("An Error occurred: %s", new Object[] { Integer.valueOf(ex.getErrorCode()) }));
			    	  LogHandler.stackerr(ex);
			      }
		    }
		}
		return result;
	}

    public static int getInt(final String table, final String UUID, final String columnName) {
        final String query = "SELECT * FROM `" + dbprefix + table + "` WHERE `uuid` = '" + UUID + "';";
        int result = 0;
        if (MySQLCore.mysqlExists(query)) {
            final ResultSet res = getDatabaseInstance().select(query);
            if (res != null) {
                try {
                    while (res.next()) {
                        try {
                            if (res.getString(columnName) != null || res.getString(columnName) != "" || res.getString(columnName) != " ") {
                                result = res.getInt(columnName);
                            }
                        } catch (Exception ex) {
                            LogHandler.err("Could not reach MySQLDatabase!");
                            LogHandler.stackerr(ex);
                        }
                    }
                } catch (SQLException ex) {
                    LogHandler.err(String.format("An Error occurred: %s", new Object[] { Integer.valueOf(ex.getErrorCode()) }));
                    LogHandler.stackerr(ex);
                }
            }
        }
        return result;
    }

    /**
     * Returns new 0-cdate when not found*
     * @param table
     * @param UUID
     * @param columnName
     * @return
     */
    public static CustomDate getDate(final String table, final String UUID, final String columnName) {
        //Format = day-month-year hour-minute-second
        //Arguments == final int day, final int month, final int year, final int hour, final int minute, final int second
        final String dateString = getString(table, UUID, columnName);
        if (dateString == null || dateString == "" || dateString == " ")
            return new CustomDate(0,0,0,0,0,0);
        final String[] dayStringArray = dateString.split(" ")[0].split("-");
        final String[] timeStringArray = dateString.split(" ")[1].split("-");
        final CustomDate result = new CustomDate(Integer.parseInt(dayStringArray[0]), Integer.parseInt(dayStringArray[1]), Integer.parseInt(dayStringArray[2]), 
                Integer.parseInt(timeStringArray[0]), Integer.parseInt(timeStringArray[1]), Integer.parseInt(timeStringArray[2]));
        return result;
    }

}
