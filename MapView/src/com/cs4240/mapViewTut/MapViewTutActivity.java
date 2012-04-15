package com.cs4240.mapViewTut;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

public class MapViewTutActivity extends MapActivity {

	//Overlays...
	List<Overlay> mapOverlayList;
	MyLocationOverlay myLocationOverlay;
	MyLocationOverlay compass;
	
	//Icons for the maps...
	Drawable drawable;
	CustomPinpointList customPinpointList;
	
	//Views...
	MapView mv;
	TextView tv;
	EditText et;
	Button searchButton;
	
	//Map objects...
	MapController controller;
	private int longitude, latitude;
	GeoPoint touchPoint, begin, end;
	LocationManager locationManager;
	LocationListener locationListener;
	Location location;
	Projection projection;

	//pairs of lat and long points for giving directions...
	String[] pairs;
	SiteManager siteManager;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		siteManager = SiteManager.getInstance();
		
		// grab ui from layout file...
		mv = (MapView) findViewById(R.id.mapview);
	    searchButton = (Button)findViewById(R.id.searchButton);
	    et = (EditText)findViewById(R.id.searchBar);
		
		// configure map...
		mv.setBuiltInZoomControls(true);
		controller = mv.getController();
		projection = mv.getProjection();
		
		// the list of overlays that the mapview is keeping track of...
		mapOverlayList = mv.getOverlays();
		Touch t = new Touch();
		mapOverlayList.add(t);

		// add the compass to the map view...
		compass = new MyLocationOverlay(MapViewTutActivity.this, mv);
		mapOverlayList.add(compass);

		// get android marker and add it to the list of overlay objects...
		//drawable = this.getResources().getDrawable(R.drawable.androidmarker);
		drawable = this.getResources().getDrawable(R.drawable.marker_orange);
		
		customPinpointList = new CustomPinpointList(drawable,MapViewTutActivity.this);

		// this should give the user's current position...
		myLocationOverlay = new MyLocationOverlay(MapViewTutActivity.this, mv);
		mapOverlayList.add(myLocationOverlay);
		myLocationOverlay.runOnFirstFix(new Runnable() {

			@Override
			public void run() {
				controller.animateTo(myLocationOverlay.getMyLocation());
				controller.setZoom(15);
			}

		});
		
		//myLocationOverlay.draw(null, mv, true);
		
		// Get a reference to the system Location Manager...
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		Criteria crit = new Criteria();
		String provider = locationManager.getBestProvider(crit, false);

		//get the last known location if possible...
		location = locationManager.getLastKnownLocation(provider);

		// placing pinpoint at our location...
		if (location != null) {
			latitude = (int) location.getLatitude();
			longitude = (int) location.getLongitude();
		
			GeoPoint point = new GeoPoint(latitude, longitude);
			OverlayItem item = new OverlayItem(point, "In Charlottesville","I'm at UVA! Wahoowa!!");
			customPinpointList.insertPinpointOverlayItem(item);
			mapOverlayList.add(customPinpointList);
		}
		//tv.setText("Location: " + latitude + ", " + longitude);

		// Define a listener that responds to location updates...
		locationListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location
				// provider
				latitude = (int)(location.getLatitude()*1E6);
				longitude = (int)(location.getLongitude()*1E6);
				//tv.setText("Latitude: "+latitude+", Longitude: "+longitude);
				GeoPoint point = new GeoPoint(latitude, longitude);
				controller.animateTo(point);
				controller.setZoom(15);
				//OverlayItem item = new OverlayItem(point, "In Charlottesville","I'm at UVA! Wahoowa!!");
				//customPinpointList.insertPinpointOverlayItem(item);
				//mapOverlayList.add(customPinpointList);
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
		
