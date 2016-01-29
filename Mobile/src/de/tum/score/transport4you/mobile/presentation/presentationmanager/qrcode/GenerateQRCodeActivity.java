package de.tum.score.transport4you.mobile.presentation.presentationmanager.qrcode;

import java.nio.ByteBuffer;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

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
import de.tum.score.transport4you.mobile.R;
import de.tum.score.transport4you.mobile.application.applicationcontroller.IMainApplication;
import de.tum.score.transport4you.mobile.application.applicationcontroller.impl.ApplicationSingleton;
import de.tum.score.transport4you.mobile.presentation.presentationmanager.IPresentation;

public class GenerateQRCodeActivity extends Activity implements OnClickListener, IPresentation{

	public static final String ARGS_TEXT = "text";
	
	private String LOG_TAG = "GenerateQRCode";
    private IMainApplication mainApplication;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		String text = intent.getStringExtra(ARGS_TEXT);
		
		//TODO: debugging
		ByteBuffer bb = ByteBuffer.allocate(16);
		bb.putInt(0x54345941);
		bb.putInt(12345);
		int currentTimeSec = (int)(System.currentTimeMillis()/1000);
		Log.d(LOG_TAG, "Current time: " + currentTimeSec);
		bb.putInt(currentTimeSec - 1);
		bb.putInt(currentTimeSec + 2000);
		text = new String(bb.array());
		
		if(text == null) {
			Log.w(LOG_TAG, "No arguments given");
			finish();
			return;
		}

        
        mainApplication = ApplicationSingleton.getApplicationController();
        mainApplication.registerActivity(this);
        
		initView(text);
	}
	
	private void initView(String content) {
		setContentView(R.layout.qrcodegenerator);
		
		Log.v(LOG_TAG, "Arguments " + content);
		
		Button b = (Button) findViewById(R.id.button1);
		b.setOnClickListener(this);

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
		QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(content, 
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

	@Override
	public void onClick(View v) {
		// TODO set ticket to used
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