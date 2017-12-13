package tworunpos;


import com.mongodb.*;

import java.util.ArrayList;

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
	public static ArrayList<Transaction> getTransactions(int count, int skip) throws Exception{

		ArrayList<Transaction> transactionsList = null;

		try{
			DBCursor cursor = transactionCollection.find().sort(new BasicDBObject().append("_id",-1)).skip(skip).limit(count);
				while(cursor.hasNext()){
					Transaction temp = new Transaction(cursor.next());
					if(transactionsList == null)
						transactionsList = new ArrayList<>();
					transactionsList.add(temp);

				}

		}catch(Exception msg){
			throw new Exception(msg);
		}

		return transactionsList;
		
	}

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
