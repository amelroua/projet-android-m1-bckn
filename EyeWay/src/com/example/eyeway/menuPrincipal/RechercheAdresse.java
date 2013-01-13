package com.example.eyeway.menuPrincipal;



import com.example.eyeway.R;
import com.example.eyeway.map.Map;
import com.example.eyeway.realiteAugmente.RealiteAugmente;
import com.google.api.client.util.escape.Escaper;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;


public class RechercheAdresse extends Activity implements OnClickListener
		 {
	/**
	 * Les parametres qui vont etre utilisés pour la soumission du formulaire
	 */
	

	int distance;
	
	private EditText editTypesBatiments;
	private EditText editVille;
	private EditText editNomRue;
	private EditText editCodePostal;
	private EditText editPays;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private Button bouton_validation_formulaire;


	    View.OnTouchListener gestureListener;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_recherche_adresse);
		
		bouton_validation_formulaire = (Button) findViewById(R.id.bouton_validation_formulaire);
		bouton_validation_formulaire.setOnClickListener(this);
		
		editTypesBatiments = (EditText) findViewById(R.id.typeBatiment);
		editVille = (EditText) findViewById(R.id.ville);
		editNomRue = (EditText) findViewById(R.id.nomRue);
		editCodePostal = (EditText) findViewById(R.id.codePostale);
		editPays = (EditText) findViewById(R.id.pays);
		
		editTypesBatiments.setFilters(new InputFilter[] {
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
		
		editVille.setFilters(new InputFilter[] {
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
		
		editNomRue.setFilters(new InputFilter[] {
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
		
		
		editPays.setFilters(new InputFilter[] {
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
		
		ActivitySwipeDetector activitySwipeDetector = new ActivitySwipeDetector(this);
		ScrollView lowestLayout = (ScrollView)this.findViewById(R.id.linear_adresse);
		lowestLayout.setOnTouchListener(activitySwipeDetector);
		
		

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
					intent.putExtra("methode", "recherche");
					intent.putExtra("motCle", editTypesBatiments.getText().toString() + 
							editVille.getText().toString() +
							editNomRue.getText().toString() +
							editCodePostal.getText().toString() +
							editPays.getText().toString());
				} else {
					intent = new Intent(getApplicationContext(),
							RealiteAugmente.class);
					intent.putExtra("methode", "recherche");
					intent.putExtra("motCle", editTypesBatiments.getText().toString() + 
							editVille.getText().toString() +
							editNomRue.getText().toString() +
							editCodePostal.getText().toString() +
							editPays.getText().toString());
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
		 
		 if(editTypesBatiments.getText().toString().equalsIgnoreCase("") && editVille.getText().toString().equalsIgnoreCase("")){
			 
			Toast.makeText(getApplicationContext(), 
					"Le types de batiments et la ville doivent être renseignés", Toast.LENGTH_SHORT).show(); 
	 	 }else{
	 		 
	 		 if(editTypesBatiments.getText().toString().equalsIgnoreCase("")){
	 			 
	 			Toast.makeText(getApplicationContext(), 
						"Le type de batiment doit être renseignés", Toast.LENGTH_SHORT).show(); 
	 		 }else{
	 			 
	 			 if(editVille.getText().toString().equalsIgnoreCase("")){
	 				 
	 				Toast.makeText(getApplicationContext(), 
	 						"La ville doit être renseignée", Toast.LENGTH_SHORT).show(); 
	 			 }else{
	 				showBoiteChoix(); 
	 			 }
	 		 }
				
	 	 }
	 }	
	}
}
