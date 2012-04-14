package com.cs4240.mapViewTut;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

public class TourListActivity extends Activity{
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tourlist);
        
        ArrayList<Tour> tourArrayList = new ArrayList<Tour>();
        

        Tour dormTour = new Tour("Dorm Tour", "Tour of UVA's fantastic dorms.");
        Tour libraryTour = new Tour("Library Tour", "Tour of UVA's awesome libraries.");
        
        tourArrayList.add(dormTour);
        tourArrayList.add(libraryTour);
        
        int[] buttonList = {R.id.button1, R.id.button2};
        
        for(int i = 0; i < buttonList.length; i++){
        	Button tourButton = (Button)findViewById(buttonList[i]);
        	final Tour tourObject = tourArrayList.get(i);
        	tourButton.setText(tourObject.getName());
            tourButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent tourIntent = new Intent(view.getContext(), TourPage.class);
                    tourIntent.putExtra("tour", tourObject);
                    startActivity(tourIntent);
                }
            });
        }
    }
}