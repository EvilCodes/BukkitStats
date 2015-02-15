package me.paulvogel.bukkitstats.handlers;

import com.mongodb.client.MongoCollection;
import me.paulvogel.bukkitstats.BukkitStats;
import me.paulvogel.bukkitstats.utils.CustomDate;
import me.paulvogel.bukkitstats.utils.DatabaseType;
import me.paulvogel.bukkitstats.utils.MongoDBCore;
import me.paulvogel.bukkitstats.utils.MySQLCore;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;

//Now also checks if the database type is MySQL or MongoDB and sends the queries for them correctly.
public class DBHandler {

    public static DatabaseType type;

    private static String dbhost;
    private static int dbport;
    private static String dbuser;
    private static String dbpass;
    private static String dbname;
    private static String dbprefix;

    //TODO Check if my mysql connection (and reading) stuff works (maybe add some debug messages)
    
    private static MySQLCore mysqlDatabaseInstance;
    private static MongoDBCore mongodbDatabaseInstance;

    //Table structures:
    //GENERAL - id, uuid, username (latest), first_login, last_login, times_logged_in, last_ip_address, 
    
    /**
     * Initiates the databaseclass with it's new instance and checks the connection.
     */
    public static void init() {
        LogHandler.log("Initiating database.");
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
        dbhost = FilesHandler.config.getString("Database.Host");
        dbport = FilesHandler.config.getInt("Database.Port");
        dbuser = FilesHandler.config.getString("Database.Username");
        dbpass = FilesHandler.config.getString("Database.Password");
        dbname = FilesHandler.config.getString("Database.Databasename");
        dbprefix = FilesHandler.config.getString("Database.Tableprefix");
        
        if (type == DatabaseType.MySQL) {
            mysqlDatabaseInstance = new MySQLCore(dbhost, dbport, dbname, dbuser, dbpass);
            if (mysqlDatabaseInstance.checkConnection())
                LogHandler.log("Database connection established.");
            else
                LogHandler.err("Error while trying to connect to database.");
            //TODO Check if db tables exist
        } else if (type == DatabaseType.MongoDB) {
            mongodbDatabaseInstance = new MongoDBCore(dbhost, dbport, dbname, dbuser, dbpass);
            if (mongodbDatabaseInstance.checkConnection())
                LogHandler.log("Database connection established.");
            else
                LogHandler.err("Error while trying to connect to database.");
            //TODO Check if db tables exist
        } else LogHandler.err("This weird exception occurs when the database type is unknown and the plugin did not shutdown.");

    }

    /**
     * Returns a string from a database column for a player with the specified uuid.
     *
     * @param table      DBTable from which the int should be selected.
     * @param UUID       UUID of the player.
     * @param columnName Name of the column of the date (like username, last_ip_address, etc.)
     * @return Simple Java string (String) value
     */
    public static String getString(final String table, final String UUID, final String columnName) {
        String result = "";
        if (type == DatabaseType.MySQL) {
            final String query = "SELECT * FROM `" + dbprefix + table + "` WHERE `uuid` = '" + UUID + "';";
            if (MySQLCore.mysqlExists(query)) {
                final ResultSet res = mysqlDatabaseInstance.select(query);
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
                        LogHandler.err(String.format("An Error occurred: %s", new Object[]{Integer.valueOf(ex.getErrorCode())}));
                        LogHandler.stackerr(ex);
                    }
                }
            }
        } else if (type == DatabaseType.MongoDB) {
            final MongoCollection<Document> collection = mongodbDatabaseInstance.getCollection(table);
            final Document document = collection.find(new Document("uuid", UUID)).first();
            if (document == null || document.isEmpty()) 
                LogHandler.err("MongoDB BSON Document (uuid query) is empty or null - queried uuid: " + UUID);
            else
                result = document.getString(columnName);
        } else
            LogHandler.err("Could not send back a result from getString() method, because of an invalid database type.");
        return result;
    }

    /**
     * Returns a integer from a database column for a player with the specified uuid.
     *
     * @param table      DbTable from which the int should be selected.
     * @param UUID       UUID of the player.
     * @param columnName Name of the column of the date (like times_logged_in, etc.)
     * @return Simple Java int (Integer) value
     */
    public static int getInt(final String table, final String UUID, final String columnName) {
        int result = 0;
        if (type == DatabaseType.MySQL) {
            final String query = "SELECT * FROM `" + dbprefix + table + "` WHERE `uuid` = '" + UUID + "';";
            if (MySQLCore.mysqlExists(query)) {
                final ResultSet res = mysqlDatabaseInstance.select(query);
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
                        LogHandler.err(String.format("An Error occurred: %s", new Object[]{Integer.valueOf(ex.getErrorCode())}));
                        LogHandler.stackerr(ex);
                    }
                }
            }
        } else if (type == DatabaseType.MongoDB) {
            final MongoCollection<Document> collection = mongodbDatabaseInstance.getCollection(table);
            final Document document = collection.find(new Document("uuid", UUID)).first();
            if (document == null || document.isEmpty())
                LogHandler.err("MongoDB BSON Document (uuid query) is empty or null - queried uuid: " + UUID);
            else
                result = document.getInteger(columnName);
        } else
            LogHandler.err("Could not send back a result from getString() method, because of an invalid database type.");
        return result;
    }

    /**
     * Returns a CustomDate Object from a database column for a player with the specified uuid <br>
     * Returns new 0-cdate when not found
     *
     * @param table      DBTable from which the date should be selected.
     * @param UUID       UUID of the player.
     * @param columnName Name of the column of the date (like first_login, last_login or so)
     * @return New CustomDate
     */
    public static CustomDate getDate(final String table, final String UUID, final String columnName) {
        //Format = day-month-year hour-minute-second
        //Arguments == final int day, final int month, final int year, final int hour, final int minute, final int second
        final String dateString = getString(table, UUID, columnName);
        if (dateString == null || dateString == "" || dateString == " ")
            return new CustomDate(0, 0, 0, 0, 0, 0);
        final String[] dayStringArray = dateString.split(" ")[0].split("-");
        final String[] timeStringArray = dateString.split(" ")[1].split("-");
        final CustomDate result = new CustomDate(Integer.parseInt(dayStringArray[0]), Integer.parseInt(dayStringArray[1]), Integer.parseInt(dayStringArray[2]),
                Integer.parseInt(timeStringArray[0]), Integer.parseInt(timeStringArray[1]), Integer.parseInt(timeStringArray[2]));
        return result;
    }
    
    public static MongoDBCore getMongodbDatabaseInstance() {
        return mongodbDatabaseInstance;
    }
    
    public static MySQLCore getMysqlDatabaseInstance() {
        return  mysqlDatabaseInstance;
    }

}
