package tworunpos;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class Article {

	private String barcode  = "";
	private Integer plu = -1;
	private String name  = "";
	private ArticleUnit unit = null;
	private Double priceGross = -0.01;
	private Double priceNet = -0.01;
	private Double vatPercentage = -0.01;
	private Double vatAmount = -0.01;
	private Boolean isDeposit = null;
	private Boolean hasDeposit = null;
	private String depositBarcode = "";
	
	
	
	public Article(){		
	}
	
	public Article(DBObject articleDbObject){		
		//DebugScreen.getInstance().print(articleDbObject.get("plu").toString());
		barcode = (articleDbObject.get("barcode") != null ? articleDbObject.get("barcode").toString():null);
		plu = (Integer) (articleDbObject.get("plu") != null ? articleDbObject.get("plu"):null);
		name = (articleDbObject.get("name") != null ? articleDbObject.get("name").toString():null);
		priceGross = (articleDbObject.get("priceGross") != null ?   (Double) articleDbObject.get("priceGross") : null );
		priceNet = (articleDbObject.get("priceNet") != null ?   (Double) articleDbObject.get("priceNet") : null );
		vatPercentage =  (articleDbObject.get("VatPercentage") != null ? (Double) articleDbObject.get("VatPercentage"):null);
		vatAmount =  (articleDbObject.get("VatAmount") != null ? (Double) articleDbObject.get("VatAmount"):null);
		isDeposit =  (articleDbObject.get("isDeposit") != null ? (Boolean) articleDbObject.get("isDeposit"):false);
		hasDeposit =  (articleDbObject.get("hasDeposit") != null ? (Boolean) articleDbObject.get("hasDeposit"):null);
		depositBarcode = (articleDbObject.get("depositBarcode") != null ? articleDbObject.get("depositBarcode").toString():null);
		
		int tempUnitString = (articleDbObject.get("unit") != null ? (int)articleDbObject.get("unit"):null);
		if(tempUnitString > -1){
			DebugScreen.getInstance().print("DEBUG: looking for unit of article. Result: "+tempUnitString);
			unit = new ArticleUnit(tempUnitString);
		}

		
	}
	
	
	public Article(String barcode, String name, ArticleUnit unit, Double priceGross ){
		setBarcode(barcode);
		setName(name);
		setUnit(unit);
		setPriceGross(priceGross);
		
	}
	
	public Article getArticle(){
		return this;
	}
	
	public int getPlu() {
		return plu;
	}
	public void setPlu(int plu) {
		this.plu = plu;
	}
	
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArticleUnit getUnit() {
		return unit;
	}
	public void setUnit(ArticleUnit unit) {
		this.unit = unit;
	}
	public Double getPriceGross() {
		return priceGross;
	}
	public void setPriceGross(Double priceGross) {
		this.priceGross = priceGross;
	}
	public Double getPriceNet() {
		//DebugScreen.getInstance().print("Price-Net: "+priceNet);
		return priceNet;
	}
	public void setPriceNet(Double priceNet) {
		this.priceNet = priceNet;
	}
	public Double getVatPercentage() {
		return vatPercentage;
	}
	public void setVatPercentage(Double vatPercentage) {
		this.vatPercentage = vatPercentage;
	}
	public Double getVatAmount() {
		return vatAmount;
	}
	public void setVatAmount(Double vatAmount) {
		this.vatAmount = vatAmount;
	}

	
	public Boolean getIsDeposit() {
		return isDeposit;
	}

	
	public Boolean isDeposit() {
		if(isDeposit == null || isDeposit == false)
			return false;
		else if (isDeposit == true)
			return true;
		else
			return false;
	}
	
	public void setIsDeposit(Boolean isDeposit) {
		this.isDeposit = isDeposit;
	}


	public Boolean getHasDeposit() {
		return hasDeposit;
	}
	
	public Boolean hasDeposit() {
		if(hasDeposit == null || hasDeposit == false)
			return false;
		else if (hasDeposit == true)
			return true;
		else
			return false;
		
	}

	public void setHasDeposit(Boolean hasDeposit) {
		this.hasDeposit = hasDeposit;
	}

	public String getDepositBarcode() {
		return depositBarcode;
	}

	public void setDepositBarcode(String depositBarcode) {
		this.depositBarcode = depositBarcode;
	}

	public boolean isWeighArticleByBarcode(){
		return Config.getInstance().isInWeighCodeList(barcode.substring(0, 1));
	}
	

	public static boolean isWeighArticleByBarcode(String barcode){
		DebugScreen.getInstance().print("first To digits of barcode: "+barcode.substring(0, 2));
		return Config.getInstance().isInWeighCodeList(barcode.substring(0, 2));
	}
	
	/*
	 * This method will return the document object for a mongodb entry
	 */
	public BasicDBObject getMyDocument(){
		
		BasicDBObject document = new BasicDBObject();
		if(!this.getBarcode().isEmpty())
			document.put("barcode", this.getBarcode());
		if(this.getPlu() > -1   )
			document.put("plu", this.getPlu());
		if(!this.getName().isEmpty())
			document.put("name", this.getName());
		if(this.getUnit() != null)
			document.put("unit", this.getUnit().getUnit());
		if(this.getPriceGross()  > -0.01)
			document.put("priceGross", this.getPriceGross());
		if(this.getPriceNet() > -0.01)
			document.put("priceNet", this.getPriceNet());
		if(this.getVatPercentage() > -0.01)
			document.put("VatPercentage", this.getVatPercentage());
		if(this.getVatAmount()  > -0.01)
			document.put("VatAmount", this.getVatAmount());
		if(this.getIsDeposit() != null)
			document.put("isDeposit", this.getIsDeposit());
		if(this.getHasDeposit() != null)
			document.put("hasDeposit", this.getHasDeposit());
		if(!this.getDepositBarcode().isEmpty())
			document.put("depositBarcode", this.getDepositBarcode());
		
		return document;
	}
	
	//converter to CartArticle
	public CartArticle toCartArticle(){
		CartArticle cartArticle = new CartArticle();
		
		cartArticle.setBarcode(barcode);
		cartArticle.setDepositBarcode(depositBarcode);
		cartArticle.setHasDeposit(hasDeposit);
		cartArticle.setIsDeposit(isDeposit);
		cartArticle.setName(name);
		cartArticle.setPlu(plu);
		cartArticle.setPriceGross(priceGross);
		cartArticle.setPriceNet(priceNet);
		cartArticle.setUnit(unit);
		cartArticle.setVatAmount(vatAmount);
		cartArticle.setVatPercentage(vatPercentage);
		
		return cartArticle;
		
	}
	
	
	
}
