package com.example.eyeway.menuPrincipal;

import com.example.eyeway.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
public class MenuPrincipal extends Activity implements OnClickListener,OnItemClickListener {
	private Button gps_status;
	private ListView list_menu;
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
		};
		ListAdapter adapter= new ListAdapter(this,R.layout.ligne_menu,tab_fonctionnalite);
		list_menu = (ListView)findViewById(R.id.liste_fonctions);
		//View header = (View)getLayoutInflater().inflate(R.layout.ligne_menu, null);
		//list_menu.addHeaderView(header);
		list_menu.setAdapter(adapter);
		list_menu.setOnItemClickListener(this);
		gps_status=(Button)findViewById(R.id.gpsStatus);
		gps_status.setOnClickListener(this);
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
		
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if(arg0.getId()==list_menu.getId()){
			//if(v.getId()==R.id.gpsStatus.getId()){
			if(arg2==0){
				Toast.makeText(getApplicationContext(), "Saisir destination", Toast.LENGTH_SHORT).show();
			}else if(arg2==1){
				Toast.makeText(getApplicationContext(), "Navigation Instantanée", Toast.LENGTH_SHORT).show();
			}else if(arg2==2){
				Toast.makeText(getApplicationContext(), "Gérer les points d'interêt", Toast.LENGTH_SHORT).show();
			}else if(arg2==3){
				Toast.makeText(getApplicationContext(), "Rechercher dans une zone", Toast.LENGTH_SHORT).show();
			}
				
			}
	}
}
