package com.example.eyeway.menuPrincipal;

import java.util.ArrayList;

import com.example.eyeway.R;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.example.eyeway.fouilleDedonne.FouilleDonnee;
import com.example.eyeway.fouilleDedonne.Lieu;
import com.example.eyeway.fouilleDedonne.Lieu.Geometry;
import com.example.eyeway.fouilleDedonne.Sauvegarde;
import com.example.eyeway.realiteAugmente.RealiteAugmente;

import android.location.GpsStatus;
import com.example.eyeway.fouilleDedonne.Lieu.MyLocation;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Avoir un bouton pour le status du GPS signifie avoir une async task,
 *  qui ecoute le status du gps et qui envoie un message a notre activity lorsque le status change (Désactivé/En attente de signal/Prêt)
 * Pour l'instant l'interface n'est pas le plus important donc on ne gere pas ce bouton.
 * @author chris
 *
 */

public class MenuPrincipal extends Activity implements OnClickListener,OnItemClickListener {

	private LocationManager manager;
	private Button status_gps;
	private ListView list_menu;
	// GPS Location
	GPSTracker gps; 

	ConnectionDetector cd ;
	AlertDialogManager alert = new AlertDialogManager();
	Boolean isInternetPresent = false ;
	Sauvegarde s=new Sauvegarde(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		//setTheme(R.style.Theme_perso);
		setContentView(R.layout.activity_menu_principal);
		
		ArrayList<Fonctionnalite> tab_fonctionnalite =new ArrayList<Fonctionnalite>();
		tab_fonctionnalite.add(new Fonctionnalite(R.drawable.menu_recherche_par_mot_clef,getString(R.string.title_activity_recherche_par_mot_cle)));
		tab_fonctionnalite.add(new Fonctionnalite(R.drawable.menu_recherche_dans_zone,getString(R.string.title_activity_recherche_perimetre)));
		tab_fonctionnalite.add(new Fonctionnalite(R.drawable.menu_recherche_par_adresse,getString(R.string.title_activity_recherche_par_adresse)));
		tab_fonctionnalite.add(new Fonctionnalite(R.drawable.menu_recherche_instantanee,getString(R.string.title_activity_navigation_instantane)));
		tab_fonctionnalite.add(new Fonctionnalite(R.drawable.menu_favoris,getString(R.string.title_activity_gerer_poi)));
		ListAdapter adapter=null;
		if(isTablet(this)){
			adapter=new ListAdapter(this,R.layout.ligne_menu,tab_fonctionnalite);
		}else{
			adapter=new ListAdapter(this,R.layout.ligne_menu_mobile,tab_fonctionnalite);
		}
		
		list_menu = (ListView)findViewById(R.id.liste_fonctions);
		manager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		//View header = (View)getLayoutInflater().inflate(R.layout.ligne_menu, null);
		//list_menu.addHeaderView(header);
		list_menu.setAdapter(adapter);
		list_menu.setOnItemClickListener(this);


		cd = new ConnectionDetector(getApplicationContext());

		isInternetPresent = true ;//cd.isConnectionToInternet();

		if(!isInternetPresent){
			alert.showAlertDialog(MenuPrincipal.this, "Données Désactivées","Activer l'accés aux données sur le réseau mobile",false);
		}else{
			Log.d("GPS","la");
			gps = new GPSTracker(this);

		}

	}
	public static boolean isTablet(Context context) {
	    boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
	    boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
	    return (xlarge || large);
	}

	@Override
	public void onPause(){
		super.onPause();
		if(gps != null)
			gps.stopUsingGPS();
	}

	@Override
	public void onStop(){
		super.onStop();
		if(gps != null)
			gps.stopUsingGPS();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_menu_principal, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if(arg0.getId()==list_menu.getId()){
			//if(v.getId()==R.id.gpsStatus.getId()){

			if(!gps.isGPSEnabled && !gps.isNetworkEnabled){

				gps = new GPSTracker(this);
				
			}else{

				Intent monIntent;
				switch(arg2){
				case 0 : 
					alert.showRecherche(this);
					break;
				case 1 : 
					monIntent= new Intent(this,RecherchePerimetre.class);
					startActivity(monIntent);
					break;
				case 2 : 
					monIntent= new Intent(this,RechercheAdresse.class);
					startActivity(monIntent);
					break;

				case 3 : 

					monIntent= new Intent(this,RealiteAugmente.class);
					monIntent.putExtra("methode","instantane");
					startActivity(monIntent);

					break;
				case 4 : 

					Sauvegarde s = new Sauvegarde(this);
					ArrayList<Lieu> lesLieux = s.getLieuxEnregistres();

					if(lesLieux.size() != 0){

						monIntent= new Intent(this,GestionPointsInterets.class);
						monIntent.putExtra("lieux",lesLieux);

						startActivity(monIntent);

					}else{

						Toast.makeText(this,"Vous n'avez aucun POI favoris", Toast.LENGTH_LONG).show();
					}
					break;

				}
			}
		}
	}
}
