package com.example.eyeway;

import com.example.eyeway.Map.Map;
import com.example.eyeway.fouilleDedonne.FouilleDonnee;
import com.example.eyeway.menuPrincipal.MenuPrincipal;
import com.example.eyeway.realiteAugmente.RealiteAugmente;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v4.app.NavUtils;
import android.view.View.OnClickListener;
public class MainActivity extends Activity implements OnClickListener{
	private Button realite;
	private Button map;
	private Button fouille;
	private Button linterface;
	private Button splash;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realite=(Button) findViewById(R.id.realite);
        map=(Button) findViewById(R.id.map);
        fouille=(Button) findViewById(R.id.fouille);
        linterface=(Button) findViewById(R.id.linterface);
        splash = (Button) findViewById(R.id.splash);
        realite.setOnClickListener(this);
        map.setOnClickListener(this);
        fouille.setOnClickListener(this);
        linterface.setOnClickListener(this);
        splash.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
	@Override
	public void onClick(View v) {
		if(v.getId()==realite.getId()){
			Intent monIntent = new Intent(this,RealiteAugmente.class);
	    	startActivity(monIntent);
		}
		if(v.getId()==map.getId()){
			 Intent monIntent = new Intent(this,Map.class);
			 startActivity(monIntent);
		}
		if(v.getId()==fouille.getId()){
			Intent monIntent = new Intent(this,FouilleTest.class);
			startActivity(monIntent);  
			
		}
		if(v.getId()==linterface.getId()){
			 Intent monIntent = new Intent(this,MenuPrincipal.class);
			 startActivity(monIntent);  
		}
		
		if(v.getId() == splash.getId()){
			
			 Intent monIntent = new Intent(this,SplashScreen.class);
			 startActivity(monIntent); 
		}
	}

}
