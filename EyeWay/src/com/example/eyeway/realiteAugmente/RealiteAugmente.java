package com.example.eyeway.realiteAugmente;

import java.util.ArrayList;

import com.example.eyeway.R;
import com.google.android.maps.GeoPoint;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

public class RealiteAugmente extends Activity implements LocationListener{
	private SensorManager sensorMngr;
	private SensorEventListener sensorLstr;
	private int mScreenWidth;
	private int mScreenHeight;
	private Location myLocation;
	private Context ctx;
	private LocationManager locationManager;
	private ArrayList<Icon> icons;
	private boolean creation = true ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_realite_augmente);

		initialisationEcouteursGPS();
		initialisionEcouteurAccelerometre();

	}

	/**
	 * Permet d'écouter l'accelerometre
	 */
	public void initialisionEcouteurAccelerometre() {

		sensorLstr = createListener();

		sensorMngr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		sensorMngr.registerListener(sensorLstr,
				sensorMngr.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_UI);
	}

	/**
	 * Permet d'écouter le GPS, le GPS prendra lui même soit le réseau soir le
	 * GPS
	 */
	public void initialisationEcouteursGPS() {

		// On créer notre manager
		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		// On update le manager tout les 100 mili secondes
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1,
				1, this);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 1, 1, this);

		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
				& !locationManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

			boiteDedialog();

		}
	}

	/**
	 * Permet de créer une nouvelle icone par rapport à son nom et ces points de
	 * coordonnées GPS. Le nom servira à savoir qu'elle image aura l'icone
	 * 
	 * @param name
	 *            - Nom de l'icone, détermine aussi son image
	 * @param longitude
	 *            - Longitude du point GPS
	 * @param latitude
	 *            - Lattitude du point GPS
	 * @return - Retourne une nouvelle icone dans un linearLayout
	 */
	public Icon newIcons(String name, double longitude, double latitude) {

		return new Icon(ctx, name, latitude, longitude, myLocation);

	}

	public void initialisationFenetre() {

		this.icons = new ArrayList<Icon>();

		// On récupère le context
		ctx = this;

		Icon icon = newIcons("Restaurant", 1.89756, 47.86541);

		FrameLayout layoutMain = (FrameLayout) findViewById(R.id.main);
		layoutMain.addView(icon);
		this.icons.add(icon);

		icon = newIcons("Stade Omnisport", 1.9429176719970656,
				47.842080636471515);

		layoutMain.addView(icon);
		this.icons.add(icon);
		// On récupère la ta taille de l'écran
		mScreenWidth = getScreenWidth();
		mScreenHeight = getScreenHeight();

	}

	public void boiteDedialog() {

		// On instancie notre layout en tant que View
		LayoutInflater factory = LayoutInflater.from(this);
		final View alertDialogView = factory.inflate(R.layout.boitedialogue,
				null);

		// Création de l'AlertDialog
		AlertDialog.Builder adb = new AlertDialog.Builder(this);

		// On affecte la vue personnalisé que l'on a crée à notre AlertDialog
		adb.setView(alertDialogView);

		// On donne un titre à l'AlertDialog
		adb.setTitle("GPS pas activé");

		// On modifie l'icône de l'AlertDialog pour le fun ;)
		adb.setIcon(android.R.drawable.ic_dialog_alert);

		// On affecte un bouton "OK" à notre AlertDialog et on lui affecte un
		// évènement
		adb.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Navigation vers un autre ecran
				startActivityForResult(
						new Intent(
								android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
						0);

			}
		});

		// On crée un bouton "Annuler" à notre AlertDialog et on lui affecte un
		// évènement
		adb.setNegativeButton("Non", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				// On retourne à l'application

			}
		});

		// On affiche la boite de dialogue
		adb.show();

	}

	/*
	 * // Methode qui permet de recuperer notre localisation actuelle en locale
	 * // Récupère la derniere position connu public final Location
	 * getMyLocation(Context _context) {
	 * 
	 * Location location = null;
	 * 
	 * try {
	 * 
	 * List<String> providers = locationManager.getProviders(true);
	 * 
	 * for (int i = providers.size() - 1; i >= 0; i--) {
	 * 
	 * // Récupère la derniere localisation connu location =
	 * locationManager.getLastKnownLocation(providers.get(i)); }
	 * 
	 * } catch (Exception e) {
	 * 
	 * }
	 * 
	 * return location; }
	 */
	// Methode qui permet de récupèrer la hauteur de l'écran
	public int getScreenHeight() {

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.heightPixels;
	}

	// Methode qui permet de récupèrer la largeur de l'écran
	public int getScreenWidth() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;
	}

	@Override
	protected void onStop() {
		super.onStop();
		sensorMngr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensorMngr.unregisterListener(sensorLstr,
				sensorMngr.getDefaultSensor(Sensor.TYPE_ORIENTATION));
	}

	public final static float getSpotAngle(Context _context, GeoPoint p,
			Location _me) {

		Location location = new Location("LOCATION_SERVICE");

		try {
			
			location.setLatitude(p.getLatitudeE6());
			location.setLongitude(p.getLatitudeE6());
			
		} catch (Exception e) {
		}
		
		return _me.bearingTo(location);
	}

	public final static double angleDirection(double spotAngle, double direction) {
		double angle;
		
		if (spotAngle > 0) {

			if (direction < spotAngle - 180)

				angle = 360 - spotAngle + direction;

			else

				angle = direction - spotAngle;

		} else {

			if (direction > spotAngle + 180)

				angle = direction - spotAngle - 360;

			else

				angle = direction - spotAngle;

		}

		return (angle);
	}

	public final static void moveSpot(Context c, View tv, GeoPoint p,
			float azimut, Location me, int screenWidth, float roll,
			int screenHeight, float pitch) {
		
		int angle = (int) (angleDirection(getSpotAngle(c, p, me), azimut));

		LayoutParams lp = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		int marginTop;
		
		if (pitch < screenHeight / 2)
			
			marginTop = (int) ((roll - 90) / 90 * screenHeight);
		
		else
			
			marginTop = -(int) ((roll - 90) / 90 * screenHeight);

		lp.setMargins(angle * screenWidth / 90, marginTop, 0, 0);
		lp.gravity = Gravity.CENTER;
		lp.width = FrameLayout.LayoutParams.WRAP_CONTENT;
		lp.height = FrameLayout.LayoutParams.WRAP_CONTENT;
		tv.setLayoutParams(lp);

	}

	// Notre ecouteur
	public SensorEventListener createListener() {
		return new SensorEventListener() {
			public void onAccuracyChanged(Sensor _sensor, int _accuracy) {

			}

			public void onSensorChanged(SensorEvent evt) {

				if (evt.sensor.getType() == Sensor.TYPE_ORIENTATION) {

					if (myLocation != null) {

						final float vals[] = evt.values;
						final float azimut = vals[0] - 270;
						final float roll = vals[2];
						final float pitch = Math.abs(vals[1]);
						Icon ic;

						for (int i = 0; i < icons.size(); i++) {

							ic = icons.get(i);
							// On place les icons sur l'écran
							moveSpot(ctx, ic, ic.getGeoPoint(), azimut,
									myLocation, mScreenWidth, roll,
									mScreenHeight, pitch);

						}
					}
				}
			}
		};
	}

	/**
	 * Permet de recalculer la distance entre ma localisation
	 * et celle des icons
	 */
	public void recalculerDistance() {

		for (int i = 0; i < icons.size(); i++) {

			icons.get(i).miseAJourLabel(myLocation);
		}
	}

	/**
	 * Met à jour la localisation
	 */
	@Override
	public void onLocationChanged(Location location) {

		// On met notre localisation à jour
		myLocation = location;
		
		if(creation){
			
			creation = false ;
			// Si on a pas encore créé notre fenêtre 
			initialisationFenetre();
		
			
		}
		
		// Permet de recalculer la distance en ma position et celle
		// de mes icons
		recalculerDistance();
		// GeoPoint g = new GeoPoint( (int) (location.getLatitude() * 100000),
		// (int) location.getLongitude() * 1000000);
	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
}