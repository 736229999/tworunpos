package Devices;

import tworunpos.Cart;
import jpos.JposException;
import jpos.POSPrinter;
import jpos.POSPrinterConst;
import Prints.Receipt;

public class JPosPrinter extends JPosDevice implements JPosDeviceInterface {

	
    private POSPrinter ptr = new POSPrinter();
    
    // constants defined for convience sake (could be inlined)
    private String ESC    = ((char) 0x1b) + "";
    private String LF     = ((char) 0x0a) + "";
    private String SPACES = "                                                                      ";
          
    
    
	
	public JPosPrinter(String deviceName) {
		super();
		//close first
		//this.close();
		this.open(deviceName);
		//this.printDemo();
	}
	
	public void print(String stringToPrint){
		try{
			
			// begining a transaction
			// This transaction mode causes all output to be buffered			
			ptr.transactionPrint(POSPrinterConst.PTR_S_RECEIPT, POSPrinterConst.PTR_TP_TRANSACTION);
			
			ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, stringToPrint);
			 //   after feeding to the cutter position
	        ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|100fP");

			// terminate the transaction causing all of the above buffered data to be sent to the printer
			ptr.transactionPrint(POSPrinterConst.PTR_S_RECEIPT, POSPrinterConst.PTR_TP_NORMAL);
		}catch(JposException e){
			
		}
		
	}
	
	
	
	
	public void makeDemo()  {

		/* this amthod does some prints on the device to see if everything works well.
		e.g. this method is executeable during the development or when starting the application
		 */

		try{
			// check if the cover is open
			if (ptr.getCoverOpen() == true)
			{
				// cover open so do not attempt printing
				throw new Exception("Cover Open");
			}
	            
			// check if the printer is out of paper
			if (ptr.getRecEmpty() == true)
			{
				// the printer is out of paper so do not attempt printing
				throw new Exception("Printer out of Paper");
			}
	            
			// begining a transaction
			// This transaction mode causes all output to be buffered
			ptr.transactionPrint(POSPrinterConst.PTR_S_RECEIPT, POSPrinterConst.PTR_TP_TRANSACTION);

			
			//create transactionprint
			  //get an Iterator object for Vector using iterator() method.
			
			Receipt testReceipt = new Receipt();
			testReceipt.addNewSimpleLineToBody("--- tworun POS ---");

			
			
			ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, testReceipt.toString());

            // the ESC + "|100fP" control code causes the printer to execute a paper cut
            //   after feeding to the cutter position
            ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, ESC + "|100fP");
	
			// terminate the transaction causing all of the above buffered data to be sent to the printer
			ptr.transactionPrint(POSPrinterConst.PTR_S_RECEIPT, POSPrinterConst.PTR_TP_NORMAL);

		}catch(Exception e){
			
		}
	}
	
	
	public void open(String deviceName){

		
		super.open();
        try
        {
			// To test Printer
			ptr.open(deviceName);
			ptr.claim(1000);
			ptr.setDeviceEnabled(true);
	            
			//Output by the high quality mode
			ptr.setRecLetterQuality(true);
		
        }catch(JposException e){
			e.printStackTrace();
		}
        
	}
	
	public void close(){
		super.close();
		// close the Printer and CashDrawer object
		try{
			ptr.setDeviceEnabled(false);
			ptr.release();
			ptr.close();
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	
	

}
