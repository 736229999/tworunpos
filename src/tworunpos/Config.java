package tworunpos;


import Devices.ScannerTest;
import com.mongodb.util.JSON;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


public class Config {

	
	

	// Eine (versteckte) Klassenvariable vom Typ der eigene Klasse
	private static Config instance;

	
	
    public static String configFileDirectory = "\\resources\\config\\";
	public static String configFilename = "config.trn";
	
	


	
	//If the debugscreen (output of debuginfos) should appear or not
	public Boolean debugMode = true;
	
	//The Path of the mongo extension to start, if not already existing
	public String mongoDbLocation = "C:\\Program Files\\MongoDB\\Server\\3.2\\bin\\mongod.exe";
	
	//The currency Smybol to use for printouts etc
	private String currencySmybol = "€";
    private String currencySymbolLatin = "EUR";
	private String currencyName = "EURO";
	private boolean useCurrencySymbolOnReceipt = false;
	private boolean useCurrencyNameOnReceipt = false;
	private String[] weighItems =  new String[10];

    private JSONObject devices;


	private  String scaleComPort = "COM3";
	
	
	public Config() throws IOException {
		try {
			// read the json file
            JSONParser parser = new JSONParser();


            String filePath = new File("").getAbsolutePath();
            filePath = filePath.concat(configFileDirectory+configFilename);
            Object object = parser.parse(new FileReader(filePath));
            JSONObject jsonObject = (JSONObject)object;


			// get debugmode
			Boolean json_debugMode = (Boolean) jsonObject.get("debugMode");
			debugMode = json_debugMode;
			
			
			// get path of mongodb
			String json_mongoDbLocation =  jsonObject.get("mongoDbLocation").toString();
			mongoDbLocation = json_mongoDbLocation;
			

			// get currency symbol
			String json_currencySmybol = (String) jsonObject.get("currencySymbol");
			if(json_currencySmybol != null)
				currencySmybol = json_currencySmybol;

            // get currency symbol Latin
            String json_currencySymbolLatin = (String) jsonObject.get("currencySymbolLatin");
            if(json_currencySymbolLatin != null)
                currencySymbolLatin = json_currencySymbolLatin;

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
						System.out.println("Found WeighBarcode in Config: "+json_weighBarcodes.get(i)+"  i:"+i+"   size:"+json_weighBarcodes.size());
						weighItems[i] =json_weighBarcodes.get(i).toString() ;
						System.out.println("Read WeighBarcode in Config: "+this.weighItems[i]);
					}
				}

				//parse config for devices
                devices = (JSONObject)jsonObject.get("devices")  ;



		} catch (ParseException ex) {
			ex.printStackTrace();
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}

	}


	public Boolean isLineDisplayActive(){
        return isDeviceActive("lineDisplay");
    }

    public Boolean isPrinterActive(){
        return isDeviceActive("printer");
    }


    public Boolean isScaleActive(){
        return isDeviceActive("scale");
    }


    public Boolean isScannerActive(){
        return isDeviceActive("scanner");
    }

    private boolean isDeviceActive(String device){
	    boolean tmp = (boolean) ((JSONObject) devices.get(device)).get("active");
	    if(tmp == true)
            return  true;
	    else
	        return false;
	}

    //get procols of devices
    public String getScannerProtocol(){return getDeviceProtocol("scanner");}
    public String getPrinterProtocol(){return getDeviceProtocol("printer");}
    public String getLineDisplayProtocol(){return getDeviceProtocol("linedisplay");}
    public String getScaleProtocol(){return getDeviceProtocol("scale");}
    private String getDeviceProtocol(String device){
        String tmp = (String) ((JSONObject) devices.get(device)).get("protocol");
        return tmp;
    }


    //get name of devices
    public String getScannerName(){return getDeviceName("scanner");}
    public String getPrinterName(){return getDeviceName("printer");}
    public String getLineDisplayName(){return getDeviceName("linedisplay");}
    public String getScaleName(){return getDeviceName("scale");}
    private String getDeviceName(String device){
        String tmp = (String) ((JSONObject) devices.get(device)).get("name");
        return tmp;
    }


    //get port of devices
    public String getScannerPort(){return getDevicePort("scanner");}
    public String getPrinterPort(){return getDevicePort("printer");}
    public String getLineDisplayPort(){return getDevicePort("linedisplay");}
    public String getScalePort(){return getDevicePort("scale");}
    private String getDevicePort(String device){
        String tmp = (String) ((JSONObject) devices.get(device)).get("port");
        return tmp;
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


	public String getScaleComPort() {
		return scaleComPort;
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



    //for testing
    public static void main ( String[] args )
    {


        try
        {


            Config config =  Config.getInstance();


        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
