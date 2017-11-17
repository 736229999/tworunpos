package Devices;

import jpos.JposException;
import jpos.LineDisplay;

public class JLineDisplay extends JPosDevice implements JPosDeviceInterface {

	
    private LineDisplay device = new LineDisplay();
    

    
	
	public JLineDisplay(String deviceName) throws Exception {
		super();
		//close first
		this.close();
		this.open(deviceName);
		//this.printDemo();
	}
	
	
	
	
	
	
	public void makeDemo()  {
		try{
			System.out.println("display has "+device.getColumns()+" cols and "+device.getRows()+" rows.");

			device.displayTextAt(0,0, "two-run", 0);
			device.displayTextAt(1,1, "POS", 0);


		}catch(Exception e){
			
		}
	}
	
	
	public void open(String deviceName) throws Exception{

		
		super.open();
        try
        {
			// To test Printer
        	device.open(deviceName);
        	device.claim(1000);
        	device.setDeviceEnabled(true);
	            

		
        }catch(JposException e){
			e.printStackTrace();
			throw new Exception("LineDisplay open failed");
		}
        
	}
	
	public void close(){
		super.close();
		// close the Printer and CashDrawer object
		try{
			device.setDeviceEnabled(false);
			device.release();
			device.close();
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	
	

}
