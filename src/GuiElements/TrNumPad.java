package GuiElements;

import  tworunpos.GuiElements;

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

public class TrNumPad extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final int numpadHeight = 400;
	
	/*
	 * 
	 * Params
	 * 	JTextfield: textfield, the Numbad Values should be written into
	 * 	textfieldFormat: 0: no format, 1: currency with 2 zeros
	 * return a JPanel containing a full numpad designed with jButtons for touchscreens
	 * 
	 */
	public TrNumPad(final JTextField textFieldOne) {

		

	}
	
	private void setValueForTextfield(String value,JTextField textfield){
		textfield.setText(textfield.getText() + value);
	}
	
}