		//If the user wants directions give them to him or her..
		// TODO Get the user's current location and desired location if they press the search button...
		//new GetDirections().execute(location.getLatitude()+","+location.getLongitude(),"Washington,D.C.");
	}

	@Override
	protected void onPause() {
		compass.disableCompass();
		myLocationOverlay.disableMyLocation();
		locationManager.removeUpdates(locationListener);
		super.onPause();
	}

	@Override
	protected void onResume() {
		compass.enableCompass();
		// Register the listener with the Location Manager to receive location updates...
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,	100, locationListener);
		myLocationOverlay.enableMyLocation();
		
		searchButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String searchText = et.getText().toString();
				ArrayList<Site> siteList = siteManager.getSiteList();
				Comparator<Site> c = new Comparator<Site>(){

					@Override
					public int compare(Site s1, Site s2) {
						return s1.getName().compareTo(s2.getName());
					}
					
				};
				int index = Collections.binarySearch(siteList, new Site(searchText),c);
				Toast.makeText(getApplicationContext(), searchText, Toast.LENGTH_LONG);
			}
			
		});
		super.onResume();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	//another overlay...a class to handle touch events basically...
	class Touch extends Overlay {
		long start;
		long stop;

		public boolean onTouchEvent(MotionEvent e, MapView map) {
			if (e.getAction() == MotionEvent.ACTION_DOWN) {
				start = e.getEventTime();
				latitude = (int) e.getX();
				longitude = (int) e.getY();

				// place where we touch on the screen...
				touchPoint = map.getProjection()
						.fromPixels(latitude, longitude);
			}
			if (e.getAction() == MotionEvent.ACTION_UP) {
				stop = e.getEventTime();
			}
			if (stop - start > 1500) {
				AlertDialog alert = new AlertDialog.Builder(
						MapViewTutActivity.this).create();
				alert.setTitle("Pick An Action");
				alert.setButton("Pinpoint",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								OverlayItem overlayItem = new OverlayItem(
										touchPoint, "Example", "Example text..");
								CustomPinpointList pinpoint = new CustomPinpointList(
										drawable, MapViewTutActivity.this);
								pinpoint.insertPinpointOverlayItem(overlayItem);
								// mapOverlayList contains all of the overlays.
								mapOverlayList.add(pinpoint);

							}
						});
				alert.setButton2("Get Address",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Geocoder geocoder = new Geocoder(
										getBaseContext(), Locale.getDefault());
								// list that holds addresses
								try {
									List<Address> address = geocoder.getFromLocation(
											touchPoint.getLatitudeE6() / 1E6,
											touchPoint.getLongitudeE6() / 1E6,
											1);
									if (address.size() > 0) {
										String display = "";

										for (int i = 0; i < address.get(0)
												.getMaxAddressLineIndex(); i++) {
											Address add = address.get(0);
											// display +=
											// address.get(0).getAddressLine(i)+"\n";
											display += add.getAddressLine(i)
													+ "\n";
										}
										Toast t = Toast.makeText(
												getBaseContext(), display,
												Toast.LENGTH_LONG);
										t.show();
									}
								} catch (IOException e) {

									e.printStackTrace();
								}

							}
						});
				alert.setButton3("Toggle View",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (mv.isSatellite()) {
									mv.setSatellite(false);
								} else {
									mv.setSatellite(true);
								}

							}
						});
				alert.show();
				return true;
			}
			return false;
		}

	}
	
	class GetDirections extends AsyncTask<String, Void, String[]>{

		@Override
		protected String[] doInBackground(String... params) {
			String source = params[0];
			String dest = params[1];
			String urlString =  "http://maps.google.com/maps?f=d&hl=en&saddr="
					   + source + "&daddr=" + dest
					   + "&ie=UTF8&0&om=0&output=kml";
			
			Document doc = null;
			String pathContent = "";
			
			try {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(urlString);
				HttpResponse response = client.execute(get);
				HttpEntity entity = response.getEntity();
				
				
				//start parsing the response
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				doc = db.parse(entity.getContent());
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			NodeList n1 = doc.getElementsByTagName("LineString");
			for(int i = 0; i < n1.getLength(); i++){
				Node rootNode = n1.item(i);
				NodeList configItems = rootNode.getChildNodes();

				for(int j = 0; j < configItems.getLength(); j++){
					Node lineStringNode = configItems.item(j);
					NodeList path = lineStringNode.getChildNodes();
					pathContent = path.item(0).getNodeValue();
				}
			}
			String[] tempContent = pathContent.split(" ");
			return tempContent;

		}
		
		protected void onPostExecute(String[] content){
			pairs = content;
			String[] lnglat = pairs[0].split(",");
			
			//Starting point...
			GeoPoint startingPoint = new GeoPoint((int)(Double.parseDouble(lnglat[1]) * 1E6),(int)(Double.parseDouble(lnglat[0]) * 1E6));
			begin = startingPoint;
			controller.setCenter(begin);
			controller.setZoom(17);
			mapOverlayList.add(new RouteOverlay(startingPoint, startingPoint));
			
			//navigate the path...
			GeoPoint gp1;
			GeoPoint gp2 = begin;
			
			for(int i = 1; i < pairs.length; i++){
				lnglat = pairs[i].split(",");
				gp1 = gp2;
				
				gp2 = new GeoPoint((int)(Double.parseDouble(lnglat[1]) * 1E6),(int)(Double.parseDouble(lnglat[0]) * 1E6));
				mapOverlayList.add(new RouteOverlay(gp1,gp2));
			}
			
			mapOverlayList.add(new RouteOverlay(gp2,gp2));
			controller.animateTo(begin);
		}
		
	}

}