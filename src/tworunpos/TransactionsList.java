package tworunpos;


import com.mongodb.*;

public class TransactionsList {


	private static DB db;
	private static DBCollection transactionCollection;

	// Singleton!
	static TransactionsList instance;

	public TransactionsList(){
		db = DatabaseClient.getInstance().getConnectedDB();
		transactionCollection = db.getCollection("transactionsList");
		

	}

	/*
	This method will return a list of Transaction between two datetimes
	 */
	/*public static Transaction lookupTransactionsByDate() throws Exception{

		//TODO replace for transaction
		BasicDBObject query = new BasicDBObject("plu", plu);
		DBObject foundDocument = transactionCollection.findOne(query);

		DebugScreen.getInstance().print("query: "+query.toString()+"   db: "+db.getName() + "     collection: "+transactionCollection.getName());


		if(foundDocument != null){
			DebugScreen.getInstance().print("FOUND Article by PLU: "+foundDocument.toString());
			DebugScreen.getInstance().print("Start Looking Up by Barcode"+foundDocument.get("barcode").toString());
			return lookupArticleByBarcode(foundDocument.get("barcode").toString());
		}
		else{
			DebugScreen.getInstance().print("Throw Exception by lookupArticlebyPlu");
			throw new Exception("PLU nicht gefunden");
		}


	}*/


	/*
	This method will return a list of Transaction by its ID
	 */
	public static  void lookupTransactionsById(String barcode){

	}

	/*
	This method will return a list of Transaction by its ID
	 */
	public static  void getLastTransaction(){

	}


	/*
	This method will return a list of Transaction by count (newest ones, with shift)
	 */
	public static Article lookupTransactionsByCount(String barcode) throws Exception{
		DebugScreen.getInstance().print("lookupArticleByBarcode in ArticleList...");

		//check, if barcode is a weighBarcode, that means, it includes the weight given in gram of the article.
		//this should be interpreted as the quantity
		// AA BBBB P CCCCC P
		// A = Barcodeprefix, which shows if its a weighBarcode or not. Usually 29 or 28 is used
		// B = Articlecode
		// P = Checksum
		// C = quantity in grams

		if(Article.isWeighArticleByBarcode(barcode)){
			
			String quantity = barcode.substring(8,12);
			if(quantity != ""){
				Buffer.getInstance().setPosQuantityBuffer(Float.parseFloat(quantity)/1000);
			}
			
			barcode = barcode.substring(0,7);

		}
		//TODO
		
		BasicDBObject query = new BasicDBObject("barcode", barcode);
		DBObject foundDocument = transactionCollection.findOne(query);
		DebugScreen.getInstance().print("query: "+query.toString());
		DebugScreen.getInstance().print("db: "+db.getName());
		DebugScreen.getInstance().print("collection: "+transactionCollection.getName());

		
		if(foundDocument != null){
			System.out.println("FOUND Article: "+foundDocument.toString());
			System.out.println("Start Converting Doc to Article");
			Article foundArticle = new Article(foundDocument);
			DebugScreen.getInstance().print("Converted Article Name:"+foundArticle.getName());
			System.out.println("Finished Converting Doc to Article");
			
			return foundArticle;
		}
		else{
			throw new Exception("BARCODE nicht gefunden");
		}
		
		
	}
	
	
	//DATABASE FUNCITONS ----------------------------------------------------


	/*
	 * this function will delete the whole collection. be careful
	 */
	public void drop(){
		transactionCollection.drop();
	}
	
	public long countAll(){
		return transactionCollection.count();
	}
	
	
	/*
	 * this function will insert a article to the article list.
	 * this means, its also added to the used database.
	 */
	public void addTransaction(Transaction transaction){
		//todo upsert

		System.out.println(transaction.getMyDocument().toString());

		DebugScreen.getInstance().print("DB: Start insert article into collection");
		transactionCollection.insert(WriteConcern.SAFE,transaction.getMyDocument());
		DebugScreen.getInstance().print("DB: Insert done");
	}
	
	

	//Singleton get Instantce
	public static synchronized TransactionsList getInstance () {
		if (TransactionsList.instance == null) {
			TransactionsList.instance = new TransactionsList();
		}
		return TransactionsList.instance;
	}
	
	
	
}
