package com.example.eyeway;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.eyeway.fouilleDedonne.FouilleDonnee;
import com.example.eyeway.fouilleDedonne.Lieu;
import com.example.eyeway.fouilleDedonne.ListeLieu;
import com.example.eyeway.fouilleDedonne.PlaceDetails;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class FouilleTest extends Activity {

	ListeLieu Lieux;
	PlaceDetails details;

	
	public String KEY_REFERENCE = "reference"; // id of the place
	public String KEY_NAME = "name"; // name of the place
	public String KEY_VICINITY = "vicinity"; // Place area name

	
	ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String,String>>();
	ListView lv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fouille_test1);
		lv = (ListView) findViewById(R.id.list);
		
		new TestLieu().execute("recherche");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_fouille_test, menu);
		return true;
	}


	class TestLieu extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			if(params[0].equals("proximite"))
				testgetLieuParProximite();
			else if (params[0].equals("recherche"))
				testgetLieuParRecherche();
			return null;
		}

		protected void onPostExecute(String file_url) {
			runOnUiThread(new Runnable() {
				public void run() {
					String status = Lieux.status;

					if(status.equals("OK")){
						// Successfully got places details
						if (Lieux.results != null) {
							// loop through each place
							for (Lieu p : Lieux.results) {
								HashMap<String, String> map = new HashMap<String, String>();

								// Place reference won't display in listview - it will be hidden
								// Place reference is used to get "place full details"
								map.put(KEY_REFERENCE, p.reference);

								// Place name
								map.put(KEY_NAME, p.name);

								// adding HashMap to ArrayList
								placesListItems.add(map);
							}
							// list adapter
							ListAdapter adapter = new SimpleAdapter(FouilleTest.this, placesListItems,
									R.layout.activity_fouille_liste_item,
									new String[] { KEY_REFERENCE, KEY_NAME},
									new int[] {R.id.reference, R.id.name });

							// Adding data into listview
							lv.setAdapter(adapter);
						}
					}
				}
			});    

		}

		//Fonctionne
		public void testgetDetails(){

			FouilleDonnee fd=new FouilleDonnee();
			details=fd.getDetails("CpQBggAAAGAqhZ-mEBAbbEvpYxwLkfs268DA44qO4IIISsKMjFodvHpu_eEdoefg3sn9g-nRwUo6Uc2XcIXZ4uJlq6-LlkzalDfcOn6XLwboK-x53pWyQDowTzGyj6HXJSUATDK0_pgxRXM6hKjKpYmZHERQ9LTwuXz3A4jlvCv1nuZ2klI3jlitoQgUk2A1AqMUNFybSBIQQWJrTEvNEKOOE0kZZwDoOxoUU2jguW8ph6uwfincnrSd6VK_Img&sensor=true&language=fr&key=AIzaSyDjWK46sXjISDvz38EsP0N-YegOAU_I0Cs");
		}
		//Pas testé TODO
		public void testgetLieuParProximite(){
			FouilleDonnee fd=new FouilleDonnee();
			ArrayList<String> types=new ArrayList<String> ();
			Lieux = fd.getLieuProximiteParType(47.845489, 1.939776,types, 10);

		}
		//Pas testé TODO
		public void testgetLieuParRecherche(){
			FouilleDonnee fd=new FouilleDonnee();
			Lieux = fd.getLieuParRecherche("restaurant olivet");

		}
	}
}
