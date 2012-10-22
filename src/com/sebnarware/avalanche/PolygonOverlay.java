package com.sebnarware.avalanche;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class PolygonOverlay extends Overlay {

	private GeoPoint[] polygon;
	private Paint paintOutline;
	private Paint paintFill;
	
	public PolygonOverlay(GeoPoint[] polygon) {
		
	    this.polygon = polygon;

	    paintOutline = new Paint();
	    paintOutline.setARGB(165, 0, 0, 0);
	    paintOutline.setStrokeWidth(2);
	    paintOutline.setStrokeCap(Paint.Cap.ROUND);
	    paintOutline.setAntiAlias(true);
	    paintOutline.setDither(false);
	    paintOutline.setStyle(Paint.Style.STROKE);
	
	    paintFill = new Paint();
	    paintFill.setARGB(165, 255, 242, 0);
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		
	    // NOTE this method gets called twice (once for each with shadow true and false); we only need to 
		// draw once, so only take action for one of the two calls, for efficiency
	    if (!shadow) {
	    	
		    // create a path from the array of geo points
		    Path path = new Path();

		    for (int i = 0; i < polygon.length; i++) {
			    Point pointInPixels = mapView.getProjection().toPixels(polygon[i], null);
		    	if (i == 0) {
		    		path.moveTo(pointInPixels.x, pointInPixels.y);
		    	} else {
		    		path.lineTo(pointInPixels.x, pointInPixels.y);
		    	}
		    }

		    path.close();

		    // draw the outline, and the fill
		    canvas.drawPath(path, paintOutline);
		    canvas.drawPath(path, paintFill);
	    }
	}

}
