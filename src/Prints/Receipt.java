package Prints;

import tworunpos.Cart;
import tworunpos.CartArticle;
import tworunpos.Config;
import tworunpos.Helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;



public class Receipt implements Print {

	
	
	//a receipt is designed in that way:
	/* --------------------
	 * TITLE
	 * --------------------
	 * HEAD
	 * --------------------
	 * BODY MESSAGES
	 * --------------------
	 * ARTICLES
	 * --------------------
	 * TAXES
	 * --------------------
	 * FOOTER 1
	 * --------------------
	 * BARCODE
	 * --------------------
	 * FOOTER 2
	 * --------------------
	 * 
	 */
    
	  
	//////////// printNormal() function ///////////////////////////////////////
    //   ESC + "|rA"          -> Right alignment
    //   ESC + "|cA"          -> Center alignment
    //   ESC + "|4C"          -> Double high double wide character printing
    //   ESC + "|bC"          -> normal-bold character printing // we call it just bold
    //   ESC + "|uC"          -> Underline character printing
	/////////////////////////////////////////////////////////////////////////////
	
    // constants defined for convience sake (could be inlined)
    private String ESC    = ((char) 0x1b) + "";
    private String LF     = ((char) 0x0a) + "";
    private String EURO   = ((char) 0xD5) + "";
    private String SPACES = "                                                                      ";


    private String title ="Bon";
    private String head = "";
    private String body = "";
    
    private Vector articles = new Vector();
    private String totalGross;
    private String totalNet;
    private String totalTax;
    private String paymentGiven;
    private String paymentChange;
    private String paymentType;
    
    private String taxesTitle ="Steuern";
    private String taxes;
    
    private String footer1 = "";
    private String footer2 = "";
    private String horizontalRuleSign = "-";
    
    private boolean useCurrencySymbol = Config.getInstance().isUseCurrencySymbolOnReceipt();
    private boolean useCurrencyName = Config.getInstance().isUseCurrencyNameOnReceipt();
    private String currencySymbol = "€";
    private String currencyName = "EUR";
    

    
	
    //char width of printer - default value
    private int charWidth = 42;

	
	public Receipt() {
		//currencySymbol = Config.getInstance().getCurrencySymbol();
		//if(currencySymbol == "€")
			currencySymbol = EURO;
	}


	
	public void addNewSimpleLineToBody(String line){
		body += (line + LF);
	}

	public void addNewSimpleboldLineToBody(String line){
		
	}


	public void addNewEmptyLineToBody(){
		
	}


	
	public String createNewFullLine(	String leftString,
			int leftStringMinSize, 
			int leftStringMaxSize, 
			String rightString,
			int rightStringMinSize, 
			int rightStringMaxSize, 
			String type){
		
			String line ="";
			int diffMaxSize = charWidth-(rightStringMaxSize+leftStringMaxSize);
			line = createNewFullLine(	
					leftString,	//		String leftString,
					leftStringMinSize,				//		int leftStringMinSize, 
					leftStringMaxSize,				//		int leftStringMaxSize, 
					"",			//		String middleString,
					diffMaxSize,				//		int middleStringMinSize, 
					diffMaxSize,				//		int middleStringMaxSize, 
					rightString,			//		String rightString,
					rightStringMinSize,				//		int rightStringMinSize,
					rightStringMaxSize,				//		int rightStringMaxSize, 
					type		//		String type
					);

			return line;	}
	
	
	/* 
	 * This method will create a line like this:
	 * 1 x Tomato                  12,00 €
	 * 
	 * type: 	normal|bold|big
	 */
	public String createNewFullLine(	String leftString,
								int leftStringMinSize, 
								int leftStringMaxSize, 
								String middleString,
								int middleStringMinSize, 
								int middleStringMaxSize, 
								String rightString,
								int rightStringMinSize,
								int rightStringMaxSize, 
								String type){
		
		
		//first cut the string to the max size of the width of printerpaper
		String endString = "";
		
		
		//define format/type
		String fontType = "";
		if(type=="bold"){
			fontType = ESC + "|bC"; 
		}else if(type=="big"){
			fontType = ESC + "|4C";
			if((leftStringMaxSize+rightStringMaxSize) > (charWidth/2)){
				leftStringMaxSize = (charWidth/4);
				rightStringMaxSize = (charWidth/4);
			}
			middleStringMaxSize = (charWidth/2)-(leftStringMaxSize+rightStringMaxSize);
			middleStringMaxSize = (middleStringMaxSize<0?0:middleStringMaxSize);
		}
		
		
		
		
		//trim leftString
		if(leftString.length() > leftStringMaxSize){
			leftString = leftString.substring(0,(leftStringMaxSize));			
		}else if(leftStringMaxSize > 0){
			leftString = fixedLengthString(leftString, leftStringMaxSize,  "left");
		}
		
		
		//trim middleString
		if(middleString.length() > middleStringMaxSize){
			middleString = middleString.substring(0,(middleStringMaxSize));			
		}else if(middleStringMaxSize > 0){
			middleString = fixedLengthString(middleString, middleStringMaxSize, "left");
		}
		
		
		
		
		//trim rightString
		if(rightString.length() > rightStringMaxSize){
			rightString = rightString.substring(0,(rightStringMaxSize));			
		}else if(rightStringMaxSize > 0){
			rightString = fixedLengthString(rightString, rightStringMaxSize, "right");
		}
		
		

		endString = fontType+leftString+middleString+rightString+LF;
		
		return endString;
	}
	
