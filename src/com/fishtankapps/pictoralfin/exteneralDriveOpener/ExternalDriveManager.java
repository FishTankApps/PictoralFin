//package com.fishtankapps.pictoralfin.exteneralDriveOpener;
//
//import java.util.ArrayList;
//
//import com.fishtankapps.pictoralfin.customExceptions.FeatureNotSupportedException;
//
//import jmtp.PortableDevice;
//import jmtp.PortableDeviceManager;
//
//@Deprecated
//public class ExternalDriveManager {	
//	private ArrayList<PortableDevice> devices;
//	
//	private PortableDeviceManager manager;
//	
//	public ExternalDriveManager() {
//		devices = new ArrayList<>();	
//		
//		try {
//			manager = new PortableDeviceManager();
//		} catch (java.lang.UnsatisfiedLinkError e) {
//			throw new FeatureNotSupportedException();
//		}
//		
//		refresh();
//	}
//	
//	public void refresh() {
//		
//		if(!devices.isEmpty())
//			for(PortableDevice device : devices)
//				device.close();
//		
//		devices.clear();		
//		
//		manager.refreshDeviceList();
//		for (PortableDevice device : manager) {
//			try {
//				device.open();					
//			} catch (jmtp.DeviceAlreadyOpenedException e) {}
//			
//			devices.add(device);
//		}
//	}
//	
//	public void close() {
//		if(!devices.isEmpty())
//			for(PortableDevice device : devices)
//				device.close();
//		
//	}
//	
//	public ArrayList<PortableDevice> getDevices() {
//		return devices;
//	}
//}
// 