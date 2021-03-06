package tworunpos;

import Exceptions.CounterException;
import Exceptions.ZSessionException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;

import java.util.Date;

public class ZSession {
/*
 */

	private Integer counter;

	private ObjectId mongoId;
	private Date dateTimeAtStartSession;
	private Date dateTimeAtEndSession;
	private String sesssionOpenedByUserId;
    private String sesssionClosedByUserId;
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


	public ZSession(User creator) throws CounterException {

        dateTimeAtStartSession = new Date();
		sesssionOpenedByUserId = creator.getUserId();
		counter = new Counter(this.getClass().getSimpleName()).getCounter();

	}

	public void close(User closer) throws ZSessionException {
	    //todo do all calcucaltions
		/*sesssionClosedByUserId
		sumOfSessionGross;
		sumOfSessionNet;
		sumOfSessionTax;
		countOfReturnedArticles;
		sumOfReturnedArticlesInclTax;
		sumOfReturnedArticlesExclTax;
		countOfDepositArticles;
		sumOfDepositArticlesInclTax;
		sumOfDepositArticlesExclTax;*/


		sesssionClosedByUserId = closer.getUserId();
        dateTimeAtEndSession = new Date();

        save();


    };

	public ZSession(DBObject transactionDbObject){

		mongoId = (transactionDbObject.get("_id") != null ? (ObjectId)transactionDbObject.get("_id"):null);

		counter = (transactionDbObject.get("counter") != null ? (Integer) transactionDbObject.get("counter"):null);
        dateTimeAtStartSession = (transactionDbObject.get("dateTimeAtStartSession") != null ? (Date) transactionDbObject.get("dateTimeAtStartSession"):null);
		dateTimeAtEndSession =  (transactionDbObject.get("dateTimeAtEndSession") != null ?  (Date) ( transactionDbObject.get("dateTimeAtEndSession")) :null);
        sesssionOpenedByUserId = (transactionDbObject.get("sesssionOpenedByUserId") != null ?  (String) transactionDbObject.get("sesssionOpenedByUserId") :null);
        sesssionClosedByUserId = (transactionDbObject.get("sesssionClosedByUserId") != null ?  (String) transactionDbObject.get("sesssionClosedByUserId") :null);
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
    public static void incrementCounter() throws CounterException {
        new Counter(ZSession.class.getSimpleName()).increment();
    }


	public ObjectId getMongoId() {
		return mongoId;
	}

	public ZSession getZSession(){
		return this;
	}

	public Date getDateTimeAtStartSession() {
		return dateTimeAtStartSession;
	}

	public Date getDateTimeAtEndSession() {
		return dateTimeAtEndSession;
	}

	public String getSesssionOpenedByUserId() {
		return sesssionOpenedByUserId;
	}

	public String getSesssionClosedByUserId() {
		return sesssionClosedByUserId;
	}

	/*
	 * This method will return the document object for a mongodb entry
	 */
	public BasicDBObject getMyDocument(){

		//create basic transaction attributes
		BasicDBObject mainDocument = new BasicDBObject();
		if(mongoId != null )
			mainDocument.put("_id",mongoId);
		if(counter != null )
			mainDocument.put("counter",counter);
		if(dateTimeAtStartSession != null )
			mainDocument.put("dateTimeAtStartSession",dateTimeAtStartSession);
		if(dateTimeAtEndSession != null )
			mainDocument.put("dateTimeAtEndSession",dateTimeAtEndSession);
		if(sesssionOpenedByUserId != null )
			mainDocument.put("sesssionOpenedByUserId",""+sesssionOpenedByUserId);
        if(sesssionClosedByUserId != null )
            mainDocument.put("sesssionClosedByUserId",""+sesssionClosedByUserId);
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
	

	public void save() throws ZSessionException {
	    //save this Object in DB
	    ZSessionList zList = new ZSessionList();
        zList.upsert(this);
    }
	
	
	
}
