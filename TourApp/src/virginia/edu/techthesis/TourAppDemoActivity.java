package virginia.edu.techthesis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TourAppDemoActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        SiteManager siteManager = SiteManager.getInstance();
        
        Button tours = (Button) findViewById(R.id.tourButton);
        tours.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent tourListIntent = new Intent(view.getContext(), TourList.class);
                startActivity(tourListIntent);
            }
        });
        
        Button sites = (Button) findViewById(R.id.siteButton);
        sites.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent siteListIntent = new Intent(view.getContext(), SiteList.class);
                startActivity(siteListIntent);
            }
        });
    }
}
    

