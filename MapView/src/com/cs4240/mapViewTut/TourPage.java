package com.cs4240.mapViewTut;

import java.io.File;
import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TourPage extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tourpage);
        
        Intent intent = getIntent();
        Tour tourObject = (Tour) intent.getParcelableExtra("tour");

    	TextView title = (TextView)findViewById(R.id.title);
    	TextView description = (TextView)findViewById(R.id.description);
        
    	title.setText(tourObject.getName());
    	description.setText(tourObject.getDescription());
    }
}