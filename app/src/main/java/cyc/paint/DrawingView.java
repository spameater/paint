package cyc.paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * This is demo code to accompany the Mobiletuts+ tutorial series:
 * - Android SDK: Create a Drawing App
 * 
 * Sue Smith
 * August 2013
 *
 */
public class DrawingView extends View {
    private static String TAG = DrawingView.class.getName();
	//drawing path
	private Path drawPath;
	//drawing and canvas paint
	private Paint drawPaint, canvasPaint;


    private boolean isEraseEnabled = false ;
    /**
     * what is being painted ? is it the drawPaint or is it eraserPaint?
     */
    private Paint currentPaint ;
	//initial color
	private int paintColor = 0xFF660000;
    private static final String erasorColor = "#FFFFFF";
	//canvas
	private Canvas drawCanvas;
	//canvas bitmap
	private Bitmap canvasBitmap;

	public DrawingView(Context context, AttributeSet attrs){
		super(context, attrs);
		setupDrawing();
	}

	//setup drawing
	private void setupDrawing(){

		//prepare for drawing and setup paint stroke properties
		drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
		canvasPaint = new Paint(Paint.DITHER_FLAG);

        //set current paint
        currentPaint = drawPaint;
	}
	
	//size assigned to view
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		drawCanvas = new Canvas(canvasBitmap);
	}
	
	//draw the view - will be called after touch event
	@Override
	protected void onDraw(Canvas canvas) {
        Log.v(TAG, "onDraw with Paint " + currentPaint.toString());
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		canvas.drawPath(drawPath, currentPaint);
	}
	
	//register user touches as drawing action
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float touchX = event.getX();
		float touchY = event.getY();

        if (!isEraseEnabled){//draw only when eraser is not enabled
            //respond to down, move and up events

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    drawPath.moveTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    drawPath.lineTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_UP:
                    drawPath.lineTo(touchX, touchY);
                    drawCanvas.drawPath(drawPath, drawPaint);
                    drawPath.reset();
                    break;
                default:
                    return false;
            }
            //redraw
            invalidate();
            return true;
        }
        else {
            return false;
        }
		
	}
	
	/**
     * update the current used paint
	 */
	public void setColor(String newColor){
        assert newColor != null ;
		invalidate();
		paintColor = Color.parseColor(newColor);
		drawPaint.setColor(paintColor);
	}

    /**
     * update the brush size
     * @param size
     */
    public void setBrushSize(int size){
        invalidate();
        drawPaint.setStrokeWidth(size);
    }

    /**
     * enable the eraser
     */
    public void enableEraser(){
        setColor(erasorColor);
    }

}
