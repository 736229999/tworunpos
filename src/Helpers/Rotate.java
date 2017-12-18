package Helpers;

import static java.lang.Character.digit;

public final class Rotate {

    public static final int ROTATE_LEFT = 1;
    public static final int ROTATE_RIGHT = 2;

    public static char rotl(char value, int bitstorotate)
    {

        if(bitstorotate == 0)
            return value;

        char tmprslt =0;
        char tmprslt2 =0;
        char mask=0;;
        char target=0;
        int sizet = 16;

        bitstorotate %= sizet;

        // determine which bits will be impacted by the rotate
        //mask = calcmask(bitstorotate, ROTATE_LEFT);
        //own mask method
        mask = 0xFFFF;
        mask = (char)((mask & 0xFFFF) >> bitstorotate);
        mask = (char)~mask;


        // save off the affected bits
        tmprslt = (char)(value & mask);

        // perform the actual rotate
        target = (char)((value & 0xffff)  << bitstorotate);

        // now shift the saved off bits
        int rotateBack = (sizet - bitstorotate);
        tmprslt2 = (char)( (tmprslt ) >> rotateBack);


        // add the rotated bits back in (in the proper location)
        target = (char) (target | tmprslt2);

        // now return the result
        return (char)target;
    }



    public static char rotr(char value, int bitstorotate)
    {

        if(bitstorotate == 0)
            return value;

        char tmprslt =0;
        char tmprslt2 =0;
        char mask=0;;
        char target=0;
        int sizet = 16;

        bitstorotate %= sizet;

        // determine which bits will be impacted by the rotate
        //mask = calcmask(bitstorotate, ROTATE_LEFT);
        //own mask method
        mask = 0xFFFF;
        mask = (char)((mask & 0xFFFF) << bitstorotate);
        mask = (char)~mask;


        // save off the affected bits
        tmprslt = (char)(value & mask);

        // perform the actual rotate
        target = (char)((value & 0xffff)  >> bitstorotate);

        // now shift the saved off bits
        int rotateBack = (sizet - bitstorotate);
        tmprslt2 = (char)( (tmprslt ) << rotateBack);


        // add the rotated bits back in (in the proper location)
        target = (char) (target | tmprslt2);

        // now return the result
        return (char)target;
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




}