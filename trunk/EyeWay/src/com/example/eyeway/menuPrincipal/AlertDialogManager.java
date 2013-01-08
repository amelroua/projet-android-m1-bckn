package com.example.eyeway.menuPrincipal;

import com.example.eyeway.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class AlertDialogManager {

	public Context mContext;
	
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
}
