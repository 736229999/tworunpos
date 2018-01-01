package Devices;


import com.usb.core.Device;
import jpos.JposException;
import jpos.events.DataEvent;
import jpos.events.DataListener;
import jpos.util.JposPropertiesConst;
import tworunpos.Config;
import tworunpos.DebugScreen;



public class DeviceManager {

	// Own Instance for singleton
	private static DeviceManager instance;
	
	
	JPosPrinter printer = null;
	JPosScanner scanner = null;
	JLineDisplay lineDisplay = null;
	ComScaleDialog06 scale = null;


	
	public DeviceManager()  throws Exception
	{
		System.setProperty("jpos.config.populatorFile", "jpos.xml");
		System.setProperty(JposPropertiesConst.JPOS_POPULATOR_FILE_PROP_NAME, "jpos.xml");


		try
		{
			DebugScreen.getInstance().print("-- opening scale");
			scale = ComScaleDialog06.getInstance();
            DebugScreen.getInstance().print("-- scale opened ("+ Config.getInstance().getScaleComPort()+")");

		}
		catch ( Exception e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DebugScreen.getInstance().print("-- opening scanner");
		//printer = new JPosPrinter("POSPrinter");
		scanner = new JPosScanner("DLS-Magellan-RS232-Scanner");
		DebugScreen.getInstance().print("-- scanner opened");



		//printer = new JPosPrinter("POSPrinter");
		DebugScreen.getInstance().print("-- opening printer");
		printer = new JPosPrinter("POSPrinter");
		printer.makeDemo();
		DebugScreen.getInstance().print("-- printer opened");


//		lineDisplay = new JLineDisplay("WN_BA63_USB");
//		lineDisplay.makeDemo();
	}
	
	
	public JPosPrinter getPrinter() {
		return printer;
	}

    public JPosScanner getScanner() {
        return scanner;
    }

	public JLineDisplay getLineDisplay() {
		return lineDisplay;
	}

	public ComScaleDialog06 getScale() { return scale;	}

	public void closeAllDevices(){

		if(scanner != null){
			DebugScreen.getInstance().print("--close scanner");
			scanner.close();
			DebugScreen.getInstance().print("--scanner closed");
		}

		if(printer != null){
			DebugScreen.getInstance().print("--close printer");
			printer.close();
			DebugScreen.getInstance().print("--printer closed");
		}

		if(scale != null){
			//scale.close();
			DebugScreen.getInstance().print("--scale closed");
		}
	}

	//for testing
	public static void main ( String[] args )
	{
		try
		{
			DeviceManager dm = DeviceManager.getInstance();



		}
		catch ( Exception e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
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