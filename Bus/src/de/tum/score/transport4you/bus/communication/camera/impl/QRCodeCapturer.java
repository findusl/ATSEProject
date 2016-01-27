package de.tum.score.transport4you.bus.communication.camera.impl;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import de.tum.score.transport4you.bus.communication.camera.ICameraQRCodeListener;

public class QRCodeCapturer extends JFrame implements Runnable, ThreadFactory, ICameraQRCodeListener{

	private static final long serialVersionUID = 6441489157408381878L;

	/* Used for storing the singleton instance */
	private static QRCodeCapturer instance = null;

	private Executor executor = Executors.newSingleThreadExecutor(this);

	private Webcam webcam = null;
	private WebcamPanel panel = null;
	private JTextArea textarea = null;
	
	private List<ICameraQRCodeListener> listeners;

	private QRCodeCapturer() {
		super();
		init();//init layout
		listeners = new LinkedList<ICameraQRCodeListener>();
		listeners.add(this);
	}
	
	public void init() {
		setLayout(new FlowLayout());
		setTitle("Read QR / Bar Code With Webcam");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension size = WebcamResolution.QVGA.getSize();

		webcam = Webcam.getWebcams().get(0);
		webcam.setViewSize(size);

		panel = new WebcamPanel(webcam);
		panel.setPreferredSize(size);

		textarea = new JTextArea();
		textarea.setEditable(false);
		textarea.setPreferredSize(size);

		add(panel);
		add(textarea);

		pack();
	}
	
	public static QRCodeCapturer getInstance() {
		if(instance == null)
			instance = new QRCodeCapturer();
		return instance;
	}
	
	public void startService() {
		setVisible(true);
		executor.execute(this);
	}

	public void addCameraQRCodeListener(ICameraQRCodeListener listener) {
		listeners.add(listener);
	}
	
	public boolean removeCameraQRCodeListener(ICameraQRCodeListener listener) {
		return listeners.remove(listener);
	}

	@Override
	public void run() {

		do {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return; //service has been interrupted. finish it
			}

			Result result = null;
			BufferedImage image = null;

			if (webcam.isOpen()) {

				if ((image = webcam.getImage()) == null) {
					continue;
				}

				LuminanceSource source = new BufferedImageLuminanceSource(image);
				BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

				try {
					result = new MultiFormatReader().decode(bitmap);
				} catch (NotFoundException e) {
					// fall thru, it means there is no QR code in image
				}
			}

			if (result != null && result.getBarcodeFormat().equals(BarcodeFormat.QR_CODE)) {
				for(ICameraQRCodeListener listener : listeners) {
					listener.onQRCodeRead(result.getText());
				}
			}

		} while (true);
	}
	
	@Override
	public void onQRCodeRead(String content) {
		TicketValidator tv = new TicketValidator(content.getBytes());
		String result = "Ticket invalid.";
		if(tv.isValid()) {
			if(tv.invalidateTicket()) {
				result = "Ticket is valid.";
			} else {
				result = "System error.";
			}
		}
		textarea.setText(result);
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r, "example-runner");
		t.setDaemon(true);
		return t;
	}
}