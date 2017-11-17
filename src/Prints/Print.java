package Prints;

public interface Print {
/* all possible printouts from the receiptprinter should be an isntance of this interafce */

	public String getTitle();
	public String getBody();
	public String getFooter1();
	public String getFooter2();
}
