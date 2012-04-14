package com.cs4240.mapViewTut;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainPageActivity extends Activity{
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);
        TextView tv = (TextView)findViewById(R.id.tour_title);
        Button mapButton = (Button)findViewById(R.id.mapButton);
        Button siteButton = (Button)findViewById(R.id.siteButton);
        Button tourButton = (Button)findViewById(R.id.tourButton);
        
        mapButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainPageActivity.this, MapViewTutActivity.class);
				startActivity(i);
			}
        	
        });
        
        siteButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//Send it to a site page...
				Intent i = new Intent(MainPageActivity.this, SiteListActivity.class);
				startActivity(i);
			}
        	
        });
        
        tourButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainPageActivity.this, TourListActivity.class);
				startActivity(i);
				
			}
        	
        });
	}
	
		
	
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
