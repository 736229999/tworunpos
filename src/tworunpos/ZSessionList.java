package tworunpos;


import Exceptions.CounterException;
import Exceptions.ZSessionException;
import com.mongodb.*;

import java.util.ArrayList;

public class ZSessionList {


	private static DB db;
	private static DBCollection collection;
	private static ZSession currentZSession;



	public ZSessionList(){
		db = DatabaseClient.getInstance().getConnectedDB();
		collection = db.getCollection("zSessionList");
	}



	/*
	This method will return a list of Transaction by its ID
	 */
	public  void getZSessionById(String id){
		//todo
	}



	/*
	This method will return the latest transaction
	If there is no openSession it will create one
	 */
	public static ZSession getOpenZSession(User creator) throws ZSessionException, CounterException {
		ZSession zSession = null;


        BasicDBObject search = new BasicDBObject("dateTimeAtEndSession", new BasicDBObject("$exists", false));
        BasicDBObject order = new BasicDBObject("_id",-1);

		DBCursor result = collection.find(search).sort(order);

		if(  result.one() != null  ){
			zSession = new ZSession(result.one());
			DebugScreen.getInstance().print("open ZSession found: "+zSession.getMyDocument().toString());
		}
		else{
			zSession= new ZSession(creator);
			addZSession(zSession);

			//increment counter in DB
			ZSession.incrementCounter();
			DebugScreen.getInstance().print("New ZSession created: "+zSession.getMyDocument().toString());


		}
		return zSession;
	}


	/*
	This method will close the currently open session. This means, it will lookup in the db for the latest
	open one (by not existing end date) and will write an end date.
	*/
	public void closeOpenZSession() throws ZSessionException {
		ZSession zSession = null;


		BasicDBObject search = new BasicDBObject("dateTimeAtEndSession", new BasicDBObject("$exists", false));
        BasicDBObject order = new BasicDBObject("_id",-1);

        DBCursor result = collection.find(search).sort(order);
        System.out.println(result.size());
        System.out.println(result.iterator().next());

		if(  result.one() != null  ){
			zSession = new ZSession(result.one());
			zSession.close();
			DebugScreen.getInstance().print(zSession.getMyDocument().toString());
		}else{
            DebugScreen.getInstance().print("Close Z action performed, but no Z found.");
        }
	}

	/*
	This Method saves or updates an Object to the DB
	 */
    public void upsert (ZSession zSession) throws ZSessionException {

        WriteResult result = collection.update(
                new BasicDBObject("counter", zSession.getCounter()),
                new BasicDBObject("$set", zSession.getMyDocument()),
                true, false);
        if(result.getN() < 1) {
            throw new ZSessionException("Datenbankfehler beim Schreiben des Z-Berichtes in ZSessionList.");

        }

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
	public static void addZSession(ZSession zSession){
		DebugScreen.getInstance().print("DB: Start insert Transaction into collection");
		collection.insert(WriteConcern.SAFE,zSession.getMyDocument());
		DebugScreen.getInstance().print("DB: Insert done");
	}
	

	
	
}
