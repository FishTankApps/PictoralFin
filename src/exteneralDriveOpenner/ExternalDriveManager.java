package exteneralDriveOpenner;

import java.util.ArrayList;

import jmtp.PortableDevice;
import jmtp.PortableDeviceManager;

public class ExternalDriveManager {	
	private ArrayList<PortableDevice> devices;
	
	private PortableDeviceManager manager;
	
	public ExternalDriveManager() {
		devices = new ArrayList<>();
		manager = new PortableDeviceManager();
		refresh();
	}
	
	public void refresh() {
		
		if(!devices.isEmpty())
			for(PortableDevice device : devices)
				device.close();
		
		devices.clear();		
		
		manager.refreshDeviceList();
		for (PortableDevice device : manager) {
			try {
				device.open();					
			} catch (jmtp.DeviceAlreadyOpenedException e) {}
			
			devices.add(device);
		}
	}
	
	public void close() {
		if(!devices.isEmpty())
			for(PortableDevice device : devices)
				device.close();
		
	}
	
	public ArrayList<PortableDevice> getDevices() {
		return devices;
	}
}
 