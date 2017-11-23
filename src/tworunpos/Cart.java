package tworunpos;

import Devices.JPosDeviceManager;
import Devices.JPosPrinter;
import Exceptions.CheckoutGeneralException;
import Exceptions.CheckoutPaymentException;
import Prints.Receipt;
import com.mongodb.*;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.Vector;



public class Cart extends Observable {
	
	// Eine (versteckte) Klassenvariable vom Typ der eigene Klasse
	private static Cart instance = null;
	
	private Vector<CartArticle> articles = new Vector<CartArticle>();
	private DB db = DatabaseClient.getInstance().getConnectedDB();
	private DBCollection transactionCollection;
	
	
	private Date dateTimeAtStartTransaction;
	private Date dateTimeAtEndTransaction;
	
	
	//vars to be filled to be able to checkout
	private String paymentType;
	private Double paymentGiven;
	private String transactionType;
	private Integer transactionZ;
	private Double transactionAmountGross;
	private Double 	transactionAmountNet;
	private Integer cashierId;

	//receipt to be printed out
	private Receipt receipt = new Receipt();
	
	
	//groupedTaxes
	char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	ArrayList<GroupedTaxStruct> groupedTaxes;
	public class GroupedTaxStruct {
		  public char letterID;
		  public String description;
		  public Double value;
		  
		  public Double vatSum;
		  public Double netSum;
		  public Double grossSum;
		  
		  //letterID = A,B,C...
		  //description = description of the tax e.g MwSt. 7%
		  //value = value of the tax in number e.g. 7
		  //vatSum = sum of the vat included in gross 
		  //netSUm = net value included in gross
		  //grossSum = net + vat
		  public GroupedTaxStruct(char letterID, String description, Double value, Double vatSum, Double netSum, Double grossSum) {
			this.letterID = letterID;
			this.description = description;
		    this.value = value;
		    this.vatSum = vatSum;
		    this.netSum = netSum;
		    this.grossSum = grossSum;
		  }
		  public void addValue(Double vatSum, Double netSum, Double grossSum){
			  this.vatSum += vatSum;
			  this.netSum += netSum;
			  this.grossSum += grossSum;
		  }

	}


	public Cart(DBObject cartObject){

		//DebugScreen.getInstance().print(articleDbObject.get("plu").toString());
		transactionZ = (cartObject.get("transactionZ") != null ? (Integer) cartObject.get("transactionZ"):null);
		dateTimeAtStartTransaction =  (cartObject.get("dateTimeAtStartTransaction") != null ?  (Date) ( cartObject.get("dateTimeAtStartTransaction")) :null);
		dateTimeAtEndTransaction = (cartObject.get("dateTimeAtEndTransaction") != null ?  (Date) cartObject.get("dateTimeAtEndTransaction") :null);
		transactionType = (cartObject.get("transactionType") != null ?   cartObject.get("transactionType").toString() : null );
		transactionAmountGross = (cartObject.get("transactionAmountGross") != null ?   (Double) cartObject.get("transactionAmountGross") : null );
		transactionAmountNet =  (cartObject.get("transactionAmountNet") != null ? (Double) cartObject.get("transactionAmountNet"):null);
		cashierId =  (cartObject.get("cashierId") != null ? (Integer) cartObject.get("cashierId"):null);

		//create lines of transaction
		BasicDBList articlesDBList = (BasicDBList) cartObject.get("transactionLines");
		BasicDBObject[] articlesArray = articlesDBList.toArray(new BasicDBObject[0]);
		for(BasicDBObject dbObj : articlesArray) {
			// add each item from the articlesArray to articles List
			CartArticle ca = new CartArticle(dbObj);
			articles.add(ca);
		}



	}

	public Cart(){
		
		dateTimeAtStartTransaction = new Date();
		transactionCollection = db.getCollection("transactionList");
	}
	

	public Vector<CartArticle> getArticles(){
		return articles;
	}
	
	
	public void addArticleByBarcode(String barcode, float quantity) throws Exception{

		ArticleList articleList = ArticleList.getInstance();
			try {
				if(!barcode.matches("[0-9]+") || barcode.length() < 8){
					DebugScreen.getInstance().print("Invalid EAN-Barcode");
					throw new Exception("Invalid EAN-Barcode");
				}
				
				DebugScreen.getInstance().print("try to find Barcode: "+barcode);
				try{
					Article tempArticle = articleList.lookupArticleByBarcode(barcode);
					DebugScreen.getInstance().print("ArticleFoundByBarcode: "+tempArticle.getMyDocument());
					
					//check again if quantity buffer changed by weighbarcode
					if(quantity != Buffer.getInstance().posQuantityBuffer)
						quantity = Buffer.getInstance().posQuantityBuffer;
					
					this.addArticle(tempArticle, quantity);
				}catch (Exception e) {
					DebugScreen.getInstance().print("Article not found by Barcode: "+barcode);
					//DebugScreen.getInstance().printStackTrace(e);
					throw new Exception("Artikel nicht gefunden");
				}	
				
				
			} catch (Exception e) {
				barcode = "";
				DebugScreen.getInstance().print(e.getMessage());
				DebugScreen.getInstance().printStackTrace(e);
				throw new Exception("Artikel nicht gefunden");
			}
			
			
			PosState.getInstance().changeStateToSellingProcess(false);
		
	}
	
