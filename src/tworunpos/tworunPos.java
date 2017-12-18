package tworunpos;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.util.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.KeyboardFocusManager;
import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;




import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;

import java.awt.Color;
import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import Exceptions.CounterException;
import Exceptions.ZSessionException;
import org.apache.commons.lang.ArrayUtils;

import Exceptions.CheckoutGeneralException;
import Exceptions.CheckoutPaymentException;
import GuiElements.*;

import com.mongodb.DB;
import sun.security.ssl.Debug;

public class tworunPos extends JFrame {
	private static final long serialVersionUID = 1L;


	
	private JFormattedTextField textfieldOne;


	private String frameTitle;
	
	private JPanel displayPanel;
	private TrDisplay display;
	

	private JPanel rightBarPanel = new JPanel();
	private JPanel rightBarPanelCenter;
	private JPanel rightBarPanelNorth;
	private JPanel rightBarPanelWest;
	private JPanel rightBarPanelSouth;
	private JPanel rightBarPanelEast;
	
	
	
	private JPanel actionsPanel;
	private JTable table;


	TrUserLogin loginmask;




	private User salesUser = null;
	private ZSession openZSession = null;
	private Cart cart = null;
			
	private JPanel mainPanel;

	
	
	//Gui Helper
	GuiElements gui = new GuiElements();
	

	//State
	PosState state = PosState.getInstance();


	
	
	
	//BARCODE BUFFER FROM SCANNER
	private String barcodeBuffer = "";
	
	
	//shared connections and sources
	private DB db;
	private DataFileImporter myDataImporter;
	private Config config;
	private DebugScreen debug;

	private ZSessionList zSessionList;



	private List<LoginEventListener> listeners = new ArrayList<LoginEventListener>();
	public void addListener(LoginEventListener toAdd) {
		listeners.add(toAdd);
	}

	public tworunPos(String frameTitle, DB db, DataFileImporter importer, ArticleList articleList, Config config, DebugScreen debug)  {

		this.frameTitle = frameTitle;
		this.db = db;
		this.myDataImporter = importer;
		this.config = config;
		this.debug = DebugScreen.getInstance();

		state.addObserver(new stateObserver());

		//----------------- LOGIN
		this.salesUser = new User();
		//start the loginmask to login; this method will stop the process here, until user logs in
		state.changeStateToLogin(false);



        while(!salesUser.loggedIn()){};

		//------------------ZSession
		//initZSession();

		setTitle(frameTitle);
		createMainGui();
		initWindow();



		//close the loginscreen and continue with sale screen
		//loginmask.setVisible(false); //you can't see me!
		//loginmask.dispose(); //Destroy the JFrame object

		

	/*
		
		//listen to all keystrokes for commands e.g. barcodes from scanner
		//textfieldOne.addKeyListener(this);
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

	        @Override
	        public boolean dispatchKeyEvent(KeyEvent ke) {
	        	
	                switch (ke.getID()) {
		                case KeyEvent.KEY_PRESSED:
		                	
		                		//if a number is pressed, write it into the buffer
			                	if(ArrayUtils.contains( new int[]{KeyEvent.VK_0,
			                			KeyEvent.VK_1,
			                			KeyEvent.VK_2,
			                			KeyEvent.VK_3,
			                			KeyEvent.VK_4,
			                			KeyEvent.VK_5,
			                			KeyEvent.VK_6,
			                			KeyEvent.VK_7,
			                			KeyEvent.VK_8,
			                			KeyEvent.VK_9},ke.getKeyCode())){
		                    		try {
		                				barcodeBuffer += KeyEvent.getKeyText(ke.getKeyCode());
		                				DebugScreen.getInstance().print("Number entered, new barcodebuffer: "+barcodeBuffer);
		                			} catch (Exception e1) {
		                				// TODO Auto-generated catch block
		                				display.showSimpleTextOnDisplay(e1.getMessage());
		                			}
		                        	 return true;
		                        }
		                	
		                		//if enter is pressed, lookup the article to sell it
		                    	if(ke.getKeyCode() == KeyEvent.VK_ENTER){
		                    		try {
		                				cart.addArticleByBarcode(barcodeBuffer, Buffer.getInstance().getPosQuantityBuffer());
		                			} catch (Exception e1) {
		                				// TODO Auto-generated catch block
		                				display.showSimpleTextOnDisplay(e1.getMessage());
		                				PosState.getInstance().changeStateToSellingProcess(false);
		                			}
		                        	 return true;
		                        }
	     		                break;

		
		                case KeyEvent.KEY_RELEASED:{
		                	if (ke.getKeyCode() == KeyEvent.VK_W) {
		                        return false;
		                    }
		                }break;
		                    
		                    
	                }
	                return false;
	       }
	    });*/
		
		
//		state.changeState(PosState.posStateReady);
		state.changeStateToReady(true);
		
		


		
		
	}

