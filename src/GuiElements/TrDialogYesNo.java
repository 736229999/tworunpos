package GuiElements;

import javax.swing.JOptionPane;

public class TrDialogYesNo extends JOptionPane {

	private int yesNo = 1;
	
	public TrDialogYesNo(String text, String title){
		
		yesNo = JOptionPane.showConfirmDialog(null,text,title,YES_NO_OPTION);
		
	}
	
	public boolean isYes(){
		return (yesNo == JOptionPane.YES_OPTION ? true : false);
	}
}
