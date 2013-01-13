package com.example.eyeway.menuPrincipal;



import com.example.eyeway.R;
import com.example.eyeway.map.Map;
import com.example.eyeway.realiteAugmente.RealiteAugmente;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;


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
		
		LayoutInflater factory = LayoutInflater.from(this);
		final View alertDialogView = factory.inflate(R.layout.boitedialogue,
				null);

		// Création de l'AlertDialog
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		
		bouton_validation_formulaire = (ImageView) findViewById(R.id.bouton_validation_formulaire);
		bouton_validation_formulaire.setOnClickListener(this);
		
		edit_text_mot_cle = (EditText) findViewById(R.id.editText1);

	
		
		adb.show();

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
					intent.putExtra("motCle", edit_text_mot_cle.getText().toString());
				} else {
					intent = new Intent(getApplicationContext(),
							RealiteAugmente.class);
					intent.putExtra("methode", "recherche");
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
