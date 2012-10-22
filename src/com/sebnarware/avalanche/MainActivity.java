package com.sebnarware.avalanche;

import java.util.List;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.os.Bundle;

public class MainActivity extends MapActivity implements DataListener {
	
	private MapView mapView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    
	    
        // data stuff (network and JSON)
        DataManager dataManager = new DataManager(this);
        dataManager.loadRegions(); 

	    
        // map view
	    mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);

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
