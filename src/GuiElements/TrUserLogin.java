package GuiElements;

import  tworunpos.GuiElements;
import tworunpos.LoginEventListener;
import tworunpos.User;
import tworunpos.tworunPos;

import java.awt.BorderLayout;
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
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.bson.NewBSONDecoder;

public class TrUserLogin extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final int loginWindowHeight = TrDimensions.windowHeight;
	static final int loginWindowWidth = TrDimensions.windowWidth;
	
	static final int loginMaskHeight = 500;
	static final int loginMaskWidth = 300;

	private TrNumPadAction numPad;
	
	JTextField textfield;
	
	/*
	 * 
	 * Params
	 * 	JTextfield: textfield, the Numbad Values should be written into
	 * 	textfieldFormat: 0: no format, 1: currency with 2 zeros
	 * return a JPanel containing a full numpad designed with jButtons for touchscreens
	 * 
	 */
	public TrUserLogin() {

		JPanel generalPanel =  new JPanel();
		generalPanel.setLayout(new BoxLayout(generalPanel, BoxLayout.Y_AXIS));
		JPanel loginPanel =  new JPanel(new BorderLayout());
		
		
		textfield = new JTextField();
		textfield.setHorizontalAlignment(JTextField.CENTER);
		Font font1 = new Font("SansSerif", Font.BOLD, 20);
		textfield.setFont(font1);
		textfield.setPreferredSize(new Dimension(loginMaskWidth, 60));
		numPad = new TrNumPadAction(textfield);


		loginPanel.setPreferredSize(new Dimension(loginMaskWidth,loginMaskHeight));
		loginPanel.add(textfield,BorderLayout.NORTH);
		loginPanel.add(numPad,BorderLayout.CENTER);

		
		generalPanel.add(loginPanel,BorderLayout.CENTER);
		
		
		this.getContentPane().add(generalPanel);
		this.setUndecorated(true);
		this.setBackground(Color.white);
		this.getContentPane().setBackground(Color.white);
		this.getContentPane().setPreferredSize(new Dimension(loginWindowWidth,loginWindowHeight));
		this.isAlwaysOnTop();
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
		this.repaint();
	}
	
	/*
	 * Clears the inputfield for userid entry
	 */
	public void clearInput(){
		textfield.setText("");
	}


	public Integer getInputValue(){
		return   Integer.parseInt( textfield.getText());
	}

	public void addListener(LoginEventListener toAdd) {
		numPad.addListener(toAdd);
	}
	
	
	
}
