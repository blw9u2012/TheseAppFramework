package virginia.edu.techthesis;

import android.os.Parcel;
import android.os.Parcelable;

public class Site implements Parcelable { 

    private String name; 
    private String description;
    private String category;
    private String imageFile;
    
    public Site(String name, String description, String category, String imageFile) { 
        this.name = name; 
        this.description = description;
        this.category = category;
        this.imageFile = imageFile;
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
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	
    
    public String getImageFile() {
		return imageFile;
	}

	public void setImageFile(String imageFile) {
		this.imageFile = imageFile;
	}



	public static final Parcelable.Creator<Site> CREATOR = 
        new Parcelable.Creator<Site>() { 
        public Site createFromParcel(Parcel in) { 
            String nameParam = in.readString(); 
            String descriptionParam = in.readString();
            String categoryParam = in.readString();
            String imageFileParam = in.readString();
            return new Site(nameParam, descriptionParam, categoryParam, imageFileParam); 
        } 

        public Site[] newArray(int size) { 
            return new Site[size]; 
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
        p.writeString(category); 
        p.writeString(imageFile);
	}
} 
