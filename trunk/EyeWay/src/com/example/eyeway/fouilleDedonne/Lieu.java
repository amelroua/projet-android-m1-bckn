package com.example.eyeway.fouilleDedonne;

import java.io.Serializable;
import java.util.List;

import com.google.api.client.util.Key;

// PAS ENCORE TEST
public class Lieu {

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
	
	@Override
	public String toString() {
		return id+" "+name+" "+icon;
	}

	public static class Geometry implements Serializable
	{
		private static final long serialVersionUID = -1846546423355113268L;
		@Key
		public Location location;
	}

	public static class Location implements Serializable
	{
		private static final long serialVersionUID = -745398283024148157L;

		@Key
		public double lat;

		@Key
		public double lng;
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

	public List<Photo> getPhotos() {
		return null;
	}

	public List<String> getTypes() {
		return null;
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
