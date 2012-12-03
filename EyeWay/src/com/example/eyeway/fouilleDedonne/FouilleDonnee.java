package com.example.eyeway.fouilleDedonne;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class FouilleDonnee {
	
	/*
	 * TODO :
	 * 
	 * - parse le json de retour (en objet lieu ?)
	 * - Améliorer le code ...
	 */
	
	public static String PLACE_APIKEY="AIzaSyDjWK46sXjISDvz38EsP0N-YegOAU_I0Cs";

	/**
	 * Nearby Search Requests
	 * 
	 * récupere un json des lieux de type @type 
	 * et au environ de @distance autour du point ( @lat, @lng)
	 * @param lat
	 * @param lng
	 * @param type
	 * @param distance en metre
	 */
	public void getLieuProximite(double lat,double lng,String type,int distance) {

		distance=10;
		//Url pour la requête
		String url = "https://maps.googleapis.com/maps/api/place/search/json?"+
				"location="+lat+","+lng+
				"&radius="+distance+
				"&types="+type +
				"language=fr" +
				"&sensor=true"; // Utilisation du GPS -> true

		
		queryPlace(url);
	}
	
	/**
	 * Text Search Requests
	 * match query avec n'importe quel champs
	 * 
	 * @param lat
	 * @param lng
	 * @param query
	 * @param distance
	 */
	public void getLieuParNom(double lat,double lng,String query, int distance) {
		String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?"+
				"query="+query+
				"&location="+lat+","+lng+
				"&radius="+distance+
				"&sensor=true"+
				"&language=fr";
		
		queryPlace(url);
	}
	
	/**
	 * Demande les details du lieu
	 * @param reference
	 */
	public void getDetails(String reference) {
		String url = "https://maps.googleapis.com/maps/api/place/details/json?" +
				"reference="+reference+
				"&sensor=true";
		
		queryPlace(url);
	}
	
	
	/**
	 * faire la requete sur google maps api
	 * @param url
	 * @return json résultat
	 */
	private String queryPlace(String url) {
		
		//Ajout de la APIKey
		url+="&key="+PLACE_APIKEY;
		
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			//200 pour dire que la requette s'est bien déroulée
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				Log.i(this.toString(), "Erreur de chargement");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
}