	/* 
	 * This method will create a line like this:
	 * ABC    ABC   ABC   ABC
	 * The columns will have equal width.
	 * 
	 * position: left|right
	 * type: 	normal|bold|big
	 */
	public String createNewFull4ColumnLine(	String string1,
								String position1,  
								String string2,
								String position2,
								String string3,
								String position3,
								String string4,
								String position4,
								String type){
		
		
		//define the with of each string - divide the general width of paper into 4
		int width = charWidth/4;
		
		
		//first cut the string to the max size of the width of printerpaper
		String endString = "";
		
		//trim leftString
		string1 = fixedLengthString(string1, width,  position1);
		string2 = fixedLengthString(string2, width,  position2);
		string3 = fixedLengthString(string3, width,  position3);
		string4 = fixedLengthString(string4, width,  position4);
		
		
		//define format/type
		String fontType = "";
		if(type=="bold"){
			fontType = ESC + "|bC"; 
		}else if(type=="big"){
			fontType = ESC + "|4C";
		}
		
		endString = fontType+string1+string2+string3+string4+LF;
		
		return endString;
	}
	
	
	public void addNewArticleLine(CartArticle article){
		//this method will create a line on a receipt from an article
		//Example :
		//Tomato KG                  0,99 €
		
		String line = "";
		
		
		String quantity = Float.valueOf(article.getQuantity()).toString();
		String name = article.getName();
		String price = prepareCurrencyForPrint( article.getPriceGrossTotal(), " "+ article.getLetterIdOfTaxGroup() );
		
		//define amount
		
		//amount = article.
		
		line = createNewFullLine(	
						quantity,	//		String leftString,
						3,				//		int leftStringMinSize, 
						3,				//		int leftStringMaxSize, 
						" x "+name,			//		String middleString,
						29,				//		int middleStringMinSize, 
						29,				//		int middleStringMaxSize, 
						price,			//		String rightString,
						10,				//		int rightStringMinSize,
						10,				//		int rightStringMaxSize, 
						"normal"		//		String type
						);
		
		
		articles.add(line);
		

	}
	
	public void setArticles(Cart cart){
		addNewEmptyLineToBody();
		for(int i = 0; i < cart.getCart().size();i++){
			addNewArticleLine(cart.getCart().get(i));
		}
		
		
		double totalGross =cart.getSumOfCartGross() ;
	    double totalNet =cart.getSumOfCartNet();
	    double totalTax = totalGross-totalNet;
	    double paymentGiven = cart.getPaymentGiven();
	    double paymentChange = paymentGiven-totalGross;
		
		
		this.totalGross = prepareCurrencyForPrint(totalGross, "");
	    this.totalNet = prepareCurrencyForPrint(totalNet,"");
	    this.totalTax = prepareCurrencyForPrint(totalGross-totalNet, "");
	    this.paymentGiven = prepareCurrencyForPrint(cart.getPaymentGiven(), "");
	    this.paymentChange = prepareCurrencyForPrint(paymentGiven-totalGross, "");
	    this.paymentChange = (paymentGiven-totalGross > 0 ?  "-"+this.paymentChange : this.paymentChange);
	    this.paymentType = cart.getPaymentType();
	    		
	}
	
	
	
	public String horizontalRule(){
		String hr = horizontalRuleSign;
		for(int i = 1; i < charWidth; i++){
			hr += horizontalRuleSign;
		}
		hr += LF;
		
		return hr;
	}
	
	public String getHead(){

		if(head != ""){
			return head+LF+horizontalRule();			
		}
		return head;
	}
	
	public String getBody(){
		if(body != ""){
			return body+LF+horizontalRule();			
		}
		return body;
	}
	
	
	public String getArticles(){
		
		
		//All Articles
		String articlesTemp = "";
		articlesTemp += createNewFullLine("", 15, 15, currencyName+"  ", 10, 10, "normal");
		for(int i = 0; i < articles.size(); i++ ){
			articlesTemp += articles.elementAt(i);
		}
		
		
		//Sums of receipt
		articlesTemp += horizontalRule();
		articlesTemp += createNewFullLine("Netto", 15, 15, totalNet, 10, 10, "normal");
		articlesTemp += createNewFullLine("Zzgl. MwSt.", 15, 15, totalTax, 10, 10, "normal");
		articlesTemp += createNewFullLine("zu zahlen", 15, 15, totalGross, 10, 10, "big");
		articlesTemp += horizontalRule();
		articlesTemp += createNewFullLine((!paymentType.isEmpty()?paymentType:"Gegeben"), 15, 15, paymentGiven, 10, 10, "bold");
		articlesTemp += createNewFullLine("Rückgeld", 15, 15, paymentChange, 10, 10, "bold");
		articlesTemp += LF;

		
		
		
		return articlesTemp;
	}
	
