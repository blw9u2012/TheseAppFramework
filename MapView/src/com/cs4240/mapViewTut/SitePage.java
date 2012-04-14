package com.cs4240.mapViewTut;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class SitePage extends Activity {
	ImageView image;
	TextView description;
	Site siteObject;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sitepage2);
        
        //get site object from sitelistactivity...
        Intent intent = getIntent();
        siteObject = (Site) intent.getParcelableExtra("site");

    	TextView title = (TextView)findViewById(R.id.title);
    	image = (ImageView)findViewById(R.id.image);
    	description = (TextView)findViewById(R.id.description);
        
    	title.setText(siteObject.getName());
    	description.setText(siteObject.getDescription());

    	int imageId = getResources().getIdentifier(siteObject.getName().toLowerCase(), "drawable", getPackageName());
    	if(imageId != 0){
    		image.setImageResource(imageId);	
    	}
    	else{
    		new loadPicture().execute(siteObject.icon);
    	}
    	
    }
    private class loadPicture extends AsyncTask<String, Void, Drawable>{

		@Override
		protected Drawable doInBackground(String... params) {
			String url = params[0];
			Drawable drawable = LoadImageFromWeb(url);
			return drawable;
		}
		
		protected void onPostExecute(Drawable d){
			image.setImageDrawable(d);
		}
    	
    }
  private Drawable LoadImageFromWeb(String url){
	  try {
		InputStream is = (InputStream)new URL(url).getContent();
		Drawable d = Drawable.createFromStream(is, "srcName");
		return d;
	} catch (Exception e) {
		e.printStackTrace();
		return null;
	} 
	  
  }
}