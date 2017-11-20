package tworunpos;


import Api.RestServer;
import Devices.JPosDeviceManager;
import GuiElements.TrSounds;
import com.mongodb.DB;

import java.io.IOException;


public class mainApplication {

	static tworunPos mainApplikation;
	static SplashScreenPos splashScreen;
	
	//generall DB connection to use
	private static DB db;
	public static DataFileImporter DataImporter;
	private static ArticleList articleList;
	
	//device manager to manage all pos specific hardware over JPos standard
	public static JPosDeviceManager deviceManager;

	//RESTful Api to import data
	public static RestServer api;

	//load config
	private static Config config;
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
	
		

		//SHUTDOWN Hook to clean up resources
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
		    public void run() {
		    	System.out.println("shutdownHook started");
		    	
		    	//close devices
		    	System.out.println("close devices");
		    	try {
					JPosDeviceManager.getInstance().closeAllDevices();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	
		    	//close mongoDB connections
		    	System.out.println("close database connection");
		    	DatabaseClient.getInstance().close();
		    	
		    	
		    	TrSounds.fail();
		    }
		}));
		
		
		
		
		//show Splashscreen First
		splashScreen = SplashScreenPos.getInstance();
		splashScreen.show();
		

		/*
		 * load config
		 */
		splashScreen.setText("Loading Config File ...");
		config = Config.getInstance();
		
		
		/*
		 * start debugscreen if debug is activated in config
		 */
		splashScreen.setText("Starting Debugscreen ...");
		DebugScreen debug =  DebugScreen.getInstance();


		/*
		 * Start the API
		 */
		try {
			api = new RestServer();
			api.run();
		} catch (Exception e1) {

			e1.printStackTrace();
		}


		/*
		 * Start Devicemanager
		 */
		splashScreen.setText("Starting Device Manager ...");
		try {
			deviceManager = JPosDeviceManager.getInstance();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		
		
		/*
		 * Connect to database tworunPOS in Mongodb
		 */
		splashScreen.setText("Connecting to Database ...");
		db = DatabaseClient.getInstance().getDB("tworunPOS");

		
		/*
		 * Create the article list connected to the db
		 */
		splashScreen.setText("Loading Artilcle List ...");
		articleList = new ArticleList();


		
		/*
		 * start fileWatcher for incoming updates  CSV
		 */
		splashScreen.setText("Starting Importer ...");
		try {
			//if(config.getImportFileType().equals(Config.importFileTypeDefault)){
				DataImporter = new DataImporterDefault("C:/tworunpos/import", true,  articleList,config);
			//}
//			else if (config.getImportFileType().equals(Config.importFileTypeMyaSoft)){
//				//DataImporter = new DataImporterMyasoft("/import", true, db, articleList,config);
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	    

		/*
		 * start the mainapplication 
		 */
		splashScreen.setText("Starting Main App ...");
		splashScreen.dispose();
		mainApplikation =  new tworunPos("tworunPOS 0.1", db, DataImporter, articleList, config, debug);
		mainApplikation.show();
		
	}

	
}
