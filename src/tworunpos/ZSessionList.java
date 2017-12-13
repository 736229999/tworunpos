package tworunpos;


import com.mongodb.*;

import java.util.ArrayList;

public class ZSessionList {


	private static DB db;
	private static DBCollection collection;
	private static ZSession currentZSession;

	// Singleton!
	static ZSessionList instance;

	public ZSessionList(){
		db = DatabaseClient.getInstance().getConnectedDB();
		collection = db.getCollection("zSessionList");
	}



	/*
	This method will return a list of Transaction by its ID
	 */
	public static  void getZSessionById(String id){

	}


	public static void startNewZSession(){

		if(currentZSession != null){
			//find the next id



			//give it to the new session
			ZSession tmpZSession;
			tmpZSession = new ZSession();
			//tmpZSession.setId();
		}
	}


	/*
	This method will return the latest transaction
	 */
	public static  ZSession getCurrentZSession() throws Exception {
		ZSession zSession = null;


		BasicDBObject searchObject = new BasicDBObject();
		searchObject.put("dateTimeAtEndSession", "");
		DBCursor cursor = collection.find(searchObject);

		if(  cursor.one() != null  ){
			zSession = new ZSession(cursor.one());
			zSession = new ZSession(cursor.one());
			DebugScreen.getInstance().print(zSession.getMyDocument().toString());
		}
		else{
			throw new Exception("No open Z-Session available.");
		}
		return zSession;
	}


	/*
	This method will return a list of Transaction by count (newest ones, with shift)
	 */
	public static ArrayList<ZSession> getTransactions(int count, int skip) throws Exception{

		ArrayList<ZSession> zSessionList = null;

		try{
			DBCursor cursor = collection.find().sort(new BasicDBObject().append("_id",-1)).skip(skip).limit(count);
				while(cursor.hasNext()){
					ZSession temp = new ZSession(cursor.next());
					if(zSessionList == null)
						zSessionList = new ArrayList<>();
					zSessionList.add(temp);

				}

		}catch(Exception msg){
			throw new Exception(msg);
		}

		return zSessionList;
		
	}

	//DATABASE FUNCITONS ----------------------------------------------------


	/*
	 * this function will delete the whole collection. be careful
	 */
	public void drop(){
		collection.drop();
	}

	public long count(){
		return collection.count();
	}
	
	
	/*
	 * this function will insert a transaction to the transaction list.
	 * this means, its also added to the used database.
	 */
	public void addZSession(ZSession zSession){
		DebugScreen.getInstance().print("DB: Start insert Transaction into collection");
		collection.insert(WriteConcern.SAFE,zSession.getMyDocument());
		DebugScreen.getInstance().print("DB: Insert done");
	}
	
	

	//Singleton get Instantce
	public static synchronized ZSessionList getInstance () {
		if (ZSessionList.instance == null) {
			ZSessionList.instance = new ZSessionList();
		}
		return ZSessionList.instance;
	}
	
	
	
}
