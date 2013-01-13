package com.example.eyeway.realiteAugmente;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.StringTokenizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eyeway.FouilleTest;
import com.example.eyeway.R;
import com.example.eyeway.Map.Map;
import com.example.eyeway.fouilleDedonne.DetailLieu;
import com.example.eyeway.fouilleDedonne.FouilleDonnee;
import com.example.eyeway.fouilleDedonne.Lieu;
import com.example.eyeway.fouilleDedonne.PlaceDetails;
import com.example.eyeway.fouilleDedonne.Sauvegarde;
import com.google.android.maps.GeoPoint;

public class Icon extends LinearLayout {

	public String KEY_REFERENCE = "reference"; // id of the place
	public String KEY_NAME = "name"; // name of the place
	private TextView label;
	private ImageView icon;
	private ImageView photoDescription;
	private GeoPoint geoPoint;
	private Context ctx;
	private String name;
	private String description;
	private String adresse ;
	private Lieu lieu ;
	private PlaceDetails details;
	private String webSite ;
	private String phone ; 
	private String typeIcon ;

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
			Location myLocation, String phoneNumer , String siteWeb) {

		super(context);
		this.adresse = adresse;
		this.description = description;
		this.name = name;
		this.lieu = null ;
		this.phone = null;
		this.webSite = null;
		creerIcon(context, Imview, latitude, longitude, myLocation, type);
	}

	public Icon(Context context, ImageView im , Lieu l , Location myLocation){
		super(context);

		lieu = l ;
		this.adresse = "";
		this.name="";
		this.webSite="";
		this.phone="";
		StringBuffer type = new StringBuffer("");
		// On récupère son type
		for (int j = 0; j < l.getTypes().size(); j++) {

			type.append(l.getTypes().get(j) + " ");

		}

		Log.d("Log",type.toString());
		creerIcon(context, im, l.getLatitude(), l.getLongitude(), myLocation, type.toString());

	}

	private void creerIcon(Context context, ImageView Imview, double latitude, double longitude,
			Location myLocation,final String type){

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

		if (Imview == null || typeTemp.equalsIgnoreCase("nouveau")) {

			// On choisi l'icon en fonction du type
			choixIcon(typeTemp);
			photoDescription = icon;
			description = typeTemp;

		} else {

			icon.setBackgroundResource(R.drawable.favorite);
			photoDescription = icon;

		}

		icon.setClickable(true);

		typeIcon = typeTemp ;
		Log.d("Je doit faire",type);
		
		if(typeIcon.equalsIgnoreCase("nouveau")){
			
			this.name = lieu.getNom();
			this.adresse = lieu.getFormatted_address();
			this.phone = lieu.getFormatted_phone_number();
			this.webSite = lieu.getWebsite();
			this.description = lieu.getVicinity();
		}
		
		icon.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				if(lieu != null && (webSite != null || phone != null) && !typeIcon.equalsIgnoreCase("nouveau")){
					Log.d("retrouv", typeIcon);
					new RequeteDetail().execute("details");

				}else{
					
					Log.d("je passe la ", "ICI");
					details = new PlaceDetails();
				}
				showBoiteInformation();

			}
		});

		LinearLayout l = new LinearLayout(ctx);
		l.setOrientation(VERTICAL);
		l.setGravity(Gravity.CENTER);
		l.addView(icon);
		l.addView(label);
		// On ajoute l'icon et son label à la vue
		//this.addView(icon);
		//this.addView(label);
		this.addView(l);
	}


	/**
	 * Permet de choisir l'image de l'icon en fonction de son type
	 * 
	 * @param type
	 *            - Type du POI
	 */
	private void choixIcon(String type) {

		if (type.equalsIgnoreCase("bank")){

			icon.setBackgroundResource(R.drawable.bank);

		}else{

			if (type.equalsIgnoreCase("bicycle_store")){

				icon.setBackgroundResource(R.drawable.bicycle);

			}else{

				if(type.equalsIgnoreCase("book_store")){

					icon.setBackgroundResource(R.drawable.book);

				}else{

					if(type.equalsIgnoreCase("bowling_alley")){

						icon.setBackgroundResource(R.drawable.bowling);

					}else{

						if(type.equalsIgnoreCase("bus_station")){

							icon.setBackgroundResource(R.drawable.bus);

						}else{

							if(type.equalsIgnoreCase("cafe")){

								icon.setBackgroundResource(R.drawable.coffee);

							}else{

								if(type.equalsIgnoreCase("car_repair")){

									icon.setBackgroundResource(R.drawable.car);

								}else{

									if(type.equalsIgnoreCase("car_wash")){

										icon.setBackgroundResource(R.drawable.car);

									}else{

										if(type.equalsIgnoreCase("clothing_store")){

											icon.setBackgroundResource(R.drawable.clothing);

										}else{

											if(type.equalsIgnoreCase("food")){

												icon.setBackgroundResource(R.drawable.food);

											}else{

												if(type.equalsIgnoreCase("grocery_or_supermarket")){

													icon.setBackgroundResource(R.drawable.supermaket);

												}else{

													if(type.equalsIgnoreCase("gym")){

														icon.setBackgroundResource(R.drawable.sport);

													}else{

														if(type.equalsIgnoreCase("hair_care")){

															icon.setBackgroundResource(R.drawable.img_epingle);

														}else{

															if(type.equalsIgnoreCase("hardware_store")){

																icon.setBackgroundResource(R.drawable.sytemestore);


															}else{

																if(type.equalsIgnoreCase("hospital")){

																	icon.setBackgroundResource(R.drawable.hospital);

																}else{

																	if(type.equalsIgnoreCase("insurance_agency")){

																		icon.setBackgroundResource(R.drawable.insurance);

																	}else{

																		if(type.equalsIgnoreCase("library")){

																			icon.setBackgroundResource(R.drawable.library);

																		}else{

																			if(type.equalsIgnoreCase("police")){

																				icon.setBackgroundResource(R.drawable.police);

																			}else{

																				if(type.equalsIgnoreCase("post_office")){

																					icon.setBackgroundResource(R.drawable.poste);

																				}else{

																					if(type.equalsIgnoreCase("restaurant")){

																						icon.setBackgroundResource(R.drawable.restaurant);

																					}else{

																						if(type.equalsIgnoreCase("stadium")){

																							icon.setBackgroundResource(R.drawable.stadium);

																						}else{

																							if(type.equalsIgnoreCase("store")){

																								icon.setBackgroundResource(R.drawable.restaurant);

																							}else{

																								if(type.equalsIgnoreCase("university")){

																									icon.setBackgroundResource(R.drawable.university);

																								}else{

																									if(type.equalsIgnoreCase("bar")){


																										icon.setBackgroundResource(R.drawable.cocktail);

																									}else{

																										if(type.equalsIgnoreCase("nouveau")){

																											icon.setBackgroundResource(R.drawable.favoris);

																										}else{
																											icon.setBackgroundResource(R.drawable.img_epingle);
																										}
																									}
																								}
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
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

		if(lieu != null){

			while(details == null){
				//attend
			}
		}
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

		if(lieu != null && !typeIcon.equalsIgnoreCase("nouveau")){
			image.setBackgroundDrawable(LoadImageFromWebOperations(lieu.getIcon()));
			this.name = lieu.getNom();
		}else{
			image.setImageDrawable(photoDescription.getBackground());
		}

		TextView text = (TextView) alertDialogView.findViewById(R.id.titre);
		text.setText(name);

		text = (TextView) alertDialogView.findViewById(R.id.description);
		text.setText(description);


		text = (TextView) alertDialogView.findViewById(R.id.distance);
		text.setText(this.label.getText());

		text = (TextView) alertDialogView.findViewById(R.id.adresse);
		text.setText(this.adresse);

		text = (TextView) alertDialogView.findViewById(R.id.webSite);
		text.setText(this.webSite);

		text = (TextView) alertDialogView.findViewById(R.id.phone);
		text.setText(this.phone);

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
		
		if(!typeIcon.equalsIgnoreCase("nouveau")){
			
			Button b = (Button) alertDialogView.findViewById(R.id.sauvegarder);
			b.setVisibility(View.VISIBLE);
			b.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					Sauvegarde s = new Sauvegarde(ctx);
					s.sauvegarderLieu(lieu);					
				}
			});
			
		}

		adb.show();

	}


	private Drawable LoadImageFromWebOperations(String url)
	{
		try
		{
			InputStream is = (InputStream) new URL(url).getContent();
			Drawable d = Drawable.createFromStream(is, "src name");
			return d;
		}catch (Exception e) {
			System.out.println("Exc="+e);
			return null;
		}
	}

	class RequeteDetail extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			FouilleDonnee fd=new FouilleDonnee();

			if(params[0].equals("details")){

				Log.d("details",lieu.getReference());
				details=fd.getDetails(lieu.getReference());
				Log.d("Null ?", (details == null) +"");
				if(details != null){
					if(lieu != null){
						lieu.name = details.result.getNom();
						lieu.vicinity = details.result.getTypes().get(0);
						lieu.formatted_address = details.result.getFormatted_address();
						lieu.website = details.result.getWebsite();
						lieu.formatted_phone_number = details.result.getFormatted_phone_number();
						
					}	
					Log.d("web", ""+details.result.getWebsite());
					
					adresse = details.result.getFormatted_address();
					webSite = details.result.getWebsite();
					phone = details.result.getFormatted_phone_number();
					details.status = "execute";
					
					Log.d("adresse", details.result.getFormatted_address() + " ja");
				}

			}

			return null;
		}

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
