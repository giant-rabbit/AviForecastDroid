package com.sebnarware.avalanche;

import java.util.List;

//import com.flurry.android.FlurryAgent;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ToggleButton;

public class MainActivity extends MapActivity implements DataListener {
	
    public final static String INTENT_EXTRA_WEB_VIEW_URL = "com.sebnarware.avalanche.WEB_VIEW_URL";

    private static final String TAG = "MainActivity";
    private static final int INFO_DIALOG = 1;
    private static final int DEFAULT_MAP_ZOOM_LEVEL = 8;

    private DataManager dataManager;
	private MapView mapView;
	private MyLocationOverlay myLocationOverlay;
	private ToggleButton buttonToday;
	private ToggleButton buttonTomorrow;
	private ToggleButton buttonTwoDaysOut;

//	@Override
//	protected void onStart()
//	{
//		super.onStart();
//		FlurryAgent.onStartSession(this, "29QWBK7Z3ZYCY8CBHGM5");
//	}
//	 
//	@Override
//	protected void onStop()
//	{
//		super.onStop();		
//		FlurryAgent.onEndSession(this);
//	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	Log.i(TAG, "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            
        
        // data stuff (network and JSON)
        dataManager = new DataManager(this);
        dataManager.loadRegions(); 
	    
        
        // map view
	    mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);

	    
	    // add an overlay that draws the blue dot at the user location
	    // NOTE use a overloaded version of MyLocationOverlay to deal with a bug on some phones; see http://joshclemm.com/blog/?p=148
	    myLocationOverlay = new FixedMyLocationOverlay(this, mapView);
	    mapView.getOverlays().add(myLocationOverlay);
	    mapView.invalidate();
	    
	    // when we get a first location fix, pan/zoom the map around the user's location
	    myLocationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
            	// NOTE run this on the UI thread
            	runOnUiThread(new Runnable() {
            		public void run() {
            			// pan and zoom the map
            			mapView.getController().animateTo(myLocationOverlay.getMyLocation());
            			mapView.getController().setZoom(DEFAULT_MAP_ZOOM_LEVEL);
            		}
            	});
            }
        });
	    
	    
        // set up mode timeframe mode buttons
        buttonToday = (ToggleButton) findViewById(R.id.buttonToday);
        buttonTomorrow = (ToggleButton) findViewById(R.id.buttonTomorrow);
        buttonTwoDaysOut = (ToggleButton) findViewById(R.id.buttonTwoDaysOut);
        setTimeframeMode(TimeframeMode.Today);

        
        // set up legend with a click action
        ImageView imageViewLegend = (ImageView) findViewById(R.id.imageviewLegend);
        final Context self = this; 
        imageViewLegend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick called for legend image view; starting danger scale activity");
                
    		    Intent intent = new Intent(self, DangerScaleActivity.class);
    			self.startActivity(intent);
            }
        });
    }

	@Override
	protected void onResume() {
    	Log.i(TAG, "onResume called");
		super.onResume();

		// when our activity resumes, we want to start listening for location updates
		myLocationOverlay.enableMyLocation();
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
	    PolygonOverlay overlay = new PolygonOverlay(this, regionData);
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    mapOverlays.add(overlay);
	    
	    // force a redraw
	    mapView.invalidate();
	}

	@Override
	public void forecastUpdated(RegionData regionData) {
		
	    // force a redraw
	    mapView.invalidate();
	}

	public void setTimeframeToToday(View view) {
		setTimeframeMode(TimeframeMode.Today);
	}
	
	public void setTimeframeToTomorrow(View view) {
		setTimeframeMode(TimeframeMode.Tomorrow);
	}

	public void setTimeframeToTwoDaysOut(View view) {
		setTimeframeMode(TimeframeMode.TwoDaysOut);
	}

	private void setTimeframeMode(TimeframeMode timeframeMode) {

		Log.i(TAG, "setTimeframeMode setting mode to: " + timeframeMode);
		
		dataManager.setTimeframeMode(timeframeMode);
		
		// update button toggle states
		switch (timeframeMode) {
			case Today:
				buttonToday.setChecked(true);
				buttonTomorrow.setChecked(false);
				buttonTwoDaysOut.setChecked(false);
				break;
			case Tomorrow:
				buttonToday.setChecked(false);
				buttonTomorrow.setChecked(true);
				buttonTwoDaysOut.setChecked(false);
				break;
			case TwoDaysOut:
				buttonToday.setChecked(false);
				buttonTomorrow.setChecked(false);
				buttonTwoDaysOut.setChecked(true);
				break;
		}
		
	    // force a redraw
	    mapView.invalidate();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_main, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.menuitem_info:
	    		Log.i(TAG, "onOptionsItemSelected received click on info");
	    		showDialog(INFO_DIALOG);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case INFO_DIALOG:
			Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.dialog_info_title);
			builder.setMessage(R.string.dialog_info_message);
			builder.setPositiveButton(R.string.dialog_info_positive_button, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
		    		Log.i(TAG, "CancelOnClickListener ok clicked");
				}
			});
			AlertDialog dialog = builder.create();
			dialog.show();
		}
		return super.onCreateDialog(id);
	}
	
}
