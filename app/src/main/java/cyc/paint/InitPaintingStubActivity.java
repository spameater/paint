package cyc.paint;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ooVoo.oovoosample.R;
import com.ooVoo.oovoosample.VideoCall.VideoCallActivity;

public class InitPaintingStubActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init_painting_stub);
	}

	@Override protected void onResume(){
		super.onResume();
		startImageEditor();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.init_painting_stub, menu);
		return true;
	}

	/**
	 * how it works :
	 * 3. open up the ImageEditor
	 */
	private void startImageEditor() {
		File nfile = new File(VideoCallActivity.storedImagePath);
		if (nfile.canRead()){
			Intent i = new Intent(this, PaintingActivity2.class);
			i.putExtra(PaintingActivity.IMG_PATH_EXTRA, VideoCallActivity.storedImagePath);
			startActivity(i);
		}
		else {
			Toast.makeText(this, "file cannot be read" , Toast.LENGTH_LONG).show();
		}

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


}
