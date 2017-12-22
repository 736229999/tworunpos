package GuiElements;

import Devices.ComScaleDialog06;
import Exceptions.DeviceException;
import tworunpos.Article;
import tworunpos.ArticleList;
import tworunpos.Buffer;
import tworunpos.Cart;
import tworunpos.DebugScreen;
import  tworunpos.GuiElements;
import tworunpos.PosState;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;



public class TrActionsForSaleNumPad extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final int numpadHeight = 400;
	
	ArticleList articleList = ArticleList.getInstance();
	Cart cart = Cart.getInstance();
	JTextField textfield = null;
	TrDisplay display = null;
	Buffer buffer = Buffer.getInstance();
	
	/*
	 * 
	 * Params
	 * 	JTextfield: textfield, the Numbad Values should be written into
	 * 	textfieldFormat: 0: no format, 1: currency with 2 zeros
	 * return a JPanel containing a full numpad designed with jButtons for touchscreens
	 * 
	 */
	public TrActionsForSaleNumPad(JTextField textfield, TrDisplay display) {
		this.textfield = textfield;
		this.display = display;
		
		int buttonFontSize = 10;

		//JPanel this = new JPanel();
		this.setLayout(new GridLayout(5, 1, 0, 0));
		this.setPreferredSize(new Dimension(GuiElements.rightBarWidth/5, numpadHeight));
		this.setMaximumSize(new Dimension(GuiElements.rightBarWidth/5, numpadHeight));
//		this.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		// define all KeyPad Buttons

		

		JButton btnKeyPad_x = new TrButtonDefault("×", new Dimension(), false);
		this.add(btnKeyPad_x);
		btnKeyPad_x.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				buttonActionMultiplicatedClicked();
			}
		});
		
		JButton btnAction_plu = new TrButtonDefault("PLU", new Dimension(), false);
		this.add(btnAction_plu);
		btnAction_plu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				buttonActionPluClicked();
			}
		});

		JButton btnAction_barcode = new TrButtonDefault("Barcode", new Dimension(), false);
		this.add(btnAction_barcode);
		btnAction_barcode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				buttonActionBarcodeClicked();
			}
		});
		
		
		final TrToggleButton buttonActionDepositRefund = new TrToggleButton("Pfandrücknahme", new Dimension(), false);
		this.add(buttonActionDepositRefund);
		buttonActionDepositRefund.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(buttonActionDepositRefund.isSelected()){
					PosState.getInstance().changeState(PosState.posStateRefundDeposit);
				}else{
					PosState.getInstance().changeState(PosState.posStateSellingProcess);
				}
			}
		});

		final TrToggleButton buttonActionRefund = new TrToggleButton("Retoure", new Dimension(), false);
		this.add(buttonActionRefund);
		buttonActionRefund.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(buttonActionRefund.isSelected()){
					PosState.getInstance().changeState(PosState.posStateRefund);
				}else{
					PosState.getInstance().changeState(PosState.posStateSellingProcess);
				}
			}
		});


	}
	
	private void buttonActionBarcodeClicked(){
		//barcode will stored in this variable
		String barcode = textfield.getText();
		try {
			if(!barcode.matches("[0-9]+") || barcode.length() < 8){
				throw new Exception("Not able to parse textfield for Barcode");
			}
			
			DebugScreen.getInstance().print("try to find Barcode: "+barcode);
			try{
				Article tempArticle = articleList.lookupArticleByBarcode(barcode);
				DebugScreen.getInstance().print("ArticleFoundByBarcode: "+tempArticle.getMyDocument());
				cart.addArticle(tempArticle,Buffer.getInstance().getPosQuantityBuffer());
			}catch (DeviceException e){
				System.out.println("Waagen Statuscode: "+e.getMessage()+" - "+Integer.parseInt(e.getMessage()));
				String message = "undefiniertes Waagenresultat";
				switch(Integer.parseInt(e.getMessage())){
					case 0: message = ComScaleDialog06.errorCode00;break;
					case 1: message = ComScaleDialog06.errorCode01;break;
					case 2: message = ComScaleDialog06.errorCode02;break;
					case 10: message = ComScaleDialog06.errorCode10;break;
					case 11: message = ComScaleDialog06.errorCode11;break;
					case 12: message = ComScaleDialog06.errorCode12;break;
					case 13: message = ComScaleDialog06.errorCode13;break;
					case 20: message = ComScaleDialog06.errorCode20;break;
					case 21: message = ComScaleDialog06.errorCode21;break;
					case 22: message = ComScaleDialog06.errorCode22;break;
					case 30: message = ComScaleDialog06.errorCode30;break;
					case 31: message = ComScaleDialog06.errorCode31;break;
					case 32: message = ComScaleDialog06.errorCode32;break;
					case 33: message = ComScaleDialog06.errorCode33;break;
					case 34: message = ComScaleDialog06.errorCode34;break;
				}



				display.showSimpleTextOnDisplay(message);
			}
			catch (Exception e) {
				DebugScreen.getInstance().print("Article not found by Barcode: "+barcode);
				DebugScreen.getInstance().printStackTrace(e);
				display.showSimpleTextOnDisplay("Artikel nicht gefunden");
				
				
			}	
			
			
		} catch (Exception e) {
			barcode = "";
			DebugScreen.getInstance().print(e.getMessage());
			DebugScreen.getInstance().printStackTrace(e);
			display.showSimpleTextOnDisplay("Artikel nicht gefunden - ungültiges EAN-Format");
		}
		
		
		PosState.getInstance().changeStateToSellingProcess(false);
			
	}
	
	private void buttonActionMultiplicatedClicked(){
		try {
			buffer.setPosQuantityBuffer(textfield.getText());
			textfield.setText(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			display.showSimpleTextOnDisplay(e.getMessage());
		}
	}
	
	private void buttonActionPluClicked(){
			
			//plu will stored in this variable
			int one = 0;
			
			try {
				one = Integer.parseInt(textfield.getText());
				
			} catch (NumberFormatException e) {
				one = -1;
				DebugScreen.getInstance().print("Not able to parse textfield for PLU");
				DebugScreen.getInstance().printStackTrace(e);
			}
			
			DebugScreen.getInstance().print("try to find plu: "+one);
			
			if(one > -1){
				try{
					Article tempArticle = articleList.lookupArticlebyPlu(one);
					DebugScreen.getInstance().print("Article found by PLU: "+tempArticle.getMyDocument());
					cart.addArticle(tempArticle, Buffer.getInstance().getPosQuantityBuffer());
				}catch (DeviceException e){
					display.showSimpleTextOnDisplay(e.getMessage());
				}
				catch (Exception e) {
					DebugScreen.getInstance().print("Article not found by PLU: "+one);
					DebugScreen.getInstance().printStackTrace(e);
					display.showSimpleTextOnDisplay("PLU nicht gefunden");
					TrSounds.fail();
					
				}			
			}else{
				DebugScreen.getInstance().print("Invalid PLU-Entry: "+one);
				display.showSimpleTextOnDisplay("Ungültige PLU-Eingabe");
				TrSounds.fail();
			}
			
			PosState.getInstance().changeStateToSellingProcess(false);

			
	}
	

}
