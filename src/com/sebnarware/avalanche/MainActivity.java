package com.sebnarware.avalanche;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends MapActivity implements DataListener {
	
    private static final String TAG = "MainActivity";

	private MapView mapView;
	private MyLocationOverlay myLocationOverlay;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(TAG, "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    
	    
        // data stuff (network and JSON)
        DataManager dataManager = new DataManager(this);
        dataManager.loadRegions(); 

	    
        // map view
	    mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    
	    myLocationOverlay = new FixedMyLocationOverlay(this, mapView);
	    mapView.getOverlays().add(myLocationOverlay);
	    mapView.postInvalidate();
	    
	    zoomToMyLocation();
    }
	
	private void zoomToMyLocation() {
    	Log.i(TAG, "zoomToMyLocation called");

		GeoPoint myLocationGeoPoint = myLocationOverlay.getMyLocation();
		if(myLocationGeoPoint != null) {
	    	Log.i(TAG, "zoomToMyLocation location found");
			mapView.getController().animateTo(myLocationGeoPoint);
			mapView.getController().setZoom(8);
		}
		else {
	    	Log.i(TAG, "zoomToMyLocation location not found");
			Toast.makeText(this, "Cannot determine location", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onResume() {
    	Log.i(TAG, "onResume called");
		super.onResume();

		// when our activity resumes, we want to register for location updates
		myLocationOverlay.enableMyLocation();
	    
		// center on the user's location, if available
	    zoomToMyLocation();
	}

	@Override
	protected void onPause() {
    	Log.i(TAG, "onPause called");
		super.onPause();
		
		// when our activity pauses, we want to stop listening for location updates
		myLocationOverlay.disableMyLocation();
	}
	   
	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}

	@Override
	public void regionAdded(RegionData regionData) {
	    PolygonOverlay overlay = new PolygonOverlay(regionData.getPolygon());
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    mapOverlays.add(overlay);
	    
	    // force a redraw
	    // BUGBUG is there a better way to do this (i.e. just invalidate the specific overlay, not the whole map view)? 
	    mapView.invalidate();
	}

	@Override
	public void forecastUpdated(RegionData regionData) {
		
	    // force a redraw
	    // BUGBUG is there a better way to do this (i.e. just invalidate the specific overlay, not the whole map view)? 
	    mapView.invalidate();
	}
}
