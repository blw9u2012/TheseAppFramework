package com.cs4240.mapViewTut;

import java.util.ArrayList;


public class SiteManager{
	
	private static SiteManager instance = null;
	
	public static SiteManager getInstance() {
		if(instance == null){
			instance = new SiteManager();
		}
		return instance;
	}
	
	//initialize array list
    private ArrayList<Site> siteList = new ArrayList<Site>();
    
    //create sites here
    Site clemons = new Site("Clemons", "It's a library that opens 24/7. It smells.", "Library");
    Site rotunda = new Site("Rotunda", "It's a building that looks cool. It's located on the lawn.", "SightSeeing");
    
    //default constructor for site manager
    public SiteManager(){
    	siteList.add(clemons);
    	siteList.add(rotunda);
    }

	public ArrayList<Site> getSiteList() {
		return siteList;
	}

	public void setSiteList(ArrayList<Site> siteArrayList) {
		this.siteList = siteArrayList;
	}
}
