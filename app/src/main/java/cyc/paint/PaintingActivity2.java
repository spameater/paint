package cyc.paint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import cloudiary.android.CloudinaryInstanceWrapper;

import com.edmodo.cropper.CropImageView;
import com.ooVoo.oovoosample.R;
import com.ooVoo.oovoosample.VideoCall.VideoCallActivity;
import com.oovoo.core.ConferenceCore;

public class PaintingActivity2 extends Activity implements OnClickListener 
 {

	public static final String IMG_PATH_EXTRA = "the path of the img";
	private static final String TAG = PaintingActivity2.class.getName();
	private DrawingView2 draw ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.painting2);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		String imgPath = (String) getIntent().getExtras().get(IMG_PATH_EXTRA);
		Log.i(TAG,"image path: " + imgPath ) ;
		Bitmap bm = BitmapFactory.decodeFile(imgPath);
		initDisplay(bm);
		initButtons();
	}

	private void initButtons() {
		ImageButton sendButton = (ImageButton) findViewById(R.id.paintingViewSendButton);
		ImageButton clearButton = (ImageButton) findViewById(R.id.paintingViewClearButton);
		ImageButton crop = (ImageButton) findViewById(R.id.paintingViewCropButton);
		sendButton.setOnClickListener(this);
		clearButton.setOnClickListener(this);
		crop.setOnClickListener(this);
	}

	/**
	 * set up the display to show the drawingView and hide the CropView
	 * @param bm
	 */
	private void initDisplay(Bitmap bm) {
		draw = (DrawingView2) findViewById(R.id.drawView);
		draw.setBitmap(bm);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.painting, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.paintingViewSendButton:
			sendButtonOnClicked();
			break;
		case R.id.paintingViewClearButton: 
			clearButtonOnClicked();
			break;
		case R.id.paintingViewCropButton:
			cropButtonOnClicked();
			break;
		default:
			break;
		}
		
	}
	/**
	 * onclickListener for paint buttons
	 * @param view
	 */
	public void paintClicked(View view){
		String color = view.getTag().toString();
		draw.setColor(color);
		Log.i(TAG,"clicked on paint : color set :" + color);
	}
	/**
	 * crop button clicked 
	 */
	private void cropButtonOnClicked() {
		//get cropped img
		CropImageView civ = (CropImageView) findViewById(R.id.cropImgView);
		Bitmap rotated, bm = civ.getCroppedImage();
		//rotate the img 
		rotated = rotateBmp(bm,0); //0 - no rotation
		//send to participant
		upLoadImageAndSendUrlToParticipant(rotated); 
		//disable crop button
		ImageButton crop = (ImageButton) findViewById(R.id.paintingViewCropButton);
		crop.setVisibility(View.INVISIBLE);
	}

	/**
	 * create a new bm or return the original one with the rotated angle deg
	 * @param bm 
	 * @param deg
	 * @return
	 */
	public static Bitmap rotateBmp(Bitmap bm, int deg) {
		if (deg == 0 ) return bm ; //return original if deg is 0
		Matrix matrix = new Matrix();
		matrix.postRotate(deg); //XXX rotate the image by 90 degrees , might be needed in the future
		return Bitmap.createBitmap(bm,0,0,bm.getWidth() , bm.getHeight() , matrix , true);
	}
	private void clearButtonOnClicked() {
		Log.i(TAG,"clear button pressed");
		draw.clearDrawing();
	}
	/**
	 * display a crop view on send button pressed
	 */
	private void sendButtonOnClicked() {
		Log.i(TAG,"send button pressed");
		Bitmap bitmap = draw.toBitmap();
		showCropView(bitmap);
	}
	private void showCropView(Bitmap bitmap) {
		//set cropImageView
		CropImageView civ = (CropImageView) findViewById(R.id.cropImgView);
		civ.setImageBitmap(bitmap);
	    civ.setAspectRatio(16,9);
		civ.setVisibility(View.VISIBLE);
		draw.setVisibility(View.INVISIBLE);
		//set crop button 
		ImageButton crop = (ImageButton) findViewById(R.id.paintingViewCropButton);
		ImageButton save = (ImageButton) findViewById(R.id.paintingViewSendButton);
		save.setVisibility(View.INVISIBLE);
		crop.setVisibility(View.VISIBLE);
		
	}

	private void upLoadImageAndSendUrlToParticipant(Bitmap bitmap) {
		DrawingView.saveToFile(bitmap,100,VideoCallActivity.storedImagePath);

		new AsyncTask<String, Void, String>() {
			FileInputStream fis;
			
			@Override
			protected String doInBackground(String... params) {
				String url = null;
				try {
					fis = new FileInputStream(new File(VideoCallActivity.storedImagePath));
					url = CloudinaryInstanceWrapper.getInstance().uploadImage(fis);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				return url;
			}
			
			@Override protected void onPostExecute(String u){
				sendIncallMsg(u);
				finish(); //return to conference
			}
		}.execute("");
	}

	/**
	 * Send a str as in call message 
	 * @param str
	 */
	void sendIncallMsg(String str) {
		if (ConferenceCore.instance().inCallMessagesPermitted()) {
			ConferenceCore.instance(this).inCallMessage(str.getBytes());
			Toast.makeText(this, "msg sent", Toast.LENGTH_LONG).show();
			Log.i(TAG,"sent byte array");
		}
		else {
			Toast.makeText(this, "incall message unavilable" , Toast.LENGTH_LONG).show();
			Log.e(TAG,"cannot send because incall message is not permitted");
		}
	}
}
