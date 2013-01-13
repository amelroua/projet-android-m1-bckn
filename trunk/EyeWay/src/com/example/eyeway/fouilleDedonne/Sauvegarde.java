package com.example.eyeway.fouilleDedonne;

import java.util.ArrayList;

import com.example.eyeway.R;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

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

public class Sauvegarde implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ArrayList<String> nomLieuxEnregistres=new ArrayList<String>();
	private static ArrayList<String> nomPlaceDetailsEnregistres=new ArrayList<String>();

	Context fileContext; //nécessaire pour fileContext.openFileOutput
	private static int lastIdLieu=0;
	private static int lastIdPlaceDetail=0;
	
	//private static Sauvegarde instance=null;
	
	//impossible de faire le pattern singleton ...
	public Sauvegarde(Context fileContext){
		this.fileContext=fileContext;
	}

	public void sauvegarderLieu(Lieu l){
		FileOutputStream fos=null;
		try{
			String nomLieu=l.getNom();
			fos = fileContext.openFileOutput(nomLieu, Context.MODE_PRIVATE);
			byte[] b=null;
			b=getBytes(l);
			fos.write(b);
			fos.close();
			Toast.makeText(fileContext.getApplicationContext(), "Point d'intêret ajouté", Toast.LENGTH_SHORT).show();
			nomLieuxEnregistres.add(nomLieu);
			lastIdLieu++;
		}catch(Exception e){
			Toast.makeText(fileContext.getApplicationContext(), "Exception save", Toast.LENGTH_SHORT).show();
		}
	}
	public void sauvegarderPlaceDetails(PlaceDetails p){
		FileOutputStream fos=null;
		try{
			String nomPlaceDetails="placeDetail"+lastIdPlaceDetail;
			fos = fileContext.openFileOutput(nomPlaceDetails, Context.MODE_PRIVATE);
			byte[] b=null;
			b=getBytes(p);
			fos.write(b);
			fos.close();
			Toast.makeText(fileContext.getApplicationContext(), "Point d'intêret ajouté", Toast.LENGTH_SHORT).show();
			nomLieuxEnregistres.add(nomPlaceDetails);
		}catch(Exception e){
			Toast.makeText(fileContext.getApplicationContext(), "Exception save", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Prendre un objet et le convertir en tableau de bits
	 * 
	 * @param obj l'objet dont on veut la représentation binaire
	 * @return le tableau de bits représentant l'objet
	 */
	private byte[] getBytes(Object obj) {
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

	private  Object lire(String nomFichier){
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

	private Lieu lireLieu(String nomLieu){
		Lieu l=(Lieu)lire(nomLieu);
		return l;
	}

	private PlaceDetails lirePlaceDetails(String nomPlaceDetail){
		PlaceDetails p=(PlaceDetails)lire(nomPlaceDetail);
		return p;
	}

	public ArrayList<Lieu> getLieuxEnregistres(){
		ArrayList<Lieu> res=new ArrayList<Lieu>();
		for(int i=0; i<nomLieuxEnregistres.size(); i++){
			Lieu l = lireLieu(nomLieuxEnregistres.get(i));
			res.add(l);
		}
		return res;
	}
	public ArrayList<PlaceDetails> getPlaceDetailsEnregistres(){
		ArrayList<PlaceDetails> res=new ArrayList<PlaceDetails>();
		for(int i=0; i<nomPlaceDetailsEnregistres.size(); i++){
			PlaceDetails p = lirePlaceDetails(nomPlaceDetailsEnregistres.get(i));
			res.add(p);
		}
		return res;
	}

	public void supprimerLieu(Lieu l){
		ArrayList<Lieu> lieux=getLieuxEnregistres();
		for(int i=0; i<lieux.size();i++){
			if(lieux.get(i).equals(l)){
				fileContext.deleteFile(l.getNom());
				 supprimerNomFichierLieu(l);
				
			}
		}
	}
	public void supprimerNomFichierLieu(Lieu l){
		for(int i=0; i<nomLieuxEnregistres.size();i++){
			if(l.getNom().equals(nomLieuxEnregistres.get(i))){
				nomLieuxEnregistres.remove(i);
			}
		}
	}
	public Lieu getLieuParNom(String nom){
		ArrayList<Lieu> lieux=getLieuxEnregistres();
		for(int i=0; i<lieux.size();i++){
			if(nom.equals(lieux.get(i).getNom())){
				return lieux.get(i);
			}
		}
		return null;
	}
}
