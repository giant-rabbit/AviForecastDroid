package com.sebnarware.avalanche;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends MapActivity implements LocationListener, DataListener {
	
    private static final String TAG = "MainActivity";

	private MapView mapView;
	private LocationManager locationManager;
	private MyLocationOverlay myLocationOverlay;
	private boolean haveUpdatedUserLocation = false;

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
	    mapView.invalidate();
	    

        
        // location stuff
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation != null) {
        	onLocationChanged(lastKnownLocation);
        }

    }
	
	@Override
	public void regionAdded(RegionData regionData) {
	    PolygonOverlay overlay = new PolygonOverlay(regionData);
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

	@Override
	protected void onResume() {
    	Log.i(TAG, "onResume called");
		super.onResume();

		// when our activity resumes, we want to register for location updates
		myLocationOverlay.enableMyLocation();
	    
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1000, this);
	}

	@Override
	protected void onPause() {
    	Log.i(TAG, "onPause called");
		super.onPause();
		
		// when our activity pauses, we want to stop listening for location updates
		myLocationOverlay.disableMyLocation();
		
		locationManager.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		if (location != null && !haveUpdatedUserLocation) {
	    	Log.i(TAG, "onLocationChanged location found, positioning map");
	    	GeoPoint locationGeoPoint = new GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6));
			mapView.getController().animateTo(locationGeoPoint);
			mapView.getController().setZoom(8);

			haveUpdatedUserLocation = true;
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
	   
	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}

}
