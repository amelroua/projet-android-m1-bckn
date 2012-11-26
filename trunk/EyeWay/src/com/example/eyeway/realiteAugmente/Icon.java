package com.example.eyeway.realiteAugmente;




import com.example.eyeway.R;
import com.google.android.maps.GeoPoint;

import android.content.Context;
import android.location.Location;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Icon extends LinearLayout{

	


	private TextView label ;
	private ImageView icon ;
	private GeoPoint geoPoint ;
	private Context ctx ;

	
	public Icon(Context context) {
		super(context);
	}
	
	
	public Icon(Context c , String name , double latitude , double longitude, Location myLocation){
		
		
		super(c);
		ctx = c ;
		label = new TextView(c);
		icon = new ImageView(c);
		
		
		
		// On ajoute notre nouveau point GPS
		geoPoint = new GeoPoint((int) (latitude * 100000), (int) (longitude * 100000)) ;
		Location location = new Location("myprovider");
		location.setLatitude(latitude);
		location.setLongitude(longitude);
		
		double distance = calculDistance(myLocation, location);

		this.modifierLabel(distance);
		
		icon.setClickable(true);
		
		icon.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Toast.makeText(ctx, "Cliquable l'image", Toast.LENGTH_SHORT).show();

			}
		});
		
		if (name.equalsIgnoreCase("restaurant")) {

			icon.setBackgroundResource(R.drawable.restaurant);

		} else {

			icon.setBackgroundResource(R.drawable.img_epingle);
		}

		this.addView(icon);
		this.addView(label);

	}
	
	private void modifierLabel(double distance){
		
		if (distance > 1000) {
			
			label.setText((int) distance / 1000 + " km ");
			
		} else {

			label.setText((int) distance / 1000 + " m ");

		}
	
	}
	
	public void miseAJourLabel(Location myLocation) {
		
		Location l = new Location("myProvider");
		l.setLatitude(this.getLatitude() / 100000);
		l.setLongitude(this.getLongitude() / 100000);
		
		double distance = calculDistance(myLocation,l);
		modifierLabel(distance);
	}
	
	public static double calculDistance(Location a, Location b) {

		return a.distanceTo(b);

	}
	// ------------- GETTERS AND SETTERS --------------------
	
	public double getLatitude(){
		
		return this.geoPoint.getLatitudeE6();
	}
	
	public double getLongitude(){
		
		return this.geoPoint.getLongitudeE6();
	}
	
	public TextView getLabel() {
		return label;
	}

	public void setLabel(TextView label) {
		this.label = label;
	}

	public ImageView getIcon() {
		return icon;
	}

	public void setIcon(ImageView icon) {
		this.icon = icon;
	}

	public GeoPoint getGeoPoint() {
		return geoPoint;
	}

	public void setGeoPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}

	public Context getCtx() {
		return ctx;
	}

	public void setCtx(Context ctx) {
		this.ctx = ctx;
	}


}