	private void initZSession(){

		zSessionList = new ZSessionList();
		try {
			openZSession = zSessionList.getOpenZSession();
			GuiElements.displayInfoMessageBox("Z-No: "+openZSession.getCounter());
		} catch (ZSessionException e) {
			GuiElements.displayInfoMessageBox("Z-Fehler: "+e.getMessage());

			e.printStackTrace();
		} catch (CounterException e) {

			GuiElements.displayInfoMessageBox("Counter-Fehler: "+e.getMessage());
			e.printStackTrace();
		}

	}

		private class LoginObserver implements LoginEventListener {
			@Override
			public void update(String action) {

				if(action == "login"){
					salesUser.login(loginmask.getInputValue());
					initZSession();
                    //close the loginscreen and continue with sale screen
                    loginmask.setVisible(false); //you can't see me!
                    loginmask.dispose(); //Destroy the JFrame object
					state.changeStateToReady(true);
                }
				else if(action == "logout"){

				}
			}
		}


	    private class cartObserver implements Observer {

			@Override
			public void update(Observable o, Object arg) {
				DebugScreen.getInstance().print("update from cart");
				
				//arg 0 is the action we want to do
				//arg 1 is the object we want to handle
				Object[] args = (Object[])arg;
				if(args[0] == "add"){
					//füge article der artikelliste der hauptanwendung hinzu
					addArticleToTable((CartArticle)args[1]);
					display.showArticleOnDisplayForSale(((CartArticle)args[1]).getQuantity(),(CartArticle)args[1],cart);
					state.changeStateToSellingProcess(false);
					
				}else if(args[0] == "removeByArticleWithNotification"){
					//f�ge artikel als "minusartikel" in die liste hinzu
					CartArticle article = (CartArticle)args[1];
			
					addArticleToTable((CartArticle)args[1]);
					display.showArticleOnDisplayForCancellation(1,article,cart);
					
					PosState.getInstance().changeStateToSellingProcess(false);
					
					
				}else if(args[0] == "removeByPosition"){
					//entferne article aus der artikelliste
					int position = Integer.parseInt(args[1].toString());
					CartArticle article = (CartArticle)args[2];
			
					removeArticleFromTableByPosition(position);
					display.showArticleOnDisplayForCancellation(1,article,cart);
					state.changeStateToSellingProcess(false);
					
				}else if(args[0] == "drop"){
					

				}
			}
	    }
	    private class stateObserver implements Observer {

			@Override
			public void update(Observable o, Object arg) {
				DebugScreen.getInstance().print("update from state");
				Object[] args = (Object[])arg;
				if(args[0] == "changePosState" && (int)args[1] == PosState.posStateBoot ){
					
				}else if(args[0] == "changePosState" && (int)args[1] == PosState.posStateCheckout ){
					changeStateToCheckout();
				}else if(args[0] == "changePosState" && (int)args[1] == PosState.posStateReady ){
					changeStateToReady();
				}else if(args[0] == "changePosState" && (int)args[1] == PosState.posStateRefundDeposit ){
					changeStateToRefundDeposit();
				}else if(args[0] == "changePosState" && (int)args[1] == PosState.posStateSellingProcess  ){
					if(args.length > 2 && (boolean)args[2] == false){
						changeStateToSellingProcess(false);						
					}
					else{
						changeStateToSellingProcess(true);						
					}
				}else if(args[0] == "changePosState" && (int)args[1] == PosState.posStateSettings ){
					changeStateToSettings();
				}else if(args[0] == "changePosState" && (int)args[1] == PosState.posStateLogin ){
					DebugScreen.getInstance().print("changeStateToLogin");
					changeStateToLogin(true);
					//todo wait on loginscreen until login done
				}
			}
	    }

	    

	private void createCart(){
		if(cart != null )
			cart.reset();
		cart = null;
		cart = Cart.getInstance();
		cart.addObserver(new cartObserver());
		//TODO beim zweoten verkauf ist der cart nicht leer
	}

