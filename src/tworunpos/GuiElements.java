package tworunpos;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Formatter;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class GuiElements {

	
	static final String iconImageDelete = "images/icon_remove.png";
	
	
	static Color buttonBackgroundColorGeneral = new Color(192, 191, 171);
	static Color buttonForegroundColorGeneral = new Color(255, 255, 255);
	static Color buttonBorderColorGeneral = Color.WHITE;
	static Border buttonBorderGeneral = BorderFactory.createLineBorder(buttonBorderColorGeneral, 2);

	
	static Color buttonBackgroundColorHighlight = new Color(118, 166, 166);
	static Color buttonForegroundColorHighlight = Color.WHITE;
	static Color buttonBorderColorHighlight = Color.WHITE;
	static Border buttonBorderHighlight = BorderFactory.createLineBorder(buttonBorderColorGeneral, 2);
	
	static Color buttonBackgroundColorAbort = new Color(118, 166, 166);
	static Color buttonForegroundColorAbort = new Color(255,230,206);
	static Color buttonBorderColorAbort = Color.WHITE;
	
	static Color buttonBackgroundColorCTA = new Color(192,109,95);
	static Color buttonForegroundColorCTA = new Color(255,230,206);
	static Color buttonBorderColorCTA = Color.WHITE;
	
	
	static final int alertTypeError = 0;
	static final int alertTypeSuccess = 1;
	static final int alertTypeInfo = 2;
	
	static final int numPadNotFormatted = 0;
	static final int numPadCurrencyFormatted = 1;
	
	
	public static final int rightBarWidth = 418;
	static final int rightBarHeight = 668 ;
	
	static final int numpadHeight = 400;
	
	
	public static void displayErrorMessageBox(String text){
		System.out.println(text);
		 GuiElements.showAlertBox(text, alertTypeError);
	}
	
	public static void displaySuccessMessageBox(String text){
		System.out.println(text);
		GuiElements.showAlertBox(text, alertTypeSuccess);

	}
	
	public static void displayInfoMessageBox(String text){
		System.out.println(text);
		GuiElements.showAlertBox(text, alertTypeInfo);
	}
	
	public static void showAlertBox(String text, int messageType){
		
		JLabel messageLabel = new JLabel("<html>"+text+"</html>");
		final JFrame messageFrame = new JFrame();
		messageFrame.setUndecorated(true);
		

		
		Color backgroundColor = new Color(255,255,255);
		Color foregroundColor = new Color(50,50,50);
		Color borderColor  = new Color(50,50,50);
		
		//define colors depending on messagetype
		switch(messageType){
			case alertTypeError:{
				backgroundColor = new Color(196,65,60);
				foregroundColor = new Color(255,255,255);
				borderColor = new Color(255,255,255);
				break;
			}
			case alertTypeSuccess:{
				backgroundColor = new Color(113,196,91);
				foregroundColor = new Color(255,255,255);
				borderColor = new Color(255,255,255);
				break;
			}
			case alertTypeInfo:{
				backgroundColor = new Color(255,255,255);
				foregroundColor = new Color(80,80,80);
				borderColor  = new Color(80,80,80);
				break;
			}
		}

		
		
		
		messageFrame.getContentPane().setPreferredSize(new Dimension(300,100));
		messageFrame.setSize(300, 100);
		messageFrame.setAlwaysOnTop(true);
		messageFrame.setResizable(false);
		messageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		messageFrame.pack();
		messageFrame.setVisible(true);
		messageFrame.setLocationRelativeTo(null);
			
		// MAIN PANEL
		JPanel settingsMainPanel;
		settingsMainPanel = new JPanel();
		
		settingsMainPanel.setBounds(0, 0, 300, 100);
		settingsMainPanel.setLayout(new BorderLayout(0, 0));
		settingsMainPanel.setBorder(BorderFactory.createLineBorder(borderColor, 2));
		messageFrame.getContentPane().add(settingsMainPanel);

		
		settingsMainPanel.setBackground(backgroundColor);
		messageLabel.setBackground(backgroundColor);
		messageLabel.setForeground(foregroundColor);
		messageLabel.setPreferredSize(new Dimension(100, 50));

		
		messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		settingsMainPanel.add(messageLabel,BorderLayout.NORTH);
		
		
		//Close Button
		JButton closeButton = new JButton("Schließen");
		closeButton.setBackground(backgroundColor);
		closeButton.setForeground(foregroundColor);
		closeButton.setPreferredSize(new Dimension(100, 50));
		closeButton.setBorder(BorderFactory.createLineBorder(borderColor, 1));
		settingsMainPanel.add(closeButton,BorderLayout.SOUTH);
		closeButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e)
		    {
		    	messageFrame.dispose();
		    }
		});
			

		
		messageFrame.pack();
		
		
	}
	
	
	public final JProgressBar getStyledDefaultProgressBar(int start, int end){
		JProgressBar progressBar = new JProgressBar(start, end);
		  progressBar.setStringPainted(true);
		  //progressBar.setBackground(GuiElements.buttonBackgroundColorGeneral);
		  //progressBar.setForeground(GuiElements.buttonForegroundColorGeneral);
		  progressBar.setBorder(GuiElements.buttonBorderGeneral);
		  return progressBar;
	}

	
	public final JButton getStyledDefaultButton(String text, Dimension dim, Boolean border){
		JButton button = new JButton(text);
		button.setOpaque(true);
		//button.setBackground(GuiElements.buttonBackgroundColorGeneral);
		//button.setForeground(GuiElements.buttonForegroundColorGeneral);
		if(border == true){
			button.setBorder(GuiElements.buttonBorderGeneral);
		}else{
			button.setBorder(new LineBorder(Color.WHITE));			
		}
		

		button.setMargin(new Insets(0, 0, 0, 0));
		button.setFocusPainted(false);
		button.setSize(dim);
		button.setPreferredSize(dim);
		  return button;
	}

	public final JButton getStyledAbortButton(String text, Dimension dim, Boolean border){
		JButton button = new JButton(text);
		button.setOpaque(true);
		//button.setForeground(GuiElements.buttonForegroundColorAbort);
		//button.setBackground(GuiElements.buttonBackgroundColorAbort);
		if(border == true){
			button.setBorder(GuiElements.buttonBorderGeneral);
		}else{
			button.setBorder(new LineBorder(Color.WHITE));			
		}
		button.setSize(dim);
		button.setPreferredSize(dim);
		return button;
	}
	
	public final JButton getStyledCtaButton(String text, Dimension dim, Boolean border){
		JButton button = new JButton(text);
		button.setOpaque(true);
		//button.setForeground(GuiElements.buttonForegroundColorCTA);
		//button.setBackground(GuiElements.buttonBackgroundColorCTA);
		if(border == true){
			button.setBorder(GuiElements.buttonBorderGeneral);
		}else{
			button.setBorder(new LineBorder(Color.WHITE));			
		}
		button.setSize(dim);
		button.setPreferredSize(dim);
		return button;
	}
	

	public final JButton getStyledHighlightButton(String text, Dimension dim, Boolean border){
		JButton button = new JButton(text);
		button.setOpaque(true);
		//button.setBackground(GuiElements.buttonBackgroundColorHighlight);
		//button.setForeground(GuiElements.buttonForegroundColorHighlight);
		button.setText(text);
		if(border == true){
			button.setBorder(GuiElements.buttonBorderHighlight);
		}else{
			button.setBorder(new LineBorder(Color.WHITE));			
		}
		button.setSize(dim);
		button.setPreferredSize(dim);
		  return button;
	}
	
	
	public final ImageIcon getIconDelete(){
		
		try {
			InputStream imageStream=this.getClass().getClassLoader().getResourceAsStream(iconImageDelete);
			
			if(imageStream == null){
				return null;				
			}
			
			BufferedImage image = ImageIO.read(imageStream);
			ImageIcon icon = new ImageIcon(image);
			return icon;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return null;
	}
	
	
	
	

	
	
	private void setValueForTextfield(String value,JTextField textfield, int format){
		switch(format){
			case 1 : setFormattedValueCurrencyForTextfield(value,textfield);break;
			case 0 : setNonFormattedValueForTextfield(value, textfield);
			default:;break;
			
		}
		
	}
	
	private void setNonFormattedValueForTextfield(String value, JTextField textfield){
		textfield.setText(textfield.getText() + value);
	}
	
	
	private void setFormattedValueCurrencyForTextfield(String setValue, JTextField textfield){
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
	
}
