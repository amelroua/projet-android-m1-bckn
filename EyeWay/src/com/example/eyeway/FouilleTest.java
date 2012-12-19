package com.example.eyeway;

import java.util.ArrayList;

import com.example.eyeway.fouilleDedonne.DetailLieu;
import com.example.eyeway.fouilleDedonne.FouilleDonnee;
import com.example.eyeway.fouilleDedonne.Lieu;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;

public class FouilleTest extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fouille_test);
        //testgetDetails(); //fonctionne
        //testgetLieuParProximite();
        testgetLieuParRecherche();
    }
    //Fonctionne
    public void testgetDetails(){
    	AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        FouilleDonnee fd=new FouilleDonnee();
        DetailLieu d=fd.getDetails("CpQBggAAAGAqhZ-mEBAbbEvpYxwLkfs268DA44qO4IIISsKMjFodvHpu_eEdoefg3sn9g-nRwUo6Uc2XcIXZ4uJlq6-LlkzalDfcOn6XLwboK-x53pWyQDowTzGyj6HXJSUATDK0_pgxRXM6hKjKpYmZHERQ9LTwuXz3A4jlvCv1nuZ2klI3jlitoQgUk2A1AqMUNFybSBIQQWJrTEvNEKOOE0kZZwDoOxoUU2jguW8ph6uwfincnrSd6VK_Img&sensor=true&language=fr&key=AIzaSyDjWK46sXjISDvz38EsP0N-YegOAU_I0Cs");
        String s=d.toString();
        alertDialog.setTitle("Resultat de la requete...");
        alertDialog.setMessage(s);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which) {
              // here you can add functions
           }
        });
        //alertDialog.setIcon(R.drawable.icon);
        alertDialog.show();
    }
    //Pas testé TODO
    public void testgetLieuParProximite(){
    	FouilleDonnee fd=new FouilleDonnee();
    	ArrayList<Lieu> Lieux = fd.getLieuProximiteParType(47.845489, 1.939776,"", 10);
		/*
    	for(Lieu l : Lieux) {
			l.toString();
		}
		*/
    	AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Resultat de la requete...");
        alertDialog.setMessage(Lieux.get(0).toString());
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which) {
              // here you can add functions
           }
        });
        //alertDialog.setIcon(R.drawable.icon);
        alertDialog.show();
    }
    //Pas testé TODO
    public void testgetLieuParRecherche(){
    	FouilleDonnee fd=new FouilleDonnee();
    	ArrayList<Lieu> Lieux = fd.getLieuParRecherche("restaurant olivet");
		/*
    	for(Lieu l : Lieux) {
			l.toString();
		}
		*/
    	AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Resultat de la requete...");
        alertDialog.setMessage(Lieux.get(0).toString());
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which) {
              // here you can add functions
           }
        });
        //alertDialog.setIcon(R.drawable.icon);
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_fouille_test, menu);
        return true;
    }
}
