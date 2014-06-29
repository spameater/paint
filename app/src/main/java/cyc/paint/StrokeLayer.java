package cyc.paint;

import android.graphics.Path;
/**
 * immutable object that represent a layer of paint
 * @author michen
 *  unused
 */
@Deprecated
public class StrokeLayer {
	public final int color ;
	public final Path path ;
	
	public StrokeLayer (int color){
		this.color = color;
		path = new Path();
	}
}
