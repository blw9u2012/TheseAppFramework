package com.cs4240.mapViewTut;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class CustomPinpointList extends ItemizedOverlay {
	private ArrayList<OverlayItem> pinpoints = new ArrayList<OverlayItem>();
	private Context mContext;
	
	public CustomPinpointList(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
	    mContext = context;
	}

	//method used to add items to our overlay list...
	public void insertPinpointOverlayItem(OverlayItem o){
		pinpoints.add(o);
		populate();
	}
	
	@Override
	protected OverlayItem createItem(int i) {
	    return pinpoints.get(i);
	}

	@Override
	public int size() {
		return pinpoints.size();
	}
	
	@Override
	protected boolean onTap(int index){
		OverlayItem item = pinpoints.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();
		return true;
	}

}
