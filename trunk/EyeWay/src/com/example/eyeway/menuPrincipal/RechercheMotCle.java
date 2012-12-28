package com.example.eyeway.menuPrincipal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.example.eyeway.R;
import com.example.eyeway.Map.Map;
import com.example.eyeway.R.layout;
import com.example.eyeway.R.menu;
import com.example.eyeway.fouilleDedonne.FouilleDonnee;
import com.example.eyeway.fouilleDedonne.Lieu;
import com.example.eyeway.realiteAugmente.RealiteAugmente;

import android.location.GpsStatus;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class RechercheMotCle extends Activity implements OnClickListener
		 {
	/**
	 * Les parametres qui vont etre utilisés pour la soumission du formulaire
	 */
	

	int distance;

	
	private EditText edit_text_mot_cle;
	private ImageView bouton_validation_formulaire;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_recherche_motcle);
		
		bouton_validation_formulaire = (ImageView) findViewById(R.id.bouton_validation_formulaire);
		bouton_validation_formulaire.setOnClickListener(this);
		
		edit_text_mot_cle = (EditText) findViewById(R.id.editText1);

		edit_text_mot_cle.setFilters(new InputFilter[] {
			    new InputFilter() {
			       
			    		@Override
					public CharSequence filter(CharSequence source, int start,
							int end, Spanned dest, int dstart, int dend) {
			    			   if(source.equals("")){ // for backspace
					                return source;
					            }
					            if(source.toString().matches("[a-zA-Z ]+")){
					                return source;
					            }
					            return "";
					}
			    }
			});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_menu_principal, menu);
		return true;
	}

	/**
	 * Boite de dialogue qui s'ouvre lors du clic sur le calcul d'itinéraire
	 */
	void showBoiteChoix() {

		// Création de l'AlertDialog
		AlertDialog.Builder adb = new AlertDialog.Builder(this);

		// On donne un titre à l'AlertDialog
		adb.setTitle("Choix Visualisation");

		// On modifie l'icône de l'AlertDialog pour le fun ;)
		adb.setIcon(R.drawable.info);

		// adb.setCancelable(true);
		String[] choix = { "Map", "Réalité augmenté" };

		adb.setItems(choix, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent;
				if (which == 0) {

					intent = new Intent(getApplicationContext(), Map.class);
				} else {
					intent = new Intent(getApplicationContext(),
							RealiteAugmente.class);
					intent.putExtra("methode", "motCle");
					intent.putExtra("motCle", edit_text_mot_cle.getText().toString());
				}
				startActivity(intent);

				dialog.dismiss();
			}
		});

		adb.show();
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	 if (v.getId() == R.id.bouton_validation_formulaire) {

								
				showBoiteChoix();
	
	 }	
	}

}
