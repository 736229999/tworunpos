package tworunpos;

import java.io.IOException;

import com.mongodb.DB;


interface DataFileImporter
{
	static DB db = null;
	static Config config = null;
	static ArticleList articleListForImport = null;
	static String fullImportFileDirectory = "C:/tworunpos/import/";
	
	
	public void fullImportItems(String filepath) throws IOException;
	public void fullImportCashiers(String filepath) throws IOException;
	
	 
    public String getFullImportItemsFilename();
	public void setFullImportItemsFilename(String fullImportItemsFilename);
	public String getFullImportCashiersFilename();
	public void setFullImportCashiersFilename(String fullImportCashiersFilename);

	
}