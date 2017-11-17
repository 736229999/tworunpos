package tworunpos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Buffer {

	
	

	// Eine (versteckte) Klassenvariable vom Typ der eigene Klasse
	private static Buffer instance;
	// Verhindere die Erzeugung des Objektes �ber andere Methoden
	//private Config() {}
	// Eine Zugriffsmethode auf Klassenebene, welches dir '''einmal''' ein konkretes 
	// Objekt erzeugt und dieses zur�ckliefert.
	// Durch 'synchronized' wird sichergestellt dass diese Methode nur von einem Thread 
	// zu einer Zeit durchlaufen wird. Der n�chste Thread erh�lt immer eine komplett 
	// initialisierte Instanz.
	
	
	
	
	
    public float posQuantityBuffer = 1;

	
	
	public Buffer() throws IOException{
		
	}
	
	
	public float getPosQuantityBuffer() {
		return posQuantityBuffer;
	}

	public void setPosQuantityBuffer(float posQuantityBuffer){
		this.posQuantityBuffer = posQuantityBuffer;
	}

	public void setPosQuantityBuffer(String posQuantityBuffer) throws Exception{
		NumberFormat formatter = NumberFormat.getNumberInstance(Locale.GERMANY);
		this.posQuantityBuffer = formatter.parse(posQuantityBuffer).floatValue();
	}
	
	public void resetPosQuantityBuffer(){
		this.posQuantityBuffer = 1.0F;
	}
	
	public void resetAllBuffers(){
		resetPosQuantityBuffer();
	}

	//Singleton get Instantce
	public static synchronized Buffer getInstance () {
		if (Buffer.instance == null) {
			try {
				Buffer.instance = new Buffer ();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return Buffer.instance;
	}


	

}
