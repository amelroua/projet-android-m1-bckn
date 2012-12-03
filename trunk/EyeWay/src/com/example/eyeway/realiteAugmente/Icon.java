package com.example.eyeway.realiteAugmente;




import java.util.ArrayList;

import com.example.eyeway.R;
import com.example.eyeway.Map.Map;
import com.google.android.maps.GeoPoint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class Icon extends LinearLayout{

	

	//The "x" and "y" position of the "Show Button" on screen.
		Point p;
	private TextView label ;
	private ImageView icon ;
	private GeoPoint geoPoint ;
	private Context ctx ;
	private View layout ;
	private String name ;
	
	public Icon(Context context, Activity a) {
		super(context);
	}
	
	
	
	/// TEST ///
	
	
	
	//////////////////
	
	public Icon(Context c , String name , double latitude , double longitude, Location myLocation){
		
		
		super(c);
		this.name = name ;
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
				
			    	   
			    	   showBoiteInformation();
			    

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
	
	
	// ---------- Essai PopPup ---------
	
	
	
	void showBoiteChoix(){
		

	    
	 
 

       
        //Création de l'AlertDialog
        AlertDialog.Builder adb = new AlertDialog.Builder(ctx );
        
        //adb.setView(alertDialogView);
      
        //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
        //adb.setView(alertDialogView);
 
        //On donne un titre à l'AlertDialog
        adb.setTitle("Choix Itinéraire");
 
        //On modifie l'icône de l'AlertDialog pour le fun ;)
        adb.setIcon(R.drawable.info);
        
    
       
      //  adb.setCancelable(true);
        String [] choix = {"Map","Réalité Augmentée"};
        
      
        adb.setItems(choix, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
              	Intent monIntent = new Intent(ctx,Map.class);
            	ctx.startActivity(monIntent);
            	dialog.dismiss();
        }
        });
        

  
        adb.show();
	}
	
	
	void showBoiteInformation() {
		
		//On instancie notre layout en tant que View
        LayoutInflater factory = LayoutInflater.from(ctx);
        final View alertDialogView = factory.inflate(R.layout.popup_layout, null);
 
         
        //Création de l'AlertDialog
        AlertDialog.Builder adb = new AlertDialog.Builder(ctx);
 
        //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
        adb.setView(alertDialogView);
 
        //On donne un titre à l'AlertDialog
        adb.setTitle("Informations");
 
        //On modifie l'icône de l'AlertDialog pour le fun ;)
        adb.setIcon(R.drawable.info);
        
        TextView text = (TextView) alertDialogView.findViewById(R.id.titre);
        text.setText(name);
        
        
        text = (TextView) alertDialogView.findViewById(R.id.description);
        text.setText("Petit resto sympa en bord de mer \n A NE PAS LOUPER Petit resto sympa en bord de mer \n A NE PAS LOUPERPetit resto sympa en bord de mer \n A NE PAS LOUPERPetit resto sympa en bord de mer \n A NE PAS LOUPERPetit resto sympa en bord de mer \n A NE PAS LOUPERPetit resto sympa en bord de mer \n A NE PAS LOUPERPetit resto sympa en bord de mer \n A NE PAS LOUPERPetit resto sympa en bord de mer \n A NE PAS LOUPERPetit resto sympa en bord de mer \n A NE PAS LOUPERPetit resto sympa en bord de mer \n A NE PAS LOUPERPetit resto sympa en bord de mer \n A NE PAS LOUPERPetit resto sympa en bord de mer \n A NE PAS LOUPER");

        
        text = (TextView) alertDialogView.findViewById(R.id.distance);
        text.setText(this.label.getText());
       
      //  adb.setCancelable(true);
        	
        adb.setPositiveButton("Itinéraire", new DialogInterface.OnClickListener() {
 			public void onClick(DialogInterface dialog, int which) {
 				
 				showBoiteChoix();
 				
 			}
 		});
        // On crée un bouton "Annuler" à notre AlertDialog et on lui affecte un
     		// évènement
     		adb.setNegativeButton("Fermer", new DialogInterface.OnClickListener() {
     			public void onClick(DialogInterface dialog, int which) {
     				
     				
     				
     			}
     		});
        
        adb.show();
		
		
	}
	
	// Get the x and y position after the button is draw on screen
	// (It's important to note that we can't get the position in the onCreate(),
	// because at that stage most probably the view isn't drawn yet, so it will return (0, 0))
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

	   int[] location = new int[2];

	   // Get the x, y location and store it in the location[] array
	   // location[0] = x, location[1] = y.
	   icon.getLocationOnScreen(location);

	   //Initialize the Point with x, and y positions
	   p = new Point();
	   p.x = location[0];
	   p.y = location[1];
	}
	
	// The method that displays the popup.
		void showPopup(Point p) {
	   int popupWidth = 400;
	   int popupHeight = 300;

	   // Inflate the popup_layout.xml
	   LinearLayout viewGroup = (LinearLayout) ((Activity) ctx).findViewById(R.id.popup);
	   LayoutInflater layoutInflater = (LayoutInflater) ctx
	     .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	   layout = layoutInflater.inflate(R.layout.popup_layout, viewGroup);

	   // Creating the PopupWindow
	   final PopupWindow popup = new PopupWindow(ctx);
	   popup.setContentView(layout);
	   popup.setWidth(popupWidth);
	   popup.setHeight(popupHeight);
	   popup.setFocusable(true);

	   // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
	   int OFFSET_X = 30;
	   int OFFSET_Y = 30;

	   // Clear the default translucent background
	   popup.setBackgroundDrawable(new BitmapDrawable());

	   // Displaying the popup at the specified location, + offsets.
	   popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);
/*
	   // Getting a reference to Close button, and close the popup when clicked.
	   Button close = (Button) layout.findViewById(R.id.close);
	   close.setOnClickListener(new OnClickListener() {

	     @Override
	     public void onClick(View v) {
	       popup.dismiss();
	     }
	   });
	   */
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
