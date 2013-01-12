package com.example.eyeway.menuPrincipal;

import java.util.ArrayList;

import com.example.eyeway.R;
import com.example.eyeway.fouilleDedonne.Lieu;
import com.example.eyeway.fouilleDedonne.Lieu.Geometry;
import com.example.eyeway.fouilleDedonne.Lieu.MyLocation;
import com.example.eyeway.fouilleDedonne.Sauvegarde;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class GestionPointsInterets extends Activity implements OnClickListener,OnItemClickListener{

	private ListView list_poi;
	Sauvegarde s=new Sauvegarde(this);
	private AdapterLieu adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_gestion_points_interets);
		ArrayList<Lieu> lesLieux=new ArrayList<Lieu>();
		lesLieux=s.getLieuxEnregistres();
		adapter=new AdapterLieu(this,R.layout.ligne_lieu,lesLieux);
		list_poi = (ListView)findViewById(R.id.liste_lieux);
		list_poi.setAdapter(adapter);
		list_poi.setOnItemClickListener(this);
		if(adapter.getCount()!=0){
			list_poi.setBackgroundResource(R.drawable.border1);
			Log.i("Adapter get count diff ", "Adapter get count diff");
		}else{
			Log.i("Adapter get count 0", "Adapter get count 0");
			Toast.makeText(getApplicationContext(),"Adapter get count 0", Toast.LENGTH_SHORT).show();
		}
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if(arg0.getId()==list_poi.getId()){

			String nom =adapter.getItem(arg2).getNom();
			Toast.makeText(getApplicationContext(),"Clic sur le Lieu "+nom, Toast.LENGTH_SHORT).show();
			Lieu l = s.getLieuParNom(nom); //faire le business avec le lieu : afficher l'alertdialog
		}
	}
	public void afficherAlertDialogDetailsPoi(final Lieu l){
		LayoutInflater factory = LayoutInflater.from(this);
		final View alertDialogView = factory.inflate(R.layout.alertdialog_details_poi,null);

		// Création de l'AlertDialog
		AlertDialog.Builder adb = new AlertDialog.Builder(this);

		// On affecte la vue personnalisé que l'on a crée à notre AlertDialog
		adb.setView(alertDialogView);

		// On donne un titre à l'AlertDialog
		adb.setTitle("Détail d'un Point d'intérêt");

		adb.setPositiveButton("Afficher ", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				//TODO
				//Appeler la méthode pour afficher un POI

			}
		});

		adb.setNegativeButton("Supprimer ce Poi", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				afficherAlertDialogSuppression(l);

				return ;

			}
		});


		adb.show();
	}

	public void afficherAlertDialogSuppression(final Lieu l){
		// Création de l'AlertDialog
		AlertDialog.Builder adb = new AlertDialog.Builder(this);	
		//AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		adb.setTitle("Supprimer un Point d'intérêt");
		adb.setMessage("Etes vous sur de vouloir désinstaller ce point d'intérêt ?");
		adb.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// here you can add functions
				s.supprimerLieu(l);
			}
		});
		adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				afficherAlertDialogSuppression(l);
				return ;

			}
		});
		adb.show();
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

}