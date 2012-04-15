package com.cs4240.mapViewTut;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;


public class RouteOverlay extends Overlay{
	private GeoPoint startPoint;
	private GeoPoint endPoint;

	
	public RouteOverlay(GeoPoint startPoint, GeoPoint endPoint) {
		super();
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}

	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
	    Projection projection = mapView.getProjection();
	    
	    if(shadow == false){
		    Paint paint = new Paint();
		    paint.setAntiAlias(true);
		    Point start = new Point();
		    projection.toPixels(startPoint, start);
		    paint.setColor(Color.BLUE);
		    Point end = new Point();
		    projection.toPixels(endPoint, end);
		    paint.setStrokeWidth(5);
		    canvas.drawLine((float)start.x,(float) start.y, (float)end.x, (float)end.y, paint);
	    }
	    return super.draw(canvas, mapView, shadow, when);

	    
	}
	@Override
	public void draw(Canvas canvas, MapView map, boolean shadow){
		super.draw(canvas, map, shadow);
	}
	
	
	
}
