package com.example.eyeway;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MenuPrincipal extends Activity {

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu_principal, menu);
        return true;
    }
}
