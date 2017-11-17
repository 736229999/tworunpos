package Devices;

import tworunpos.DebugScreen;

public abstract class JPosDevice implements JPosDeviceInterface {

	
	private String className;
	
	private String deviceName;
	

	
	public JPosDevice(){
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null) {
			className = enclosingClass.getName();
		} else {
			className = getClass().getName();
		}
	}
	
	
	@Override
	public void open() {
		DebugScreen.getInstance().print("Open Device"+className);
	}

	@Override
	public void close() {
		DebugScreen.getInstance().print("Close Device"+className);
	}

}
