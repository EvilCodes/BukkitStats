package me.paulvogel.bukkitstats.utils;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.paulvogel.bukkitstats.handlers.LogHandler;
import org.bson.Document;

public class MongoDBCore {
    
    private String host;
    private int port;
    private String username;
    private String password;
    private String database;
    
    private boolean isConnected;
    
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    /**
     * Used to setup a new mysql database instance.
     * @param host Database host (server)
     * @param database Database name
     * @param username Username
     * @param password Password
     */
    public MongoDBCore(String host, int port, String database, String username, String password) {
        this.database = database;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;

        initialize();
    }
    
    public MongoClient getMongoClient() { return this.mongoClient; }
    
    public MongoDatabase getMongoDatabase() { return this.mongoDatabase; }

    public Boolean checkConnection() { return isConnected; }

    private void initialize() {
        this.mongoClient = new MongoClient("mongodb://" + this.username + ":" + this.password + "@" + this.host + ":" + this.port);
        try {
            this.mongoDatabase = mongoClient.getDatabase(this.database);
            this.isConnected = true;
        } catch (final MongoException ex) {
            LogHandler.err("Error while connecting to MongoDB database " + this.database);
            LogHandler.stackerr(ex);
            this.isConnected = false;
        }
    }

    public void close() {
        try {
            if (this.database != null && this.mongoClient != null) {
                this.mongoClient.close();
            }
        } catch (final Exception ex) {
            LogHandler.err("Failed to close database connection! " + ex.getMessage());
        }
    }

    public MongoCollection<Document> getCollection(final String collectionName) {
        return this.mongoDatabase.getCollection(collectionName);
    }
    
    
    /*
    public ResultSet select(String query) {
    }

    public void insert(String query) {
    }

    public static ResultSet executeQuery(String query, boolean modifies) {

    }

    public static boolean mongoExists(String query) {
        
    }

    public void update(String query) {
        
    }

    public void delete(String query) {
        
    }

    public Boolean execute(String query) {
        
    }

    public Boolean existsTable(String table) {

    }

    public Boolean existsColumn(String tabell, String colum) {

    }
    */


}