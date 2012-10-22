package com.sebnarware.avalanche;

import com.google.android.maps.GeoPoint;

public class RegionData {
	
	private String regionId; 
	private String displayName;
	private String URL;
	private GeoPoint[] polygon;
	private ForecastDay[] forecast;

	public RegionData(String regionId, String displayName, String URL, GeoPoint[] polygon) {
	    this.regionId = regionId;
	    this.displayName = displayName;
	    this.URL = URL;
	    this.polygon = polygon;
	}
	
	public void setForecast(ForecastDay[] forecast) {
		this.forecast = forecast;
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
	
	public ForecastDay[] getForecast() {
		return forecast;
	}
	
	public int aviLevelForTimeframeMode() {
		// BUGBUG temp
		return AviLevel.CONSIDERABLE;
	}

}
