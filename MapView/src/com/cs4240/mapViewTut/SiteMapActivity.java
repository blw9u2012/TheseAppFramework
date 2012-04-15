package com.cs4240.mapViewTut;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class SiteMapActivity extends MapActivity{
	SiteManager siteManager;
	CustomPinpointList pinpointList;
	GeoPoint point;
	MapView map;
	Drawable d;
	MapController controller;
	List<Overlay> mapOverlayList;
	LocationManager manager;
	LocationListener locationListener;
	Location location;
	MyLocationOverlay myLocation;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.site_map);
		map = (MapView)findViewById(R.id.siteMapView);
		d = getResources().getDrawable(R.drawable.marker_orange);
		controller = map.getController();
		mapOverlayList = map.getOverlays();
		manager = (LocationManager)this.getSystemService(LOCATION_SERVICE);
		siteManager = SiteManager.getInstance();
		pinpointList = new CustomPinpointList(d,SiteMapActivity.this);
		ArrayList<Site> sites = siteManager.getSiteList();
		
		map.setBuiltInZoomControls(true);
		Criteria crit = new Criteria();
		String provider = manager.getBestProvider(crit, false);

		//get the last known location if possible...
		location = manager.getLastKnownLocation(provider);
		
		// placing pinpoint at our location...
		if (location != null) {
			int latitude = (int) location.getLatitude();
			int longitude = (int) location.getLongitude();
		
			GeoPoint point = new GeoPoint(latitude, longitude);
			OverlayItem item = new OverlayItem(point, "Me","I'm here...");
			pinpointList.insertPinpointOverlayItem(item);
			mapOverlayList.add(pinpointList);
			
		}
		
		myLocation = new MyLocationOverlay(SiteMapActivity.this, map);
		myLocation.runOnFirstFix(new Runnable(){

			@Override
			public void run() {
				controller.animateTo(myLocation.getMyLocation());
				//controller.setZoom(19);
				
			}
			
		});
		
		// Define a listener that responds to location updates...
		locationListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location
				// provider
				int latitude = (int)(location.getLatitude()*1E6);
				int longitude = (int)(location.getLongitude()*1E6);

				GeoPoint point = new GeoPoint(latitude, longitude);
				controller.animateTo(point);
				//controller.setZoom(15);
				}

			@Override
			public void onProviderDisabled(String provider) {

			}

			@Override
			public void onProviderEnabled(String provider) {

			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}
		};
		// Register the listener with the Location Manager to receive location
		// updates...
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				100, locationListener);
		
		for(int i = 0; i < sites.size(); i++){
			Site poi = sites.get(i);
			point = new GeoPoint((int) (poi.latitude*1E6), (int)(poi.longitude*1E6));
			OverlayItem item = new OverlayItem(point,poi.getName(),poi.getDescription());
			pinpointList.insertPinpointOverlayItem(item);
			
		}
		mapOverlayList.add(pinpointList);
		
		
		
	}
		
	@Override
	protected void onPause() {
		myLocation.disableMyLocation();
		manager.removeUpdates(locationListener);
		super.onPause();
	}

	@Override
	protected void onResume() {
		myLocation.enableMyLocation();
		super.onResume();
	}
		
		
	@Override
	protected boolean isRouteDisplayed() {
		
		return false;
	}

}