	protected void initWindow() {

		
		state.changeState(PosState.posStateBoot);
		
		this.setUndecorated(true);
		this.setBackground(Color.white);
		this.getContentPane().setBackground(Color.white);
		this.getContentPane().setPreferredSize(new Dimension(TrDimensions.windowWidth,TrDimensions.windowHeight));
		this.isAlwaysOnTop();
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();

		// maximaze window
//		 GraphicsEnvironment env =
//		 GraphicsEnvironment.getLocalGraphicsEnvironment();
//		this.setExtendedState(Frame.MAXIMIZED_BOTH);

	}

	protected void createMainGui() {
		getContentPane().setLayout(new BorderLayout());


		// MAIN PANEL
		mainPanel = new JPanel();
		mainPanel.setBackground(Color.white);
		mainPanel.setBounds(0, 0, 1024, 768);
		mainPanel.setLayout(new BorderLayout(0, 0));
		getContentPane().add(mainPanel, BorderLayout.CENTER);

		// DISPLAY PANEL - NORTH
		displayPanel = new JPanel();
		
		displayPanel.setBorder(new EmptyBorder(TrDimensions.displayPanelBorder,TrDimensions.displayPanelBorder,TrDimensions.displayPanelBorder,TrDimensions.displayPanelBorder) );
		displayPanel.setPreferredSize(new Dimension(TrDimensions.displayWidth, TrDimensions.displayHeight));
		displayPanel.setMaximumSize(new Dimension(TrDimensions.displayWidth, TrDimensions.displayHeight));
		displayPanel.setBackground(new Color(255,255,255));
		displayPanel.setLayout(new BorderLayout());
		
		//display
		display = TrDisplayForCashier.getInstance();
		displayPanel.add(display, BorderLayout.WEST);
		
		//input field
	     textfieldOne = new JFormattedTextField(); 			
		Font fontTextFieldOne = new Font("SansSerif", Font.BOLD, 20);
		textfieldOne.setPreferredSize( new Dimension( GuiElements.rightBarWidth-15, 24 ) );
		textfieldOne.setMaximumSize(new Dimension(GuiElements.rightBarWidth-15, 24));
		textfieldOne.setSize( new Dimension( GuiElements.rightBarWidth-15, 24 ) );
//		textfieldOne.setBorder(BorderFactory.createLineBorder(Color.cyan));
		textfieldOne.setBorder(BorderFactory.createEmptyBorder());
		textfieldOne.setFont(fontTextFieldOne);
		displayPanel.add(textfieldOne, BorderLayout.EAST);
				
		display.showLogoOnDisplay();
		mainPanel.add(displayPanel, BorderLayout.NORTH);
		

		// RIGHTBAR PANEL - EAST�
		rightBarPanel.setLayout(new BorderLayout());
		mainPanel.add(rightBarPanel, BorderLayout.EAST);
		initRightBarDefault();


		// SCROLLPANE ARTICLE TABLE - WEST
		initArticleTable();
		mainPanel.add(getGuiScrollPaneArticleTable(), BorderLayout.WEST);
		
		
		//STATUSBAR SOUTH
		
		//JLabel statusbar = new JLabel("statusbar");
		JPanel statusbar = new JPanel();
		statusbar.setLayout(new GridLayout(1,4));
//		statusbar.setBorder(BorderFactory.createBevelBorder(1));
		
		
		Dimension statusBarDimension = new Dimension(new Dimension(TrDimensions.windowWidth,30)); 
		JPanel digitalClock = new DigitalClock();
		digitalClock.setPreferredSize(statusBarDimension);
		statusbar.add(digitalClock);
		statusbar.setBackground(Color.WHITE);
		statusbar.setSize(statusBarDimension);
		statusbar.setPreferredSize(statusBarDimension);
		
		statusbar.add(new JLabel("Z: "+openZSession.getCounter()));
		statusbar.add(new JLabel("Kassennummer Kassierername"));
		statusbar.add(new JLabel("Online-Status"));

		mainPanel.add(statusbar, BorderLayout.SOUTH);
		
	}
	
	public void cleanRightBar(){
		if(rightBarPanelCenter != null)
			rightBarPanel.remove(rightBarPanelCenter);
		if(rightBarPanelNorth != null)
			rightBarPanel.remove(rightBarPanelNorth);
		if(rightBarPanelSouth != null)
			rightBarPanel.remove(rightBarPanelSouth);
		if(rightBarPanelEast != null)
			rightBarPanel.remove(rightBarPanelEast);
		if(rightBarPanelWest != null)
			rightBarPanel.remove(rightBarPanelWest);
	}
	
