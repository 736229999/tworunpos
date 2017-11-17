package GuiElements;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import tworunpos.Article;
import tworunpos.Cart;
import tworunpos.GuiElements;

public class TrDisplay extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Pricedisplay
	private JPanel priceDisplayPanel; //Container for Display
	JLabel priceDisplayLabelUpLeft = new JLabel();
	JLabel priceDisplayLabelUpCenter = new JLabel();
	JLabel priceDisplayLabelUpRight = new JLabel();
	JLabel priceDisplayLabelCenter = new JLabel();
	JLabel priceDisplayLabelDownLeft = new JLabel();
	JLabel priceDisplayLabelDownCenter = new JLabel();
	JLabel priceDisplayLabelDownRight = new JLabel();
	
	
	static final String iconImageLogo = "images/logo.png";
	
	public TrDisplay(Dimension dim){
		initPriceDisplay();
		setPreferredSize(dim);
		setMinimumSize(dim);
		setMaximumSize(dim);
	}
	
	public void initPriceDisplay(){
		
		this.setLayout(new BorderLayout());

		
		//Display NORTH - This is designed for Article Information
		JPanel priceDisplayNorth = new JPanel();
		this.add(priceDisplayNorth, BorderLayout.NORTH);
		priceDisplayNorth.setLayout(new BorderLayout());
		
		
		priceDisplayLabelUpLeft = new JLabel();
		priceDisplayNorth.add(priceDisplayLabelUpLeft, BorderLayout.WEST);
		priceDisplayLabelUpCenter = new JLabel();
		priceDisplayNorth.add(priceDisplayLabelUpCenter, BorderLayout.CENTER);
		priceDisplayLabelUpRight = new JLabel();
		priceDisplayNorth.add(priceDisplayLabelUpRight, BorderLayout.EAST);

		
		//Display CENTER - This is designed price displaying Article Price
		JPanel priceDisplayCenter = new JPanel();
		this.add(priceDisplayCenter, BorderLayout.CENTER);

		
		priceDisplayLabelCenter = new JLabel();
		priceDisplayCenter.add(priceDisplayLabelCenter, BorderLayout.CENTER);

		
		//DISPLAY SOUTH - this is designe for additional information
		JPanel priceDisplaySouth = new JPanel();
		priceDisplaySouth.setLayout(new BorderLayout());
		this.add(priceDisplaySouth, BorderLayout.SOUTH);
		
		
		priceDisplayLabelDownLeft = new JLabel();
		priceDisplaySouth.add(priceDisplayLabelDownLeft, BorderLayout.WEST);
		priceDisplayLabelDownCenter = new JLabel();
		priceDisplaySouth.add(priceDisplayLabelDownCenter, BorderLayout.CENTER);
		priceDisplayLabelDownRight = new JLabel();
		priceDisplaySouth.add(priceDisplayLabelDownRight, BorderLayout.EAST);

		
		priceDisplayNorth.setBackground(new Color(255,255,255));
		priceDisplayCenter.setBackground(new Color(255,255,255));
		priceDisplaySouth.setBackground(new Color(255,255,255));
		
		priceDisplayLabelCenter.setFont(new Font("Arial", Font.BOLD, 25));
		priceDisplayLabelUpLeft.setFont(new Font("Arial", Font.BOLD, 16));
		priceDisplayLabelUpCenter.setFont(new Font("Arial", Font.BOLD, 16));
		priceDisplayLabelUpRight.setFont(new Font("Arial", Font.BOLD, 16));
		priceDisplayLabelDownLeft.setFont(new Font("Arial", Font.BOLD, 16));
		priceDisplayLabelDownCenter.setFont(new Font("Arial", Font.BOLD, 16));
		priceDisplayLabelDownRight.setFont(new Font("Arial", Font.BOLD, 16));
		priceDisplayLabelCenter.setHorizontalAlignment(SwingConstants.CENTER);
		priceDisplayLabelCenter.setVerticalAlignment(SwingConstants.CENTER);
		priceDisplayLabelUpCenter.setHorizontalAlignment(SwingConstants.CENTER);		
		priceDisplayLabelDownCenter.setHorizontalAlignment(SwingConstants.CENTER);

	}

	
	//##########  DISPLAY
	
		public void updatePriceDisplay(String upLeft, String upCenter, String upRight, String center, String downLeft, String downCenter, String downRight){
			
			priceDisplayLabelCenter.setIcon(null);
			
			priceDisplayLabelUpLeft.setText(upLeft);
			priceDisplayLabelUpCenter.setText(upCenter);
			priceDisplayLabelUpRight.setText(upRight);
			priceDisplayLabelCenter.setText(center);
			priceDisplayLabelDownLeft.setText(downLeft);
			priceDisplayLabelDownCenter.setText(downCenter);
			priceDisplayLabelDownRight.setText(downRight);
		}
		
		public void clearPriceDisplay(){
			priceDisplayLabelCenter.setIcon(null);
			priceDisplayLabelUpLeft.setText("");
			priceDisplayLabelUpCenter.setText("");
			priceDisplayLabelUpRight.setText("");
			priceDisplayLabelCenter.setText("");
			priceDisplayLabelDownLeft.setText("");
			priceDisplayLabelDownCenter.setText("");
			priceDisplayLabelDownRight.setText("");
		}

		


		public void  showArticleOnDisplayForSale(float amount, Article article, Cart cart){
			String articleAmountText = amount+" "+article.getUnit();
			String articleNameText = article.getName();
			String articlePriceText = article.getPriceGross()+" € / "+article.getUnit();
			String articleSum = String.format("%.2f", (amount*article.getPriceGross())).toString()+" €";
			String sumOfCart = "Summe "+String.format("%.2f", cart.getSumOfCartGross() )+" €";
			updatePriceDisplay(articleAmountText,articleNameText,articlePriceText,articleSum,"VERKAUF","",sumOfCart);
		}

		

		
		public void  showArticleOnDisplayForCancellation(int amount, Article article, Cart cart){
			String articleAmountText = amount+" "+article.getUnit();
			String articleNameText = article.getName();
			String articlePriceText = - article.getPriceGross()+" € / "+article.getUnit();
			String articleSum = String.format("- %.2f", (amount*article.getPriceGross())).toString()+" €";
			String sumOfCart = "Summe "+String.format("%.2f", cart.getSumOfCartGross() )+" €";
			updatePriceDisplay(articleAmountText,articleNameText,articlePriceText,articleSum,"STORNO","",sumOfCart);
		}

		
		
		public void  showSimpleTextOnDisplay(String text){	
			
			updatePriceDisplay("","","",text,"","","");
		}
		
		public void  showLogoOnDisplay(){	
			
			clearPriceDisplay();
			ImageIcon logo = this.getLogo();
	
			priceDisplayLabelCenter.addMouseListener(new MouseAdapter() {

			    public void mouseClicked(MouseEvent evt) {
					JLabel logoLabel = (JLabel)evt.getSource();

			         if (evt.getClickCount() == 5) {   // Triple-click
			        	 int dialogButton = JOptionPane.YES_NO_OPTION;
			        	 int dialogResult = JOptionPane.showConfirmDialog (null, "Sicher, dass du die Anwendung schließen möchtest?","Warning",dialogButton);
			        	
			        	 if(dialogResult == JOptionPane.YES_OPTION)
			        		 System.exit(0);
			        }
			    }
			});
			
			
			priceDisplayLabelCenter.setIcon(logo);
	
			
		}
		
		
		public void  showCompletePriceOnDisplay(Cart cart){
			String sumOfCart = "Z-Summe "+String.format("%.2f", cart.getSumOfCartGross() )+" €";
			updatePriceDisplay("","","",sumOfCart,"","","");
		}
		
		public void  clearDisplay(){		
			updatePriceDisplay("","","","","","","");
		}
		
		public  ImageIcon getLogo(){
			
			
			try {
				InputStream imageStream=this.getClass().getClassLoader().getResourceAsStream(iconImageLogo);
				
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
		
		

	
	
}