	public String getFooter1(){
		
		
		int twoColumnsWidth = charWidth/2;
		footer1 += createNewFullLine("Datum", twoColumnsWidth, twoColumnsWidth, "Kasse", twoColumnsWidth, twoColumnsWidth, "normal");
		
		Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("d.M.y HH:mm:ss");
        String dateAndTime =  sdf.format(cal.getTime());
        footer1 += createNewFullLine(dateAndTime, twoColumnsWidth, twoColumnsWidth, "X", twoColumnsWidth, twoColumnsWidth, "normal");

		
		if(footer1 != ""){
			return footer1+LF+horizontalRule();			
		}

		return footer1;
	}
	
	
	public String getFooter2(){
				
		footer2 = createNewCenteredLine("Vielen Dank für Ihren Einkauf :)", "bold");
		
		if(footer2 != ""){
			
			return footer2+LF;			
		}
		return footer2;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * 
	 * This method prints the final receipt
	 */
	public String toString(){
		String wholeReceipt = "";
		wholeReceipt += getTitle();
		wholeReceipt += getHead();
		wholeReceipt += getBody();

		wholeReceipt += getArticles();
		wholeReceipt += getTaxes();

		wholeReceipt += getFooter1();
		wholeReceipt += getFooter2();

		return wholeReceipt;
	}
	
	
	public static String fixedLengthString(String string, int length, String leftOrRight) {
		String lor = "";
		if(leftOrRight == "left"){
			lor = "-";
		}
		
	    return String.format("%"+lor+length+ "."+length+ "s", string);
	}
	
	public String prepareCurrencyForPrint(double value, String suffix){
		String tempCurrencyString = Helpers.roundForCurrencyTwoDigits(value);
		if(useCurrencySymbol == true)
			tempCurrencyString += " "+currencySymbol;
		if(useCurrencyName == true)
			tempCurrencyString += " "+currencyName;
		
		tempCurrencyString += suffix;
		return tempCurrencyString;
	}



	@Override
	public String getTitle() {
		return createNewCenteredLine(this.title, "bold")+LF;
	}
	
	
	public void setTaxes(Cart cart){
		taxes ="";
		Double vatTotalSum = 0.00D;
		Double netTotalSum = 0.00D;
		Double grossTotalSum = 0.00D;
		
		//title
		taxes += createNewCenteredLine(taxesTitle, "normal");
		
		//headline
		taxes += createNewFull4ColumnLine("MWST%",
				"left",
				"MWST",
				"right",
				"+  Netto",
				"right",
				"=  Brutto",
				"right",
				"normal");
		
		//groupedtaxes
		for(int i=0;i<cart.getGroupedTaxes().size();i++){
			
			//sum up 
			vatTotalSum += cart.getGroupedTaxes().get(i).vatSum;
			netTotalSum += cart.getGroupedTaxes().get(i).netSum;
			grossTotalSum += cart.getGroupedTaxes().get(i).grossSum;
			
			//create line for groupedtaxes
			taxes += createNewFull4ColumnLine(cart.getGroupedTaxes().get(i).letterID+" "+cart.getGroupedTaxes().get(i).value+"%",
					"left",
					prepareCurrencyForPrint(cart.getGroupedTaxes().get(i).vatSum,""),
					"right",
					prepareCurrencyForPrint(cart.getGroupedTaxes().get(i).netSum,""),
					"right",
					prepareCurrencyForPrint(cart.getGroupedTaxes().get(i).grossSum,""),
					"right",
					"normal");
		}
		
		
		//summ
		taxes += horizontalRule();
		taxes += createNewFull4ColumnLine("Summe",
				"left",
				prepareCurrencyForPrint(vatTotalSum,""),
				"right",
				prepareCurrencyForPrint(netTotalSum,""),
				"right",
				prepareCurrencyForPrint(grossTotalSum,""),
				"right",
				"normal");
		
	}
	
	public String getTaxes() {
		return taxes+LF;
	}

	
	public String createNewCenteredLine(String text, String type) {
		
		int leftSpace = (charWidth-text.length())/2;
		String temp = fixedLengthString( text, leftSpace+text.length(), "right");
		
		//define format/type
		String fontType = "";
		if(type=="bold"){
			fontType = ESC + "|bC"; 
		}
		
		return fontType+temp+LF;
	}
	
	
}