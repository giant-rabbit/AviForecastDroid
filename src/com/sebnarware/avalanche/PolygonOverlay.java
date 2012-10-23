package com.sebnarware.avalanche;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class PolygonOverlay extends Overlay {

	private static final int OVERLAY_ALPHA = (int) (0.65 * 255);
	
	private RegionData regionData;
	private Paint paintOutline;
	
	public PolygonOverlay(RegionData regionData) {
		
	    this.regionData = regionData;

	    paintOutline = new Paint();
	    paintOutline.setARGB(OVERLAY_ALPHA, 0, 0, 0);
	    paintOutline.setStrokeWidth(2);
	    paintOutline.setStrokeCap(Paint.Cap.ROUND);
	    paintOutline.setAntiAlias(true);
	    paintOutline.setDither(false);
	    paintOutline.setStyle(Paint.Style.STROKE);
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		
	    // NOTE this method gets called twice (once for each with shadow true and false); we only need to 
		// draw once, so only take action for one of the two calls, for efficiency
	    if (!shadow) {
	    	
		    // create a path from the array of geo points
	    	// because paths are in pixels, not geopoints, we have to rebuild this every time the map zooms
		    Path path = new Path();
		    GeoPoint[] polygon = regionData.getPolygon();

		    for (int i = 0; i < polygon.length; i++) {
			    Point pointInPixels = mapView.getProjection().toPixels(polygon[i], null);
		    	if (i == 0) {
		    		path.moveTo(pointInPixels.x, pointInPixels.y);
		    	} else {
		    		path.lineTo(pointInPixels.x, pointInPixels.y);
		    	}
		    }
		    path.close();

		    // get the appropriate avi level fill color (based on the forecast for the region and the timeframe mode)
		    int aviLevel = regionData.aviLevelForCurrentTimeframeMode();
		    Paint paintFill = getColorForAviLevel(aviLevel); 
		    
		    // draw the outline, and the fill
		    canvas.drawPath(path, paintOutline);
		    canvas.drawPath(path, paintFill);
	    }
	}
	
	private Paint getColorForAviLevel(int aviLevel) {
		
		Paint paint = new Paint();
		
		switch (aviLevel) {
		case AviLevel.LOW:
			paint.setARGB(OVERLAY_ALPHA, 80, 184, 72);
			break;
		case AviLevel.MODERATE:
			paint.setARGB(OVERLAY_ALPHA, 255, 242, 0);
			break;
		case AviLevel.CONSIDERABLE:
			paint.setARGB(OVERLAY_ALPHA, 247, 148, 30);
			break;
		case AviLevel.HIGH:
			paint.setARGB(OVERLAY_ALPHA, 237, 28, 36);
			break;
		case AviLevel.EXTREME:
			paint.setARGB(OVERLAY_ALPHA, 35, 31, 32);
			break;
		default:
			paint.setARGB(OVERLAY_ALPHA, 255, 255, 255);
			break;
		}
		
		return paint;
	}

}
