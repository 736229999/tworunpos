package GuiElements;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class TrToggleButton extends JToggleButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	static Color buttonBorderColorGeneral = Color.WHITE;
	static Color buttonBackgroundColorGeneral = new Color(192, 191, 150);
	static Color buttonForegroundColorGeneral = new Color(255, 255, 255);
	static Border buttonBorderGeneral = BorderFactory.createLineBorder(buttonBorderColorGeneral, 2);

	
	public TrToggleButton(String text, Dimension dim, Boolean border){		
		this.setOpaque(true);
		this.setBackground(buttonBackgroundColorGeneral);
		this.setForeground(buttonForegroundColorGeneral);
		this.setText(text);
		if(border == true){
			this.setBorder(buttonBorderGeneral);
		}else{
			this.setBorder(new LineBorder(Color.WHITE));			
		}
		this.setSize(dim);
		this.setPreferredSize(dim);
		;
	}
}