	//this method can add refund or a simple article to sell to the cart
	// the type, if its a refund or a sell depends on the state of the pos
	public void addArticle(Article articleA, float quantity) throws Exception{
		DebugScreen.getInstance().print("--- --- --- --- --- ---");
		DebugScreen.getInstance().print("addArticle into Cart");
		setChanged();
		


		
		
		
		//check quantity
		DebugScreen.getInstance().print("unit: "+articleA.getUnit().getUnit()+"   isPiece: "+articleA.getUnit().isPiece()+"    quantity%1 = "+(quantity%1));
		if(articleA.getUnit().isPiece() && (quantity%1) != 0 ){
			throw new Exception("Ungültige Artikelanzahl");
		}
		
		//first, convert the article to CartArticle.
		//with that, we will be able to extend some cart specific attributes
		CartArticle article =  articleA.toCartArticle();
		article.setQuantity(quantity);
		
		//check if in refund mode - and add article to refund
		if(PosState.getInstance().getState() == PosState.posStateRefund){
			addRefundByArticle(article);
		}
		
		//Refund the deposit of an artice
		else if(PosState.getInstance().getState() == PosState.posStateRefundDeposit){
			addRefundDepositeOfArticle(article);
		}
		//its a deposit i try to sell? no way, refund it as it is
		else if(article.isDeposit()){
			addRefundByArticle(article);
		}
		
		//usual article, so simple
		else{
			addSimpleArticle(article);
		}
		
	}
	
	
	//this method adds an usual article to the cart
	public void addSimpleArticle(CartArticle article){
		//SIMPLE ARTICLE INTO CART
				//add this article to the cart vector
				articles.add(article);
				notifyObservers( new Object[]{"add", article} );			
			

				//RELATED ARTICLE INTO CART
				//check if there is some related article (e.g. deposite)
				if(article.hasDeposit()){
					System.out.println("Identified as deposite article. getHasDeposite: "+article.getHasDeposit()+". getDepositBarcode: "+article.getDepositBarcode() );
					try {
						setChanged();
						CartArticle depositArticle = ArticleList.lookupArticleByBarcode(article.getDepositBarcode()).toCartArticle();
						System.out.println("deposite: "+depositArticle);
						articles.add(depositArticle);
						notifyObservers( new Object[]{"add", depositArticle} );
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
	}
	
	
	
	//this method only adds the deopsit of an article as refund to the cart
	//example: scan the empty bottle and get the deposit back
	public void addRefundDepositeOfArticle(CartArticle article){
		
		
		//getr deposit artice
		Article deposit;
		if(article.hasDeposit())
		{			
			try {
				deposit =	ArticleList.lookupArticleByBarcode(article.getDepositBarcode());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				GuiElements.displayErrorMessageBox("Kein Pfandartikel");
				e.printStackTrace();
				return;
			}
		}else{
			return;
		}
	
		addRefundByArticle(deposit.toCartArticle());
	}
	
	
	//adds any article as refund to the cart
	public void addRefundByArticle(CartArticle article){
		setChanged();
		article.setPriceGross(-article.getPriceGross());
		article.setPriceNet(-article.getPriceNet());
		article.setIsRefund(true);
		articles.add(article);
		notifyObservers( new Object[]{"removeByArticleWithNotification", article}  );
	}
	
	
	
	public void removeArticlebyArticle(CartArticle article){
		setChanged();
		articles.remove(article);
		notifyObservers( new Object[]{"removeByArticle", article}  );
	}

	
	public void removeArticlebyPosition(int position){
		setChanged();
		Article article = articles.get(position);
		articles.remove(position);
		notifyObservers( new Object[]{"removeByPosition", position, article}  );

		
		
		//remove also related article (deposit)
		if(article.hasDeposit()){
			String articlesDepositBarcode = article.getDepositBarcode();
			for(int i = position; i < articles.size(); i++){
				Article tempDepositeArticle = articles.get(position);
				if(tempDepositeArticle.getBarcode().equals(articlesDepositBarcode)){
					setChanged();
					articles.remove(position);
					notifyObservers( new Object[]{"removeByPosition", position, tempDepositeArticle}  );

				}
			}
		}
		
		
		
	}
	
	public void clear(){
		removeAllArticles();
	}
	public void drop(){
		removeAllArticles();
	}

	public void removeAllArticles(){
		setChanged();
		articles.clear();
		notifyObservers( new Object[]{"removeAllArticles"}  );
	}
	
	public CartArticle getArticleFromCart(int position){
		
		return  (CartArticle) articles.get(position);
	}
	
	
	public void dublicateLastArticleInCart() throws Exception{
		if(articles.size()<1)
			return;
		
		addArticle(articles.get(articles.size()-1), 1);
		
	}
	
	public Double getSumOfCartGross(){
		Double sum = 0.00D;
		for (int i=0; i < articles.size(); i++)
			sum = (Double) (sum + articles.elementAt(i).getPriceGrossTotal());
		
		return  sum;
	}
	
	
	public Double getSumOfCartNet(){
		Double sum = 0.00D;
		for (int i=0; i < articles.size(); i++)
			sum = Double.sum( sum , articles.elementAt(i).getPriceNetTotal() );
		return sum;
	}
	
	public void createGroupedTaxes(){
		/*
		 * THis method will create a multidimensional ArrayList (embedded Struct into Arraylist)
		 * to group tax sums in classes like:
		 * 7% : 87,34
		 * 19% : 223,33
		 */
		
		DebugScreen.getInstance().print("Cart: Start creating grouped taxes");
		
		groupedTaxes = new ArrayList<GroupedTaxStruct>();

		//iterate over cart to check all articles
		for (int i=0; i < articles.size(); i++){
			String name = articles.get(i).getVatPercentage()+"";
			Double value =  articles.get(i).getVatPercentage();
			Double vatAmount = articles.get(i).getVatAmountTotal();
			Double netAmount = articles.get(i).getPriceNetTotal();
			Double grossAmount = articles.get(i).getPriceGrossTotal();
			char letterId = '\0';
			
			//group them
			boolean done = false;
			
			//lookup for existing group of the tax value
			for(int a = 0;a < groupedTaxes.size(); a++){
				DebugScreen.getInstance().print("Lookup for taxgroup:"+groupedTaxes.get(a).value+" == "+value);
				if(groupedTaxes.get(a).value.equals(value)){
					groupedTaxes.get(a).addValue(vatAmount, netAmount, grossAmount);
					done = true;
					DebugScreen.getInstance().print("found tax group: "+groupedTaxes.get(a).value);
					
					//temp save to write it back to cartarticle
					letterId = groupedTaxes.get(a).letterID;
					
				}
			}
			
			//no group existing? so create a new one
			if(done != true){
				//temp save to write it back to cartarticle and use for the new group
				letterId = this.alphabet[groupedTaxes.size()];
				groupedTaxes.add(new GroupedTaxStruct(letterId ,name, value, vatAmount, netAmount, grossAmount));

				done = true;
				DebugScreen.getInstance().print("new tax group created for: "+name+" ("+value+") Article i:"+i);
			}
	
			//Since we are here and defined the letterId, we will write back the letterId back into CartArticle as well.
			articles.get(i).setLetterIdOfTaxGroup(letterId);
			
		}
		DebugScreen.getInstance().print("End creating grouped taxes");
	}
	

	public ArrayList<GroupedTaxStruct> getGroupedTaxes() {
		return groupedTaxes;
	}
	
	
	public void debug(){
		System.out.println("Cartsize: "+articles.size());
		
		for(int i = 0; i < articles.size();i++){
			System.out.println(i+": "+articles.elementAt(i).toString());
		}
		
	}


	/*
	This method is the final checkout step! It will save the transaction to the database.
	 */
	public Boolean finilize() throws CheckoutPaymentException,CheckoutGeneralException {

		//check if everthing is ok

		//check if articles are in the cart
		if (articles.isEmpty()) {
			throw new CheckoutGeneralException("Bitte zuerst Artikel einscannen/eingeben!");
		}


		//check payment details
		if (paymentType == null) {
			throw new CheckoutPaymentException("Bitte eine Zahlungsart wählen!");
		}

		//check if amount is positive or not // if not it means we have to do a payout
		if (this.getSumOfCartGross() <= 0) {

		}

		if (this.getSumOfCartGross() > 0 && (paymentGiven == null || paymentGiven <= 0)) {
			throw new CheckoutPaymentException("Bitte einen Zahlungsbetrag eingeben!");
		}

		if (Helpers.roundForCurrency(paymentGiven) < Helpers.roundForCurrency(this.getSumOfCartGross())) {
			throw new CheckoutPaymentException("Der Zahlbetrag ist nicht hoch genug!!");
		}

		//prepare for receipt printout
		this.createGroupedTaxes();
		DebugScreen.getInstance().clear();
		DebugScreen.getInstance().print("Start Print Receipt");
		receipt.setArticles(this);
		receipt.setTaxes(this);

		//save transaction in db
		dateTimeAtEndTransaction = new Date();
		Transaction transaction = new Transaction(this);
		TransactionList transactionList = TransactionList.getInstance();
		transactionList.addTransaction(transaction);

		//BasicDBObject transactionObject = getMyDocument();
		//transactionCollection.insert(transactionObject);




		//print receipt
		JPosPrinter printer = null;
		try {
			printer = JPosDeviceManager.getInstance().getPrinter();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (printer != null) {
			try {
				printer.print(receipt.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		DebugScreen.getInstance().print(receipt.toString());
		DebugScreen.getInstance().print("End Print Receipt");

		return true;


	}
	
	/*
	 * This method will return the document object for a mongodb entry
	 */
	public BasicDBObject getMyDocument(){
		
		//create basic transaction attributes
		BasicDBObject mainDocument = new BasicDBObject();
		mainDocument.put("transactionZ", 1); //todo bind to Z-Number
		mainDocument.put("transaction", this.dateTimeAtStartTransaction);
		mainDocument.put("transactionDateTimeAtStart", this.dateTimeAtStartTransaction);
		mainDocument.put("transactionDateTimeAtEnd", this.dateTimeAtEndTransaction);
		mainDocument.put("transactionType", "POS"); //todo
		mainDocument.put("transactionAmountGross", this.getSumOfCartGross());
		mainDocument.put("transactionAmountNet", this.getSumOfCartNet());
		mainDocument.put("cashierId", 1); //todo program cashiers
			
		//create lines of transaction
		BasicDBList  lines = new BasicDBList();	
		for(int i = 0; i < articles.size();i++){
			lines.add(articles.elementAt(i).getMyDocument() );
		}
		mainDocument.put("transactionLines", lines);
		
/*
		//payment info
		BasicDBObject paymentInfo = new BasicDBObject();		
		paymentInfo.put("paymentType", paymentType);
		paymentInfo.put("paymentGiven", paymentGiven);
		paymentInfo.put("paymentRest", 123); //todo
		mainDocument.put("paymentInfo", paymentInfo);
*/

		
		return mainDocument;
	}


	public void setPaymentType(String string) {
		this.paymentType = string;
	}

	public String getPaymentType() {
		return paymentType;
	}
	


	public Double getPaymentGiven() {
		return paymentGiven;
	}


	public void setPaymentGiven(Double paymentGiven) {
		this.paymentGiven = paymentGiven;
	}
	

	public void setPaymentGiven(String getText) throws ParseException {
		
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		DecimalFormat format = new DecimalFormat("0.##");
		format.setDecimalFormatSymbols(symbols);
		Double oldVal = format.parse(getText).doubleValue();
		
		this.paymentGiven = oldVal;
	}

	public Receipt getReceipt() {
		return receipt;
	}

	public Integer getCountOfReturnedArticles() {
		Integer count = 0;
		for (int i=0; i < articles.size(); i++){
			if(articles.elementAt(i).isRefund())
				count++;
		}
		return count;
	}

	public Double getSumOfReturnedArticlesInclTax() {
		Double sum = 0.00;
		for (int i=0; i < articles.size(); i++){
			if(articles.elementAt(i).isRefund())
				sum =+ articles.elementAt(i).getPriceGrossTotal();
		}
		return sum;
	}

	public Double getSumOfReturnedArticlesExclTax() {
		Double sum = 0.00;
		for (int i=0; i < articles.size(); i++){
			if(articles.elementAt(i).isRefund())
				sum =+ articles.elementAt(i).getPriceNetTotal();
		}
		return sum;
	}

	public Date getDateTimeAtStartTransaction() {
		return dateTimeAtStartTransaction;
	}

	public Date getDateTimeAtEndTransaction() {
		return dateTimeAtEndTransaction;
	}

	public void reset(){
		removeAllArticles();
		instance = null;
		instance = new Cart();
	}
	
	//Singleton get Instantce
	public static  Cart getInstance() {
		if (instance == null ) {
			DebugScreen.getInstance().print("new cart created - getInstance()");
			instance = new Cart ();
		}
		return instance;
	}

	
	


}
