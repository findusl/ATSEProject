package de.tum.score.transport4you.mobile.communication.dataconnectioncontroller.error;

public class RESTException extends Exception {
	
	public RESTException(String error) {
		super(error);
	}
	
	public RESTException(String error, Throwable throwable) {
		super(error, throwable);
	}
}
