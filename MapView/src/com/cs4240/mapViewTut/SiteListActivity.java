package com.cs4240.mapViewTut;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class SiteListActivity extends Activity {
	/** Called when the activity is first created. */
	String places;
	ArrayList<Site> siteList;
	LocationManager manager;
	Location location;
	double lng, lat;
	ArrayList<NameValuePair> nvp;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sitelist);

		SiteManager siteManager = SiteManager.getInstance();

		siteList = siteManager.getSiteList();

		//get user's location...
		manager = (LocationManager)this.getSystemService(LOCATION_SERVICE);
		Criteria crit = new Criteria();
		String provider = manager.getBestProvider(crit, true);
		location = manager.getLastKnownLocation(provider);
		
		
		Button showSiteMapButton = (Button)findViewById(R.id.showPlacesOnMapButton);
		Button placesSearchButton = (Button)findViewById(R.id.placesSearchButton);
		
		showSiteMapButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(SiteListActivity.this, SiteMapActivity.class);
				startActivity(i);
				
			}
			
		});
		
		placesSearchButton.setOnClickListener(new OnClickListener(){
			//TODO need to fix this...
			@Override
			public void onClick(View v) {
				//execute a places search around the user's current location..
				lat = location.getLatitude();
				lng = location.getLongitude();
				String latitude = String.valueOf(lat);
				String longitude = String.valueOf(lng);
				siteList.clear();
				nvp = new ArrayList<NameValuePair>();
				PlaceSearch ps = new PlaceSearch(nvp);
				ps.execute(latitude,longitude);
			}
			
		});

		//execute a places search around the user's current location..
		lat = location.getLatitude();
		lng = location.getLongitude();
		String latitude = String.valueOf(lat);
		String longitude = String.valueOf(lng);
		
		nvp = new ArrayList<NameValuePair>();
		PlaceSearch ps = new PlaceSearch(nvp);
		ps.execute(latitude,longitude);

		
	}

	private void addSiteButtons(ArrayList<Site> siteList) {
		// retrieve a reference to the container layout
		LinearLayout buttonContainer = (LinearLayout) findViewById(R.id.buttonContainer);

		// dynamically generate site buttons
		for (int i = 0; i < siteList.size(); i++) {
			
			Button siteButton = new Button(this);
			
			final Site siteObject = siteList.get(i);
			
			siteButton.setText(siteObject.getName());
			siteButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					Intent siteIntent = new Intent(view.getContext(),SitePage.class);
					siteIntent.putExtra("site", siteObject);
					startActivity(siteIntent);
				}
			});

			// adds dynamic button to the GUI
			buttonContainer.addView(siteButton);
		}
	}

	class PlaceSearch extends AsyncTask<String, Void, String> {

		// Fill in the API key you want to use.
		private static final String API_KEY = "AIzaSyAk4Ur6RglVpcgJsaz7yE0AADUz9rElO38";

		// The different Places API endpoints.
		private static final String PLACES_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";

		private static final boolean PRINT_AS_STRING = false;

		ArrayList<NameValuePair> nameValuePairs;
		HttpClient client;
		String result;

		public PlaceSearch(ArrayList<NameValuePair> nvp) {
			this.nameValuePairs = nvp;
			this.client = new DefaultHttpClient();
		}

		public String doSearch(String latitude, String longitude) {
			String line;
			try {
				String request_url = PLACES_SEARCH_URL + "key=" + API_KEY
						+ "&location=" + latitude + "," + longitude
						+ "&radius=1000&sensor=false";
				// HttpPost("https://maps.googleapis.com/maps/api/place/search/xml?location=-33.8670522,151.1957362&radius=500&types=food&name=harbour&sensor=false&key=AIzaSyAk4Ur6RglVpcgJsaz7yE0AADUz9rElO38");
				HttpPost httpPost = new HttpPost(request_url);
				HttpResponse httpResponse = client.execute(httpPost);
				HttpEntity entity = httpResponse.getEntity();
				InputStream is = entity.getContent();

				// get response string...
				BufferedReader bf = new BufferedReader(new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();
				while ((line = bf.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				result = sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		public void placeList(String str) {
			String vin = "vicinty";
			double rating = 2.3;
			try {
				JSONObject resultObject = new JSONObject(str);
				JSONArray results = resultObject.getJSONArray("results");
				for (int i = 0; i < results.length(); i++) {
					JSONObject site = results.getJSONObject(i);
					JSONObject siteLocation = site.getJSONObject("geometry").getJSONObject("location");

					double latitude = siteLocation.getDouble("lat");
					double longitude = siteLocation.getDouble("lng");
					if (!site.isNull("vicinity")) {
						vin = site.getString("vicinity");
					}

					if (!site.isNull("rating")) {
						rating = site.getDouble("rating");
					}
					String ref = site.getString("reference");
					String id = site.getString("id");
					String name = site.getString("name");
					String icon = site.getString("icon");

					// add site object
					Site newSite = new Site(latitude, longitude, vin, rating,name, ref, id,icon);
					siteList.add(newSite);

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		@Override
		protected String doInBackground(String... params) {
			PlaceSearch ps = new PlaceSearch(nameValuePairs);
			String latitude = params[0];
			String longitude = params[1];
			String result = ps.doSearch(latitude, longitude);
			ps.placeList(result);
			return result;
		}

		protected void onPostExecute(String result) {
			places = result;
			addSiteButtons(siteList);
		}
	}
}