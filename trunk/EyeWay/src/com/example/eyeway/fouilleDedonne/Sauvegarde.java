package com.example.eyeway.fouilleDedonne;

import java.util.ArrayList;

import com.example.eyeway.R;
import com.example.eyeway.Map.Map;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.example.eyeway.fouilleDedonne.FouilleDonnee;
import com.example.eyeway.fouilleDedonne.Lieu;
import com.example.eyeway.realiteAugmente.RealiteAugmente;

import android.location.GpsStatus;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;

public class Sauvegarde {

	Context fileContext; //nécessaire pour fileContext.openFileOutput
	static int idLieu=0;
	static int idPlaceDetail=0;
	public Sauvegarde(Context fileContext){
		this.fileContext = fileContext;
	}

	/**
	 * @param lieuAenregistrer
	 */
	public void sauvegarder(Object o,String nom){
		FileOutputStream fos=null;
		try{
			fos = fileContext.openFileOutput(nom, Context.MODE_PRIVATE);
			byte[] b=null;
			b=getBytes(o);
			fos.write(b);
			fos.close();
			Toast.makeText(fileContext.getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
		}catch(Exception e){
			Toast.makeText(fileContext.getApplicationContext(), "Exception save", Toast.LENGTH_SHORT).show();
		}
	}

	public void sauvegarderLieu(Lieu l){
		sauvegarder(l,"lieu"+idLieu); //todo gerer les noms uniques
	}
	public void sauvegarderPlaceDetails(PlaceDetails p){
		sauvegarder(p,"placeDetail"+idPlaceDetail); //todo gerer les noms uniques
	}

	/**
	 * Prendre un objet et le convertir en tableau de bits
	 * 
	 * @param obj l'objet dont on veut la représentation binaire
	 * @return le tableau de bits représentant l'objet
	 */
	public byte[] getBytes(Object obj) {
		ByteArrayOutputStream bos=null;
		ObjectOutputStream oos=null;
		byte [] data=null;
		try{
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			data=bos.toByteArray();
			oos.close();
			bos.close();
		}catch(Exception e){
			Toast.makeText(fileContext.getApplicationContext(), "Exception getBytes", Toast.LENGTH_LONG).show();
		}
		return data;
	}
	
	public Object lire(String nomFichier){
		Object res=null;
		try {
			FileInputStream fis=fileContext.openFileInput(nomFichier);
			ObjectInputStream is = new ObjectInputStream(fis);
			res= is.readObject();
			is.close();
			
		} catch (Exception e) {
			Toast.makeText(fileContext.getApplicationContext(),"exception a la lecture "+e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		return res;
	}
	/*
	public Lieu lire(){
		
	}
	*/
}
