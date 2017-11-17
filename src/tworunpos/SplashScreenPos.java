package tworunpos;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class SplashScreenPos extends JFrame{

	
	
	// Eine (versteckte) Klassenvariable vom Typ der eigene Klasse
	static SplashScreenPos instance;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage splashImage;
	private JLabel splashImageLabel;
	private String splashImagePath = "images/splash.png";
	private JLabel loadingTextLabel;
	
	public SplashScreenPos(){
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		setLayout(new GridBagLayout());
		
		
		
		//create splashimage
		try {
			
			InputStream splashStream=this.getClass().getClassLoader().getResourceAsStream(splashImagePath);
			if(splashStream == null){
				return;
			}
			splashImage = ImageIO.read(splashStream);
			splashImageLabel = new JLabel(new ImageIcon(splashImage));
			splashImageLabel.setLayout(new GridBagLayout());
			this.add(splashImageLabel);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		
		if(splashImageLabel != null)
			this.add(splashImageLabel);
		
		
		
		
		// SET TEXT
		loadingTextLabel = new JLabel("Initializing Applikation ...");
		loadingTextLabel.setFont(loadingTextLabel.getFont().deriveFont(12));
		loadingTextLabel.setForeground(Color.BLACK);
		loadingTextLabel.setHorizontalAlignment(JLabel.LEFT);
		loadingTextLabel.setVerticalAlignment(JLabel.BOTTOM);
		loadingTextLabel.setPreferredSize(new Dimension(400,160));
		loadingTextLabel.setSize(400, 160);
		//loadingTextLabel.setBorder(BorderFactory.createLineBorder(Color.RED,1));

		//splashImageLabel.setBorder(BorderFactory.createLineBorder(Color.GREEN,1));
		splashImageLabel.add(loadingTextLabel);
	
		
		 
		this.setUndecorated(true);
		this.setBackground(new Color(0, 255, 0, 0));

		this.getContentPane().setPreferredSize(dim);
		this.setSize(dim);
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		

		
		
		this.pack();	
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		
	}
	
	public void setText(String text){
		loadingTextLabel.setText(text);
	}
	
	

	//Singleton get Instantce
	public static synchronized SplashScreenPos getInstance () {
		if (SplashScreenPos.instance == null) {
			SplashScreenPos.instance = new SplashScreenPos ();
		}
		return SplashScreenPos.instance;
	}
	

}
