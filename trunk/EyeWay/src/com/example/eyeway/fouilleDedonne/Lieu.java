package com.example.eyeway.fouilleDedonne;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.provider.OpenableColumns;

import com.google.api.client.util.Key;

// PAS ENCORE TEST
//Il faut que cette classe soit serializable pour appliquer le writeObject() dessus
public class Lieu implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public Lieu(){

	}
	/**
	 * L utilisateur veut enregistrer sa position actuelle comme favoris
	 * Il saisis certains champs seulement, et l'id n'est pas connu
	 * @param name
	 * @param reference
	 * @param icon
	 * @param vicinity
	 * @param geometry
	 * @param formatted_address
	 */
	public Lieu(String name, String reference, String icon, String vicinity, Geometry geometry, String formatted_address,String phoneNumer,String website){
		id="";
		this.name=name;
		this.reference=reference;
		this.icon=icon;
		this.vicinity=vicinity;
		this.geometry=geometry;
		this.formatted_address=formatted_address;
		this.formatted_phone_number = phoneNumer;
		this.website = website;
	}

	@Key	
	public String id;

	@Key
	public String name;

	@Key
	public String reference;

	@Key
	public String icon;

	@Key
	public String vicinity;

	@Key
	public Geometry geometry;

	@Key
	public String formatted_address;

	/** DETAILS */
	@Key
	public String formatted_phone_number;

	@Key
	public String website;

	@Key
	public List<String> types ;

	@Override
	public String toString() {
		return id+" "+name+" "+icon;
	}

	public static class Geometry implements Serializable
	{
		private static final long serialVersionUID = -1846546423355113268L;
		
		@Key
		public MyLocation location;
		
		public Geometry(){
			
		}
		
		public Geometry(MyLocation location){
			this.location=location;
		}

	}

	public static class MyLocation implements Serializable
	{
		private static final long serialVersionUID = -745398283024148157L;
		
		@Key
		public double lat;

		@Key
		public double lng;
		
		public MyLocation(){
			
		}
		
		public MyLocation(double lat, double lng){
			this.lat=lat;
			this.lng=lng;
		}
		
		
	}



	public String getId() {
		return id;
	}

	public String getNom() {
		return name;
	}

	public String getReference() {
		return reference;
	}

	public String getIcon() {
		return icon;
	}

	public String getVicinity() {
		return vicinity;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public String getFormatted_address() {
		return formatted_address;
	}

	public String getFormatted_phone_number() {
		return formatted_phone_number;
	}


	public List<String> getTypes() {
		return this.types;
	}

	public double getLatitude() {
		return geometry.location.lat;
	}

	public double getLongitude() {
		return geometry.location.lng;
	}

	public String getWebsite() {
		return website;
	}

}
