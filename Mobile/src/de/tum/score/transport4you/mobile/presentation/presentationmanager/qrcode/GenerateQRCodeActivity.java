package de.tum.score.transport4you.mobile.presentation.presentationmanager.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import de.tum.score.transport4you.mobile.R;

public class GenerateQRCodeActivity extends Activity{

	public static final String ARGS_TEXT = "text";
	
	private String LOG_TAG = "GenerateQRCode";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qrcodegenerator);
		
		Intent intent = getIntent();
		String text = intent.getStringExtra(ARGS_TEXT);
		
		if(text == null) {
			Log.w(LOG_TAG, "No arguments given");
			finish();
			return;
		}
		
		Log.v(LOG_TAG, "Arguments " + text);

		//Find screen size
		WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		Point point = new Point();
		display.getSize(point);
		int width = point.x;
		int height = point.y;
		int smallerDimension = width < height ? width : height;
		smallerDimension = smallerDimension * 3/4;

		//Encode with a QR Code image
		QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(text, 
				null, 
				Contents.Type.TEXT,  
				BarcodeFormat.QR_CODE.toString(), 
				smallerDimension);
		try {
			Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
			ImageView myImage = (ImageView) findViewById(R.id.imageView1);
			myImage.setImageBitmap(bitmap);

		} catch (WriterException e) {
			e.printStackTrace();
		}
	}

}