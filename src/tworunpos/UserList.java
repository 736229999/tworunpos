package tworunpos;


import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class UserList {

	
	private static DBCollection userCollection;
	private static DB db;

	
	
	public UserList(){
		db = DatabaseClient.getInstance().getConnectedDB();
		userCollection = db.getCollection("userList");
		
		//createDummiesInDatabase();		
	}
	
	public  User lookupUserById(String id) throws Exception{

		BasicDBObject query = new BasicDBObject("userId", id);
		DBObject foundDocument = userCollection.findOne(query);
		
		System.out.println("query: "+query.toString());
		System.out.println("db: "+db.getName());
		System.out.println("collection: "+userCollection.getName());

		
		if(foundDocument != null){
			System.out.println("FOUND: "+foundDocument.toString());
			System.out.println("Start Converting Doc to Article");
			User foundUser = new User(foundDocument);
			System.out.println("Finished Converting Doc to Article");
			return foundUser;
		}
		else{
			throw new Exception("PLU nicht gefunden");
		}
		
		
	}
	
	
	
	//DATABASE FUNCITONS ----------------------------------------------------

	/*
	 * This function creates some dummy articles in mongodb
	 */
	public void createDummiesInDatabase(){

		//frist drop all data and create new collection with dummydata
//		articleCollection.drop();
//		
//		Article temp1 = new Article("1929292929292", "Eti Puff", "St�ck", 0.99);
//		temp1.setPlu(101);
//
//		
//		Article temp2 = new Article("19283746583922", "G�ll� Lokum", "St�ck", 2.99);
//		temp2.setPlu(102);
//
//		
//		Article temp3 = new Article("33332643345445", "Arcelik Ekmek Firini", "St�ck", 49.90);
//		temp3.setPlu(103);
//
//			
//		addArticle(temp1);
//		addArticle(temp2);
//		addArticle(temp3);


	}
	
	/*
	 * this function will delete the whole colelction. be careful
	 */
	public void drop(){
		userCollection.drop();
	}
	
	public long countAll(){
		return userCollection.count();
	}
	
	
	/*
	 * this function will insert a article to the article list.
	 * this means, its also added to the used database.
	 */
	public void addUser(User user) {
		//todo upsert
		try{
			userCollection.insert(user.getMyDocument());			
		}catch(Exception e){
			DebugScreen.getInstance().print("Error in inserting User into DB");
			DebugScreen.getInstance().printStackTrace(e);
			DebugScreen.getInstance().print("Try to repair the database");
			try {
				DatabaseClient.getInstance().repairDatabase();
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	
	
	
}
