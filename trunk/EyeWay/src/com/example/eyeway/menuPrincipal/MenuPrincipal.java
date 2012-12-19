package com.example.eyeway.menuPrincipal;

import com.example.eyeway.R;
import com.example.eyeway.Map.Map;
import com.example.eyeway.fouilleDedonne.FouilleDonnee;
import com.example.eyeway.realiteAugmente.RealiteAugmente;

import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
	private Button gps_status;
	private ListView list_menu;
	private LocationManager locationManager;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setTheme(R.style.Theme_perso);
		setContentView(R.layout.activity_menu_principal);
		Fonctionnalite tab_fonctionnalite []= new Fonctionnalite[]{
				new Fonctionnalite(R.drawable.text_field,"Saisir destination"),
				new Fonctionnalite(R.drawable.eye,"Navigation instantannée"),
				new Fonctionnalite(R.drawable.star,"Gérer les points d'intérêt"),
				new Fonctionnalite(R.drawable.find_area,"Rechercher dans une zone"),
				new Fonctionnalite(R.drawable.default_image,"Realite"),
				new Fonctionnalite(R.drawable.default_image,"Map"),
				new Fonctionnalite(R.drawable.default_image,"Fouille")
		};
		ListAdapter adapter= new ListAdapter(this,R.layout.ligne_menu,tab_fonctionnalite);
		list_menu = (ListView)findViewById(R.id.liste_fonctions);
		//View header = (View)getLayoutInflater().inflate(R.layout.ligne_menu, null);
		//list_menu.addHeaderView(header);
		list_menu.setAdapter(adapter);
		list_menu.setOnItemClickListener(this);
		gps_status=(Button)findViewById(R.id.gpsStatus);
		gps_status.setOnClickListener(this);
		locationManager= (LocationManager) getSystemService(LOCATION_SERVICE);
	}
	public void changerAffichageGpsStatus(){
		
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			gps_status.setText("Gps Activé");
			gps_status.setTextColor(R.color.plus_clair);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_menu_principal, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==gps_status.getId()){
			//différents cas :
			//Le gps peut etre activé ou non
			//Si le gps n'est pas activé
			if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
				startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),0);
			}else 
			//le gps est activé
			{
				Toast.makeText(getApplicationContext(), "GPS deja activé", Toast.LENGTH_SHORT).show();
				
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if(arg0.getId()==list_menu.getId()){
			//if(v.getId()==R.id.gpsStatus.getId()){
			Intent monIntent;
			switch(arg2){
			case 0 : 
				Toast.makeText(getApplicationContext(), "TODO", Toast.LENGTH_SHORT).show();
				break;
			case 1 : 
				Toast.makeText(getApplicationContext(), "TODO", Toast.LENGTH_SHORT).show();
				break;
			case 2 : 
				Toast.makeText(getApplicationContext(), "TODO", Toast.LENGTH_SHORT).show();
				break;
			case 3 : 
				Toast.makeText(getApplicationContext(), "TODO", Toast.LENGTH_SHORT).show();
				break;
			case 4 : 
				monIntent  = new Intent(this,RealiteAugmente.class);
				startActivity(monIntent);
				break;
			case 5 : 
				monIntent = new Intent(this,Map.class);
				startActivity(monIntent);
				break;
			case 6 : 
				monIntent = new Intent(this,FouilleDonnee.class);
				startActivity(monIntent);
				break;  
			}	
		}
	}
}
