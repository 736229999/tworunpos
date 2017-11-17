package GuiElements;

import  tworunpos.GuiElements;
import tworunpos.Helpers;

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

public class TrNumPadCurrency extends TrNumPad {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final int numpadHeight = 400;
	private Double exactAmount = 0.00D;
	
	/*
	 * 
	 * Params
	 * 	JTextfield: textfield, the Numbad Values should be written into
	 * 	textfieldFormat: 0: no format, 1: currency with 2 zeros
	 * return a JPanel containing a full numpad designed with jButtons for touchscreens
	 * 
	 */
	public TrNumPadCurrency(final JTextField textFieldOne) {

		super(textFieldOne);

		int buttonFontSize = 17;

		//JPanel this = new JPanel();
		this.setLayout(new GridLayout(4, 3, 0, 0));
		this.setPreferredSize(new Dimension(GuiElements.rightBarWidth, numpadHeight));
		this.setMaximumSize(new Dimension(GuiElements.rightBarWidth, numpadHeight));
//		this.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		// define all KeyPad Buttons

		JButton btnKeyPad_1 = new TrButtonHighlight("1", new Dimension(), false);
		this.add(btnKeyPad_1);
		btnKeyPad_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setValueForTextfield("1",textFieldOne);
			}
		});

		JButton btnKeyPad_2 = new TrButtonHighlight("2", new Dimension(), false);
		this.add(btnKeyPad_2);
		btnKeyPad_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setValueForTextfield("2",textFieldOne);
				
			}
		});

		JButton btnKeyPad_3 = new TrButtonHighlight("3", new Dimension(), false);
		this.add(btnKeyPad_3);
		btnKeyPad_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setValueForTextfield("3",textFieldOne);
			}
		});

		JButton btnKeyPad_4 = new TrButtonHighlight("4", new Dimension(), false);
		this.add(btnKeyPad_4);
		btnKeyPad_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setValueForTextfield("4",textFieldOne);
			}
		});

		JButton btnKeyPad_5 = new TrButtonHighlight("5", new Dimension(), false);
		this.add(btnKeyPad_5);
		btnKeyPad_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setValueForTextfield("5",textFieldOne);
			}
		});

		JButton btnKeyPad_6 = new TrButtonHighlight("6", new Dimension(), false);
		this.add(btnKeyPad_6);
		btnKeyPad_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setValueForTextfield("6",textFieldOne);
			}
		});

		JButton btnKeyPad_7 = new TrButtonHighlight("7", new Dimension(), false);
		this.add(btnKeyPad_7);
		btnKeyPad_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setValueForTextfield("7",textFieldOne);
			}
		});

		JButton btnKeyPad_8 = new TrButtonHighlight("8", new Dimension(), false);
		this.add(btnKeyPad_8);
		btnKeyPad_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setValueForTextfield("8",textFieldOne);
			}
		});

		JButton btnKeyPad_9 = new TrButtonHighlight("9", new Dimension(), false);
		this.add(btnKeyPad_9);
		btnKeyPad_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setValueForTextfield("9",textFieldOne);
			}
		});

		JButton btnKeyPadDelete = new TrButtonHighlight("C", new Dimension(), false);
		btnKeyPadDelete.setFont(new Font("Arial", Font.BOLD, buttonFontSize ));
		btnKeyPadDelete.setBorder(new LineBorder(Color.WHITE));
		this.add(btnKeyPadDelete);

		JButton btnKeyPad_0 = new TrButtonHighlight("0", new Dimension(), false);
		this.add(btnKeyPad_0);
		btnKeyPad_0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setValueForTextfield("0",textFieldOne);
			}
		});

		JButton btnKeyPadPlus = new TrButtonHighlight("exakt", new Dimension(), false);
		this.add(btnKeyPadPlus);
		btnKeyPadPlus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Double exactAmountForDisplay = Helpers.roundForCurrency(exactAmount);
				textFieldOne.setText(exactAmountForDisplay.toString().replace(".", ","));
			}
		});
		
		btnKeyPadDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(textFieldOne.getText().length() < 1)
					return;
				textFieldOne.setText("0,00");
				textFieldOne.requestFocus();
			}
		});
		

	}
	
	private void setValueForTextfield(String setValue,JTextField textfield){
	
		String getText = ( textfield.getText().isEmpty() ? "0" : textfield.getText() ) ;
		try {
			DecimalFormatSymbols symbols = new DecimalFormatSymbols();
			symbols.setDecimalSeparator(',');
			DecimalFormat format = new DecimalFormat("0.##");
			format.setDecimalFormatSymbols(symbols);
			Double oldVal = format.parse(getText).doubleValue();
			Double valToAdd = Double.parseDouble(setValue);
			Double val = (oldVal*10)+(valToAdd/100);
			textfield.setText( String.format( "%.2f", val ));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setExactAmount(Double exactAmount) {
		this.exactAmount = exactAmount;
	}
}
