package Helpers;

import static java.lang.Character.digit;

public final class Rotate {

    public static final int ROTATE_LEFT = 1;
    public static final int ROTATE_RIGHT = 2;


  /* @param bitstorotate number of bits to rotate
	 * @param direction which direction to rotate ROTATE_LEFT or ROTATE_RIGHT
	 * @return the mask for masking out the bits which will be displaced in the rotate
	 */
    private static int calcmask(int bitstorotate, int direction)
    {
        int mask = 0;
        int c;

        if (bitstorotate == 0)
            return 0;

        c = 0x00000000;
        mask = (c >> bitstorotate);
        if (direction == ROTATE_RIGHT)
        {
            mask = (c >> (32 - bitstorotate));
            mask = ~mask;
        }
        else
            mask = (c >> bitstorotate);

        return mask;
    }


            /* @param value the value to be rotated right
	 * @param bitstorotate the number of bits to rotate right
	 * @param sizet the size of the resultant value (8, 16 or 32)
	 *
             * @return the value rotated right "bitstorotate" bits
	 */
    private static int rotr(int value, int bitstorotate, int sizet)
    {
        int tmprslt =0;
        int mask=0;;
        int target=0;

        bitstorotate %= sizet;
        target = value;

        // determine which bits will be impacted by the rotate
        mask = calcmask(bitstorotate, ROTATE_RIGHT);

        // save off the bits which will be impacted
        tmprslt = value & mask;

        // perform the actual rotate right
        target = (value  >> bitstorotate);

        // now rotate the saved off bits so they are in the proper place
        tmprslt <<= (sizet - bitstorotate);

        // now add the saved off bits
        target |= tmprslt;

        // and return the result
        return target;
    }

    private static int rotl(int value, int bitstorotate, int sizet)
    {
        int tmprslt =0;
        int mask=0;;
        int target=0;

        bitstorotate %= sizet;

        // determine which bits will be impacted by the rotate
        mask = calcmask(bitstorotate, ROTATE_LEFT);
        // shift the mask into the correct place (i.e. if we are delaying with a byte rotate, we
        // need to ensure we have the mask setup for a byte or 8 bits)
        mask >>>= (32 - sizet);

        // save off the affected bits
        tmprslt = value & mask;

        // perform the actual rotate
        target = (value  << bitstorotate);

        // now shift the saved off bits
        tmprslt >>>= (sizet - bitstorotate);

        // add the rotated bits back in (in the proper location)
        target |= tmprslt;

        // now return the result
        return target;
    }


    public static int rotr(int value, int bitstorotate)
    {
        return (rotr(value, bitstorotate, 32));
    }

    public static short rotr(short value, int bitstorotate)
    {
        short result;

        result = (short) rotr((0x0000ffff & value), bitstorotate, 16);

        return result;
    }

    public static char rotr(char value, int bitstorotate)
    {
        char result;

        result = (char) rotr((0x0000ffff & value), bitstorotate, 16);

        return result;
    }

    public static byte rotr(byte value, int bitstorotate)
    {
        byte result;

        result = (byte) rotr((0x000000ff & value), bitstorotate, 8);

        return result;

    }

    public static int rotl(int value, int bitstorotate)
    {
        return(rotl(value, bitstorotate, 32));
    }

    public static short rotl(short value, int bitstorotate)
    {
        short result;

        result = (short) rotl((0x0000ffff & value), bitstorotate, 16);

        return result;

    }

    public static char rotl(char value, int bitstorotate)
    {
        char result;

        result = (char) rotl((0x0000ffff & value), bitstorotate, 16);

        return result;

    }




    public static byte rotl(byte value, int bitstorotate)
    {
        byte result;

        result = (byte) rotl((0x000000ff & value), bitstorotate, 8);

        return result;

    }


    public static final byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }

    public static int charArrayToInt(char []data,int start,int end) throws NumberFormatException
    {
        int result = 0;
        for (int i = start; i <= end; i++)
        {
            int int1 = (int)data[i];


            int digit = int1 - (int)'0';
            if ((digit < 0) || (digit > 9))
            {

                throw new NumberFormatException();
            }
            result *= 10;
            result += digit;
        }
        return result;
    }

    public static int hexToInt(char a){
        int result;

        result = Character.digit(a,16);

        return result;
    }

    public static void main(String args[])
    {

        System.out.println("rotl32 22, 17 result: " + Rotate.rotl(22, 17));
        System.out.println("rotl16 22, 17 result: " + Rotate.rotl((short) 22, 17));
        System.out.println("rotl8 22, 17 result: " + Rotate.rotl((byte) 22, 17));

        System.out.println("rotr32 22, 4 result: " + Rotate.rotr(22, 4));
        System.out.println("rotr16 22, 4 result: " + Rotate.rotr((short) 22, 4));
        System.out.println("rotr8 22, 4 result: " + Rotate.rotr((byte) 22, 4));

        System.out.println("rotr8 -128, 7 result: " + Rotate.rotr((byte) -128, 7));
        System.out.println("rotr8 -1, 8 result: " + Rotate.rotr((byte) -1, 8));

    }

}