package de.tum.score.transport4you.bus.communication.camera.impl;

import org.apache.log4j.Logger;

import de.tum.score.transport4you.bus.communication.camera.IStartup;

public class Startup implements IStartup {

	/* Used for storing the singleton instance */
	private static Startup instance = null;
	
	/* Logger */
	private Logger logger = Logger.getLogger("Communication");
	
	/**
	 * Returns the singleton instance of this class
	 * @return
	 */
	public static IStartup getInstance() {
		if(instance==null){
			instance = new Startup();
		}
		
		return instance;
	}
	
	@Override
	public void init() {
		logger.info("Initialize QR Code reader");
		QRCodeCapturer.getInstance().startService();
		logger.info("QR Code reader initialized");
	}

}
