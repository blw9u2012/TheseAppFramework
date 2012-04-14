package virginia.edu.techthesis;

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

public class SitePage extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sitepage);
        
        
        Intent intent = getIntent();
        Site siteObject = (Site) intent.getParcelableExtra("site");

    	TextView title = (TextView)findViewById(R.id.title);
    	ImageView image = (ImageView)findViewById(R.id.image);
    	TextView description = (TextView)findViewById(R.id.description);
        
    	title.setText(siteObject.getName());
    	description.setText(siteObject.getDescription());
    	String imageString = siteObject.getImageFile();

    	int imageId = getResources().getIdentifier(imageString, "drawable", getPackageName());
    	image.setImageResource(imageId);
    }
}