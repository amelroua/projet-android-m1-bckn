package com.example.eyeway.realiteAugmente;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eyeway.R;
import com.example.eyeway.fouilleDedonne.FouilleDonnee;
import com.example.eyeway.fouilleDedonne.Lieu;
import com.google.android.maps.GeoPoint;

public class RealiteAugmente extends Activity implements LocationListener,
		OnLongClickListener {

	private SensorManager sensorMngr;
	private SensorEventListener sensorLstr;
	private int mScreenWidth;
	private int mScreenHeight;
	private Location myLocation;
	private Context ctx;
	private LocationManager locationManager;
	private ArrayList<Icon> icons;
	private boolean creation = true;
	int rotation = 0;
	private int distance ;
	ImageView im;
	ArrayList<Lieu> lieux; 
	ArrayList<String> types ;
	private String methode ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// On lie notre classe au layout
		setContentView(R.layout.activity_realite_augmente);
		Bundle b = getIntent().getExtras();
		methode = b.getString("methode");
		
		types = (ArrayList<String>) b.get("types");
		distance = b.getInt("distance");
		
		
		FrameLayout l = (FrameLayout) findViewById(R.id.main);

		// On rend notre écran cliquable avec taphold
		l.setOnLongClickListener(this);
		
		
		// On initialise nos écouteurs
		initialisationEcouteursGPS();
		initialisionEcouteurAccelerometre();

	}

	/**
	 * On initialise l'écouteur de l'accelerometre
	 * 
	 */
	public void initialisionEcouteurAccelerometre() {

		sensorLstr = createListener();

		sensorMngr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		// On initialise avec un temps suffisant pour le jeu
		sensorMngr.registerListener(sensorLstr,
				sensorMngr.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_UI);
	}

	/**
	 * Permet d'écouter le GPS, le GPS prendra lui même soit le réseau soit le
	 * GPS
	 */
	public void initialisationEcouteursGPS() {

		// On créer notre manager
		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		// On update le manager tout les 100 mili secondes

		// Avec le GPS
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1,
				1, this);

		// Avec le réseau
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 1, 1, this);

		// Si le réseau et le gps ne sont pas allumé
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
	public Icon newIcons(Lieu l) {

		return new Icon(ctx,null,l,myLocation);

	}

	/**
	 * Permet d'initialiser notre écran avec les icons
	 */
	public void initialisationFenetre() {

		this.icons = new ArrayList<Icon>();
		
		FouilleDonnee fd = new FouilleDonnee();
		
		if(methode.equals("proximite")){
		
			lieux = fd.getLieuProximiteParType(
				myLocation.getLatitude(), myLocation.getLongitude(),
				types, distance);
		}
		
		Lieu l;
		Icon ic;

		// On récupère le context
				ctx = this;
				
		if(lieux.size() == 0){
			
			Toast.makeText(ctx, "Aucun résultat pour votre recherche", Toast.LENGTH_SHORT).show();
		}
		

		// Pour tous les lieux
		for (int i = 0; i < lieux.size(); i++) {

			
			// On récupère le lieu
			l = lieux.get(i);
			
			// On créer un nouvel icon
			ic = newIcons(l);

			ajoutIcon(ic);
		}

		// On récupère la ta taille de l'écran
		mScreenWidth = getScreenWidth();
		mScreenHeight = getScreenHeight();

	}

	/**
	 * Permet d'ajouter un icon à la vue et à la liste de tous les icons
	 * 
	 * @param icon
	 *            - Icon à rajouter à notre vue
	 */
	public void ajoutIcon(Icon icon) {

		FrameLayout layoutMain = (FrameLayout) findViewById(R.id.main);
		layoutMain.addView(icon);
		icons.add(icon);

	}

	/**
	 * Permet d'afficher la boite de dialogue permettant de demander si
	 * l'utilisateur veut allumer le gps
	 */
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

	/**
	 * Méthode qui permet de connaitre la hauteur de l'écran
	 * 
	 * @return - Retourne la hauteur de l'écran
	 */
	public int getScreenHeight() {

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.heightPixels;
	}

	/**
	 * Méthode qui permet de connaitre la largeur de l'écran
	 * 
	 * @return - Retourne la largeur de l'écran
	 */
	public int getScreenWidth() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;
	}

	@Override
	/**
	 * Méthode appelée lorsque l'on arrive sur l'application
	 */
	protected void onResume() {
		super.onResume();
		// On enregistre nos écouteurs
		sensorMngr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		sensorMngr.registerListener(sensorLstr,
				sensorMngr.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	protected void onPause() {

		super.onPause();

		// On se désenregistre quand l'application est en pause
		sensorMngr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensorMngr.unregisterListener(sensorLstr,
				sensorMngr.getDefaultSensor(Sensor.TYPE_ORIENTATION));
	}

	@Override
	protected void onStop() {
		super.onStop();

		// On se désenregistre quand on quitte l'application
		sensorMngr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensorMngr.unregisterListener(sensorLstr,
				sensorMngr.getDefaultSensor(Sensor.TYPE_ORIENTATION));
	}

	/**
	 * Permet de récupèrer l'angle entre ma position et celle du point d'intêret
	 * 
	 * @param _context
	 *            - Context de l'application
	 * @param p
	 *            - Point d'intêret
	 * @param _me
	 *            - Ma localisation
	 * @return - Retourne l'angle
	 */
	public final static float getSpotAngle(Context _context, GeoPoint p,
			Location _me) {

		Location location = new Location("LOCATION_SERVICE");

		try {

			location.setLatitude(p.getLatitudeE6() / 100000.0);
			location.setLongitude(p.getLatitudeE6() / 100000.0);

		} catch (Exception e) {
		}

		// Permet de connaitre l'angle entre deux localisations
		return _me.bearingTo(location);
	}

	/**
	 * Permet de calculer l'angle entre moi et un point d'intêret en fonction de
	 * l'orientation de l'écran
	 * 
	 * @param spotAngle
	 *            - Angle entre moi et le point d'interêt
	 * @param direction
	 *            - Orientation de l'écran
	 * @return - Retourne angle
	 */
	public final static double angleDirection(double spotAngle, double direction) {
		double angle;

		// Si l'angle est supèrieur à 0
		if (spotAngle > 0) {

			// Si la direction est inférieur à l'angle - 180
			if (direction < spotAngle - 180)

				angle = 360 - spotAngle + direction;

			else
				// Direction supèrieure
				angle = direction - spotAngle;

		} else {

			if (direction > spotAngle + 180)

				angle = direction - spotAngle - 360;

			else

				angle = direction - spotAngle;

		}

		return (angle);
	}

	/**
	 * Méthode qui permet de faire bouger les icons sur l'écran
	 * 
	 * @param context
	 * @param vue
	 * @param geoPoint
	 * @param azimut
	 * @param myLocation
	 * @param screenWidth
	 * @param roll
	 * @param screenHeight
	 * @param pitch
	 */
	public final static void moveSpot(Context context, View vue,
			GeoPoint geoPoint, float azimut, Location myLocation,
			int screenWidth, float roll, int screenHeight, float pitch) {

		// Angle entre moi et le point d'intêret
		int angle = (int) (angleDirection(
				getSpotAngle(context, geoPoint, myLocation), azimut));

		// On récupère le layout de l'écran
		LayoutParams lp = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);

		// Marge du dessus
		int marginTop;

		// Si le pitch est inférieur à la moitié de la hauteur de l'écran
		if (pitch < screenHeight / 2)

			marginTop = (int) ((roll - 90) / 90 * screenHeight);

		else

			marginTop = -(int) ((roll - 90) / 90 * screenHeight);

		// On change les marges
		lp.setMargins(angle * screenWidth / 90, marginTop, 0, 0);

		// On change la gravité pour le mettre au centre
		lp.gravity = Gravity.CENTER;

		// On met la largeur nécessaire
		lp.width = FrameLayout.LayoutParams.WRAP_CONTENT;

		// On met la hauteur nécessaire
		lp.height = FrameLayout.LayoutParams.WRAP_CONTENT;

		// On change le layout de la vue
		vue.setLayoutParams(lp);

	}

	/**
	 * Création de notre propre écouteur
	 * 
	 * @return - On retourne notre écouteur
	 */
	public SensorEventListener createListener() {
		return new SensorEventListener() {
			public void onAccuracyChanged(Sensor _sensor, int _accuracy) {

			}

			/**
			 * Permet de gérer les mouvements du sensor de mouvement
			 */
			public void onSensorChanged(SensorEvent evt) {

				if (evt.sensor.getType() == Sensor.TYPE_ORIENTATION) {

					if (myLocation != null) {

						final float vals[] = evt.values;
						final float azimut = vals[0] - 180;
						final float roll = vals[2];
						final float pitch = Math.abs(vals[1]);
						Icon ic;

						// Pour tous les icons
						for (int i = 0; i < icons.size(); i++) {

							ic = icons.get(i);

							// On place l'icon sur l'écran
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
	 * Permet de recalculer la distance entre ma localisation et celle des icons
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

		if (creation) {

			Log.d("Localisé", "true");

			creation = false;

			// Si on a pas encore créé notre fenêtre
			initialisationFenetre();

		}

		// Permet de recalculer la distance en ma position et celle
		// de mes icons
		recalculerDistance();

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

	/**
	 * Permet d'ajouter un nouveau point d'intêret
	 */
	private void ajouterNouveauPoint() {

		// On instancie notre layout en tant que View
		LayoutInflater factory = LayoutInflater.from(this);
		final View alertDialogView = factory.inflate(R.layout.nouveau_point,
				null);

		// Création de l'AlertDialog
		AlertDialog.Builder adb = new AlertDialog.Builder(this);

		// On affecte la vue personnalisé que l'on a crée à notre AlertDialog
		adb.setView(alertDialogView);

		im = (ImageView) alertDialogView.findViewById(R.id.image);
		im.setBackgroundResource(R.drawable.favorite);

		// On donne un titre à l'AlertDialog
		adb.setTitle("Ajout Du Point D'Intêret");

		// On modifie l'icône de l'AlertDialog pour le fun ;)
		adb.setIcon(R.drawable.ajouter);

		EditText edit = (EditText) alertDialogView
				.findViewById(R.id.modifAdresse);

		Geocoder geoCoder = new Geocoder(this);

		try {

			// On essaye de récupèrer l'adresse de notre position actuelle
			Address adresse = geoCoder.getFromLocation(
					myLocation.getLatitude(), myLocation.getLongitude(), 1)
					.get(0);

			int taille = adresse.getMaxAddressLineIndex();
			String ad = "";
			String tmp;

			for (int i = 0; i < taille; i++) {

				tmp = adresse.getAddressLine(i);

				if (tmp != null) {

					ad += tmp + " ";
				}
			}

			edit.setText(ad);

		} catch (IOException e) {
			Log.d("erreur", "geoCoder");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		adb.setPositiveButton("Enregister",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						TextView nom = (TextView) alertDialogView
								.findViewById(R.id.titre);
						TextView description = (TextView) alertDialogView
								.findViewById(R.id.description);
						
						EditText edit = (EditText) alertDialogView
								.findViewById(R.id.modifAdresse);
						
						Icon i = new Icon(ctx, im, nom.getText().toString(),
								description.getText().toString(),"nouveau",edit.getText().toString(), myLocation
										.getLatitude(), myLocation
										.getLongitude(), myLocation);
						ajoutIcon(i);

					}
				});

		// On crée un bouton "Annuler" à notre AlertDialog et on lui affecte un
		// évènement
		adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		adb.show();
	}

	/*
	 * protected void onActivityResult(int requestCode, int resultCode, Intent
	 * data) {
	 * 
	 * super.onActivityResult(requestCode, resultCode, data);
	 * 
	 * switch (requestCode) { case 1234: if (resultCode == RESULT_OK) {
	 * 
	 * Uri selectedImage = data.getData(); InputStream imageStream; try {
	 * imageStream = getContentResolver().openInputStream( selectedImage);
	 * Bitmap yourSelectedImage = BitmapFactory .decodeStream(imageStream);
	 * 
	 * imageStream.close();
	 * 
	 * im.setBackgroundDrawable(new BitmapDrawable(ctx .getResources(),
	 * yourSelectedImage));
	 * 
	 * } catch (FileNotFoundException e) { // TODO Auto-generated catch block
	 * e.printStackTrace();
	 * 
	 * } catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace();
	 * 
	 * } catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * } }
	 * 
	 * };
	 */

	/**
	 * Boite de dialogue permettant de demander à l'utilisateur s'il veut
	 * ajouter un nouveau point d'intêret
	 */
	private void showEnregistrerPoint() {

		// Création de l'AlertDialog
		AlertDialog.Builder adb = new AlertDialog.Builder(this);

		// On donne un titre à l'AlertDialog
		adb.setTitle("Nouveau point d'intêret");

		adb.setMessage("Voulez vous enregistrer la position actuelle comme nouveau point d'intêret");
		// On modifie l'icône de l'AlertDialog pour le fun ;)
		adb.setIcon(R.drawable.ajouter);
		// On affecte un bouton "OK" à notre AlertDialog et on lui affecte un
		// évènement
		adb.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				ajouterNouveauPoint();
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

	@Override
	/**
	 * Gére l'événement taphold
	 */
	public boolean onLongClick(View v) {
		showEnregistrerPoint();
		return false;
	}

}