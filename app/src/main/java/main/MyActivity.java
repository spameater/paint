package main;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import cyc.paint.DrawingView;
import cyc.paint.R;

/**
 * TODO implement button functions
 * TODO add cropping capability
 *
 */
public class MyActivity extends Activity
        implements View.OnClickListener
{
    private static final String TAG = MyActivity.class.getName();
    /**
     * functional buttons
     */
    private ImageButton save;
    private ImageButton eraser;
    private ImageButton brush;
    /**
     * paint size buttons
     */
    private ImageButton largeBrush, midBrush, smallBrush;

    private DrawingView drawView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_activirty);
        initDrawView();
        initButtons();
    }

    private void initDrawView(){
        drawView = (DrawingView) findViewById(R.id.myDrawView);
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
        brush.setOnClickListener(this);
        eraser.setOnClickListener(this);
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

    /**
     * set the color on paint button clicked
     * this method depends on the tag of the
     * image button to be the hex color code
     * @param v
     */
    public void paintClicked(View v){
        String color = (String) v.getTag();
        drawView.setColor(color);
    }

    /**
     * called when a brushButton is clicked
     * this must be public
     * @param v
     */
    public void brushClicked(View v){
        String size = (String) v.getTag();
        drawView.setBrushSize(Integer.parseInt(size));
        Log.i(TAG,"brush size changed" + size.toString());
    }

    /**
     * TODO undone
     * @param view
     */
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId){
            case R.id.save: //@TODO save button
            break;
            case R.id.eraserButton:
                drawView.enableEraser();
                break;
            case R.id.brush:
                //TODO
                break;
            default:
                Log.e(TAG,"onClick: error viewId" + viewId);
                break;
        }
    }
}
