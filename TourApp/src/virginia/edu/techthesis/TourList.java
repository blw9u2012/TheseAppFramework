package virginia.edu.techthesis;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class TourList extends Activity{
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tourlist);
      
        TourManager tourManager = TourManager.getInstance();
        ArrayList<Tour> tourList = tourManager.getTourList();
      
        addTourButtons(tourList);
    }
    
    private void addTourButtons(ArrayList<Tour> tourList) {
        // retrieve a reference to the container layout
        LinearLayout buttonContainer = (LinearLayout)findViewById(R.id.buttonContainer);
        
        //dynamically generate site buttons
    	for(int i = 0; i < tourList.size(); i++){
        	Button tourButton = new Button(this);
        	final Tour tourObject = tourList.get(i);
        	tourButton.setText(tourObject.getName());
            tourButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent tourIntent = new Intent(view.getContext(), TourPage.class);
                    startActivity(tourIntent);
                }
            });

            // adds dynamic button to the GUI
            buttonContainer.addView(tourButton);
    	}
    }
}