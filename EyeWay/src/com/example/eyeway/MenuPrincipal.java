package com.example.eyeway;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MenuPrincipal extends Activity {

	private ListView list_menu;
	private ArrayAdapter<String> list_menu_adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        list_menu=(ListView) findViewById(R.id.liste_fonctions);
        ArrayList<String> liste_fonctions= new ArrayList<String>();
        liste_fonctions.add("Saisir destination");
        liste_fonctions.add("Navigation instantannée");
        liste_fonctions.add("Gérer les points d'intérêt");
        liste_fonctions.add("Rechercher dans une zone");
        list_menu_adapter=new ArrayAdapter<String>(this,R.layout.ligne_menu,R.id.texte_ligne,liste_fonctions);
        list_menu.setAdapter(list_menu_adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu_principal, menu);
        return true;
    }
}
