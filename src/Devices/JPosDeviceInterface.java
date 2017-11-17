package Devices;


public interface JPosDeviceInterface {

/*All new Devices by JavaPOS should be an inherit this interface*/


	String deviceName = "";
	
	public void open();
	public void close();

	
}
