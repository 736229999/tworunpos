package Devices;

import gnu.io.*;
import tworunpos.Article;
import tworunpos.DebugScreen;;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.TooManyListenersException;

public class ComScaleDialog06 implements SerialPortEventListener{

    private String comPortName;
    private Integer baudRate = 9600;

    //for containing the ports that will be found
    private Enumeration ports = null;
    //map the port names to CommPortIdentifiers
    private HashMap portMap = new HashMap();

    //this is the object that contains the opened port
    private CommPortIdentifier portId = null;
    private SerialPort serialPort = null;

    //input and output streams for sending and receiving data
    private InputStream input = null;
    private OutputStream output = null;

    //just a boolean flag that i use for enabling
    //and disabling buttons depending on whether the program
    //is connected to a serial port or not
    private boolean bConnected = false;

    //the timeout value for connecting with the port
    final static int TIMEOUT = 2000;

    //some ascii values for for certain things


    final static char STX = 0x02;
    final static char ETX = 0x03;
    final static char EOT = 0x04;
    final static char ENQ = 0x05;
    final static char ACK = 0x06;
    final static char NAK = 0x21;
    final static char ESC = 0x27;


    final static int STX_ASCII = 2;
    final static int ETX_ASCII = 3;
    final static int EOT_ASCII = 4;
    final static int ENQ_ASCII = 5;
    final static int ACK_ASCII = 6;
    final static int NAK_ASCII = 7;
    final static int ESC_ASCII = 8;

    final static int SPACE_ASCII = 32;
    final static int DASH_ASCII = 45;
    final static int NEW_LINE_ASCII = 10;



/*

    final static int STX = 2;
    final static int ETX = 3;
    final static int EOT = 4;
    final static int ENQ = 5;
    final static int ACK = 6;
    final static int NAK = 21;
    final static int ESC = 27;*/

    //a string for recording what goes on in the program
    //this string is written to the GUI
    String logText = "";


    public ComScaleDialog06() {
//todo read params from config
            comPortName = "COM3";
            baudRate = 9600;
    }

