package de.tum.score.transport4you.mobile.presentation.presentationmanager.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import de.tum.score.transport4you.mobile.R;

public class GenerateQRCodeActivity extends Activity implements OnClickListener{

 private String LOG_TAG = "GenerateQRCode";

 @Override
 public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.qrcodegenerator);

  Button button1 = (Button) findViewById(R.id.button1);
  button1.setOnClickListener(this);

 }

 public void onClick(View v) {

  switch (v.getId()) {
  case R.id.button1:
   EditText qrInput = (EditText) findViewById(R.id.qrInput);
   String qrInputText = new String(new char[]{0x00, 0x01, 0x88, 0x97, 0x55});
   Log.v(LOG_TAG, qrInputText);

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
   QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrInputText, 
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


   break;

   // More buttons go here (if any) ...

  }
 }

}