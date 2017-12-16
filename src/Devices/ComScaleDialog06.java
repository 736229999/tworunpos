package Devices;

import Helpers.Rotate;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * This version of the TwoWaySerialComm example makes use of the 
 * SerialPortEventListener to avoid polling.
 *
 */
public class ComScaleDialog06
{



    //return codes of string 11
    final static int S11_SEND_AGAIN = 2;
    final static int S11_CHECK_OK = 1;
    final static int S11_CHECK_FAILED = 0;


    final static char NUL = 0x01;
    final static char STX = 0x02;
    final static char ETX = 0x03;
    final static char EOT = 0x04;
    final static char ENQ = 0x05;
    final static char ACK = 0x06;
    final static char BEL = 0x07;
    final static char BS = 0x08;
    final static char HT = 0x09;
    final static char LF = 0x0A;
    final static char VT = 0x0B;
    final static char FF = 0x0C;
    final static char CR = 0x0D;
    final static char SO = 0x0E;
    final static char SI = 0x0F;
    final static char NAK = 0x21;
    final static char ESC = 0x1B;

    static OutputStream out1;

    public ComScaleDialog06()
    {
        super();
    }

    void connect ( String portName ) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);

            if ( commPort instanceof SerialPort )
            {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(9600,SerialPort.DATABITS_7,SerialPort.STOPBITS_1,SerialPort.PARITY_ODD);

                System.out.println("COM3 Port offen");
                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();

                out1=out;
                //(new Thread(new SerialReader(in))).start();
                (new Thread(new SerialWriter(out))).start();
                serialPort.addEventListener(new SerialReader(in));
                serialPort.notifyOnDataAvailable(true);

            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }
    }

    /**
     * Handles the input coming from the serial port. A new line character
     * is treated as the end of a block in this example. 
     */
    public static class SerialReader implements SerialPortEventListener
    {
        private InputStream in;
        private byte[] buffer = new byte[1024];

        public SerialReader ( InputStream in )
        {
            this.in = in;
        }

        public void serialEvent(SerialPortEvent arg0) {
            int data;
            //emtpy buffer
            buffer  = new byte[buffer.length];

            //todo check why cheksum is nor working

            try
            {
                int len = 0;
                while ( ( data = in.read()) > -1 )
                {
                    buffer[len++] = (byte) data;
                }


                int stringId = 0;
                try {

                    //hack own interpretation of NAK = 99 and ACK = 98
                    if(buffer[0] == ACK)
                        stringId = 98;
                    else if (buffer[0] == NAK)
                        stringId = 99;


                    stringId = getStringNumberOfIncomingString(buffer);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }


                System.out.println("IncomeString: "+buffer);
                System.out.println("Number: "+stringId);




                //String number_int = ""+number;
                switch(stringId){
                    case 11:{ //code 11
                        int d = getDValueOfString11(buffer);
                        int z1 = getZ1ValueOfString11(buffer);
                        int z2 = getZ2ValueOfString11(buffer);
/*
                        //todo remove this after debug
                        if(z1 != 0 || z2 != 0){
                            String send = getString1("222222");
                            out1.write(send.getBytes());
                            return;
                        }*/


                        System.out.println("com: Send set 10");  //todo wants checksum - send set 10
                        if(d == S11_SEND_AGAIN){
                            String send = getString10(z1,z2);
                            out1.write(send.getBytes());
                        }else{
                            //String send = getString8();
                            //out1.write(send.getBytes());
                            if (d==S11_CHECK_FAILED){
                                String send = getStringToGetCalculation();
                                out1.write(send.getBytes());
                            }

                        }
                    }break;

                    case 2: System.out.println("now proceed result");break; //todo weight result - proceed result
                    case 9: System.out.println("print status information");break; //todo print info

                    //ACK - checksum was correct - get calculation
                    case 98: System.out.println("get calculation");
                        String send = getStringToGetCalculation();
                        out1.write(send.getBytes());
                    ;break; //todo print info

                    //NAK - failure - please get the error -> break into default
                    case 99: System.out.println("get status information"); //todo print info
                    default : {

                        out1.write(getString8().getBytes());
                        System.out.print("com: Ask for status");
                    }break; //todo NAK / error - ask for statusinfo with set 08
                }

            }
            catch ( IOException e )
            {
                e.printStackTrace();
                System.exit(-1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }


    /** */
    public static class SerialWriter implements Runnable
    {
        OutputStream out;



        public  void writetoport(String send) {

            try {
                out.write(send.getBytes());
                out.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }



        public SerialWriter ( OutputStream out )
        {
            this.out = out;
        }

        public void run ()
        {
            try
            {
                int c = 0;
                while ( ( c = System.in.read()) > -1 )
                {
                    this.out.write(c);
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }

    //Strings we send

    public static String getString1(String grundpreis){
        return ""+EOT+STX+"01"+ESC+grundpreis+ESC+ETX;
    }

    //Übermittlung von Grundpreis, Tarawert
    public static String getString3(String grundpreis, String tara){
        return ""+EOT+STX+"03"+ESC+grundpreis+ESC+tara+ETX;
    }

    //Übermittlung von Grundpreis,  Text
    public static String getString4(String grundpreis, String text){
        return ""+EOT+STX+"04"+ESC+grundpreis+ESC+text+ETX;
    }

    //Übermittlung von Grundpreis, Tarawert und Text
    public static String getString5(String grundpreis, String tara, String text){
        return ""+EOT+STX+"05"+ESC+grundpreis+ESC+tara+ESC+text+ETX;
    }

    //Anforderung der Statusinformation nach Empfang
    public static String getString8(){
        return ""+EOT+STX+"08"+ETX;
    }

    public static String getStringToGetCalculation(){
        return ""+EOT+ENQ;
    }

    //Checksummen + Korrekturwert and wie Waage übermitteln
    public static String getString10(int z1, int z2 ){

        //coding
        //byte high = (byte) (z & 0xF0);
        //byte low = (byte) (z  & 0x0F);


        //verschlüssele

        //Split value in 4 bits (higher nibble, lower nibble)


        //todo calculation

        //0xF336 KW
        char  kw = (char)((0xF<<12)+(0x3<<8)+(0x3<<4)+(0x6)); //unsigned short
        char  cs = (char)((0x4<<12)+(0x7<<8)+(0x1<<4)+(0x1));


        char csRotated = Rotate.rotl(cs,z1);
        char kwRotated = Rotate.rotr(kw,z2);


        //char csRotated = (char)(Integer.rotateLeft(cs,z1) & 0x0000FFFF);
        //char kwRotated = (char)(Integer.rotateRight(kw,z2) & 0x0000FFFF);
        //byte[] kwBack = Rotate.intToByteArray(kwRotated);
        //String kwStringFinal = new String(kwBack);


        String kwRotated_String = String.format("%04x", (int) kwRotated);
        String csRotated_String = String.format("%04x", (int) csRotated);
        String result = ""+EOT+STX+"10"+ESC+csRotated_String+kwRotated_String+ETX;
        return result;
    }

    //Logische Versionsnummer ein / aus
    public static String getString20(boolean b){
        String onoff = (b == true ? "1" : "0");
        return ""+EOT+STX+"20"+ESC+onoff+ETX;
    }
    public static String getString80(){
        return ""+EOT+STX+"80"+ESC;
    }

    public static String getString81(){
        return ""+EOT+STX+"81"+ETX;
    }

    public static String getENQ(){
        return ""+EOT+ENQ;
    }


    //extrahiere satznummer des empfangenen strings
    public static int  getStringNumberOfIncomingString(byte[] string) throws Exception {
        if(string.length >= 3){

            char [] charArray = new char[string.length];
            for(int i = 0; i < string.length; i++){
                charArray[i] = (char)string[i];
            }

            int result =           Rotate.charArrayToInt(charArray,1,2);

            return result;
        }
        else
            throw new Exception("Incoming string not well formed: "+string);
    }

    //extract value Z (int) of string 11
    public static int  getZ1ValueOfString11(byte[] string) throws Exception {
    if(string.length >= 3){

        char [] charArray = new char[string.length];
        for(int i = 0; i < string.length; i++){
            charArray[i] = (char)string[i];
        }

        int result = Rotate.hexToInt(charArray[5]);

        return result;
    }
    else
        throw new Exception("Incoming string not well formed: "+string);
}

    public static int  getZ2ValueOfString11(byte[] string) throws Exception {
        if(string.length >= 3){

            char [] charArray = new char[string.length];
            for(int i = 0; i < string.length; i++){
                charArray[i] = (char)string[i];
            }

            int result = Rotate.hexToInt(charArray[6]);

            return result;
        }
        else
            throw new Exception("Incoming string not well formed: "+string);
    }

    //extract value D (int) of string 11
    public static int  getDValueOfString11(byte[] string) throws Exception {
        if(string.length >= 5){

            char [] charArray = new char[string.length];
            for(int i = 0; i < string.length; i++){
                charArray[i] = (char)string[i];
            }

            int result =           Rotate.charArrayToInt(charArray,4,4);

            return result;
        }
        else
            throw new Exception("Incoming string not well formed: "+string);
    }




    public static void main ( String[] args )
    {
        try
        {
            ComScaleDialog06 com = new ComScaleDialog06();
            com.connect("COM3");

         out1.write(getString1("111111").getBytes());

        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}