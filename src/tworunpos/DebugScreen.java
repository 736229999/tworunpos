package tworunpos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class DebugScreen extends JFrame {

	// Eine (versteckte) Klassenvariable vom Typ der eigene Klasse
	static DebugScreen instance;
	// Verhindere die Erzeugung des Objektes �ber andere Methoden
	private DebugScreen() {}
	// Eine Zugriffsmethode auf Klassenebene, welches dir '''einmal''' ein konkretes 
	// Objekt erzeugt und dieses zur�ckliefert.
	// Durch 'synchronized' wird sichergestellt dass diese Methode nur von einem Thread 
	// zu einer Zeit durchlaufen wird. Der n�chste Thread erh�lt immer eine komplett 
	// initialisierte Instanz.
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTextArea textarea =new JTextArea("Debug Mode started\n");
	
	public DebugScreen(Boolean debugMode){
		
		if(!debugMode)
			return;
		
		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		int width=200;
		int height = screenDim.height/2;
		Dimension debugScreenDim = new Dimension(width,(int) height-50); 
		
		JPanel debugPanel = new JPanel();
		debugPanel.setLayout(new BorderLayout(0,0));
		debugPanel.setBackground(new Color( 255, 255, 255));
		
		this.getContentPane().add(debugPanel);
		
		
		debugPanel.add(textarea, BorderLayout.CENTER);
	
		this.setBackground(new Color( 255, 255, 255));


		this.getContentPane().setPreferredSize(debugScreenDim);
		this.setSize(debugScreenDim);
		this.isAlwaysOnTop();
		this.setResizable(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setLocation( (int) (screenDim.width-width-20),screenDim.height/4);
		this.pack();	

		this.show();
	}
	
	
	public void print(String text){
		System.out.println(text);
		textarea.setText(text+"\n"+textarea.getText());
	}
	
	public void clear(){
		textarea.setText("");
	}
	
	public void printStackTrace(Exception e){
		e.printStackTrace();
		textarea.setText(Helpers.getStackTraceAsString(e));
	}
	
	
	//Singleton get Instantce
	public static synchronized DebugScreen getInstance () {
		if (instance == null) {
			instance = new DebugScreen (Config.getInstance().getDebugMode());
		}
		return instance;
	}
	

}
