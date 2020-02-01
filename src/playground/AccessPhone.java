package playground;

import java.io.IOException;

import exteneralDriveOpenner.ExternalDriveManager;


public class AccessPhone {

	public static void main(String[] args) throws IOException {
		ExternalDriveManager driveManager = new ExternalDriveManager();
					
		
		System.out.println("\n\n\n\n");
		
		for(String s : driveManager.getDeviceNames())
			System.out.println(s);
	}	
}
