package tworunpos;


import com.mongodb.*;

public class ArticleList {

	
	private static DB db;
	private static DBCollection articleCollection;

	// Singleton!
	static ArticleList instance;
	
	public ArticleList(){
		db = DatabaseClient.getInstance().getConnectedDB();
		articleCollection = db.getCollection("articleList");
		
		//createDummiesInDatabase();
	}
	
	public static Article lookupArticlebyPlu(int plu) throws Exception{

		
		BasicDBObject query = new BasicDBObject("plu", plu);
		DBObject foundDocument = articleCollection.findOne(query);
		
		DebugScreen.getInstance().print("query: "+query.toString()+"   db: "+db.getName() + "     collection: "+articleCollection.getName());

		
		if(foundDocument != null){
			DebugScreen.getInstance().print("FOUND Article by PLU: "+foundDocument.toString());
			DebugScreen.getInstance().print("Start Looking Up by Barcode"+foundDocument.get("barcode").toString());
//			Article foundArticle = new Article(foundDocument);
//			DebugScreen.getInstance().print("Finished Converting Doc to Article");
//			DebugScreen.getInstance().print("CONVERTED Article: "+foundArticle.getMyDocument());
			
			return lookupArticleByBarcode(foundDocument.get("barcode").toString());
		}
		else{
			DebugScreen.getInstance().print("Throw Exception by lookupArticlebyPlu");
			throw new Exception("PLU nicht gefunden");
		}
		
		
	}
	
	public static Article lookupArticleByBarcode(String barcode) throws Exception{
		DebugScreen.getInstance().print("lookupArticleByBarcode in ArticleList...");

		//check, if barcode is a weighBarcode, that means, it includes the weight given in gram of the article.
		//this should be interpreted as the quantity
		// AA BBBB P CCCCC P
		// A = Barcodeprefix, which shows if its a weighBarcode or not. Usually 29 or 28 is used
		// B = Articlecode
		// P = Checksum
		// C = quantity in grams
		DebugScreen.getInstance().print("isWeighArticleByBarcode:"+Article.isWeighArticleByBarcode(barcode)+"  Barcode:"+barcode);
		if(Article.isWeighArticleByBarcode(barcode)){
			
			String quantity = barcode.substring(8,12);
			if(quantity != ""){
				Buffer.getInstance().setPosQuantityBuffer(Float.parseFloat(quantity)/1000);
			}
			
			barcode = barcode.substring(0,7);

		}
		//TODO
		
		BasicDBObject query = new BasicDBObject("barcode", barcode);
		DBObject foundDocument = articleCollection.findOne(query);
		DebugScreen.getInstance().print("query: "+query.toString());
		DebugScreen.getInstance().print("db: "+db.getName());
		DebugScreen.getInstance().print("collection: "+articleCollection.getName());

		
		if(foundDocument != null){
			System.out.println("FOUND: "+foundDocument.toString());
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
		articleCollection.drop();
	}
	
	public long countAll(){
		return articleCollection.count();
	}
	
	
	/*
	 * this function will insert a article to the article list.
	 * this means, its also added to the used database.
	 */
	public void addArticle(Article article){
		//todo upsert

		System.out.println(article.getMyDocument().toString());

		DebugScreen.getInstance().print("DB: Start insert article into collection");

		articleCollection.insert(WriteConcern.SAFE,article.getMyDocument());
		DebugScreen.getInstance().print("DB: Insert done");
	}
	
	

	//Singleton get Instantce
	public static synchronized ArticleList getInstance () {
		if (ArticleList.instance == null) {
			ArticleList.instance = new ArticleList ();
		}
		return ArticleList.instance;
	}
	
	
	
}
