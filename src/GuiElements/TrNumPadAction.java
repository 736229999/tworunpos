package GuiElements;

import tworunpos.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class TrNumPadAction extends TrNumPad{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final int numpadHeight = 400;
	Integer value;


	private List<LoginEventListener> listeners = new ArrayList<LoginEventListener>();
	public void addListener(LoginEventListener toAdd) {
		listeners.add(toAdd);
	}


	/*
	 * Used for User login
	 *
	 * Params
	 * 	JTextfield: textfield, the Numbad Values should be written into
	 * 	textfieldFormat: 0: no format, 1: currency with 2 zeros
	 * return a JPanel containing a full numpad designed with jButtons for touchscreens
	 * 
	 */
	public  TrNumPadAction(final JTextField textFieldOne) {

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

		JButton btnKeyPadPlus = new TrButtonHighlight("OK", new Dimension(), false);
		this.add(btnKeyPadPlus);
		btnKeyPadPlus.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				//todo move this away from here
				//close applicaiton by hidden code
				if(textFieldOne.getText().equals("1453"))
					mainApplication.close();


				value =  Integer.parseInt( textFieldOne.getText());
				//User.getInstance().login( Integer.parseInt( textFieldOne.getText() ));

				// Notify everybody that may be interested.
				for (LoginEventListener hl : listeners)
					hl.update("login" );

			}
		});
		
		btnKeyPadDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(textFieldOne.getText().length() < 1)
					return;
				textFieldOne.setText("");
				textFieldOne.requestFocus();
			}
		});
		

	}


	public Integer getValue() {
		return value;
	}

	private void setValueForTextfield(String value, JTextField textfield){
		textfield.setText(textfield.getText() + value);
	}

}
