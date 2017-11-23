package tworunpos;


import com.mongodb.*;

public class TransactionList {


	private static DB db;
	private static DBCollection transactionCollection;

	// Singleton!
	static TransactionList instance;

	public TransactionList(){
		db = DatabaseClient.getInstance().getConnectedDB();
		transactionCollection = db.getCollection("transactionList");
	}



	/*
	This method will return a list of Transaction by its ID
	 */
	public static  void getTransactionsById(String barcode){

	}


	/*
	This method will return the latest transaction
	 */
	public static  Transaction getLastTransaction() throws Exception {
		Transaction transaction = null;
		DBCursor cursor = transactionCollection.find().sort(new BasicDBObject().append("_id",-1)).limit(1);

		if(  cursor.one() != null  ){
			transaction = new Transaction(cursor.one());
			DebugScreen.getInstance().print(transaction.getMyDocument().toString());
		}
		else{
			throw new Exception("Transaction not found");
		}
		return transaction;
	}


	/*
	This method will return a list of Transaction by count (newest ones, with shift)
	 */
	/*public static Transaction getTransactionsByCount(int count, int skip) throws Exception{

	*//*	DBCursor cursor = transactionCollection.find();
		if( cursor.hasNext() )
			DBObject obj = cursor.next();


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
			throw new Exception("BARCODE not found");
		}*//*
		
		
	}
	*/
	
	//DATABASE FUNCITONS ----------------------------------------------------


	/*
	 * this function will delete the whole collection. be careful
	 */
	public void drop(){
		transactionCollection.drop();
	}

	public long count(){
		return transactionCollection.count();
	}
	
	
	/*
	 * this function will insert a article to the article list.
	 * this means, its also added to the used database.
	 */
	public void addTransaction(Transaction transaction){
		DebugScreen.getInstance().print("DB: Start insert Transaction into collection");
		transactionCollection.insert(WriteConcern.SAFE,transaction.getMyDocument());
		DebugScreen.getInstance().print("DB: Insert done");
	}
	
	

	//Singleton get Instantce
	public static synchronized TransactionList getInstance () {
		if (TransactionList.instance == null) {
			TransactionList.instance = new TransactionList();
		}
		return TransactionList.instance;
	}
	
	
	
}
