package com.example.eyeway.fouilleDedonne;

import java.util.List;

// PAS ENCORE TEST
public class Lieu {
	
	private double latitude, longitude;
	private String icone;
	private String nom;
	private String reference;
	private String vicinity;
	private List<Photo> photos;
	private List<String> types;
	

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
	
	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getIcone() {
		return icone;
	}

	public String getNom() {
		return nom;
	}

	public String getReference() {
		return reference;
	}

	public String getVicinity() {
		return vicinity;
	}

	public List<Photo> getPhotos() {
		return photos;
	}

	public List<String> getTypes() {
		return types;
	}
	
	public String toString() {
		String toString="";
		toString+="-------------------\n"
				+"("+latitude+","+longitude+")\n"
				+"icone : "+icone+")\n"
				+"nom : "+nom+")\n"
				+"reference : "+reference+")\n"
				+"vicinity : "+vicinity+")\n"
				+"types : ";
		
		for(String type : types) {
			toString+=type+" ";
		}
		
		toString+="\n\n";
		for(Photo p : photos) {
			toString+=" h : "+p.getHeight()+" w : "+p.getWidth()+
					"\nphoto ref : "+p.getPhoto_reference()+"\n\n";
		}

		return toString;
	}
	
}
