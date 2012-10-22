package com.sebnarware.avalanche;

import com.google.android.maps.GeoPoint;

public class RegionData {
	
	private String regionId; 
	private String displayName;
	private String URL;
	private GeoPoint[] polygon;
	
	public RegionData(String regionId, String displayName, String URL, GeoPoint[] polygon) {
	    this.regionId = regionId;
	    this.displayName = displayName;
	    this.URL = URL;
	    this.polygon = polygon;
	}

	public String getRegionId() {
		return regionId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getURL() {
		return URL;
	}

	public GeoPoint[] getPolygon() {
		return polygon;
	}
	
	// set forecast
	
	// aviLevelForMode
}
