package com.sebnarware.avalanche;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class PolygonOverlay extends Overlay {

	private GeoPoint point;
	private float radius; // in meters
	private Paint paintOutline;
	private Paint paintFill;
	
	public PolygonOverlay(GeoPoint point, float radius) {
	    this.point = point;
	    this.radius = radius;

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

		// BUGBUG temp data
	    GeoPoint[] polygon = new GeoPoint[3];
	    polygon[0] = new GeoPoint(50000000, -100000000);
	    polygon[1] = new GeoPoint(55000000, -100000000);
	    polygon[2] = new GeoPoint(55000000, -105000000);
	    
	    
	    // NOTE this method gets called twice (once for each with shadow true and false); we only need to draw once
	    if (!shadow) {
	    	
		    // create a path from the array of geo points
		    Path path = new Path();
		    path.setFillType(Path.FillType.EVEN_ODD);

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
