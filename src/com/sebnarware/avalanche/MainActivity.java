package com.sebnarware.avalanche;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.os.Bundle;


public class MainActivity extends MapActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
	    
        // testing map view
	    MapView mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);

	    
	    
	    // testing overlay
	    
		// BUGBUG temp data
	    GeoPoint[] polygon = new GeoPoint[3];
	    polygon[0] = new GeoPoint(50000000, -100000000);
	    polygon[1] = new GeoPoint(55000000, -100000000);
	    polygon[2] = new GeoPoint(55000000, -105000000);

	    PolygonOverlay overlay = new PolygonOverlay(polygon);

	    List<Overlay> mapOverlays = mapView.getOverlays();
	    mapOverlays.add(overlay);

	    
	    
        // testing network and JSON
        NetworkEngine networkEngine = new NetworkEngine();
        networkEngine.loadRegions(); 
    }
    

	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}

}
