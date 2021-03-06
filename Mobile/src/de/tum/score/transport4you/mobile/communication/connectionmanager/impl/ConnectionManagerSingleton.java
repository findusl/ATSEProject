package de.tum.score.transport4you.mobile.communication.connectionmanager.impl;

import android.content.Context;
import de.tum.score.transport4you.mobile.application.applicationcontroller.IMainApplication;
import de.tum.score.transport4you.mobile.communication.connectionmanager.ICommunication;

public class ConnectionManagerSingleton {
	private static ConnectionManager connectionManager;

	public static ICommunication getCommunication(Context cntxt, IMainApplication mainApplication) {
		if (connectionManager == null) {
			connectionManager = new ConnectionManager(cntxt, mainApplication);
		}
		return connectionManager;
	}
	
	public static IConnectionBroadcast getConnectionBroadcast (Context cntxt) {
		return connectionManager;
	}
}
