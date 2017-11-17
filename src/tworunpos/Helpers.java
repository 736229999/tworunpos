package tworunpos;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;

public final class Helpers {

	static String getStackTraceAsString(Exception e){

		StringWriter stringWriter = null;
		PrintWriter printWriter = null;
		if(stringWriter == null || printWriter == null){
			stringWriter = new StringWriter();
		
			printWriter = new PrintWriter((Writer) stringWriter);
		}
		e.printStackTrace(printWriter);
		return  stringWriter.getBuffer().toString();		
	}
	
	
	public static String roundForCurrencyTwoDigits(double value){
		return String.format("%.2f", round(value,2));
	}
	
	public static double roundForCurrency(double value){
		return round(value,2);
	}
	
	public static double round(double value, int places){
	    if (places < 0) throw new IllegalArgumentException();
	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
}
