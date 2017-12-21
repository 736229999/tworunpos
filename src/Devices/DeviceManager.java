package Devices;


import tworunpos.Config;
import tworunpos.DebugScreen;



public class DeviceManager {

	// Own Instance for singleton
	private static DeviceManager instance;
	
	
	JPosPrinter printer = null;
	JLineDisplay lineDisplay = null;
	ComScaleDialog06 scale = null;


	
	public DeviceManager() throws Exception {
		//System.setProperty("jpos.config.populatorFile", "jpos.xml");
		//System.setProperty(JposPropertiesConst.JPOS_POPULATOR_FILE_PROP_NAME, "jpos.xml");


		try
		{
			scale = ComScaleDialog06.getInstance();
            DebugScreen.getInstance().print("-- Scale opened ("+ Config.getInstance().getScaleComPort()+")");

		}
		catch ( Exception e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


//		printer = new JPosPrinter("POSPrinter");
		//printer = new JPosPrinter("POSPrinter");
		//printer.makeDemo();
//		lineDisplay = new JLineDisplay("WN_BA63_USB");
//		lineDisplay.makeDemo();
	}
	
	
	public JPosPrinter getPrinter() {
		return printer;
	}
	
	
	public JLineDisplay getLineDisplay() {
		return lineDisplay;
	}

	public ComScaleDialog06 getScale() { return scale;	}

	public void closeAllDevices(){
		if(printer != null){
			DebugScreen.getInstance().print("--close printer");
			printer.close();
			DebugScreen.getInstance().print("--printer closed");
			DebugScreen.getInstance().print("--close lineDisplay");
			lineDisplay.close();
			DebugScreen.getInstance().print("--lineDisplay closed");
		}

		if(scale != null){
			//scale.close();
			DebugScreen.getInstance().print("--scale closed");
		}
	}
	
	
	
	//Singleton get Instantce
	public static synchronized DeviceManager getInstance () throws Exception {
		if (instance == null) {
			instance = new DeviceManager ();
		}
		return instance;
	}
	
}