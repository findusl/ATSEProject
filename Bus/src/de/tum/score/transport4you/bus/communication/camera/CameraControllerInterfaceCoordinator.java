package de.tum.score.transport4you.bus.communication.camera;

import de.tum.score.transport4you.bus.communication.camera.impl.QRCodeCapturer;
import de.tum.score.transport4you.bus.communication.camera.impl.Startup;

public abstract class CameraControllerInterfaceCoordinator {
	
	public static IStartup getStartup() {
		return Startup.getInstance();
	}
	
	public static void addCameraQRCodeListener(ICameraQRCodeListener listener) {
		QRCodeCapturer.getInstance().addCameraQRCodeListener(listener);
	}
	
	public boolean removeCameraQRCodeListener(ICameraQRCodeListener listener) {
		return QRCodeCapturer.getInstance().removeCameraQRCodeListener(listener);
	}
}
