package com.example.eyeway.menuPrincipal;

import java.util.ArrayList;

import com.example.eyeway.R;
import com.example.eyeway.Map.Map;

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
import com.example.eyeway.realiteAugmente.RealiteAugmente;

import android.location.GpsStatus;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		//setTheme(R.style.Theme_perso);
		setContentView(R.layout.activity_menu_principal);
		ArrayList<Fonctionnalite> tab_fonctionnalite =new ArrayList<Fonctionnalite>();
		tab_fonctionnalite.add(new Fonctionnalite(R.drawable.text_field,getString(R.string.title_activity_recherche_par_mot_cle)));
		tab_fonctionnalite.add(new Fonctionnalite(R.drawable.find_area,getString(R.string.title_activity_recherche_perimetre)));
		tab_fonctionnalite.add(new Fonctionnalite(R.drawable.ajouterpop,getString(R.string.title_activity_recherche_par_adresse)));
		tab_fonctionnalite.add(new Fonctionnalite(R.drawable.eye,getString(R.string.title_activity_navigation_instantane)));
		tab_fonctionnalite.add(new Fonctionnalite(R.drawable.star,getString(R.string.title_activity_gerer_poi)));
		ListAdapter adapter=new ListAdapter(this,R.layout.ligne_menu,tab_fonctionnalite);
		list_menu = (ListView)findViewById(R.id.liste_fonctions);
		manager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		//View header = (View)getLayoutInflater().inflate(R.layout.ligne_menu, null);
		//list_menu.addHeaderView(header);
		list_menu.setAdapter(adapter);
		list_menu.setOnItemClickListener(this);


		cd = new ConnectionDetector(getApplicationContext());

		isInternetPresent = cd.isConnectionToInternet();

		if(!isInternetPresent){

			alert.showAlertDialog(MenuPrincipal.this, "Données Désactivées","Activer l'accés aux données sur le réseau mobile",false);

		}else{

			Log.d("GPS","la");

			gps = new GPSTracker(this);

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

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if(arg0.getId()==list_menu.getId()){
			//if(v.getId()==R.id.gpsStatus.getId()){
			Intent monIntent;
			switch(arg2){
			case 0 : 
				monIntent= new Intent(this,RechercheMotCle.class);
				startActivity(monIntent);
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
				/*
				monIntent= new Intent(this,RealiteAugmente.class);
				monIntent.putExtra("methode","instantane");
				startActivity(monIntent);
				 */
				Toast.makeText(getApplicationContext(),"clic", Toast.LENGTH_SHORT).show();
				try {
					FileInputStream fis=openFileInput("hello_file");
					ObjectInputStream is = new ObjectInputStream(fis);
					Lieu object =(Lieu) is.readObject();
					is.close();
					Toast.makeText(getApplicationContext(),object.getNom(), Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),"exception a la lecture "+e.getMessage(), Toast.LENGTH_SHORT).show();
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}

				break;
			case 4 : 
				Toast.makeText(getApplicationContext(), "TODO", Toast.LENGTH_SHORT).show();
				String FILENAME = "hello_file";
				Lieu lieuAenregistrer = new Lieu("001", "nom");
				//FileOutputStream fos=null;

				FileOutputStream fos=null;
				try {
					fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					AlertDialog alertDialog = new AlertDialog.Builder(this).create();
					alertDialog.setTitle("Reset...");
					alertDialog.setMessage("FILE exception a l ecriture "+e.getCause()+" "+e.getStackTrace());

					//alertDialog.setIcon(R.drawable.icon);
					alertDialog.show();
				}	

				byte[] b=null;
				
				
				if(fos==null){
					AlertDialog alertDialog = new AlertDialog.Builder(this).create();
					alertDialog.setTitle("Reset...");
					alertDialog.setMessage("fos NULL ");

					//alertDialog.setIcon(R.drawable.icon);
					alertDialog.show();
				}
				if(b==null){
					AlertDialog alertDialog = new AlertDialog.Builder(this).create();
					alertDialog.setTitle("Reset...");
					alertDialog.setMessage("b NULL ");

					//alertDialog.setIcon(R.drawable.icon);
					alertDialog.show();
				}
				/*
				try {

					fos.write(b);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					AlertDialog alertDialog = new AlertDialog.Builder(this).create();
					alertDialog.setTitle("Reset...");
					alertDialog.setMessage("IO exception a l ecriture "+e.getCause()+" "+e.getStackTrace());

					//alertDialog.setIcon(R.drawable.icon);
					alertDialog.show();
				}
				 */
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					AlertDialog alertDialog = new AlertDialog.Builder(this).create();
					alertDialog.setTitle("Reset...");
					alertDialog.setMessage("CLOSE exception a l ecriture "+e.getCause()+" "+e.getStackTrace());

					//alertDialog.setIcon(R.drawable.icon);
					alertDialog.show();
				}
				/*
				catch(Exception e){


					AlertDialog alertDialog = new AlertDialog.Builder(this).create();
					alertDialog.setTitle("Reset...");
					alertDialog.setMessage("exception a l ecriture "+e.getCause()+" "+e.getStackTrace());

					//alertDialog.setIcon(R.drawable.icon);
					alertDialog.show();
				}*/
				break;
			}

		}
	}
	public byte[] getBytes(Object obj) {
		ByteArrayOutputStream bos=null;
		ObjectOutputStream oos=null;
		byte [] data=null;
		try{
			bos = new ByteArrayOutputStream(); 
			oos = new ObjectOutputStream(bos); 
			oos.writeObject(obj);
			oos.flush(); 

			//bos.toByteArra
			 data =bos.toByteArray();
			
		}catch(Exception e){
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Reset...");
			alertDialog.setMessage("GETBYTES exception a l ecriture "+e.getCause()+" "+e.getStackTrace());

			//alertDialog.setIcon(R.drawable.icon);
			alertDialog.show();
		}

		try{
			oos.close(); 
			bos.close();
		}catch(Exception e){
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Reset...");
			alertDialog.setMessage("CLOSE exception a l ecriture "+e.getCause()+" "+e.getStackTrace());

			//alertDialog.setIcon(R.drawable.icon);
			alertDialog.show();
		}
		return data;

		/*
		 * 
		 * ByteArrayOutputStream bos = new ByteArrayOutputStream();
ObjectOutput out = null;
try {
  out = new ObjectOutputStream(bos);   
  out.writeObject(yourObject);
  byte[] yourBytes = bos.toByteArray();
  ...
} finally {
  out.close();
  bos.close();
}
		 */
	}

}
