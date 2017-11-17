package tworunpos;

import java.util.Observable;

import GuiElements.TrDisplay;
import GuiElements.TrDisplayForCashier;

public class PosState  extends Observable{
	
	// Eine (versteckte) Klassenvariable vom Typ der eigene Klasse
	private static PosState instance;
	// Verhindere die Erzeugung des Objektes �ber andere Methoden
	private PosState () {}
	// Eine Zugriffsmethode auf Klassenebene, welches dir '''einmal''' ein konkretes 
	// Objekt erzeugt und dieses zur�ckliefert.
	// Durch 'synchronized' wird sichergestellt dass diese Methode nur von einem Thread 
	// zu einer Zeit durchlaufen wird. Der n�chste Thread erh�lt immer eine komplett 
	// initialisierte Instanz.
	
	  
	//POS STATUS
	
	//posState referer -> one state before
	private int posStateBefore;
	
	//current posState
	private int posState;
	
	//all states definitions
	public final static  int posStateBoot = 0;
	public final static int posStateReady = 1;
	public final static int posStateSellingProcess = 2;
	public final static int posStateCheckout = 3;
	public final static int posStateSettings = 4;
	public final static int posStateRefundDeposit = 5;
	public final static int posStateRefund = 6;
  	  

	
	public int getState(){
		return posState;
	}
	
	//tells if the logout is allowed or not, e.g. during a selling process it is not possible to logout
	public boolean logoutLocked(){
		boolean lock = false;
		switch(posStateBefore){
			case posStateBoot:;
			case posStateSellingProcess:;
			case posStateRefundDeposit:;
			case posStateCheckout:lock=true;break;
		}
		
		return lock;
	}
	
	public int getStateBefore(){
		return posStateBefore;
	}
	
	public void changeState(int newState){
		setChanged();
		posStateBefore = posState;
		posState = newState;
		DebugScreen.getInstance().print("PosStatus setted from "+posStateBefore+" to "+posState);

		notifyObservers( new Object[]{"changePosState",newState});
	}
	
	public void changeStateToSellingProcess(boolean clearScreen){
		setChanged();
		posStateBefore = posState;
		posState = this.posStateSellingProcess;
			
		DebugScreen.getInstance().print("PosStatus setted from "+posStateBefore+" to "+posState);

		notifyObservers( new Object[]{"changePosState",this.posStateSellingProcess,clearScreen});
		
	}
	
	
	//Singleton get Instantce
	public static synchronized PosState getInstance () {
		if (PosState.instance == null) {
			PosState.instance = new PosState ();
		}
		return PosState.instance;
	}
	

}