    public ComScaleDialog06(String comPort) {
//todo read params from config
        comPortName = comPort;
        this.open();

        if (this.getConnected() == true)
        {
            if (this.initIOStream() == true)
            {
                this.initListener();
            }
        }

    }
    //search for all the serial ports
    //pre style="font-size: 11px;": none
    //post: adds all the found ports to a combo box on the GUI
    public void searchForPorts()
    {
        ports = CommPortIdentifier.getPortIdentifiers();

        while (ports.hasMoreElements())
        {
            CommPortIdentifier curPort = (CommPortIdentifier)ports.nextElement();

            //get only serial ports
            if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL)
            {

                portMap.put(curPort.getName(), curPort);
            }
        }
    }

    //connect to the selected port in the combo box
    //pre: ports are already found by using the searchForPorts method
    //post: the connected comm port is stored in commPort, otherwise,
    //an exception is generated
    public void open(){



        try
        {
            portId = CommPortIdentifier.getPortIdentifier(comPortName);

            CommPort commPort = null;

            //the method below returns an object of type CommPort
            commPort = portId.open(this.getClass().getName(), TIMEOUT);
            //the CommPort object can be casted to a SerialPort object


            serialPort = (SerialPort) commPort;
            serialPort.setSerialPortParams(baudRate,SerialPort.DATABITS_7,SerialPort.STOPBITS_1,SerialPort.PARITY_ODD);

            //for controlling GUI elements
            setConnected(true);

            //logging
            logText = comPortName + " opened successfully.";
            DebugScreen.getInstance().print(logText);





    }
        catch (PortInUseException e)
        {
            logText = comPortName + " is in use. (" + e.toString() + ")";

            DebugScreen.getInstance().print(logText);
        }
        catch (Exception e)
        {
            logText = "Failed to open " + comPortName + "(" + e.toString() + ")";
            DebugScreen.getInstance().print(logText);
        }
    }

    //open the input and output streams
    //pre: an open port
    //post: initialized intput and output streams for use to communicate data
    public boolean initIOStream()
    {
        //return value for whather opening the streams is successful or not
        boolean successful = false;

        try {
            //
            input = serialPort.getInputStream();
            output = serialPort.getOutputStream();
            //writeData(0, 0);

            successful = true;
            return successful;
        }
        catch (IOException e) {
            logText = "I/O Streams failed to open. (" + e.toString() + ")";
            DebugScreen.getInstance().print(logText);
            return successful;
        }
    }

    //starts the event listener that knows whenever data is available to be read
    //pre: an open serial port
    //post: an event listener for the serial port that knows when data is recieved
    public void initListener()
    {
        try
        {
           serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);

        }
        catch (TooManyListenersException e)
        {
            logText = "Too many listeners. (" + e.toString() + ")";
            DebugScreen.getInstance().print(logText);
        }
    }

    //disconnect the serial port
    //pre: an open serial port
    //post: clsoed serial port
    public void close()
    {
        //close the serial port
        try
        {
            writeData("0");

            serialPort.removeEventListener();
            serialPort.close();
            input.close();
            output.close();
            setConnected(false);


            logText = "Serial Port connection closed.";
            DebugScreen.getInstance().print(logText);
        }
        catch (Exception e)
        {
            logText = "Failed to close " + serialPort.getName() + "(" + e.toString() + ")";
            DebugScreen.getInstance().print(logText);
        }
    }

    final public boolean getConnected()
    {
        return bConnected;
    }

    public void setConnected(boolean bConnected)
    {
        this.bConnected = bConnected;
    }

    //what happens when data is received
    //pre: serial event is triggered
    //post: processing on the data it reads
    public void serialEvent(SerialPortEvent evt) {
        if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE)
        {

            try
            {
                byte[] readBuffer = new byte[100];
               // byte singleData = (byte)input.read();

                // read data
                int bytes = 0;
                try {
                    while (input.available() > 0) {
                        bytes = input.read();
                    }
                } catch (IOException e) {
                    return;
                }

                String[] messageHex = new String[bytes];
                for(int i=0; i<bytes;i++){
                    messageHex[i] = asciiToHex(new String(readBuffer,i,1));
                }
                System.out.println("Receive: "+messageHex);


                //ComScaleDialog06String comString = new ComScaleDialog06String(messageHex);

                if(bytes==21){
                    System.out.println("NAK");

                    //get status
                    String s = ""+EOT+STX+"08"+ETX;
                    System.out.println("Send: "+s);
                    writeData(s);

                    //get status
                     s = ""+EOT+STX+"08"+ETX;
                    System.out.println("Send: "+s);
                    writeData(s);

                }


            }
            catch (Exception e)
            {
                logText = "Failed to read data. (" + e.toString() + ")";
                DebugScreen.getInstance().print(logText);
            }
        }
    }



    public void sendArticle(Article article){

    }

    public void writeDataTest( ){
        try
        {
            String send = ""+EOT+STX+"01"+ESC+"11111"+ESC+ETX;
            output.write(send.getBytes());
//            output.write(ascii);
            output.flush();

        }
        catch (Exception e)
        {
            logText = "Failed to write data. (" + e.toString() + ")";
            DebugScreen.getInstance().print(logText);
        }
    }

    public void writeData(String s ){
        try
        {

            output.write(s.getBytes());
            output.flush();

        }
        catch (Exception e)
        {
            logText = "Failed to write data. (" + e.toString() + ")";
            DebugScreen.getInstance().print(logText);
        }
    }



    public String lookupAscii(char a){
        switch(a){
            case STX: return "<STX>";
            case ETX: return "<ETX>";
            case EOT: return "<EOT>";
            case ENQ: return "<ENQ>";
            case ACK: return "<ACK>";
            case NAK: return "<NAK>";
            case ESC: return "<ESC>";
        }
        return "";
    }
    public static char[] getHexValue(byte[] array){
        char[] symbols="0123456789ABCDEF".toCharArray();
        char[] hexValue = new char[array.length * 2];

        for(int i=0;i<array.length;i++)
        {
            //convert the byte to an int
            int current = array[i] & 0xff;
            //determine the Hex symbol for the last 4 bits
            hexValue[i*2+1] = symbols[current & 0x0f];
            //determine the Hex symbol for the first 4 bits
            hexValue[i*2] = symbols[current >> 4];
        }
        return hexValue;
    }

    private static String asciiToHex(String asciiValue)
    {
        char[] chars = asciiValue.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++)
        {
            hex.append(Integer.toHexString((int) chars[i]));
        }
        return hex.toString();
    }
}
