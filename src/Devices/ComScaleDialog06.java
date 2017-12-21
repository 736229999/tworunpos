package Devices;

import Exceptions.DeviceException;
import Exceptions.ScaleException;
import Helpers.Rotate;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import org.apache.commons.lang.StringUtils;
import tworunpos.Article;
import tworunpos.Config;


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


    // Singleton!
    static ComScaleDialog06 instance;

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


    //result from String 02
    private Double weight = 0.000;
    private Double baseprice = 0.000;
    private Double tara = 0.000;
    private Double calculatedSalesprice = 0.000;
    private Integer scaleStatus = 0;

    //result of String 08
    private Integer statusCode = 0;

    private String lastMessageReceived;

    public ComScaleDialog06()
    {
        super();
    }

    public void connect ( String portName ) throws Exception
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
    public  class SerialReader implements SerialPortEventListener
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
            lastMessageReceived = null;

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

                lastMessageReceived = buffer.toString();

                System.out.println("IN com: "+(new String(buffer)).toString());
                //System.out.println("Number: "+stringId);




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

                                String send = getString1("12345");
                                out1.write(send.getBytes());


                        }
                    }break;

                    case 2:{
                        System.out.println("Weight data arrived. processing...");

                        //parse Status from result
                        char statusTmp = (char)(0x00 | (buffer[1] << 4));
                        statusTmp = (char)(statusTmp | buffer[2]);
                        scaleStatus =  hexToInt(statusTmp);

                        //parse weight from result
                        String weighFromScale= (new String(buffer)).toString().substring(7,11);
                        weight = Double.parseDouble(weighFromScale)/1000;

                        //parse baseprice from result
                        String basePriceFromScale = (new String(buffer)).toString().substring(12,18);
                        baseprice = Double.parseDouble(basePriceFromScale)/100;

                        //parse salesprice from result
                        String salesPriceFromScale = (new String(buffer)).toString().substring(20,25);
                        calculatedSalesprice = Double.parseDouble(salesPriceFromScale)/100;
                        System.out.println("Weight data processed.");
                        System.out.println("**scaleStatus: "+scaleStatus);
                        System.out.println("**weight: "+weight);
                        System.out.println("**baseprice: "+baseprice);
                        System.out.println("**calculatedSalesprice: "+calculatedSalesprice);

                    }break;

                    case 9: {
                        System.out.println("09: status information");
                        char statusTmp = (char)(0x00 | (buffer[4] << 4));
                        statusTmp = (char)(statusTmp | buffer[5]);
                        statusCode =  hexToInt(statusTmp);

                    }break; //todo print info

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


    public static int hexToInt(char a){
        int result;

        result = Character.digit(a,16);

        return result;
    }

    //Strings we send

    private static String getString1(String grundpreis){
        return ""+EOT+STX+"01"+ESC+grundpreis+ESC+ETX;
    }

    //Übermittlung von Grundpreis, Tarawert
    private static String getString3(String grundpreis, String tara){
        return ""+EOT+STX+"03"+ESC+grundpreis+ESC+tara+ETX;
    }

    //Übermittlung von Grundpreis,  Text
    private static String getString4(String grundpreis, String text){
        return ""+EOT+STX+"04"+ESC+grundpreis+ESC+text+ETX;
    }

    //Übermittlung von Grundpreis, Tarawert und Text
    private static String getString5(String grundpreis, String tara, String text){
        return ""+EOT+STX+"05"+ESC+grundpreis+ESC+tara+ESC+text+ETX;
    }

    //Anforderung der Statusinformation nach Empfang
    private static String getString8(){
        return ""+EOT+STX+"08"+ETX;
    }

    private static String getStringToGetCalculation(){
        return ""+EOT+ENQ;
    }


    /*
        Checksummen + Korrekturwert and wie Waage übermitteln
        param1: higher nibble
        param2: lower nibble
         */
    private static String getString10(int z1, int z2 ){



        // KW=0xF336  CS=0x4711
        char  kw = (char)((0xF<<12)+(0x3<<8)+(0x3<<4)+(0x6)); //unsigned short
        char  cs = (char)((0x4<<12)+(0x7<<8)+(0x1<<4)+(0x1));


        //rotate cs an kw - encryption before sending data to scale
        char csRotated = Rotate.rotl(cs,z1);
        char kwRotated = Rotate.rotr(kw,z2);


        //convert rotated cs and kw to strings before sending
        String kwRotated_String = String.format("%04x", (int) kwRotated);
        String csRotated_String = String.format("%04x", (int) csRotated);

        //bring final string together as string
        String result = ""+EOT+STX+"10"+ESC+csRotated_String.toUpperCase()+kwRotated_String.toUpperCase()+ETX;
        return result;
    }

    //Logische Versionsnummer ein / aus
    private static String getString20(boolean b){
        String onoff = (b == true ? "1" : "0");
        return ""+EOT+STX+"20"+ESC+onoff+ETX;
    }
    private static String getString80(){
        return ""+EOT+STX+"80"+ESC;
    }

    private static String getString81(){
        return ""+EOT+STX+"81"+ETX;
    }

    private static String getENQ(){
        return ""+EOT+ENQ;
    }


    //extrahiere satznummer des empfangenen strings
    private static int  getStringNumberOfIncomingString(byte[] string) throws Exception {
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
    private static int  getZ1ValueOfString11(byte[] string) throws Exception {
    if(string.length >= 3){

        char [] charArray = new char[string.length];
        for(int i = 0; i < string.length; i++){
            charArray[i] = (char)string[i];
        }

        int result = hexToInt(charArray[5]);

        return result;
    }
    else
        throw new Exception("Incoming string not well formed: "+string);
}

    private static int  getZ2ValueOfString11(byte[] string) throws Exception {
        if(string.length >= 3){

            char [] charArray = new char[string.length];
            for(int i = 0; i < string.length; i++){
                charArray[i] = (char)string[i];
            }

            int result = hexToInt(charArray[6]);

            return result;
        }
        else
            throw new Exception("Incoming string not well formed: "+string);
    }

    //extract value D (int) of string 11
    private static int  getDValueOfString11(byte[] string) throws Exception {
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



    public void weighArticle(Article article) throws ScaleException {

        weight = 0.000;
        baseprice = 0.000;
        tara = 0.000;
        calculatedSalesprice = 0.000;

        //baseprice
        String baseprice = ""+((int)(article.getPriceGross()*100) ) ;
        //left pad with zeros
        baseprice = StringUtils.leftPad(baseprice, 6, "0");

        //todo tara
        //String tara = new Double(article.getTara()*100).toString();
        String tara = "0000";

        //text
        String text = article.getName().substring(0,(article.getName().length()>=13?13:article.getName().length()));
        text = StringUtils.rightPad(text, 13, " ");

        try {
            out1.write(getString5(baseprice,tara,text).getBytes());
        } catch (IOException e) {
            throw new ScaleException("Fehler beim Senden von Artikel an die Waage.");
        }


    }

    public Double getWeight() {
        return weight;
    }

    public Double getBaseprice() {
        return baseprice;
    }

    public Double getTara() {
        return tara;
    }

    public Double getCalculatedSalesprice() {
        return calculatedSalesprice;
    }

    public Integer getScaleStatus(){

        return scaleStatus;
    }

    public Integer getStatusCode(){

        return statusCode;
    }


    public String getLastMessageReceived() {
        return lastMessageReceived;
    }



    //Singleton get Instantce
    public static synchronized ComScaleDialog06 getInstance () throws DeviceException {
        if (instance == null) {
            instance = new ComScaleDialog06 ();
            try {
                instance.connect(Config.getInstance().getScaleComPort());
            } catch (Exception e) {
                e.printStackTrace();
                throw  new DeviceException(e.getMessage());
            }
        }
        return instance;
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