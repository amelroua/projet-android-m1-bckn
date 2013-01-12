package com.example.eyeway.menuPrincipal;

import com.example.eyeway.R;
import com.example.eyeway.Map.Map;
import com.example.eyeway.realiteAugmente.RealiteAugmente;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

public class AlertDialogManager implements OnClickListener{

	public Context mContext;
	int distance;


	private EditText edit_text_mot_cle;
	private ImageView bouton_validation_formulaire;

	
	@SuppressWarnings("deprecation")
	public void showAlertDialog(Context context,String title , String message,
			Boolean status){

		mContext = context;
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

		alertDialog.setTitle(title);

		alertDialog.setMessage(message);
		if(status != null)
			alertDialog.setIcon((status) ? R.drawable.ajouter : R.drawable.delete);



		alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				Activity ac = (Activity) mContext;
				ac.finish();

			}
		});
		alertDialog.show();
	}
	
	public void showRecherche(Context ctx){
		
		mContext = ctx ;
		LayoutInflater factory = LayoutInflater.from(ctx);
		final View alertDialogView = factory.inflate(R.layout.activity_recherche_motcle,
				null);

		// Création de l'AlertDialog
		AlertDialog.Builder adb = new AlertDialog.Builder(ctx);

		// On affecte la vue personnalisé que l'on a crée à notre AlertDialog
		adb.setView(alertDialogView);

		// On donne un titre à l'AlertDialog
		
		edit_text_mot_cle = (EditText) alertDialogView
				.findViewById(R.id.motcle);
		
		adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				showBoiteChoix();

			}
		});
		
		adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				return ;

			}
		});
				
		
		adb.show();
	}
	
	/**
	 * Boite de dialogue qui s'ouvre lors du clic sur le calcul d'itinéraire
	 */
	void showBoiteChoix() {

		// Création de l'AlertDialog
		AlertDialog.Builder adb = new AlertDialog.Builder(mContext);

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

					intent = new Intent(mContext, Map.class);
				} else {
					intent = new Intent(mContext,
							RealiteAugmente.class);
					intent.putExtra("methode", "recherche");
					intent.putExtra("motCle", edit_text_mot_cle.getText().toString());
				}
				mContext.startActivity(intent);

				dialog.dismiss();
			}
		});

		adb.show();
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	 if (v.getId() == R.id.bouton_validation_formulaire) {

								
				
	
	 }	
	}

	
}
