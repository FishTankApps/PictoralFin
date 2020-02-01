package exteneralDriveOpenner;

import java.util.ArrayList;

import jmtp.PortableDevice;
import jmtp.PortableDeviceManager;
import jmtp.PortableDeviceObject;
import jmtp.PortableDeviceStorageObject;

public class ExternalDriveManager {

	private ArrayList<PortableDeviceObject> externalDevices;
	
	public ExternalDriveManager() {
		externalDevices = new ArrayList<>();
		
		PortableDeviceManager manager = new PortableDeviceManager();
		for (PortableDevice device : manager) {
			device.open();
			
			
			for (PortableDeviceObject pdo : device.getRootObjects()) {
				externalDevices.add(pdo.getParent());
				if (pdo instanceof PortableDeviceStorageObject) {
					
					PortableDeviceStorageObject pds = (PortableDeviceStorageObject) pdo;
					

					System.out.println(pds.getParent().getName());
					System.out.println("  L " + pds.getName());
					
					for (PortableDeviceObject childPdo : pds.getChildObjects()) {
						System.out.println("     L " + childPdo.getOriginalFileName());
					}
				}
			}
		}
	}
	
	public void refresh() {
		
	}
	
	public String[] getDeviceNames() {
		String[] names = new String[externalDevices.size()];
		
		for(int x = 0; x < names.length; x++)
			names[x] = externalDevices.get(0).getName();
		
		return names;
	}
}
 