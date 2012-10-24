package com.sebnarware.avalanche;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class PolygonOverlay extends Overlay {
	
    private static final String TAG = "PolygonOverlay";

	private static final int OVERLAY_ALPHA = (int) (0.65 * 255);
	
	private Context context; 
	private RegionData regionData;
	private Paint paintOutline;

	public PolygonOverlay(Context context, RegionData regionData) {
		
		this.context= context;
		
	    this.regionData = regionData;

	    this.paintOutline = new Paint();
	    this.paintOutline.setARGB(OVERLAY_ALPHA, 0, 0, 0);
	    this.paintOutline.setStrokeWidth(2);
	    this.paintOutline.setStrokeCap(Paint.Cap.ROUND);
	    this.paintOutline.setAntiAlias(true);
	    this.paintOutline.setDither(false);
	    this.paintOutline.setStyle(Paint.Style.STROKE);
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		
	    // NOTE this method gets called twice (once for each with shadow true and false); we only need to 
		// draw once, so only take action for one of the two calls, for efficiency
	    if (!shadow) {
	    	
		    // create a path from the array of geo points
	    	// because paths are in pixels, not geopoints, we have to rebuild this every time the map zooms
		    Path path = new Path();
		    GeoPoint[] polygon = this.regionData.getPolygon();

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
		    int aviLevel = this.regionData.aviLevelForCurrentTimeframeMode();
		    Paint paintFill = getColorForAviLevel(aviLevel); 
		    
		    // draw the outline, and the fill
		    canvas.drawPath(path, this.paintOutline);
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

	@Override
	public boolean onTap(GeoPoint point, MapView mapView) {
		
		// first, project lat/lon points onto a flat surface, so we can run the point in polygon algorithm
		GeoPoint[] polygon = this.regionData.getPolygon();
		Point[] polygonInPixels = new Point[polygon.length];
		for (int i = 0; i < polygon.length; i++) {
			polygonInPixels[i] = mapView.getProjection().toPixels(polygon[i], null);
	    }

		Point pointInPixels = mapView.getProjection().toPixels(point, null);

		// do the point in polygon check
		boolean pointInPolygon = contains(polygonInPixels, pointInPixels);
		
		if (pointInPolygon) {
			Log.i(TAG, "onTap tap was in polygon of region: " + this.regionData.getRegionId());
			
			// start the web view activity
		    Intent intent = new Intent(this.context, WebViewActivity.class);
		    String url = this.regionData.getURL();
		    intent.putExtra(MainActivity.INTENT_EXTRA_WEB_VIEW_URL, url);
			Log.i(TAG, "onTap starting web view; url: " + url);
			this.context.startActivity(intent);
		}

		return pointInPolygon;
	}
	
    // return true if the given point is contained inside the polygon
    // see: http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
	public boolean contains(Point[] polygon, Point point) {

		boolean pointInPolygon = false;

		for (int i = 0, j = polygon.length - 1; i < polygon.length; j = i++) {
			if ((polygon[i].y > point.y) != (polygon[j].y > point.y)
					&& (point.x < (polygon[j].x - polygon[i].x) * (point.y - polygon[i].y) / (polygon[j].y - polygon[i].y) + polygon[i].x)) {
				pointInPolygon = !pointInPolygon;
			}
		}

		return pointInPolygon;
	}

}
