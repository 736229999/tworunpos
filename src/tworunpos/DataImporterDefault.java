package tworunpos;

import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.LinkOption.*;

import java.nio.file.attribute.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.*;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.ancientprogramming.fixedformat4j.format.impl.FixedFormatManagerImpl;
import com.mongodb.DB;
import com.opencsv.CSVReader;




public class DataImporterDefault extends Thread implements DataFileImporter  {


    
    private   WatchService watcher;
    private  Map<WatchKey,Path> keys;
    private  boolean recursive;
    private boolean trace = false;
    
    private DB db;
    private Config config;
    private ArticleList articleListForImport;
 
    private static char csvSeperator = ';';
	private String fullImportItemsFilename = "fullimport_items.csv";
	private String fullImportCashiersFilename = "fullimport_cashiers.csv";
    

  

    
    public void run()  {
        System.out.println("Importer Thread startet successfully!");
        // parse arguments
   
        // register directory and process its events
        try {
			processEvents();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
    }

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }
 
    /**
     * Register the given directory with the WatchService
     */
    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                System.out.format("register: %s\n", dir);
            } else {
                if (!dir.equals(prev)) {
                    System.out.format("update: %s -> %s\n", prev, dir);
                }
            }
        }
        keys.put(key, dir);
    }
 
    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    private void registerAll(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                throws IOException
            {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
 
    /**
     * Creates a WatchService and registers the given directory
     * @return 
     */
     DataImporterDefault(String stringDir, boolean recursive,  ArticleList articleList, Config config) throws IOException {
    	this.db = DatabaseClient.getInstance().getConnectedDB();
    	this.articleListForImport = articleList;
    	this.config = config;
    	
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey,Path>();
        this.recursive = recursive;
        Path dir = Paths.get(stringDir);
        if (recursive) {
            System.out.format("Scanning %s ...\n", dir);
            registerAll(dir);
            System.out.println("Done.");
        } else {
            register(dir);
        }
 
        // enable trace after initial registration
        this.trace = true;
        this.start();
    }
 
    /**
     * Process all events for keys queued to the watcher
     * @throws IOException 
     */
    void processEvents() throws IOException {
        for (;;) {
 
            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }
 
            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }
 
            for (WatchEvent<?> event: key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();
 
                // TBD - provide example of how OVERFLOW event is handled
                if (kind == OVERFLOW) {
                    continue;
                }
 
                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);
 
                // print out event
                System.out.format("%s: %s\n", event.kind().name(), child);
 
                
                
                /*
                 * Execute the import, if file for fullimport is detrected
                 * i didnt use "ENTRY_CREATED" because ENTRY_MODIFIED is triggered anyways on windows machines
                 */
                
                if( ( event.kind().name().toString() == "ENTRY_MODIFY") )
                {
            		//watch for default csv
            		if(child.toString().indexOf(fullImportItemsFilename)-fullImportFileDirectory.length() == 0){
            			try {
            				fullImportItems(child.toString());
            			} catch (FileNotFoundException e) {
            				e.printStackTrace();
            			}
            		}                		
                }
                

                
                // if directory is created, and watching recursively, then
                // register it and its sub-directories
                if (recursive && (kind == ENTRY_CREATE)) {
                    try {
                        if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                            registerAll(child);
                        }
                    } catch (IOException x) {
                        // ignore to keep sample readbale
                    }
                }
            }
 
            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);
 
                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }

   
    
    public void fullImportCashiers(String filepath){
    	
    }
    
    
    /*
     * This functions reads out a csv file and and saves the items (articles) in the database
     */
    public void fullImportItems(String fileLocation) throws IOException{
    	
    	final String fileLocationTemp = fileLocation;
    	 final JProgressBar progressBar = new GuiElements().getStyledDefaultProgressBar(1, countLines(fileLocation));
    	  


	      final JFrame progressFrame = new JFrame();
	      progressFrame.setUndecorated(true);
	      progressFrame.getContentPane().setPreferredSize(new Dimension(300,100));
	      progressFrame.setSize(500, 200);
	      progressFrame.isAlwaysOnTop();
	      progressFrame.setResizable(false);
	      progressFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	      progressFrame.pack();
	      progressFrame.setVisible(true);
	      progressFrame.setLocationRelativeTo(null);
			
		// MAIN PANEL
		JPanel settingsMainPanel;
		settingsMainPanel = new JPanel();
		settingsMainPanel.setBackground(Color.white);
		settingsMainPanel.setBounds(0, 0, 300, 100);
		settingsMainPanel.setLayout(new BorderLayout(0, 0));
		progressFrame.getContentPane().add(settingsMainPanel);
		settingsMainPanel.add(progressBar);

			
		
			//PRINT 
	      	System.out.println("--- START FULLIMPORT items ---");

	      	//DROP COLLECTION
	      	System.out.println("Dropping collection...");
	      	articleListForImport.drop();
	      	
	      	
			//START READING FILE AND IMPORTING	      	
	      	DebugScreen.getInstance().print("Starting fullimport...");
	      	Runnable runnable = new Runnable() {
    	    public void run() {

    	      	CSVReader csvReader = null;
    	  		try {
    	  			InputStreamReader file = new InputStreamReader(new FileInputStream(fileLocationTemp), "UTF-8");
    	  			csvReader = new CSVReader(file,csvSeperator);
    	  		} catch (UnsupportedEncodingException e1) {
    	  			// TODO Auto-generated catch block
    	  			e1.printStackTrace();
    	  		} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

    	      	String[] row = null;
    	      	String[] columnNames = null;

    	      	try {
    	    		int rowCounter = 0;
    	    		int columnCounter = 0;
    	    		

    	      		//iterate throgh rows
    	  			while((row = csvReader.readNext()) != null) {
    	  			 
    	  				//grab the headers and go to next line
    	  				if(rowCounter == 0){
    	  					columnNames = row;
    	  					rowCounter++;
    	  					DebugScreen.getInstance().print(row.length+" columns in csv");
    	  					continue;
    	  				}
    	  				
    	  				
    	  				//make an article to save it later in DB
    	  				Article article = new Article();
    	  				
    	  				//iterate through columns
    	  				//first line - use it as counter for columns
    	  				//mapper to article
    	  					for(columnCounter = 0; columnCounter < row.length;columnCounter++  ){
    	  				
    	  						DebugScreen.getInstance().print(columnNames[columnCounter].equals("barcode")+" Read in Column "+columnNames[columnCounter]+" Value: "+row[columnCounter].toString());

    	  						if(columnNames[columnCounter].equals("barcode")){
        	  						DebugScreen.getInstance().print("barcode column found: "+row[columnCounter].toString());
    	  							article.setBarcode(row[columnCounter].toString());
    	  							
    	  						}else if(columnNames[columnCounter].equals("plu")){
    	  							article.setPlu(Integer.parseInt(row[columnCounter]));
    	  							
    	  						}else if(columnNames[columnCounter].equals("name")){
    	  							article.setName(row[columnCounter].toString());
    	  							
    	  						}else if(columnNames[columnCounter].equals("unit")){
    	  							ArticleUnit tempUnit = new ArticleUnit();
    	  							tempUnit.setUnit(Integer.parseInt(row[columnCounter].toString()));
    	  							article.setUnit(tempUnit);
    	  							
    	  						}else if(columnNames[columnCounter].equals("priceGross")){
    	  							article.setPriceGross(Double.parseDouble(row[columnCounter].replace(',', '.')));
    	  							
    	  						}else if(columnNames[columnCounter].equals("priceNet")){
    	  							article.setPriceNet(Double.parseDouble(row[columnCounter].replace(',', '.')));
    	  							
    	  						}else if(columnNames[columnCounter].equals("VatPercentage")){
    	  							article.setVatPercentage(Double.parseDouble(row[columnCounter].replace(',', '.')));
    	  							
    	  						}else if(columnNames[columnCounter].equals("VatAmount")){
    	  							article.setVatAmount(Double.parseDouble(row[columnCounter].replace(',', '.')));
    	  							
    	  						}else if(columnNames[columnCounter].equals("isDeposit")){
    	  							Boolean b = "1".equals(row[columnCounter]);
    	  							article.setIsDeposit(b);
    	  							
    	  						}else if(columnNames[columnCounter].equals("hasDeposit")){
    	  							Boolean b = "1".equals(row[columnCounter]);
    	  							article.setHasDeposit(b);
    	  							
    	  						}else if(columnNames[columnCounter].equals("depositBarcode")){
    	  							article.setDepositBarcode(row[columnCounter].toString());
    	  							
    	  						}
    	  						
    	  	
    	  					}
    	  	
    	  					//add article to article list which will save the article to db
    	  					articleListForImport.addArticle(article);

    	  					
    	  					rowCounter++;
    	  					progressBar.setValue(rowCounter);
    	  					//output loading bar    	  					
    	  				
    	  			}
    	  			
    	  			
    	  			//report
    	  			System.out.println((rowCounter-1)+" lines proceeded");
    	  			System.out.println(articleListForImport.countAll()+" articles in mongodb");
    	  			
    	  			GuiElements message = new GuiElements();
    	  			message.displaySuccessMessageBox((rowCounter-1)+" Artikel erfolgreich importiert!");
    	  			
    	  			
    	  			 progressFrame.dispose();

    	  			csvReader.close();

    	  			
    	  			 
    	  		} catch (IOException e) {
    	  			
    	  			// TODO Auto-generated catch block
    	  			e.printStackTrace();
    	  		}    	  
    	      	
    	    	System.out.println("--- END FULLIMPORT items ---");
    	    	
    	    	
    	        
    	     }
    	  };
    	   
	      Thread thread = new Thread(runnable);
	      thread.start();
    	
	      

    	
    }
 
    public static int countLines(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }
    
    
    
    

    
	public String getFullImportItemsFilename() {
		return fullImportItemsFilename;
	}

	public void setFullImportItemsFilename(String fullImportItemsFilename) {
		this.fullImportItemsFilename = fullImportItemsFilename;
	}

	public String getFullImportCashiersFilename() {
		return fullImportCashiersFilename;
	}

	public void setFullImportCashiersFilename(
			String fullImportCashiersFilename) {
		this.fullImportCashiersFilename = fullImportCashiersFilename;
	}

    
}