package Devices;

import Exceptions.DeviceException;
import Prints.Receipt;
import jpos.JposException;
import jpos.POSPrinter;
import jpos.POSPrinterConst;
import jpos.Scanner;
import jpos.events.DataEvent;
import jpos.events.DataListener;
import tworunpos.DebugScreen;

import java.util.Observable;
import java.util.Observer;

public class JPosScanner extends JPosDevice implements JPosDeviceInterface  {






    private Scanner scanner;
    private ScannerNotifier scannerNotifier;
    // constants defined for convience sake (could be inlined)
    private String ESC    = ((char) 0x1b) + "";
    private String LF     = ((char) 0x0a) + "";
    private String SPACES = "                                                                      ";

    private DataListener myListener;


	public JPosScanner(String deviceName) throws JposException {
		super();
		//close first
		this.close();

        scanner = new Scanner();
		this.open(deviceName);

        scannerNotifier = new ScannerNotifier();
        scanner.addDataListener(scannerNotifier);

	}


	
	public String scan() throws DeviceException {
		String scanData = "";
		try{
			
			scanData = new String(scanner.getScanData());

		}catch(JposException e){
			DebugScreen.getInstance().printStackTrace(e);
			throw new DeviceException(e.getMessage());
		}
		return scanData;
	}
	
	
	

	
	
	public void open(String deviceName){

		
		super.open();
        try
        {

			//scanner.addDataListener(this);
			// Initialize the scanner. Exception thrown if a method fails.
			scanner.open(deviceName);
			scanner.claim(1000);
			scanner.setDeviceEnabled(true);
			scanner.setDataEventEnabled(true);


		
        }catch(JposException e){
			e.printStackTrace();
		}
        
	}
	
	public void close(){
		super.close();
		// close the Printer and CashDrawer object
		try{
			scanner.setDeviceEnabled(false);
			scanner.release();
			scanner.close();
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

    public ScannerNotifier getScannerNotifier() {
        return scannerNotifier;
    }

    public class ScannerNotifier extends Observable  implements DataListener  {

		//for scanner
		@Override
		public void dataOccurred(DataEvent e) {

		    //notifies observer
            setChanged();

			jpos.Scanner dc = (jpos.Scanner) e.getSource();


            try {
                String scanData = scan();

                String Msg = "Scanner DataEvent (Status=" + e.getStatus() + ") received.\n Data: "+scanData;
                System.out.println (Msg);

                notifyObservers(scanData);
            } catch (DeviceException e1) {
                e1.printStackTrace();
            }





			try {
				dc.setDataEventEnabled(true);
			} catch (JposException event){}




		}
	}




}