	public void initRightBarDefault(){
		cleanRightBar();
		
		//Numpad into Nort
		JPanel NumPadWithSaleActions = new JPanel(new BorderLayout());
		NumPadWithSaleActions.add(new TrNumPadSaleScreen(textfieldOne),BorderLayout.WEST);
		
		//create Special actions for numpad
		NumPadWithSaleActions.add(new TrActionsForSaleNumPad(textfieldOne, display),BorderLayout.EAST);
		
		rightBarPanelNorth = NumPadWithSaleActions;
		rightBarPanelCenter = getGuiPanelActions();
	
		rightBarPanel.add(rightBarPanelCenter, BorderLayout.CENTER);
		rightBarPanel.add(rightBarPanelNorth, BorderLayout.NORTH);
		validate();
		repaint(50L);
	}
	
	
	public void initRightBarCheckout(){
		cleanRightBar();
		
		rightBarPanelCenter = getGuiPanelCheckoutActions();
		rightBarPanelNorth = new TrNumPadCurrency(textfieldOne);
		((TrNumPadCurrency) rightBarPanelNorth).setExactAmount(Double.valueOf(cart.getSumOfCartGross()) );
		rightBarPanel.add(rightBarPanelCenter, BorderLayout.CENTER);
		rightBarPanel.add(rightBarPanelNorth, BorderLayout.NORTH);
		validate();
		repaint(50L);
			
	}
	
	
	public void initRightBarSettings(){
		cleanRightBar();
		rightBarPanelCenter = getGuiPanelSettingsActions();
		rightBarPanel.add(rightBarPanelCenter, BorderLayout.CENTER);
		validate();
		repaint(50L);
	}


	private JScrollPane getGuiScrollPaneArticleTable(){
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(600, 400));
		scrollPane.setMaximumSize(new Dimension(600, 400));
		//scrollPane.setViewportBorder(new LineBorder(Color.WHITE));
		scrollPane.setBorder(new LineBorder(Color.WHITE));
		scrollPane.setBackground(Color.WHITE);

