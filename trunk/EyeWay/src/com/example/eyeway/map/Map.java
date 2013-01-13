package com.example.eyeway.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.eyeway.R;
import com.example.eyeway.fouilleDedonne.FouilleDonnee;
import com.example.eyeway.fouilleDedonne.Lieu;
import com.example.eyeway.fouilleDedonne.Lieu.Geometry;
import com.example.eyeway.fouilleDedonne.Lieu.MyLocation;
import com.example.eyeway.fouilleDedonne.ListeLieu;
import com.example.eyeway.fouilleDedonne.PlaceDetails;
import com.example.eyeway.fouilleDedonne.Sauvegarde;
import com.example.eyeway.realiteAugmente.Icon;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Map extends MapActivity implements LocationListener{

	private MapView mapView;
	private MapController mc;
	private MyLocationOverlay mlo;
	private LocationManager lm;
	private Location myLocation;
	private Context ctx;
	private Lieu lieu ;
	private boolean creation = true;
	private ArrayList<Icon> icons;
	private int distance ;
	ImageView im;
	ListeLieu lieux; 
	PlaceDetails details;
	private ListItemizedOverlay lis;

	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10 ;

	private static final long MIN_TIME_BW_UPDATES = 30000 ;

	public String KEY_REFERENCE = "reference"; // id of the place
	public String KEY_NAME = "name"; // name of the place
	ArrayList<String> types ;
	private String methode ;
	private String motCle ;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		initialisationEcouteursGPS();

		setContentView(R.layout.activity_map);

		Bundle b = getIntent().getExtras();
		methode = b.getString("methode");

		if(methode.equalsIgnoreCase("proximite")){

			types = (ArrayList<String>) b.get("types");
			distance = b.getInt("distance");

		}else{

			if(methode.equalsIgnoreCase("instantane")){

				int taille = FouilleDonnee.types_place_fr.length;
				types = new ArrayList<String>();

				// Pour tous les types
				for(int i = 0 ; i < taille; i++ ){

					types.add(FouilleDonnee.types_place_fr[i]);
				}


				distance = 500 ; // On défini une distance de 500 mètre

			}else{

				if(methode.equalsIgnoreCase("favoris")){

					lieu = (Lieu) b.get("lieu");



				}else{


					motCle = b.getString("motCle"); 
				}
			}
		}
		//rrrrrrrrrrrrrrrrr
		//récupération de l'id de la carte
		mapView = (MapView) findViewById(R.id.mapView);
		Drawable drawable = this.getResources().getDrawable(R.drawable.ic_action_search);
		lis = new ListItemizedOverlay(drawable, ctx);
		mapView.setBuiltInZoomControls(true);
		this.mc = mapView.getController();
		this.mlo = new MyLocationOverlay(getApplicationContext(), mapView);
		lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
		mapView.getOverlays().add(mlo);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
		mlo.runOnFirstFix(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				mc.animateTo(mlo.getMyLocation());
				mc.setZoom(17);
			}
		});
		mapView.getOverlays().add(mlo);
		mlo.enableMyLocation();
		mapView.invalidate();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_map, menu);
		return true;
	}
	
	/**
	 * Permet d'initialiser notre écran avec les icons
	 */
	public void initialisationFenetre() {
		Log.d("ini","fenetre");
		this.icons = new ArrayList<Icon>();


		if(methode.equalsIgnoreCase("favoris")){

			Icon i = newIcons(lieu);
			ajoutIcon(i);

		}else{

			new RequeteRecherche().execute(methode);

		}
	}

	/**
	 * Permet d'écouter le GPS, le GPS prendra lui même soit le réseau soit le
	 * GPS
	 */
	public void initialisationEcouteursGPS() {

		// On créer notre manager
		lm = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);



		// Avec le GPS
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,
				MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

		// Avec le réseau
		lm.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 10000, 10, this);

		// Si le réseau et le gps ne sont pas allumé
		if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
				& !lm
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
	 * Permet d'ajouter un icon à la vue et à la liste de tous les icons
	 * 
	 * @param icon
	 *            - Icon à rajouter à notre vue
	 */
	public void ajoutIcon(Icon icon) {
		icons.add(icon);
		ajoutoverlay(icon);
	}

	public void ajoutoverlay(Icon icon)
	{
		ImageView img = icon.getIcon();
		Drawable dr = img.getDrawable();
		GeoPoint gp = icon.getGeoPoint();

		OverlayItem overlayitem = new OverlayItem(gp, icon.getName(), icon.getLabel().toString());
		overlayitem.setMarker(dr);
		lis.addOverlayItem(overlayitem);
		List<Overlay> mapOverlays = mapView.getOverlays();
		mapOverlays.add(lis);
	}
	
	protected void onResume() {
		super.onResume();

		initialisationEcouteursGPS();
	}

	@Override
	protected void onPause() {

		super.onPause();
		lm.removeUpdates(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		lm.removeUpdates(this);

	}

	/**
	 * Permet de recalculer la distance entre ma localisation et celle des icons
	 */
	public void recalculerDistance() {

		for (int i = 0; i < icons.size(); i++) {

			icons.get(i).miseAJourLabel(myLocation);
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		// On met notre localisation à jour
		myLocation = location;

		if (creation) {

			Log.d("Localisé", "true");

			creation = false;

		}else{

			if(methode.equalsIgnoreCase("instantane")){

				Log.d("Recherche" ,"new POI");

			}else{

				// Permet de recalculer la distance en ma position et celle
				// de mes icons
				recalculerDistance();

			}
		}
		GeoPoint p = new GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6));
		mc.animateTo(p);
		mc.setCenter(p);
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
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return true;
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

				EditText adresse = (EditText) alertDialogView
						.findViewById(R.id.modifAdresse);

				EditText phone = (EditText) alertDialogView
						.findViewById(R.id.phone);

				EditText webSite = (EditText) alertDialogView
						.findViewById(R.id.webSite);


				Geometry g = new Geometry(new MyLocation(myLocation.getLatitude(),myLocation.getLongitude()));
				ArrayList<String> types = new ArrayList<String>();
				types.add("nouveau");
				Lieu l = new Lieu(nom.getText().toString(),types,"","",description.getText().toString(),g,adresse.getText().toString(),
						phone.getText().toString(),webSite.getText().toString());

				Icon i = newIcons(l);

				Sauvegarde sauvegarder = new Sauvegarde(getApplicationContext());
				sauvegarder.sauvegarderLieu(l);
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


	/**
	 * Gére l'événement taphold
	 */
	public boolean onLongClick(View v) {
		showEnregistrerPoint();
		return false;
	}

	class RequeteRecherche extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			FouilleDonnee fd=new FouilleDonnee();
			Log.d("Je le fais ","Requete");
			if(params[0].equals("proximite") || params[0].equals("instantane")){
				lieux = fd.getLieuProximiteParType(myLocation.getLatitude(), myLocation.getLongitude(),
						types, distance);
				Log.d("Types ",types.get(0));
				Log.d("Distance",distance +"");
			}
			else if (params[0].equals("recherche")){

				Log.d("motcle",motCle);
				lieux = fd.getLieuParRecherche(motCle);
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			runOnUiThread(new Runnable() {
				public void run() {
					String status = "";
					if(lieux != null)
						status=lieux.status;

					if(status.equals("OK")){
						// Successfully got places details
						if (lieux != null && lieux.results != null) {

							if(lieux.results.size() == 0){

								Toast.makeText(ctx, "Aucun résultat pour votre recherche", Toast.LENGTH_SHORT).show();

							}else{

								Icon ic; 
								// loop through each place
								for (Lieu p : lieux.results) {

									// On créer un nouvel icon
									ic = newIcons(p);
									ajoutIcon(ic);

								}

								if(findViewById(R.id.linearProgress) != null){

									findViewById(R.id.linearProgress).setVisibility(View.INVISIBLE);
									setProgressBarVisibility(false);
								}
							}
						}else{
							Toast.makeText(ctx, "Aucun résultat pour votre recherche", Toast.LENGTH_SHORT).show();

						}


					}
				}

			});    
		}

	}
}
