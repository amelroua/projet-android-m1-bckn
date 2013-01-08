package com.example.eyeway.menuPrincipal;

import com.example.eyeway.R;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost.Settings;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;


public class GPSTracker extends Service implements LocationListener{

	private final Context mContext ;

	AlertDialogManager alert  = new AlertDialogManager();

	
	boolean isGPSEnabled;

	boolean isNetworkEnabled = false;

	boolean canGetLocation = false ;

	Location location = null ;

	double latitude ;
	double longitude ;

	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10 ;

	private static final long MIN_TIME_BW_UPDATES = 10000 ;


	protected LocationManager locationManager;

	public GPSTracker(Context context){
		this.mContext = context;
		isGPSEnabled = false;
		getLocation();
	}

	public Location getLocation(){

		try{
			locationManager = (LocationManager) mContext
					.getSystemService(Context.LOCATION_SERVICE);

			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if(!isGPSEnabled && !isNetworkEnabled){

				showSettingsAlert("Vous devez activé soit le réseau sans fil soit les satellites GPS");
				Log.d("info status","aucun");
				//
			}else{

				this.canGetLocation = true ;
				if(isNetworkEnabled){
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES, 
							MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
					Log.d("info status","network");


					if(locationManager != null){
						location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

						if(location != null){
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
				}

				if(isGPSEnabled){

					locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
							MIN_TIME_BW_UPDATES, 
							MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
					Log.d("info status","GPS");


					if(locationManager != null){
						location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

						if(location != null){
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
					
				}else{
				
					showSettingsAlert("Voulez-vous activer votre GPS ? L'application sera moins précise si vous n'activer pas le gps");
				}
			}

		}catch(Exception e){
			Log.d("erreur",e.toString());
		}

		return location ;
	}

	public void stopUsingGPS(){
		if(locationManager != null){
			locationManager.removeUpdates(GPSTracker.this);
		}
	}


	public double getLatitude(){
		if(location != null){
			latitude = location.getLatitude();
		}

		return latitude;
	}

	public double getLongitude(){
		if(location != null){
			longitude = location.getLongitude();
		}

		return longitude;
	}

	public boolean canGetLocation(){
		return this.canGetLocation;
	}

	public void showSettingsAlert(String message){
		LayoutInflater factory = LayoutInflater.from(mContext);
		final View alertDialogView = factory.inflate(R.layout.boitedialogue,
				null);

		// Création de l'AlertDialog
		AlertDialog.Builder adb = new AlertDialog.Builder(mContext);

		// On affecte la vue personnalisé que l'on a crée à notre AlertDialog
		adb.setView(alertDialogView);

		// On donne un titre à l'AlertDialog
		adb.setTitle("Localisation");

		// On modifie l'icône de l'AlertDialog pour le fun ;)
		adb.setIcon(android.R.drawable.ic_dialog_alert);
		
		TextView t = (TextView) alertDialogView.findViewById(R.id.TextView1);
		t.setText(message);
			
		
		// On affecte un bouton "OK" à notre AlertDialog et on lui affecte un
		// évènement
		adb.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Navigation vers un autre ecran
				Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				mContext.startActivity(intent);
			}
		});
		
		// On crée un bouton "Annuler" à notre AlertDialog et on lui affecte un
				// évènement
				adb.setNegativeButton("Non", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						if(!canGetLocation){
							
							alert.showAlertDialog(mContext,"Status du GPS",
									"Le GPS ou l'itinérance de donnée doit être activé",false);
							return ;
						}

					}
				});

				// On affiche la boite de dialogue
				adb.show();
	}
	
	@Override
	public void onLocationChanged(Location location){
		
	}
	
	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}