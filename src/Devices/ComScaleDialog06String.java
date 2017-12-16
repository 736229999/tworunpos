package Devices;

public class ComScaleDialog06String {

    private String[] originalString;
    private int stringNumber;
    private int value1;
    private int value2;
    private int value3;
    private int x;
    private int z;

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


    public ComScaleDialog06String(String[] string) {
        originalString = string;
//todo match strings
       /* for (int i = 0; i < string.length; i++) {
             string[i];
        }*/
    }



}
