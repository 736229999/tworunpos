package tworunpos;

import java.sql.Date;

import com.ancientprogramming.fixedformat4j.annotation.Align;
import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class RecordMyaPosItem {
/*
 * This class is only for the fixed length filetype importer in DataImporter
 */
	 private String barcode;
	 private String articleNumber;
	 private String articleName;
	 private Integer articlePriceVatPercentage;
	 private String articlePriceGross;
	 private String articleUnitName;


	  @Field(offset = 1, length = 17)
	  public String getBarcode() {
	    return barcode;
	  }

	  public void setBarcode(String barcode) {
	    this.barcode = barcode;
	  }

	  @Field(offset = 18, length = 25)
	  public String getArticleNumber() {
	    return articleNumber;
	  }

	  public void setArticleNumber(String articleNumber) {
	    this.articleNumber = articleNumber;
	  }

	  @Field(offset = 43, length = 51)
	  public String getArticleName() {
	    return articleName;
	  }

	  public void setArticleName(String articleName) {
	    this.articleName = articleName;
	  }
	  

	  @Field(offset = 94, length = 4)
	  public Integer getArticlePriceVatPercentage() {
	    return articlePriceVatPercentage;
	  }

	  public void setArticlePriceVatPercentage(Integer getArticlePriceVatPercentage) {
	    this.articlePriceVatPercentage = getArticlePriceVatPercentage;
	  }
	  

	  @Field(offset = 108, length = 15)
	  public String getArticlePriceGross() {
	    return articlePriceGross;
	  }

	  public void setArticlePriceGross(String articlePriceGross) {
	    this.articlePriceGross = articlePriceGross;
	  }

	  @Field(offset = 1628, length = 25)
	  public String getArticleUnitName() {
	    return articleUnitName;
	  }

	  public void setArticleUnitName(String articleUnitName) {
	    this.articleUnitName = articleUnitName;
	  }
}
