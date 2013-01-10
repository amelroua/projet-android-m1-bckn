package com.example.eyeway.Map;

import android.os.Bundle;

import com.example.eyeway.R;
import com.example.eyeway.fouilleDedonne.ListeLieu;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class PlacesMapActivity extends MapActivity{
	
	ListeLieu nearPlaces ;
	
	MapView mapView ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
}
