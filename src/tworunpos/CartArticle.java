package tworunpos;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class CartArticle extends Article{

	
	private Boolean isRefund = false;
	private char letterIdOfTaxGroup ;
	private float quantity = 1;


	public CartArticle() {
	}

	public CartArticle(DBObject articleDbObject){
		super(articleDbObject);
		isRefund =  (articleDbObject.get("isRefund") != null ? (Boolean) articleDbObject.get("isRefund"):null);
		//letterIdOfTaxGroup =  (articleDbObject.get("letterIdOfTaxGroup") != null ? (char) articleDbObject.get("letterIdOfTaxGroup"):null);
		quantity =  (articleDbObject.get("quantity") != null ? Float.parseFloat(articleDbObject.get("quantity").toString()):null);
	}

	
	public Boolean isRefund() {
		if(isRefund == null)
			return false;
		else
			return isRefund;
	}
	
	public Boolean getIsRefund() {
		return isRefund;
	}


	public void setIsRefund(Boolean isRefund) {
		this.isRefund = isRefund;
	}

	public void setLetterIdOfTaxGroup(char letterId){
		this.letterIdOfTaxGroup = letterId;
	}

	public char getLetterIdOfTaxGroup() {
		return letterIdOfTaxGroup;
	}
	
	public Double getPriceNetTotal(){
		return quantity*this.getPriceNet();
	}

	public Double getPriceGrossTotal(){
		return quantity*this.getPriceGross();
	}
	
	public Double getVatAmountTotal(){
		return quantity*this.getVatAmount();
	}


	public float getQuantity() {
		return quantity;
	}


	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}


	/*
	 * This method will return the document object for a mongodb entry
	 */
	public BasicDBObject getMyDocument(){
		
		BasicDBObject document = super.getMyDocument();
	
		//extend the document of super class
		document.put("isRefund", this.getIsRefund());
		
		document.put("quantity", this.quantity);
		
		return document;
	}
	
	
	
}
