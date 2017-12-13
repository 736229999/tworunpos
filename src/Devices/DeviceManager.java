package Devices;


import gnu.io.SerialPortEvent;
import tworunpos.DebugScreen;



public class DeviceManager {

	// Own Instance for singleton
	private static DeviceManager instance;
	
	
	JPosPrinter printer = null;
	JLineDisplay lineDisplay = null;
	ComScaleDialog06 scale = null;

	private int ENQ = 5,
			ACK = 6,
			NAK = 15,
			STX = 2,
			ETX = 3,
			EOT = 4;
	
	public DeviceManager() throws Exception {
		//System.setProperty("jpos.config.populatorFile", "jpos.xml");
		//System.setProperty(JposPropertiesConst.JPOS_POPULATOR_FILE_PROP_NAME, "jpos.xml");


		try
		{
			scale = new ComScaleDialog06("COM3");
			scale.writeData( EOT,ENQ);

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
			scale.close();
			DebugScreen.getInstance().print("--scale closed");
		}
	}
	
	
	
	//Singleton get Instantce
	public static synchronized DeviceManager getInstance () throws Exception {
		if (DeviceManager.instance == null) {
			DeviceManager.instance = new DeviceManager ();
		}
		return DeviceManager.instance;
	}
	
}