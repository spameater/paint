package Main;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import cyc.paint.R;

/**
 * TODO implement button functions
 * TODO add cropping capability
 *
 */
public class MyActivity extends Activity
        implements View.OnClickListener
{
    public static final String TAG = MyActivity.class.getName();
    /**
     * buttons
     */
    ImageButton save;
    ImageButton eraser;
    ImageButton brush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_activirty);
        initButtons();
    }

    /**
     * init image Buttons
     */
    private void initButtons() {
        //init buttons
        save = (ImageButton) findViewById(R.id.save);
        brush = (ImageButton) findViewById(R.id.brush);
        eraser = (ImageButton) findViewById(R.id.eraserButton);

        // add onclick listener
        save.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_activirty, menu);
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
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId){
            case R.id.save: //save button

            default:
                Log.e(TAG,"onClick: error viewId" + viewId);
                break;
        }
    }
}
