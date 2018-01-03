package GuiElements;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class TrButtonHighlight extends TrButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	static Color buttonBackgroundColorHighlight = Color.RED;
	static Color buttonForegroundColorHighlight = Color.WHITE;
	static Color buttonBorderColorHighlight = Color.WHITE;
	static Border buttonBorderHighlight = BorderFactory.createLineBorder(buttonBorderColorGeneral, 2);
	
	
	public TrButtonHighlight(String text, Dimension dim, Boolean border){		
		//this.setOpaque(true);
		//this.setBackground(buttonBackgroundColorHighlight);
		//this.setForeground(buttonForegroundColorHighlight);
		this.setText(text);
		if(border == true){
			this.setBorder(buttonBorderHighlight);
		}else{
			this.setBorder(new LineBorder(Color.WHITE));			
		}
		this.setSize(dim);
		this.setPreferredSize(dim);
		//setContentAreaFilled(false);
		//setFocusPainted(false);

	}

	
}
