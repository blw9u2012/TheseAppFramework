package com.cs4240.mapViewTut;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Tour implements Parcelable { 

    private String name; 
    private String description;
    private ArrayList<Site> sites;
    
    public Tour(String name, String description) { 
        this.name = name; 
        this.description = description;
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

    
    public static final Parcelable.Creator<Tour> CREATOR = 
        new Parcelable.Creator<Tour>() { 
        public Tour createFromParcel(Parcel in) { 
            String nameParam = in.readString(); 
            String descriptionParam = in.readString();
            return new Tour(nameParam, descriptionParam); 
        } 

        public Tour[] newArray(int size) { 
            return new Tour[size]; 
        } 
    }; 

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel p, int flags) {
		// TODO Auto-generated method stub
        p.writeString(name); 
        p.writeString(description); 
	}
} 
