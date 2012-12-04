package com.example.eyeway.realiteAugmente;

import java.io.IOException;
import java.util.ArrayList;

import com.example.eyeway.R;
import com.google.android.maps.GeoPoint;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter.LengthFilter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Toast;

public class RealiteAugmente extends Activity implements LocationListener , OnLongClickListener{



	private SensorManager sensorMngr;
	private SensorEventListener sensorLstr;
	private int mScreenWidth;
	private int mScreenHeight;
	private Location myLocation;
	private Context ctx;
	private LocationManager locationManager;
	private ArrayList<Icon> icons;
	private boolean creation = true ;
	private  Bitmap yourSelectedImage ;
	int rotation = 0 ;
	 ImageView im ;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);


		setContentView(R.layout.activity_realite_augmente);

		FrameLayout l = (FrameLayout) findViewById(R.id.main);
		l.setOnLongClickListener(this);
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
	public Icon newIcons(String name, String description, double longitude, double latitude) {

		return new Icon(ctx, name,description, latitude, longitude, myLocation);

	}

	public void initialisationFenetre() {

		this.icons = new ArrayList<Icon>();
		
		// On récupère le context
		ctx = this;

		Icon icon = newIcons("Restaurant","Mcdonalds", 1.89756, 47.86541);
		// Load a bitmap from a drawable, make sure this drawable exists in your project
	
		
		FrameLayout layoutMain = (FrameLayout) findViewById(R.id.main);
		
		layoutMain.addView(icon);
		this.icons.add(icon);

		icon = newIcons("Stade Omnisport","sport", 1.9429176719970656,
				47.842080636471515);

		layoutMain.addView(icon);
		this.icons.add(icon);
		// On récupère la ta taille de l'écran
		mScreenWidth = getScreenWidth();
		mScreenHeight = getScreenHeight();

	}
	
	public void ajoutIcon(Icon icon){
		
		FrameLayout layoutMain = (FrameLayout) findViewById(R.id.main);
		layoutMain.addView(icon);
		icons.add(icon );

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
	protected void onResume(){
		super.onResume() ;
		sensorMngr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensorMngr.registerListener(sensorLstr,
				sensorMngr.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_UI);
	}

	@Override 
	protected void onPause(){

		super.onPause();
		sensorMngr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensorMngr.unregisterListener(sensorLstr,
				sensorMngr.getDefaultSensor(Sensor.TYPE_ORIENTATION));
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
						
						/*
						Bitmap sprite = BitmapFactory.decodeResource(ctx.getResources(),
						        R.drawable.fleche);

						// Create two matrices that will be used to rotate the bitmap
						Matrix rotateRight = new Matrix();	
						// Set the matrices with the desired rotation 90 or -90 degrees
						rotation += 5 ;
						rotateRight.preRotate(rotation);
						
						Bitmap rSprite = Bitmap.createBitmap(sprite, 0, 0,
						        sprite.getWidth(), sprite.getHeight(), rotateRight, true);
						
						ImageView v = (ImageView) findViewById(R.id.fleche);
						v.setImageDrawable(new BitmapDrawable(ctx.getResources() , rSprite));
						*/
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
			Log.d("Localisé","true");
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


		private void ajouterNouveauPoint(){
			//On instancie notre layout en tant que View
	        LayoutInflater factory = LayoutInflater.from(this);
	        final View alertDialogView = factory.inflate(R.layout.nouveau_point, null);
	 
	         
	        //Création de l'AlertDialog
	        AlertDialog.Builder adb = new AlertDialog.Builder(this);
	 
	        //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
	        adb.setView(alertDialogView);
	        
			im = (ImageView) alertDialogView.findViewById(R.id.image);
			
			im.setClickable(true);
			im.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					
				    	   
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);//
			        final int ACTIVITY_SELECT_IMAGE = 1234;
					startActivityForResult(Intent.createChooser(intent, "Select Picture"),ACTIVITY_SELECT_IMAGE);

					
				}
			});
	        //On donne un titre à l'AlertDialog
	        adb.setTitle("Ajout Du Point D'Intêret");
	 
	        //On modifie l'icône de l'AlertDialog pour le fun ;)
	        adb.setIcon(R.drawable.ajouter);
	        
	        EditText edit = (EditText) alertDialogView.findViewById(R.id.modifAdresse);
	        Geocoder geoCoder = new Geocoder(this);
	        try {
	        	
	        	
	        	Address adresse = geoCoder.getFromLocation(myLocation.getLatitude(), myLocation.getLongitude(), 1).get(0);
	        	
	        	
	        	int taille = adresse.getMaxAddressLineIndex();
	        	String ad ="";
	        	String tmp ;
	        	for(int i=0 ; i < taille ; i++){
	        		
	        		tmp = adresse.getAddressLine(i) ;
	        		if(tmp != null){
	        			
	        			ad+=tmp +" ";
	        		}
	        	}
	        	edit.setText(ad);
	        } catch (IOException e) {
				Log.d("erreur","geoCoder");
	        	// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        /*
	         *    TextView text = (TextView) alertDialogView.findViewById(R.id.distance);
	        text.setText("0 m");
	        text.setText(name);
	        
	        
	        text = (TextView) alertDialogView.findViewById(R.id.description);
	        text.setText("Petit resto sympa en bord de mer \n A NE PAS LOUPER Petit resto sympa en bord de mer \n A NE PAS LOUPERPetit resto sympa en bord de mer \n A NE PAS LOUPERPetit resto sympa en bord de mer \n A NE PAS LOUPERPetit resto sympa en bord de mer \n A NE PAS LOUPERPetit resto sympa en bord de mer \n A NE PAS LOUPERPetit resto sympa en bord de mer \n A NE PAS LOUPERPetit resto sympa en bord de mer \n A NE PAS LOUPERPetit resto sympa en bord de mer \n A NE PAS LOUPERPetit resto sympa en bord de mer \n A NE PAS LOUPERPetit resto sympa en bord de mer \n A NE PAS LOUPERPetit resto sympa en bord de mer \n A NE PAS LOUPER");

	        
	        text = (TextView) alertDialogView.findViewById(R.id.distance);
	        text.setText(this.label.getText());
	       */
	      //  adb.setCancelable(true);
	        	
	        adb.setPositiveButton("Enregister", new DialogInterface.OnClickListener() {
	 			public void onClick(DialogInterface dialog, int which) {
	 					
	 				
	 				TextView nom = (TextView) alertDialogView.findViewById(R.id.titre);
	 				TextView description =(TextView)alertDialogView.findViewById(R.id.description);
	 				Icon i = new Icon(ctx,nom.getText().toString(),description.getText().toString(), myLocation.getLatitude(), myLocation.getLongitude(), myLocation);
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
		
		@SuppressWarnings("deprecation")
		protected void onActivityResult(int requestCode, int resultCode, Intent data) 
		{
		    super.onActivityResult(requestCode, resultCode, data); 

		    switch(requestCode) { 
		    case 1234:
		        if(resultCode == RESULT_OK){  
		            Uri selectedImage = data.getData();
		            String[] filePathColumn = {MediaStore.Images.Media.DATA};

		            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
		            cursor.moveToFirst();

		            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		            String filePath = cursor.getString(columnIndex);
		            cursor.close();

		            /*
		             * 
		            Matrix rotateRight = new Matrix();	
					// Set the matrices with the desired rotation 90 or -90 degrees
					rotation = 90 ;
					rotateRight.preRotate(rotation);
					
					yourSelectedImage = Bitmap.createBitmap(BitmapFactory.decodeFile(filePath), 0, 0,
							BitmapFactory.decodeFile(filePath).getWidth(), BitmapFactory.decodeFile(filePath).getHeight(), rotateRight, true);
		             */
		            yourSelectedImage = BitmapFactory.decodeFile(filePath);
		            /* Now you have choosen image in Bitmap format in object "yourSelectedImage". You can use it in way you want! */
		           // im.setBackgroundResource(0);
		          //  im.setBackground((new BitmapDrawable(ctx.getResources() , yourSelectedImage)));
		           // im.setImageDrawable(new BitmapDrawable(ctx.getResources() , yourSelectedImage));
		            //im.refreshDrawableState();
		           // (new BitmapDrawable(ctx.getResources() , yourSelectedImage))
		            //BitmapDrawable b =(new BitmapDrawable(ctx.getResources() , yourSelectedImage)); 
		            //Drawable d = (Drawable)b ;
		             //im.setBackground(d);
		            im.setBackgroundDrawable(new BitmapDrawable(ctx.getResources() , yourSelectedImage));
		        
		        }
		    }

		};
		private void showEnregistrerPoint(){
		
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
	public boolean onLongClick(View v) {
		showEnregistrerPoint();
		return false;
	}



}