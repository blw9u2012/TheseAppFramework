package virginia.edu.techthesis;

import java.util.ArrayList;


public class TourManager{
	
	private static TourManager instance = null;
	
	public static TourManager getInstance() {
		if(instance == null){
			instance = new TourManager();
		}
		return instance;
	}
	
	//initialize array list
    private ArrayList<Tour> tourList = new ArrayList<Tour>();
    SiteManager siteManager = SiteManager.getInstance();
  
    //create tours here
    Tour libraryTour = new Tour("Library Tour", "Tour of UVA's awesome libraries.", siteManager.getCategory("Library"));
    
    //default constructor for tour manager
    public TourManager(){
    	tourList.add(libraryTour);
    }

	public ArrayList<Tour> getTourList() {
		return tourList;
	}

	public void setTourList(ArrayList<Tour> tourList) {
		this.tourList = tourList;
	}
}
