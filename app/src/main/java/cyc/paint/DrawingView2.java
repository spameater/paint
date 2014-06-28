/**
 * 
 */
package cyc.paint;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ooVoo.oovoosample.VideoCall.VideoCallActivity;

/**
 * custom view that holds a bitmap to drawon and the drawing canvas
 * @author michen
 *
 */
public class DrawingView2 extends View {
	
	private Paint paint, bitmapPaint ;
	private Bitmap bitmap ;
	private ArrayList<StrokeLayer> strokeLayer ;
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;
	private static final String TAG = DrawingView2.class.getName();
	private int currentColor ;
	
    public DrawingView2(Context context){
		super(context);
		init();
    }
    public DrawingView2(Context context , AttributeSet as){
    	super(context, as);
		init();
    }
    public DrawingView2(Context context , AttributeSet as , int i){
    	super(context, as , i);
    	init();
    }
	/**
	 * 
	 */
	private void init() {
		initPaint();
		initBitmapPaint();
		initPath();
	}
	/**
	 * initialize paths arrlist and add the first path
	 */
	private void initPath() {
		strokeLayer = new ArrayList<StrokeLayer>();
		strokeLayer.add(new StrokeLayer(currentColor));
	}
	
	public DrawingView2(Context context, Bitmap img) {
		super(context);
		this.bitmap = img;
		init();
		
	}

	private void initBitmapPaint() {
		bitmapPaint = new Paint();
		bitmapPaint.setColor(currentColor);
	}

	/**
	 * 
	 */
	private void initPaint() {
		currentColor = Color.RED;
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setColor(currentColor);
		paint.setStyle(Style.STROKE);
		paint.setStrokeJoin(Join.ROUND);
		paint.setStrokeCap(Cap.ROUND);
		paint.setStrokeWidth(20);
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
    @Override 
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (bitmap != null){
            canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
            for(StrokeLayer layer : strokeLayer){
            	paint.setColor(layer.color);
            	canvas.drawPath(layer.path, paint);	//should I draw all or should i draw only the last ?	
            }
            
        }
    } 
    
	@Override 
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        Log.i(TAG,"touched");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                return true ;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                touch_up(); 
                invalidate();
                return true;
           default :
        	   return false;
                
        } 
    } 

    private void touch_start(float x, float y) {
        strokeLayer.get(strokeLayer.size()-1).path.moveTo(x, y);
        mX = x;
        mY = y;
    } 
    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            strokeLayer.get(strokeLayer.size()-1).path.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        } 
    } 
    private void touch_up() { 
        strokeLayer.get(strokeLayer.size()-1).path.lineTo(mX, mY);
    }
    /**
     * change the brush color
     * @param color
     */
    public void setColor(String color){
    	invalidate();
    	currentColor = Color.parseColor(color);
//    	bitmapPaint.setColor(currentColor);
    	paint.setColor(currentColor);
    	//create a new layer
    	strokeLayer.add(new StrokeLayer(currentColor));
    }
	/**
	 * @Deprecated unimplemented
	 * @param size
	 */
    public void setBrushSize(float size){
    	//TODO implement
    }
    
    /**
     * superimpose the drawing ontop of the imageView and return a 
     * bitmap
     * @return
     */
    Bitmap toBitmap(){
    	this.setDrawingCacheEnabled(true);
    	Bitmap bm = Bitmap.createBitmap(this.getDrawingCache());
    	this.setDrawingCacheEnabled(false);
    	saveToFile(bm,50,VideoCallActivity.storedImagePath); 
    	Log.i(TAG, "drawing bitmap saved to disk");
		return bm;
    }
	/**
	 * @param bm
	 */
	public static void saveToFile(Bitmap bm, int quality,String location) {
		Log.i(TAG,"saveTo file entered");
		OutputStream fout = null;
    	File imageFile = new File(location);
    	try { 
    	    fout = new FileOutputStream(imageFile);
    	    bm.compress(Bitmap.CompressFormat.JPEG, quality, fout);
    	    fout.flush();
    	    fout.close();
    	    Log.i(TAG,"file saved to :" + imageFile.getAbsolutePath());
    	} catch (IOException e) {
    	    e.printStackTrace();
    	}
	}

    /**
     * clear the drawing 
     */
    void clearDrawing(){
    	strokeLayer.get(strokeLayer.size()-1).path.reset();
    	invalidate();
    }
}
