package com.example.eyeway.realiteAugmente;

import java.util.StringTokenizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eyeway.R;
import com.example.eyeway.Map.Map;
import com.example.eyeway.fouilleDedonne.DetailLieu;
import com.example.eyeway.fouilleDedonne.FouilleDonnee;
import com.example.eyeway.fouilleDedonne.Lieu;
import com.google.android.maps.GeoPoint;

public class Icon extends LinearLayout {

	private TextView label;
	private ImageView icon;
	private ImageView photoDescription;
	private GeoPoint geoPoint;
	private Context ctx;
	private String name;
	private String description;
	private String adresse ;
	private Lieu lieu ;
	
	public Icon(Context context, Activity a) {
		super(context);
	}

	/**
	 * Permet de créer un icon en fonction de tout les paramètres
	 * 
	 * @param context
	 *            - Context de l'application
	 * @param Imview
	 *            -
	 * @param name
	 * @param description
	 * @param latitude
	 * @param longitude
	 * @param myLocation
	 */
	public Icon(Context context, ImageView Imview, String name,
			String description,String type,String adresse, double latitude, double longitude,
			Location myLocation) {
		
		super(context);
		this.adresse = adresse;
		this.description = description;
		this.name = name;
		this.lieu = null ;
		creerIcon(context, Imview, latitude, longitude, myLocation, type);
	}

	public Icon(Context context, ImageView im , Lieu l , Location myLocation){
		super(context);
	
		lieu = l ;
		StringBuffer type = new StringBuffer("");
		// On récupère son type
		for (int j = 0; j < l.getTypes().size(); j++) {

			type.append(l.getTypes().get(j) + " ");

		}
		
		creerIcon(context, im, l.getLatitude(), l.getLongitude(), myLocation, type.toString());
		
	}
	
	private void creerIcon(Context context, ImageView Imview, double latitude, double longitude,
			Location myLocation,String type){
		
		ctx = context;
		label = new TextView(context);

		// On ajoute notre nouveau point GPS
		geoPoint = new GeoPoint((int) (latitude * 100000),
				(int) (longitude * 100000));
		Location location = new Location("myprovider");
		location.setLatitude(latitude);
		location.setLongitude(longitude);

		// On calcule la distance entre nous et le POI
		double distance = calculDistance(myLocation, location);

		// On modifie le label
		this.modifierLabel(distance);
		icon = new ImageView(context);

		StringTokenizer tok = new StringTokenizer(type, " ");
		String typeTemp = tok.nextToken();
		
		photoDescription = new ImageView(context);

		if (Imview == null) {

			// On choisi l'icon en fonction du type
			choixIcon(typeTemp);
			photoDescription = icon;

		} else {
			
			icon.setBackgroundResource(R.drawable.favorite);
			photoDescription = icon;

		}

		icon.setClickable(true);

		icon.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				showBoiteInformation();
			}
		});

		// On ajoute l'icon et son label à la vue
		this.addView(icon);
		this.addView(label);
	}
	

	/**
	 * Permet de choisir l'image de l'icon en fonction de son type
	 * 
	 * @param type
	 *            - Type du POI
	 */
	private void choixIcon(String type) {

		if (type.equalsIgnoreCase("restaurant")) {

			icon.setBackgroundResource(R.drawable.restaurant);

		} else {

			if (type.equalsIgnoreCase("Bar")) {

				icon.setBackgroundResource(R.drawable.cocktail);

			} else {
				icon.setBackgroundResource(R.drawable.img_epingle);
			}
		}
	}

	/**
	 * Permet de modifier le label de l'icon Le label correspond à la distance
	 * entre nous et le POI
	 * 
	 * @param distance
	 *            - Distance entre nous et le POI
	 */
	private void modifierLabel(double distance) {

		if (distance > 1000) {

			label.setText((int) distance / 1000 + " km ");

		} else {

			label.setText((int) distance / 1000 + " m ");

		}

	}

	/**
	 * Permet de mettre à jour le label à l'aide de notre nouvelle localisation
	 * 
	 * @param myLocation
	 *            - La localisation actuelle de l'utilisateur
	 */
	public void miseAJourLabel(Location myLocation) {

		Location l = new Location("myProvider");
		l.setLatitude(this.getLatitude() / 100000.0);
		l.setLongitude(this.getLongitude() / 100000.0);

		double distance = calculDistance(myLocation, l);
		modifierLabel(distance);
	}

	/**
	 * Calcul la distance entre deux localisations
	 * 
	 * @param a
	 *            - Premiere localisation
	 * @param b
	 *            - Seconde localisation
	 * @return - Distance entre les deux localisations
	 */
	public static double calculDistance(Location a, Location b) {

		return a.distanceTo(b);

	}

	/**
	 * Boite de dialogue qui s'ouvre lors du clic sur le calcul d'itinéraire
	 */
	void showBoiteChoix() {

		// Création de l'AlertDialog
		AlertDialog.Builder adb = new AlertDialog.Builder(ctx);

		// On donne un titre à l'AlertDialog
		adb.setTitle("Choix Itinéraire");

		// On modifie l'icône de l'AlertDialog pour le fun ;)
		adb.setIcon(R.drawable.info);

		// adb.setCancelable(true);
		String[] choix = { "Map" };

		adb.setItems(choix, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent monIntent = new Intent(ctx, Map.class);
				ctx.startActivity(monIntent);
				dialog.dismiss();
			}
		});

		adb.show();
	}

	/**
	 * Permet d'afficher les informations d'une icon
	 */
	void showBoiteInformation() {

		// On instancie notre layout en tant que View
		LayoutInflater factory = LayoutInflater.from(ctx);
		final View alertDialogView = factory.inflate(R.layout.popup_layout,
				null);

		// Création de l'AlertDialog
		AlertDialog.Builder adb = new AlertDialog.Builder(ctx);

		// On affecte la vue personnalisé que l'on a crée à notre AlertDialog
		adb.setView(alertDialogView);

		// On donne un titre à l'AlertDialog
		adb.setTitle("Informations");

		// On modifie l'icône de l'AlertDialog pour le fun ;)
		adb.setIcon(R.drawable.info);

		ImageView image = (ImageView) alertDialogView.findViewById(R.id.image);
		
		
		image.setImageDrawable(photoDescription.getBackground());
		TextView text = (TextView) alertDialogView.findViewById(R.id.titre);
		text.setText(name);

		text = (TextView) alertDialogView.findViewById(R.id.description);
		text.setText(description);

		text = (TextView) alertDialogView.findViewById(R.id.distance);
		text.setText(this.label.getText());
		text = (TextView) alertDialogView.findViewById(R.id.adresse);

		if(lieu != null){
			FouilleDonnee fd = new FouilleDonnee();
			DetailLieu detail = fd.getDetails(lieu.getReference());
			text.setText(detail.toString());
		}else{
		
			text.setText(this.adresse);
		}
		
		adb.setPositiveButton("Itinéraire",
				new DialogInterface.OnClickListener() {
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

	// ------------- GETTERS AND SETTERS --------------------

	public double getLatitude() {

		return this.geoPoint.getLatitudeE6();
	}

	public double getLongitude() {

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
