package tworunpos;

import Exceptions.CounterException;
import Exceptions.ZSessionException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.Date;

public class Transaction {
/*
A Transaction represents a finilized sales process. That means items has beeen added to the Cart. The Cart is checkout out. All this data is saved as a transaction - means Cart+Payment data.
 */
	private Integer zReference;
	private Date dateTimeAtStartTransaction;
	private Date dateTimeAtEndTransaction;
	private String paymentType;
	private Double sumOfCartGross;
	private Double sumOfCartNet;
	private Double sumOfCartTax;
	private Double paymentGiven;
	private Double paymentChange;
	private String receipt;
	private Cart cart;
	private Integer countOfReturnedArticles;
	private Double sumOfReturnedArticlesInclTax;
	private Double sumOfReturnedArticlesExclTax;

	private Integer transactionNo;
	private Integer posNo;
	private Integer zNo;


	public Transaction(DBObject transactionDbObject){

		
		//DebugScreen.getInstance().print(articleDbObject.get("plu").toString());
		zReference = (transactionDbObject.get("zReference") != null ? (Integer) transactionDbObject.get("zReference"):null);
		dateTimeAtStartTransaction =  (transactionDbObject.get("dateTimeAtStartTransaction") != null ?  (Date) ( transactionDbObject.get("dateTimeAtStartTransaction")) :null);
		dateTimeAtEndTransaction = (transactionDbObject.get("dateTimeAtEndTransaction") != null ?  (Date) transactionDbObject.get("dateTimeAtEndTransaction") :null);
		paymentType = (transactionDbObject.get("paymentType") != null ?   transactionDbObject.get("paymentType").toString() : null );
		sumOfCartGross = (transactionDbObject.get("sumOfCartGross") != null ?   (Double) transactionDbObject.get("sumOfCartGross") : null );
		sumOfCartNet =  (transactionDbObject.get("sumOfCartNet") != null ? (Double) transactionDbObject.get("sumOfCartNet"):null);
		sumOfCartTax =  (transactionDbObject.get("sumOfCartTax") != null ? (Double) transactionDbObject.get("sumOfCartTax"):null);
		paymentGiven =  (transactionDbObject.get("paymentGiven") != null ? (Double) transactionDbObject.get("paymentGiven"):null);
		paymentChange =  (transactionDbObject.get("paymentChange") != null ? (Double) transactionDbObject.get("paymentChange"):null);
		receipt = (transactionDbObject.get("depositBarcode") != null ? transactionDbObject.get("depositBarcode").toString():null);
		countOfReturnedArticles = (transactionDbObject.get("countOfReturnedArticles") != null ? (Integer) (transactionDbObject.get("countOfReturnedArticles")):null);
		sumOfReturnedArticlesInclTax = (transactionDbObject.get("sumOfReturnedArticlesInclTax") != null ? (Double) transactionDbObject.get("sumOfReturnedArticlesInclTax"):null);
		sumOfReturnedArticlesExclTax = (transactionDbObject.get("sumOfReturnedArticlesExclTax") != null ? (Double) transactionDbObject.get("sumOfReturnedArticlesExclTax"):null);
		cart = (transactionDbObject.get("cart") != null ? new Cart((DBObject) transactionDbObject.get("cart")):null);
		transactionNo = (transactionDbObject.get("transactionNo") != null ? (Integer) transactionDbObject.get("transactionNo"):null);
		posNo = (transactionDbObject.get("posNo") != null ? (Integer) transactionDbObject.get("posNo"):null);
		zNo = (transactionDbObject.get("zNo") != null ? (Integer) transactionDbObject.get("zNo"):null);

	}



	public Transaction(Cart cartObj, Integer transactionCounter, Integer zCounter) throws CounterException, ZSessionException {
		cart = cartObj;
		zReference = 1;
		dateTimeAtStartTransaction = cart.getDateTimeAtStartTransaction();
		dateTimeAtEndTransaction = cart.getDateTimeAtEndTransaction();
		sumOfCartGross = cart.getSumOfCartGross();
		sumOfCartNet = cart.getSumOfCartNet();
		sumOfCartTax = Helpers.roundForCurrency(sumOfCartGross-sumOfCartNet);
		paymentType = cart.getPaymentType();
		paymentGiven = cart.getPaymentGiven();
		paymentChange = sumOfCartGross-paymentGiven;
		receipt = cart.getReceipt().toString();
		countOfReturnedArticles = cart.getCountOfReturnedArticles() ;
		sumOfReturnedArticlesInclTax = cart.getSumOfReturnedArticlesInclTax();
		sumOfReturnedArticlesExclTax = cart.getSumOfReturnedArticlesExclTax();

		transactionNo = transactionCounter;
		posNo = Config.getInstance().getPosNo();
		zNo = zCounter;

	}



	
	public Transaction getTransaction(){
		return this;
	}

	/*
	Looks for the current open Z-Session and will add a reference to it
	 */
	public void addToCurrentZSession(){
		//todo
	}

	/*
	 * This method will return the document object for a mongodb entry
	 */
	public BasicDBObject getMyDocument(){

		//create basic transaction attributes
		BasicDBObject mainDocument = new BasicDBObject();

		if(zReference != null )
			mainDocument.put("zReference",zReference);
		if(dateTimeAtStartTransaction != null )
			mainDocument.put("dateTimeAtStartTransaction",""+dateTimeAtStartTransaction);
		if(dateTimeAtEndTransaction != null )
			mainDocument.put("dateTimeAtEndTransaction",""+dateTimeAtEndTransaction);
		if(sumOfCartGross != null )
			mainDocument.put("sumOfCartGross",sumOfCartGross);
		if(sumOfCartNet != null )
			mainDocument.put("sumOfCartNet",sumOfCartNet);
		if(sumOfCartTax != null )
			mainDocument.put("sumOfCartTax",sumOfCartTax);
		if(paymentType != null )
			mainDocument.put("paymentType",paymentType);
		if(paymentGiven != null )
			mainDocument.put("paymentGiven",paymentGiven);
		if(paymentChange != null )
			mainDocument.put("paymentChange",paymentChange);
		if(receipt != null )
			mainDocument.put("receipt",receipt);
		if(cart != null )
			mainDocument.put("cart",cart.getMyDocument());
		if(countOfReturnedArticles != null )
			mainDocument.put("countOfReturnedArticles",countOfReturnedArticles);
		if(sumOfReturnedArticlesInclTax != null )
			mainDocument.put("sumOfReturnedArticlesInclTax",sumOfReturnedArticlesInclTax);
		if(sumOfReturnedArticlesExclTax != null )
			mainDocument.put("sumOfReturnedArticlesExclTax",sumOfReturnedArticlesExclTax);
		if(transactionNo != null )
			mainDocument.put("transactionNo",transactionNo);
		if(posNo != null )
			mainDocument.put("posNo",posNo);
		if(zNo != null )
			mainDocument.put("zNo",zNo);
		return mainDocument;
	}
	

	
	
	
}
