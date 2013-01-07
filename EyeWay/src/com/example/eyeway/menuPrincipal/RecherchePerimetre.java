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
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class RecherchePerimetre extends Activity implements OnClickListener,
		OnItemClickListener, OnItemSelectedListener {
	/**
	 * Les parametres qui vont etre utilisés pour la soumission du formulaire
	 */
	enum centre_perimetre {
		Autour_De_Ma_Position, Autour_Dune_autre_position
	};

	centre_perimetre centre;
	int distance;
	String types;
	//
	private ArrayList<Fonctionnalite> ensemble_types;// ne pas faire de add sur
														// ce tableau, mais sur
														// le listAdapter
	private EditText edit_text_distance;
	private ListView list_types_selectionnes;
	private Button bouton_ajout_type;
	private Button bouton_validation_formulaire;
	private ListAdapter adapter_list_view_types;
	// la liste des choix pour le centre du perimetre : Autour de ma position,
	// Autour d'une position donnée
	// pour ce spinner, on reagit directement a l'evenement de selection
	// private Spinner spinner_choix_position;
	// la liste des choix de types : Restaurant, Piscine ...
	// Pour ce spinner, on ne reagit pas directement a la selection mais apres
	// clique sur le bouton bouton_ajout_type on recupere la valeur selectionnée
	private Spinner spinner_selection_type;

	// private ArrayAdapter adapter_spinner_selection_type;
	// private ArrayAdapter adapter_spinner_types;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// setTheme(R.style.Theme_perso);
		setContentView(R.layout.activity_recherche_perimetre);
		/**
		 * Pour gérer la list view des types qui ont été selectionnés
		 */
		// ensemble_types=new HashSet<Fonctionnalite>();
		ensemble_types = new ArrayList<Fonctionnalite>();
		adapter_list_view_types = new ListAdapter(this,
				R.layout.ligne_liste_type, ensemble_types);
		// A CHANGER
		list_types_selectionnes = (ListView) findViewById(R.id.liste_types);
		edit_text_distance = (EditText) findViewById(R.id.editText1);
		// View header = (View)getLayoutInflater().inflate(R.layout.ligne_menu,
		// null);
		// list_menu.addHeaderView(header);
		list_types_selectionnes.setOnItemClickListener(this);
		list_types_selectionnes.setAdapter(adapter_list_view_types);

		bouton_ajout_type = (Button) findViewById(R.id.bouton_ajout_type);
		bouton_ajout_type.setOnClickListener(this);
		//
		bouton_validation_formulaire = (Button) findViewById(R.id.bouton_validation_formulaire);
		bouton_validation_formulaire.setOnClickListener(this);
		//

		// spinner_choix_position = (Spinner)
		// findViewById(R.id.spinner_choix_position);
		/**
		 * La liste déroulante des types de lieux : on recupere le contenu dans
		 * FouilleDonnee
		 */
		spinner_selection_type = (Spinner) findViewById(R.id.spinner_selection_type);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item,
				FouilleDonnee.types_place_fr);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_selection_type.setAdapter(dataAdapter);
		// adapter_spinner_selection_type = new
		// ArrayAdapter(this,android.R.layout.simple_spinner_item,
		// array_spinner);
		// spinner_selection_type.setAdapter(adapter);
		// spinner_choix_position.setOnItemSelectedListener(this);
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
					intent.putExtra("methode", "proximite");
					intent.putExtra("distance", distance);
					intent.putExtra("types", getArrayListeTypes());
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
		if (v.getId() == bouton_ajout_type.getId()) {
			// ajouter ce type a la liste
			String type_selectionne = "";
			type_selectionne = spinner_selection_type.getSelectedItem()
					.toString();

//			Toast.makeText(getApplicationContext(), type_selectionne,
	//				Toast.LENGTH_LONG).show();
			adapter_list_view_types.add(new Fonctionnalite(R.drawable.delete,
					type_selectionne));
		} else if (v.getId() == R.id.bouton_validation_formulaire) {

			/**
			 * Recuperer toutes les valeurs du formulaire et les soumettre a la
			 * requete :
			 */
			double lat = 0;
			double lng = 0;
			// Trouver la position du centre de recherche
			if (centre == centre_perimetre.Autour_De_Ma_Position) {
				// TODO mettre de vraies valeurs pour la position actuelle
				lat = 47.8686030;
				lng = 1.9124340;
			} else if (centre == centre_perimetre.Autour_Dune_autre_position) {
				// TODO envoyer l'utilisateur vers la carte et lui faire
				// selectionner un point
				lat = 47.8686030;
				lng = 1.9124340;
			}

			try {

				distance = Integer.parseInt(edit_text_distance.getText()
						.toString());

							// convertir la liste des types en concaténation dans une string
			ArrayList<String> types_saisis = getArrayListeTypes();
						
			if (types_saisis.size() == 0) {

				Toast.makeText(getApplicationContext(),
						"Entrer au moins un type de batiment",
						Toast.LENGTH_SHORT).show();

			} else {

				showBoiteChoix();
			}
			} catch (NumberFormatException e) {
				Toast.makeText(getApplicationContext(),
						"Entrer un entier pour la distance", Toast.LENGTH_SHORT)
						.show();
			}
		
			// Faire la requete

			//
		}
	}

	public ArrayList<String> getArrayListeTypes() {
		ArrayList<String> res = new ArrayList<String>();
		for (int i = 0; i < adapter_list_view_types.getCount(); i++) {
			res.add(adapter_list_view_types.getItem(i).title);// je sépare les
																// types par un
																// espaces, donc
																// on aura :
			// res = Bar Restaurant Discothèque ... Ensuite FouilleDonnee va
			// s'occuper de convertir en Bar+Restaurant+Discotèque
		}
		return res;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		//
	
		if (arg0.getId() == R.id.liste_types) {
			// Idée : possibilité de mettre une dialog pour demander la
			// confirmation a l'utilisateur
			// Ou bien créer un toast pour afficher qu'on a bien supprimé cet
			// item
			Toast.makeText(getApplicationContext(),
					"Suppresion du type " + adapter_list_view_types.getItem(arg2).title,
					Toast.LENGTH_SHORT).show();
			adapter_list_view_types.remove(arg2);
	
			// list_types_selectionnes.getAdapter().no
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		if (arg0.getId() == 9999) {
			if (arg2 == 0) {
				// Recherche a partir de ma position

				centre = centre_perimetre.Autour_De_Ma_Position;
			} else if (arg2 == 1) {
				centre = centre_perimetre.Autour_Dune_autre_position;
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Faire une dialog qui oblige la saisie d'un item
	}
}
