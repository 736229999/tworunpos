package tworunpos;

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



	public Transaction(DBObject transactionDbObject){
//todo save all articles as well for the transfer
		
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
	}



	public Transaction(Cart cart){
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
		cart = this.cart;
	}



	
	public Transaction getTransaction(){
		return this;
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
			mainDocument.put("dateTimeAtStartTransaction",dateTimeAtStartTransaction);
		if(dateTimeAtEndTransaction != null )
			mainDocument.put("dateTimeAtEndTransaction",dateTimeAtEndTransaction);
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
		if(countOfReturnedArticles != null )
			mainDocument.put("countOfReturnedArticles",countOfReturnedArticles);
		if(sumOfReturnedArticlesInclTax != null )
			mainDocument.put("sumOfReturnedArticlesInclTax",sumOfReturnedArticlesInclTax);
		if(sumOfReturnedArticlesExclTax != null )
			mainDocument.put("sumOfReturnedArticlesExclTax",sumOfReturnedArticlesExclTax);

		return mainDocument;
	}
	

	
	
	
}
