package de.tum.score.transport4you.mobile.presentation.presentationmanager.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import de.tum.score.transport4you.mobile.R;
import de.tum.score.transport4you.mobile.application.applicationcontroller.IMainApplication;
import de.tum.score.transport4you.mobile.application.applicationcontroller.impl.ApplicationSingleton;
import de.tum.score.transport4you.mobile.presentation.presentationmanager.IPresentation;

public class GenerateQRCodeActivity extends Activity implements OnClickListener, IPresentation {
	private static final String TAG = GenerateQRCodeActivity.class.getSimpleName();

	public static final String ARGS_TEXT = "text";

	public static final int RETURN_USED = 12;

	private String LOG_TAG = "GenerateQRCode";
	private IMainApplication mainApplication;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		byte[] content = intent.getByteArrayExtra(ARGS_TEXT);

		if (content == null) {
			Log.w(LOG_TAG, "No arguments given");
			finish();
			return;
		}

		String log = "Bytes: ";
		char[] chars = new char[content.length];
		for (int i = 0; i < chars.length; i++) {
			chars[i] = (char) (content[i] & 0xFF);
			log += content[i] + " ";
		}
		Log.i(TAG, log);
		String text = new String(chars);

		mainApplication = ApplicationSingleton.getApplicationController();
		mainApplication.registerActivity(this);

		initView(text);
	}

	private void initView(String content) {
		setContentView(R.layout.qrcodegenerator);

		Log.v(LOG_TAG, "Arguments " + content);

		Button b = (Button) findViewById(R.id.button1);
		b.setOnClickListener(this);

		// Find screen size
		WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		Point point = new Point();
		display.getSize(point);
		int width = point.x;
		int height = point.y;
		int smallerDimension = width < height ? width : height;
		smallerDimension = smallerDimension * 3 / 4;

		// Encode with a QR Code image
		QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(content, null, Contents.Type.TEXT,
				BarcodeFormat.QR_CODE.toString(), smallerDimension);
		try {
			Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
			ImageView myImage = (ImageView) findViewById(R.id.imageView1);
			myImage.setImageBitmap(bitmap);

		} catch (WriterException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		setResult(RETURN_USED);
		finish();
	}

	@Override
	public void shutdown() {
		finish();
	}

	@Override
	public void updateProgessDialog(String title, String message, boolean visible, Integer increment) {
		// TODO Auto-generated method stub

	}

}