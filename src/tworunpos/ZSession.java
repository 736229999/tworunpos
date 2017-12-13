package tworunpos;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.Date;

public class ZSession {
/*
 */

	private Integer counter;

	private Date dateTimeAtStartSesstion;
	private Date dateTimeAtEndSession;
	private User sesssionStartedByUserId;
	private Double sumOfSessionGross;
	private Double sumOfSessionNet;
	private Double sumOfSessionTax;

	private String zReportPrint;
	
	private Integer countOfReturnedArticles;
	private Double sumOfReturnedArticlesInclTax;
	private Double sumOfReturnedArticlesExclTax;

	private Integer countOfDepositArticles;
	private Double sumOfDepositArticlesInclTax;
	private Double sumOfDepositArticlesExclTax;


	public ZSession() {

		//todo generate new empty session

	}

	public ZSession(DBObject transactionDbObject){

		counter = (transactionDbObject.get("counter") != null ? (Integer) transactionDbObject.get("counter"):null);
		//DebugScreen.getInstance().print(articleDbObject.get("plu").toString());
		dateTimeAtStartSesstion = (transactionDbObject.get("dateTimeAtStartSesstion") != null ? (Date) transactionDbObject.get("dateTimeAtStartSesstion"):null);
		dateTimeAtEndSession =  (transactionDbObject.get("dateTimeAtEndSession") != null ?  (Date) ( transactionDbObject.get("dateTimeAtEndSession")) :null);
		sesssionStartedByUserId = (transactionDbObject.get("sesssionStartedByUserId") != null ?  (User) transactionDbObject.get("sesssionStartedByUserId") :null);
		sumOfSessionGross = (transactionDbObject.get("sumOfSessionGross") != null ?   (Double) transactionDbObject.get("sumOfSessionGross") : null );
		sumOfSessionNet = (transactionDbObject.get("sumOfSessionNet") != null ?   (Double) transactionDbObject.get("sumOfSessionNet") : null );
		sumOfSessionTax =  (transactionDbObject.get("sumOfSessionTax") != null ? (Double) transactionDbObject.get("sumOfSessionTax"):null);
		zReportPrint =  (transactionDbObject.get("zReportPrint") != null ? (String) transactionDbObject.get("zReportPrint"):null);
		countOfReturnedArticles =  (transactionDbObject.get("countOfReturnedArticles") != null ? (Integer) transactionDbObject.get("countOfReturnedArticles"):null);
		sumOfReturnedArticlesInclTax =  (transactionDbObject.get("sumOfReturnedArticlesInclTax") != null ? (Double) transactionDbObject.get("sumOfReturnedArticlesInclTax"):null);
		sumOfReturnedArticlesExclTax = (transactionDbObject.get("sumOfReturnedArticlesExclTax") != null ? (Double) transactionDbObject.get("sumOfReturnedArticlesExclTax"):null);
		countOfDepositArticles = (transactionDbObject.get("countOfDepositArticles") != null ? (Integer) (transactionDbObject.get("countOfDepositArticles")):null);
		sumOfDepositArticlesInclTax = (transactionDbObject.get("sumOfDepositArticlesInclTax") != null ? (Double) transactionDbObject.get("sumOfDepositArticlesInclTax"):null);
		sumOfDepositArticlesExclTax = (transactionDbObject.get("sumOfDepositArticlesExclTax") != null ? (Double) transactionDbObject.get("sumOfDepositArticlesExclTax"):null);
		//cart = (transactionDbObject.get("cart") != null ? new Cart((DBObject) transactionDbObject.get("cart")):null);

	}


	public Integer getCounter() {
		return counter;
	}

	public void setCounter(Integer counter) {
		this.counter = counter;
	}

	public ZSession getZSession(){
		return this;
	}



	/*
	 * This method will return the document object for a mongodb entry
	 */
	public BasicDBObject getMyDocument(){

		//create basic transaction attributes
		BasicDBObject mainDocument = new BasicDBObject();
		if(counter != null )
			mainDocument.put("counter",counter);
		if(dateTimeAtStartSesstion != null )
			mainDocument.put("dateTimeAtStartSesstion",dateTimeAtStartSesstion);
		if(dateTimeAtEndSession != null )
			mainDocument.put("dateTimeAtEndSession",""+dateTimeAtEndSession);
		if(sesssionStartedByUserId != null )
			mainDocument.put("sesssionStartedByUserId",""+sesssionStartedByUserId);
		if(sumOfSessionGross != null )
			mainDocument.put("sumOfSessionGross",sumOfSessionGross);
		if(sumOfSessionNet != null )
			mainDocument.put("sumOfSessionNet",sumOfSessionNet);
		if(sumOfSessionTax != null )
			mainDocument.put("sumOfSessionTax",sumOfSessionTax);
		if(zReportPrint != null )
			mainDocument.put("zReportPrint",zReportPrint);
		if(countOfReturnedArticles != null )
			mainDocument.put("countOfReturnedArticles",countOfReturnedArticles);
		if(sumOfReturnedArticlesInclTax != null )
			mainDocument.put("sumOfReturnedArticlesInclTax",sumOfReturnedArticlesInclTax);
		if(sumOfReturnedArticlesExclTax != null )
			mainDocument.put("sumOfReturnedArticlesExclTax",sumOfReturnedArticlesExclTax);
		if(countOfDepositArticles != null )
			mainDocument.put("countOfDepositArticles",countOfDepositArticles);
		if(sumOfDepositArticlesInclTax != null )
			mainDocument.put("sumOfDepositArticlesInclTax",sumOfDepositArticlesInclTax);
		if(sumOfDepositArticlesExclTax != null )
			mainDocument.put("sumOfDepositArticlesExclTax",sumOfDepositArticlesExclTax);

		return mainDocument;
	}
	

	
	
	
}
