package virginia.edu.techthesis;

import java.util.ArrayList;

public class Tour{ 

    private String name; 
    private String description;
    private ArrayList<Site> siteList;
    
    public Tour(String name, String description, ArrayList<Site> siteList) { 
        this.name = name; 
        this.description = description;
        this.siteList = siteList;
    } 

	//getters and setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
    public ArrayList<Site> getSiteList() {
		return siteList;
	}
	public void setSiteList(ArrayList<Site> siteList) {
		this.siteList = siteList;
	}
} 
