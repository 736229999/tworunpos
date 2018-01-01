package Devices;//import ...;
import jpos.*;
import jpos.events.*;
public class ScannerTest implements DataListener
{
    // Data listener’s method to process incoming scanner data.
    public void dataOccurred(DataEvent e)
    {
        jpos.Scanner dc = (jpos.Scanner) e.getSource();
        String Msg = "Scanner DataEvent (Status=" + e.getStatus() + ") received.";
        System.out.println (Msg);
        try {
            dc.setDataEventEnabled(true);
        } catch (JposException easd){}
    }
    // Method to initialize the scanner.
    public void initScanner(String openName) throws jpos.JposException
    {
        // Create scanner instance and register for data events.
        jpos.Scanner myScanner1 = new jpos.Scanner();





        myScanner1.addDataListener(this);
        // Initialize the scanner. Exception thrown if a method fails.
        myScanner1.open(openName);
        myScanner1.claim(1000);
        myScanner1.setDeviceEnabled(true);
        myScanner1.setDataEventEnabled(true);
        /*String scandata = "";
        for(int i= 0; i< 500; i++){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            scandata =  new String(myScanner1.getScanData());
            System.out.println("d:"+scandata);
        }*/

    }
    //for testing
    public static void main ( String[] args )
    {
        try
        {

            ScannerTest scanner = new ScannerTest();



            scanner.initScanner("DLS-Magellan-RS232-Scanner");



        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}