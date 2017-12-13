package tworunpos;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class Config {

	
	

	// Eine (versteckte) Klassenvariable vom Typ der eigene Klasse
	private static Config instance;
	// Verhindere die Erzeugung des Objektes �ber andere Methoden
	//private Config() {}
	// Eine Zugriffsmethode auf Klassenebene, welches dir '''einmal''' ein konkretes 
	// Objekt erzeugt und dieses zur�ckliefert.
	// Durch 'synchronized' wird sichergestellt dass diese Methode nur von einem Thread 
	// zu einer Zeit durchlaufen wird. Der n�chste Thread erh�lt immer eine komplett 
	// initialisierte Instanz.
	
	
	
	
	
    public static String configFileDirectory = "/resources/config/";
	public static String configFilename = "config.trn";
	
	


	//The filetype of the file, which contains the product informations from erp system
	public String importFileType;
	public static String importFileTypeDefault = "default";
	public static String importFileTypeMyaSoft = "myasoft";
	
	//If the debugscreen (output of debuginfos) should appear or not
	public Boolean debugMode = true;
	
	//The Path of the mongo extension to start, if not already existing
	public String mongoDbLocation = "C:\\Program Files\\MongoDB\\Server\\3.2\\bin\\mongod.exe";
	
	//The currency Smybol to use for printouts etc
	private String currencySmybol = "€";
	private String currencyName = "EUR";
	private boolean useCurrencySymbolOnReceipt = false;
	private boolean useCurrencyNameOnReceipt = false;
	
	
	private String[] weighItems =  new String[10];
	
	
	public Config() throws IOException {
		try {
			// read the json file

			System.out.println("Cannot read file. " + this.getClass().getResource("config/config.trn").getFile());

			File file = new File(this.getClass().getResource("config/config.trn").getFile());
			StringBuilder result = new StringBuilder();
			try (Scanner scanner = new Scanner(file)) {
				 
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					result.append(line).append("\n");
				}

				scanner.close();
		 
			} catch (IOException e) {

				e.printStackTrace();
			}
			
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = null;
			if(result != null){
				jsonObject = ((JSONObject) jsonParser.parse(result.toString()));
			}else{
				System.out.println("Cannot read file. " + Config.configFilename);
			}

			// get debugmode
			Boolean json_debugMode = (Boolean) jsonObject.get("debugMode");
			debugMode = json_debugMode;
			
			
			// get path of mongodb
			String json_mongoDbLocation = (String) jsonObject.get("mongoDbLocation");
			mongoDbLocation = json_mongoDbLocation;
			
			// get the type of import  file for items
			String json_importFileType = (String) jsonObject.get("importFileType");
			if(json_importFileType.equalsIgnoreCase(importFileTypeDefault))
				setImportFileType(importFileTypeDefault);
			else if(json_importFileType.equalsIgnoreCase(importFileTypeMyaSoft))
				setImportFileType(importFileTypeMyaSoft);


			// get currency symbol
			String json_currencySmybol = (String) jsonObject.get("currencySymbol");
			if(json_currencySmybol != null)
				currencySmybol = json_currencySmybol;
			
			// get currency Name
				String json_currencyName = (String) jsonObject.get("currencyName");
				if(json_currencyName != null)
					currencyName = json_currencySmybol;
				
			// get useCurrencySymbolOnReceipt
				Boolean json_useCurrencySymbol = (Boolean) jsonObject.get("useCurrencySymbolOnReceipt");
				if(json_useCurrencySymbol == true)
					useCurrencySymbolOnReceipt = true;
				
			// get useCurrencyNameOnReceipt
				Boolean json_useCurrencyName = (Boolean) jsonObject.get("useCurrencyNameOnReceipt");
				if(json_useCurrencyName == true)
					useCurrencyNameOnReceipt = true;
				

			// get useCurrencyNameOnReceipt
				JSONArray json_weighBarcodes = (JSONArray)jsonObject.get("weighBarcodes")  ;
				if(json_weighBarcodes.size() > 0)
				{
					for (int i = 0; i < json_weighBarcodes.size(); i++) {
						System.out.println("Found WeighBarcode into Config: "+json_weighBarcodes.get(i)+"  i:"+i+"   size:"+json_weighBarcodes.size());
						weighItems[i] =json_weighBarcodes.get(i).toString() ;
						System.out.println("Read WeighBarcode into Config: "+this.weighItems[i]);
					}
				}
					
				
		
		} catch (ParseException ex) {
			ex.printStackTrace();
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}

	}
	
	public String getImportFileType() {
		return importFileType;
	}

	public void setImportFileType(String importFileType) {
		this.importFileType = importFileType;
	}

	public Boolean getDebugMode() {
		return debugMode;
	}
	
	public Boolean isDebugMode() {
		return debugMode;
	}

	public void setDebugMode(Boolean debugMode) {
		this.debugMode = debugMode;
	}
	
	
	
	public String getCurrencySymbol() {
		return currencySmybol;
	}
	
	public void setCurrencySymbol(String c) {
		currencySmybol = c;
	}
	

	public boolean isUseCurrencySymbolOnReceipt() {
		return useCurrencySymbolOnReceipt;
	}

	public void setUseCurrencySymbolOnReceipt(boolean useCurrencySymbolOnReceipt) {
		this.useCurrencySymbolOnReceipt = useCurrencySymbolOnReceipt;
	}

	public boolean isUseCurrencyNameOnReceipt() {
		return useCurrencyNameOnReceipt;
	}

	public void setUseCurrencyNameOnReceipt(boolean useCurrencyNameOnReceipt) {
		this.useCurrencyNameOnReceipt = useCurrencyNameOnReceipt;
	}
	
	public boolean isInWeighCodeList(String BarcodePrefix){
		for(int i=0; weighItems.length > i;i++){
			if(BarcodePrefix.equals(weighItems[i])){
				return true;
			}
		}
		return false;
	}
	
	public String[] getWeighItems() {
		return weighItems;
	}

	public void setWeighItems(String[] weighItems) {
		this.weighItems = weighItems;
	}

	//Singleton get Instantce
	public static synchronized Config getInstance () {
		if (Config.instance == null) {
			try {
				Config.instance = new Config ();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return Config.instance;
	}


	
	

}
