package Devices;


import tworunpos.DebugScreen;



public class JPosDeviceManager {

	// Eine (versteckte) Klassenvariable vom Typ der eigene Klasse
	private static JPosDeviceManager instance;
	
	
	JPosPrinter printer = null;
	JLineDisplay lineDisplay = null;
	
	public JPosDeviceManager() throws Exception {
		//System.setProperty("jpos.config.populatorFile", "jpos.xml");
		//System.setProperty(JposPropertiesConst.JPOS_POPULATOR_FILE_PROP_NAME, "jpos.xml");
		
		
		//printer = new JPosPrinter("POSPrinter");
//		printer = new JPosPrinter("WN_TH180_USB");
//		printer.makeDemo();
//		lineDisplay = new JLineDisplay("WN_BA63_USB");
//		lineDisplay.makeDemo();
	}
	
	
	public JPosPrinter getPrinter() {
		return printer;
	}
	
	
	public JLineDisplay getLineDisplay() {
		return lineDisplay;
	}




	public void closeAllDevices(){
		if(printer != null){
			DebugScreen.getInstance().print("--close printer");
			printer.close();
			DebugScreen.getInstance().print("--printer closed");
			DebugScreen.getInstance().print("--close lineDisplay");
			lineDisplay.close();
			DebugScreen.getInstance().print("--lineDisplay closed");
		}
	}
	
	
	
	//Singleton get Instantce
	public static synchronized JPosDeviceManager getInstance () throws Exception {
		if (JPosDeviceManager.instance == null) {
			JPosDeviceManager.instance = new JPosDeviceManager ();
		}
		return JPosDeviceManager.instance;
	}
	
}