package de.tum.score.transport4you.bus.communication.camera;

public interface ICameraQRCodeListener {
	public void onQRCodeRead(byte[] content);
}