		return scrollPane;
	}
	
	private void addArticleToTable(final CartArticle article){

		 Object[] data = new Object[]{
				article.getQuantity(),
				article.getUnit(),
				article.getName(),
				article.getPriceGross(),
				article.getQuantity()*article.getPriceGross(),
				(!article.isDeposit() ||  (article.isDeposit() && article.isRefund()) ? gui.getIconDelete(): "")
		};
		
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		table.getColumnModel().getColumn(5).setCellRenderer(table.getDefaultRenderer(ImageIcon.class));


		//remove article with a click to the X button
			table.addMouseListener(new java.awt.event.MouseAdapter() {
			    @Override
			    public void mouseClicked(java.awt.event.MouseEvent evt) {
			        int row = table.rowAtPoint(evt.getPoint());
			        int column = table.columnAtPoint(evt.getPoint());
	
		            if (table.isRowSelected(row) && column == 5){
		            	
		            	//remove it, if its not a deposite article
		            	if(!cart.getArticleFromCart(row).isDeposit() ||  (cart.getArticleFromCart(row).isDeposit() && cart.getArticleFromCart(row).isRefund())){
		            		int dialogButton = JOptionPane.YES_NO_OPTION;
		            		int dialogResult = JOptionPane.showConfirmDialog (null, "<html><font color=#E90303>"+table.getModel().getValueAt(row, 2)+"</font>\n Wirklich entfernen?","Artikel Storno",dialogButton);
		            		if(dialogResult == JOptionPane.YES_OPTION){
		            			cart.removeArticlebyPosition(row);	    				
		            		}		            		
		            	}
		            	
		            }		      
		            table.clearSelection();  	
			     }
			
			});
	
		
		model.addRow( data);
	}
	
	private void removeArticleFromTableByPosition(int position ){

		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.removeRow(position);
	}
	
	//this method will delete the given Article from the table. it will lookup from bottom and will delete the first found
	private void removeArticleFromTableByArticle(Article article ){

		DefaultTableModel model = (DefaultTableModel) table.getModel();

		int position = -1;
		String compareWithFromTable = null;
		for(int i = table.getRowCount()-1; i <= 0; i--){
			compareWithFromTable = model.getValueAt(i, 2).toString();
			System.out.println(compareWithFromTable);
		}
		if(position > -1)
			model.removeRow(position);
	}
	
	
	private void initArticleTable() {
		String[] columNames = {  "Menge", "Einheit", "Bezeichnung", "Preis",	"Summe" , ""};
		
	
		//make table not editable
		TableModel model = new DefaultTableModel()
		  {

			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column)
		    {
		      return false;//This causes all cells to be not editable
		    }
		 };
		
		 
		 ((DefaultTableModel) model).setColumnIdentifiers(columNames);
		table = new JTable(model){
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Component prepareRenderer(
    		TableCellRenderer renderer, int row, int column)
    		{
    			Component c = super.prepareRenderer(renderer, row, column);
    			
    			//  Color row based on a cell value
    			//  Alternate row color

    			if (!isRowSelected(row)){
    				c.setBackground(row % 2 == 0 ? getBackground() : new Color(250,250,250));
    				//jc.setBorder(new LineBorder(GuiElements.buttonBackgroundColorGeneral) );	    				
    			}
    			

    			return c;
    		}
			
		};
		
		JTableHeader header = table.getTableHeader();
	      header.setBackground(GuiElements.buttonBackgroundColorGeneral);
	      header.setForeground(Color.white);	      
	      header.setBorder(BorderFactory.createLineBorder(GuiElements.buttonBackgroundColorGeneral));
	      Dimension headerDimension = header.getPreferredSize();
	      headerDimension.setSize(headerDimension.getWidth(), headerDimension.getHeight()+10);
	      header.setPreferredSize(headerDimension);
	      table.setBorder(BorderFactory.createEmptyBorder());
	      table.setShowGrid(true);
	      table.setGridColor(new Color(230,230,230));
		
		
		table.getColumnModel().getColumn(0).setMinWidth(50);
		table.getColumnModel().getColumn(0).setMaxWidth(50);
		table.getColumnModel().getColumn(1).setMinWidth(60);
		table.getColumnModel().getColumn(1).setMaxWidth(60);
		table.getColumnModel().getColumn(2).setMinWidth(150);
		
		
		
		table.setFont(new Font("SANS_SERIF", Font.PLAIN , 15));
		table.setFillsViewportHeight(true);


		table.setRowHeight(30);
		table.repaint();

		
	}

	private JPanel getGuiPanelActions() {
		
		
		actionsPanel = new JPanel();
		actionsPanel.setLayout(new GridLayout(2, 4, 0, 0));

		
		TrButtonDefault btnActionFree1 = new TrButtonDefault("", new Dimension(), false);
		actionsPanel.add(btnActionFree1);

		TrToggleButton btnActionFree2 = new TrToggleButton("", new Dimension(), false);
		actionsPanel.add(btnActionFree2);

		TrButtonDefault btnActionSettings = new TrButtonDefault("Einstellungen", new Dimension(), false);
		actionsPanel.add(btnActionSettings);
		
		TrButtonDefault btnActionCompleteCancellation = new TrButtonDefault("Verkauf abbrechen", new Dimension(), false);
		actionsPanel.add(btnActionCompleteCancellation);
		
		TrButtonDefault buttonActionZSumme = new TrButtonDefault("Z-Summe", new Dimension(), false);
		actionsPanel.add(buttonActionZSumme);

		TrButtonDefault btnActionCheckout = new TrButtonDefault("Checkout", new Dimension(), false);
		actionsPanel.add(btnActionCheckout);
		
		
		btnActionCompleteCancellation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				buttonCompleteCancalletionClicked();
			}
		});
		
		
		btnActionSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				buttonActionSettingsClicked();
			}
		});
		
		
		btnActionCheckout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				buttonActionCheckoutClicked();
			}
		});

		
		buttonActionZSumme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				buttonActionZSummeClicked();
			}
		});


		return actionsPanel;
	}


	private JPanel getGuiPanelSettingsActions(){


		int width = GuiElements.rightBarWidth;
		int height = GuiElements.rightBarHeight;
		
		// MAIN PANEL
		JPanel settingsMainPanel;
		settingsMainPanel = new JPanel();
		settingsMainPanel.setBackground(Color.WHITE);
		settingsMainPanel.setBounds(0, 0, width, height);
		settingsMainPanel.setSize(width, height);
		settingsMainPanel.setLayout(new BorderLayout(0, 0));
		settingsMainPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		
		//BUTTONS PANEL
		
		//Create empty Labels added the Grid
		
		int gridColumns = 3;
		int gridRows = 6;
		JPanel settingsButtonsPanel;
		settingsButtonsPanel = new JPanel();
		JPanel[][] panelHolder = new JPanel[gridRows][gridColumns];    
		settingsButtonsPanel.setBackground(Color.WHITE);
		settingsButtonsPanel.setPreferredSize(new Dimension(width, height));
		settingsButtonsPanel.setLayout(new GridLayout(gridRows,gridColumns,0,0));
		settingsButtonsPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

//		Dimension defaultDimension =new Dimension(width/gridColumns,height/gridRows);
		Dimension defaultDimension =new Dimension(200,200);
		
		for(int n = 0; n <gridRows ; n++) {
			for(int m = 0; m < gridColumns; m++) {
			
		      panelHolder[n][m] = new JPanel(new BorderLayout());
		      panelHolder[n][m].setMaximumSize(defaultDimension);
		      panelHolder[n][m].setMinimumSize(defaultDimension);
		      panelHolder[n][m].setSize(defaultDimension);
		      //panelHolder[n][m].setPreferredSize(defaultDimension);
		      panelHolder[n][m].setBackground(Color.WHITE);
		      panelHolder[n][m].add(new GuiElements().getStyledDefaultButton("",defaultDimension, false));
		      settingsButtonsPanel.add(panelHolder[n][m]);
		      
		      if(config.debugMode){
		    	  panelHolder[n][m].setBorder(BorderFactory.createLineBorder(Color.black));	
		    	  //panelHolder[n][m].add(new JLabel(n+" / "+m));
		      }
		      
		   }
		}
		settingsMainPanel.add(settingsButtonsPanel, BorderLayout.CENTER);


		JButton btnSettings_startImport = new GuiElements().getStyledDefaultButton("Import starten",panelHolder[1][1].getSize(), false);
		panelHolder[0][0].removeAll();
		panelHolder[0][0].add(btnSettings_startImport);
		btnSettings_startImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//FILENAME TO IMPORT
				
				 DebugScreen.getInstance().print( DataFileImporter.fullImportFileDirectory);
				 DebugScreen.getInstance().print(myDataImporter.getFullImportItemsFilename());
					String fileName = DataFileImporter.fullImportFileDirectory+myDataImporter.getFullImportItemsFilename();
				    DebugScreen.getInstance().print( DataFileImporter.fullImportFileDirectory);
					
					try {
						myDataImporter.fullImportItems(fileName);
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		});
		
		JButton btnSettings_zreport = new GuiElements().getStyledDefaultButton("Z-Bericht",panelHolder[1][1].getSize(), false);
		panelHolder[0][1].removeAll();
		panelHolder[0][1].add(btnSettings_zreport);
		btnSettings_zreport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//todo printout Z-report

				//Close the Z-Session
				ZSessionList zSessionList = new ZSessionList();
				try {
					zSessionList.closeOpenZSession();
					openZSession = null;
				} catch (ZSessionException e) {
					new TrDialogYesNo(e.getMessage(),"Fehler");
					e.printStackTrace();
				}
			}
		});
		
		
		JButton btnSettings_xreport = new GuiElements().getStyledDefaultButton("X-Bericht",panelHolder[1][1].getSize(), false);
		panelHolder[0][2].removeAll();
		panelHolder[0][2].add(btnSettings_xreport);
		btnSettings_xreport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
	
			}
		});
		
		
		JButton btnSettings_changeUser = new GuiElements().getStyledDefaultButton("Abmelden",panelHolder[1][1].getSize(), false);
		panelHolder[1][0].removeAll();
		panelHolder[1][0].add(btnSettings_changeUser);
		btnSettings_changeUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {

					state.changeState(PosState.posStateLogin);

					// Notify everybody that may be interested.
					for (LoginEventListener hl : listeners)
						hl.update("logout" );

				} catch (Exception e) {
					//e.printStackTrace();
					GuiElements.displayInfoMessageBox(e.getMessage());
				}
			}
		});
		
		

		
		
		JButton btnSettings_closeSettings = new GuiElements().getStyledAbortButton("<< Zurück",panelHolder[1][1].getSize(),false);
		panelHolder[5][0].removeAll();
		panelHolder[5][0].add(btnSettings_closeSettings);
		btnSettings_closeSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//close the settings
				changeStateToStateBefore();
			}
		});
		
		return settingsMainPanel;
		
		
	}

	private JPanel getGuiPanelCheckoutActions() {
		//BUTTONS PANEL
		
				//Create empty Labels added the Grid
				
				int gridColumns = 3;
				int gridRows = 2;
				JPanel settingsButtonsPanel;
				settingsButtonsPanel = new JPanel();
				JPanel[][] panelHolder = new JPanel[gridRows][gridColumns];
				settingsButtonsPanel.setBackground(Color.WHITE);
				settingsButtonsPanel.setPreferredSize(new Dimension(GuiElements.rightBarWidth, GuiElements.rightBarHeight-GuiElements.numpadHeight));	       
				settingsButtonsPanel.setLayout(new GridLayout(gridRows,gridColumns,0,0));
				
				
				for(int n = 0; n <gridRows ; n++) {
					for(int m = 0; m < gridColumns; m++) {
				      panelHolder[n][m] = new JPanel(new BorderLayout());
				      panelHolder[n][m].setPreferredSize(new Dimension(settingsButtonsPanel.getPreferredSize().width/gridColumns,settingsButtonsPanel.getPreferredSize().height/gridRows));
				      panelHolder[n][m].setBackground(Color.WHITE);
				      settingsButtonsPanel.add( panelHolder[n][m]);
				      
				      if(config.debugMode){
				    	  panelHolder[n][m].setBorder(BorderFactory.createLineBorder(Color.BLUE));	
				    	  panelHolder[n][m].add(new JLabel(n+" / "+m));
				    	  debug.print(String.valueOf(settingsButtonsPanel.getPreferredSize().width)+" / "+String.valueOf(settingsButtonsPanel.getPreferredSize().height));
				      }
				     
				      
				   }
				}
				
				
				JButton btnSettings_payment1 =  new GuiElements().getStyledDefaultButton("Bar",panelHolder[0][0].getPreferredSize(),false);
				panelHolder[0][0].add(btnSettings_payment1);
				btnSettings_payment1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						setPaymentType("Bar");
					}
				});
				
				JButton btnSettings_payment2 =  new GuiElements().getStyledDefaultButton("EC",panelHolder[0][1].getPreferredSize(),false);
				panelHolder[0][1].add(btnSettings_payment2);
				btnSettings_payment2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						setPaymentType("EC");
					}
				});
				
				JButton btnSettings_payment3 =  new GuiElements().getStyledDefaultButton("KK",panelHolder[0][2].getPreferredSize(),false);
				panelHolder[0][2].add(btnSettings_payment3);
				btnSettings_payment3.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						setPaymentType("KK");
					}
				});
				
				JButton btnSettings_closeCheckout =  new GuiElements().getStyledAbortButton("<< Zurück",panelHolder[1][0].getPreferredSize(),false);
				panelHolder[1][0].add(btnSettings_closeCheckout);
				btnSettings_closeCheckout.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						//close the settings
						changeStateToStateBefore();
					}
				});
				
				JButton btnSettings_FREE =  new GuiElements().getStyledDefaultButton("Free",panelHolder[1][1].getPreferredSize(),false);
				panelHolder[1][1].add(btnSettings_FREE);
				btnSettings_FREE.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						//close the settings
						//settingsFrame.dispose();
					}
				});

				
				JButton btnSettings_finilizeCheckout =  new GuiElements().getStyledCtaButton("OK",panelHolder[1][2].getPreferredSize(),false);
				panelHolder[1][2].add(btnSettings_finilizeCheckout);
				btnSettings_finilizeCheckout.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						//close the settings
							finilizeTransaction();
							
						
					}
				});
				
			return settingsButtonsPanel;

	}
	
	private void finilizeTransaction(){
		try {
			cart.setPaymentGiven(textfieldOne.getText());
			Boolean savedCorrectly = cart.finilize();
			if(savedCorrectly == true){
				state.changeState(PosState.posStateReady);
			}
		} catch (CheckoutPaymentException e) {
			GuiElements.displayErrorMessageBox(e.getMessage());
			debug.printStackTrace(e);
		} catch (CheckoutGeneralException e) {
			GuiElements.displayErrorMessageBox(e.getMessage());
			debug.printStackTrace(e);
		} catch (ParseException e) {
			GuiElements.displayErrorMessageBox(e.getMessage());
			debug.printStackTrace(e);
		}
		
		
	}
	
	private void setPaymentType(String type){
		cart.setPaymentType(type);
		display.showSimpleTextOnDisplay(type);
	}
	
	
	private void buttonActionSettingsClicked(){
		state.changeStateToSettings(false);
	}
	
	
	
	private void buttonActionCheckoutClicked(){
		state.changeStateToCheckout(false);
	}
	
	
	private void buttonActionZSummeClicked(){
		display.showCompletePriceOnDisplay(cart);
	}
	


	
	/*
	 * This method is triggered, when the cancellation for sale is clicked.
	 * It will ask for confirmation and trigger te Reset Tthe pos (changeStateToReady)...
	 */
	private void buttonCompleteCancalletionClicked(){
		TrDialogYesNo yesNo = new TrDialogYesNo("<html><font color=#E90303>Gesamten Verkauf wirklich abbrechen?</font>", "Zettelabbruch");
		if(yesNo.isYes()){
			state.changeState(PosState.posStateReady);			
		}
	}
	

	/*
	 *This emethod is resetting the whole pos and preparing it to a new sales.
	 *It should  be called, after finishing a sale or by aborting a sale (cancellation).
	 */
	public void changeStateToReady(){

		//debug
		//DebugScreen.getInstance().print("textfieldOne: "+textfieldOne.getText());

		//clean the cart object
		createCart();
		
		//clean all price displays
		display.clearDisplay();
		
	
		//cleear the textfield for plu
		textfieldOne.setValue(null);
		
		// change rightbar
		initRightBarDefault();
		
		//clean the table on operators screen
		DefaultTableModel dm = (DefaultTableModel) table.getModel();
		int rowCount = dm.getRowCount();
		//Remove rows one by one from the end of the table
		for (int i = rowCount - 1; i >= 0; i--) {
		    dm.removeRow(i);
		}

		Buffer.getInstance().resetAllBuffers();
		
		//set the ready status
		//display.showSimpleTextOnDisplay("Bereit");
		display.showLogoOnDisplay();
		
		validate();
		repaint(50L);
	}


	//predefined methods to make the live easier
	/*
	 * THis method makes the pos ready to get deposit
	 */
	public void changeStateToRefundDeposit(){
		TrDisplayForCashier.getInstance().clearDisplay();
		
		TrDisplayForCashier.getInstance().showSimpleTextOnDisplay("Pfandrücknahme");

	}
	

	
	/*
	 * This method is mostly used to get back to selling screen
	 */
	private void changeStateToSellingProcess(boolean clearDisplay){


		if(clearDisplay == true){
			TrDisplayForCashier.getInstance().clearDisplay();
			//showSimpleTextOnDisplay("");
		}
		//cleear the textfield for plu
		textfieldOne.setValue(null);
		textfieldOne.setText("");
		
		//clear the barcode buffer
		barcodeBuffer = "";
		Buffer.getInstance().resetPosQuantityBuffer();

		initRightBarDefault();


	}

	
	/*
	 * THis method cleansup every part of pos and prepares everything for checkout
	 */
	public void changeStateToCheckout(){
		
		
		//debug
		DebugScreen.getInstance().print("textfieldOne: "+textfieldOne.getText());

		
		display.clearDisplay();
		
		//cleear the textfield for plu
		textfieldOne.setValue("0,00");
//		textfieldOne.requestFocus();
		
		//display.showSimpleTextOnDisplay("Bitte Betrag eingeben!");
		display.showCompletePriceOnDisplay(cart);
		initRightBarCheckout();
	}
	
	
	/*
	 * this method depands on the variabke posStatusBefore and change back to it, it is used mostly to get back to an old screen
	 */
	public void changeStateToStateBefore(){
		 state.changeState(state.getStateBefore());
	}
	
	/*
	 * THis method cleansup every part of pos and prepares everything for settings
	 */
	public void changeStateToSettings(){
		
		//debug
		DebugScreen.getInstance().print("textfieldOne: "+textfieldOne.getText());

		
		display.clearDisplay();
		display.showSimpleTextOnDisplay("Option auswählen!");
		initRightBarSettings();
	}


	/*
	 * This method is mostly used to gdo login logout operations
	 */
	private void changeStateToLogin(boolean clearDisplay)  {

		DebugScreen.getInstance().print("Login/Logout");

		if(clearDisplay == true){
			TrDisplayForCashier.getInstance().clearDisplay();
			//showSimpleTextOnDisplay("");
		}

		//clear the barcode buffer
		barcodeBuffer = "";
		Buffer.getInstance().resetPosQuantityBuffer();


		initRightBarDefault();

		//logout operation
		try {

			if(salesUser!=null)
				salesUser.logout();

			loginmask = new TrUserLogin();
			//register observer for login input

			LoginObserver lo = new LoginObserver();
			loginmask.addListener(lo);
			//enable login screen
			//User logged in?


		} catch (Exception e) {
			GuiElements.displayErrorMessageBox("Logout nicht möglich.");
			DebugScreen.getInstance().print(e.getMessage());
		}

	}







}
