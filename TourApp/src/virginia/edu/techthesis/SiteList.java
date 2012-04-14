package virginia.edu.techthesis;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SiteList extends Activity{
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sitelist);
        

        SiteManager siteManager = SiteManager.getInstance();
        
        ArrayList<Site> siteList = siteManager.getSiteList();
        
        addSiteButtons(siteList);
    }
    
    private void addSiteButtons(ArrayList<Site> siteList) {
        // retrieve a reference to the container layout
        LinearLayout buttonContainer = (LinearLayout)findViewById(R.id.buttonContainer);
        
        //dynamically generate site buttons
    	for(int i = 0; i < siteList.size(); i++){
        	Button siteButton = new Button(this);
        	final Site siteObject = siteList.get(i);
        	siteButton.setText(siteObject.getName());
            siteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent siteIntent = new Intent(view.getContext(), SitePage.class);
                    siteIntent.putExtra("site", siteObject);
                    startActivity(siteIntent);
                }
            });
            // adds dynamic button to the GUI
            buttonContainer.addView(siteButton);
    	}
    }
}

