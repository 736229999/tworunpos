package tworunpos;

import com.mongodb.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Set;

public class DatabaseClient extends MongoClient{

	//hostname of the database
	String hostName = "localhost";
	
	//name of the database in the connection
	String databaseName = "tworunPOS";

	
	
	// Eine (versteckte) Klassenvariable vom Typ der eigene Klasse
	private static DatabaseClient instance = null;
	
	//generall DB connection to use
	private static DB db;
	
	DebugScreen debug = DebugScreen.getInstance();
	SplashScreenPos splashScreen = SplashScreenPos.getInstance();
	Config config = Config.getInstance();
	
	
	
	public DatabaseClient(String hostname, MongoClientOptions mongoOptions) throws UnknownHostException{
		super(hostname, mongoOptions);
	}
	
	
	
	public DatabaseClient() throws UnknownHostException {
		//super();
		
		try{
			debug.print("Looking for MongoDB");
			splashScreen.setText("Connecting to Database");
			
			//build mongo config to wait just a second for a connection
			MongoClientOptions mongoClientOptions =  MongoClientOptions.builder().connectTimeout(1000)
					.connectionsPerHost(10)
					.build();
			
			this.instance =  (DatabaseClient) new MongoClient( new ServerAddress(this.hostName, 27017) ,mongoClientOptions);
			db = this.instance.getDB( this.databaseName );
			
			this.instance.getDatabaseNames();
			
		}catch(Exception e){
			debug.print("MongoDB-Process not found. Try to start MongoDB in "+config.mongoDbLocation);
			debug.print(e.getMessage());
			

				try {
					Process process = new ProcessBuilder(config.mongoDbLocation).start();
					
					try{
						
						debug.print("MongoDB process started successfully");
						debug.print("Connecting to database tworunPOS");
						
						MongoClientOptions mongoClientOptions =  MongoClientOptions.builder().connectTimeout(1000).build();
						this.instance =  new DatabaseClient( this.hostName ,mongoClientOptions);
						db = this.instance.getDB( this.databaseName );
					}catch(Exception e2){
						
						debug.print("Failure while connecting to database.");
						
						e2.printStackTrace();
						debug.printStackTrace(e2);
						GuiElements errorMessage = new GuiElements();
						errorMessage.displayErrorMessageBox("Keine Datenbankverbindung!");
						splashScreen.dispose();
						return;
					}
					
					
				} catch (IOException e11) {

					debug.print("Starting MongoDB failed!");
					debug.printStackTrace(e11);
					

					GuiElements errorMessage = new GuiElements();
					errorMessage.displayErrorMessageBox("Keine Datenbankverbindung 2!");
					splashScreen.dispose();

					return;
				}
				
				
		}
		
		debug.print("Successfully connected to MongoDB");
		
		
		// TODO Auto-generated constructor stub
	}

	
	//Singleton get Instantce
	public static  DatabaseClient getInstance () {
		if (instance == null) {
			try {
				DebugScreen.getInstance().print("DatabaseClient getInstance - create new Client");
				instance = new DatabaseClient ();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return instance;
	}
	
	
	public  DB getConnectedDB(){
		return db;
	}

	public  void repairDatabase() throws UnknownHostException{
		//db.command(new BasicDBObject("repairDatabase", 1));
		// when
	    CommandResult result = db.command(new BasicDBObject("repairDatabase", 1));

	    // then
	   // assertThat(result, is(notNullValue()));
	   // assertThat(result.ok(), is(true));
		
		
		instance = new DatabaseClient ();
		db = this.instance.getDB( this.databaseName );
	}

	public boolean collectionExists(final String collectionName) {
		Set<String> collectionNames = db.getCollectionNames();
		for (final String name : collectionNames) {
			if (name.equalsIgnoreCase(collectionName)) {
				return true;
			}
		}
		return false;
	}

	public boolean createCollection(final String collectionName) {
		db.getCollection(collectionName);
		if(collectionExists(collectionName)){
			return true;
		}else {
			return false;
		}
	}
	
	public void close(){
		super.close();
	}
	
}
