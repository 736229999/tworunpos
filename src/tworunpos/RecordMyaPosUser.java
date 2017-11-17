package tworunpos;

import java.sql.Date;

import com.ancientprogramming.fixedformat4j.annotation.Align;
import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class RecordMyaPosUser {
/*
 * This class is only for the fixed length filetype importer in DataImporter
 */
	 private String userId;
	 private String name;


	  @Field(offset = 1, length = 5)
	  public String getUserId() {
	    return userId;
	  }

	  public void setId(String userId) {
	    this.userId = userId;
	  }

	  @Field(offset = 6, length = 100)
	  public String getName() {
	    return name;
	  }

	  public void setName(String name) {
	    this.name = name;
	  }

}
