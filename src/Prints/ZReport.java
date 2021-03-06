package Prints;

import tworunpos.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;


public class ZReport implements Print {



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

    private ZSession zSession;

    private String title ="Z-Bericht";
    private String head = "";
    private String body = "";

    private Vector articles = new Vector();
    private String totalGross;
    private String totalNet;
    private String totalTax;
    private String paymentGiven;
    private String paymentChange;
    private String paymentType;
    private Integer zNo;
    private Integer transactionNo;
    private Integer posNo;

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


	public ZReport() {
		//currencySymbol = Config.getInstance().getCurrencySymbol();
		//if(currencySymbol == "€")
			currencySymbol = Config.getInstance().getCurrencySymbol();
			currencyName = Config.getInstance().getCurrencySymbolLatin();
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
		else {
			fontType = ESC +"|1C";
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
		}else {
			fontType = ESC +"|1C";
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
	
	public void setZSession(ZSession z){
		zSession = z;
	}
	


	
	public String horizontalRule(){
		String hr = ESC +"|1C"+horizontalRuleSign;
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

		String userOpened = (zSession.getSesssionOpenedByUserId() != null ? zSession.getSesssionOpenedByUserId().toString():"");
		String userClosed = (zSession.getSesssionClosedByUserId() != null ? zSession.getSesssionClosedByUserId().toString():"");

		SimpleDateFormat sdf = new SimpleDateFormat("d.M.y HH:mm:ss");


		String datetimeClosed = (zSession.getDateTimeAtEndSession() != null ? sdf.format(zSession.getDateTimeAtEndSession()):"");
		String datetimeOpened = (zSession.getDateTimeAtStartSession() != null ? sdf.format(zSession.getDateTimeAtStartSession()):"");

		body += createNewFullLine("Z-Nummer", 15, 15, zSession.getCounter().toString(), 27, 27, "bold");
		body += createNewFullLine("Gestartet von", 15, 15, userOpened, 27, 27, "normal");
		body += createNewFullLine("", 15, 15, datetimeOpened, 20, 20, "normal");
		body += createNewFullLine("Beendet von", 15, 15, userClosed, 27, 27, "normal");
		body += createNewFullLine("", 15, 15, datetimeClosed, 20, 20, "normal");

		return body+LF+horizontalRule();

	}
	
	
	public String getArticles(){
		
		
		//All Articles
		String articlesTemp = "";

		
		return articlesTemp;
	}
	
	public String getFooter1(){
		
		
		int twoColumnsWidth = charWidth/2;
		footer1 = createNewFullLine("Datum", twoColumnsWidth, twoColumnsWidth, "", twoColumnsWidth, twoColumnsWidth, "normal");
		
		Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("d.M.y HH:mm:ss");
        String dateAndTime =  sdf.format(cal.getTime());



        footer1 += createNewFullLine(dateAndTime, twoColumnsWidth, twoColumnsWidth, "", twoColumnsWidth, twoColumnsWidth, "normal");

		
		if(footer1 != ""){
			return footer1+LF+horizontalRule();			
		}

		return footer1;
	}
	
	
	public String getFooter2(){
				
		footer2 = createNewCenteredLine("", "bold");
		
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