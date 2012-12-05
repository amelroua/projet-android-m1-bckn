package com.example.eyeway.fouilleDedonne;

import java.util.List;

// PAS ENCORE TEST
public class Lieu {
	
	double latitude, longitude;
	String icone;
	String nom;
	String reference;
	String vicinity;
	List<Photo> photos;
	List<String> types;
	
	public Lieu(double lat, double lng, String icon, String id, String name,
				List<Photo> pho, String ref, List<String> typ, String vic) {
		
		latitude=lat;
		longitude=lng;
		icone=icon;
		nom=name;
		reference=ref;
		vicinity=vic;
		photos=pho;
		types=typ;
	}
	
}
