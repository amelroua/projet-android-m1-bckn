package com.example.eyeway;

import com.example.eyeway.Map.Map;
import com.example.eyeway.fouilleDedonne.FouilleDonnee;
import com.example.eyeway.realiteAugmente.RealiteAugmente;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity implements android.view.View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    	
    public void onClickRealite(View arg0){
    	
    	Intent monIntent = new Intent(this,RealiteAugmente.class);
    	startActivity(monIntent);
    }
 
   public void onClickMap(View arg0){
    	
	   Intent monIntent = new Intent(this,Map.class);
	   startActivity(monIntent);
    }
    
   public void onClickFouille(View arg0){
   	
	   Intent monIntent = new Intent(this,FouilleDonnee.class);
	   startActivity(monIntent);  
	   
   }
   
	@Override
	public void onClick(View arg0) {

	}

    
}
