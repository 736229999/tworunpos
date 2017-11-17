package GuiElements;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.prefs.BackingStoreException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class DigitalClock extends JPanel {

	
	JLabel timelabel = new JLabel("asd");

	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String stringTime;

    public void setStringTime(String xyz) {
        this.stringTime = xyz;
    }

    public int findMinimumBetweenTwoNumbers(int a, int b) {
        return (a <= b) ? a : b;
    }

    public DigitalClock() {

    	this.setBackground(Color.WHITE);
    	
    	this.add(timelabel);
    	
        Timer t1 = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	 Calendar now = Calendar.getInstance();
                 int hour = now.get(Calendar.HOUR_OF_DAY);
                 int minute = now.get(Calendar.MINUTE);
                 int second = now.get(Calendar.SECOND);

                 String correctionHour = (hour < 10) ? "0" : "";
                 String correctionMinute = (minute < 10) ? "0" : "";
                 String correctionSecond = (second < 10) ? "0" : "";
                 setStringTime(correctionHour + hour + ":" + correctionMinute+ minute + ":" + correctionSecond + second);
                 
                 
                 

                 
                 timelabel.setText(stringTime);
      
                 repaint();
                 
                 
                 
            }

        });
        t1.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        
//        Calendar now = Calendar.getInstance();
//        int hour = now.get(Calendar.HOUR_OF_DAY);
//        int minute = now.get(Calendar.MINUTE);
//        int second = now.get(Calendar.SECOND);
//
//        String correctionHour = (hour < 10) ? "0" : "";
//        String correctionMinute = (minute < 10) ? "0" : "";
//        String correctionSecond = (second < 10) ? "0" : "";
//        setStringTime(correctionHour + hour + ":" + correctionMinute+ minute + ":" + correctionSecond + second);
//        g.setColor(Color.BLACK);
//        int length = findMinimumBetweenTwoNumbers(this.getWidth(),this.getHeight());
////        Font myFont = new Font("SansSerif", Font.PLAIN, length / 2);
//
//        Font myFont = new Font("SansSerif", Font.BOLD, 12);
//
//        
//        g.setFont(myFont);
//        g.drawString(stringTime, length/2, 12);
    }


}