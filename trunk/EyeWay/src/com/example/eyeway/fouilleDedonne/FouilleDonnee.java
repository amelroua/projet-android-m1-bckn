package com.example.eyeway.fouilleDedonne;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class FouilleDonnee {
	
	/*
	 * TODO :
	 * 
	 * - parse le json de retour (en objet lieu ?)
	 * - Améliorer le code ...
	 */
	
	private static String PLACE_APIKEY="AIzaSyDjWK46sXjISDvz38EsP0N-YegOAU_I0Cs";
	

	/**
	 * Nearby Search Requests de type
	 * 
	 * récupere un json des lieux de type 
	 * et au environ de distance autour du point ( lat, lng)
	 * @param latitude
	 * @param longitude
	 * @param type de batiement
	 * @param distance en metre
	 * @return 
	 * @return une liste de Lieu
	 */
	public List<Lieu> getLieuProximiteParType(double lat,double lng,String type,int distance) {

		distance=10;
		//Url pour la requête
		String url = "https://maps.googleapis.com/maps/api/place/search/json?"+
				"location="+lat+","+lng+
				"&radius="+distance+
				"&types="+type +
				"&slanguage=fr" +
				"&sensor=true"; // Utilisation du GPS -> true

		
		String reponse = queryPlace(url);
		return JsonToLieu(reponse);
	}
	/**
	 * Nearby Search Requests de tous type
	 * 
	 * récupere un json des lieux
	 * et au environ de distance autour du point ( lat, lng)
	 * @param latitude
	 * @param longitude
	 * @param distance en metre
	 * @return 
	 * @return une liste de Lieu
	 */
	public List<Lieu> getLieuProximite(double lat,double lng,int distance) {

		distance=10;
		//Url pour la requête
		String url = "https://maps.googleapis.com/maps/api/place/search/json?"+
				"location="+lat+","+lng+
				"&radius="+distance+
				"&language=fr" +
				"&sensor=true"; // Utilisation du GPS -> true

		
		String reponse = queryPlace(url);
		return JsonToLieu(reponse);
	}
	
	/**
	 * Text Search Requests
	 * match query avec n'importe quel champs
	 * Ex : pizza in New York
	 * 
	 * @param latitude
	 * @param longitude
	 * @param question
	 * @param distance en metre
	 * @return une liste de Lieu
	 */
	public List<Lieu> getLieuParRecherche(double lat,double lng,String query, int distance) {
		String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?"+
				"query="+query+
				"&location="+lat+","+lng+
				"&radius="+distance+
				"&sensor=true"+
				"&language=fr";
		
		String reponse = queryPlace(url);
		return JsonToLieu(reponse);
	}
	
	/**
	 * Demande les details du lieu
	 * @param reference
	 */
	public void getDetails(String reference) {
		String url = "https://maps.googleapis.com/maps/api/place/details/json?" +
				"reference="+reference+
				"&sensor=true"+
				"&language=fr";
		
		String reponse = queryPlace(url);
	}
	/**
	 * Parse un Json en une liste de lieu
	 * @param jsonString
	 * @return liste de Lieu
	 */
	private List<Lieu> JsonToLieu(String jsonString) {
		List<Lieu> lesLieux=new ArrayList<Lieu>();
		try {
			JSONObject jObject = new JSONObject(jsonString);
			JSONArray results = jObject.getJSONArray("results"); 
			JSONObject result, location, laPhoto;
			JSONArray jPhotos,jTypes;
			
			double lat,lng;
			String icon,id,name,reference,vicinity;


			for(int i=0;i<results.length();i++) {
				//Un résultat
				result=results.getJSONObject(i);
				
				//Position GPS
				location=result.getJSONObject("geometry")
						.getJSONObject("location");
				lat=location.getLong("lat");
				lng=location.getLong("lng");
				
				//L'icone
				icon=result.getString("icon");
				//ID
				id=result.getString("id");
				//Le nom
				name=result.getString("name");
				//reference
				reference=result.getString("reference");
				//vicinity
				vicinity=result.getString("vicinity");
				
			
		
				//Les photos
				List <Photo> photos=new ArrayList<Photo>();
				jPhotos = result.getJSONArray("photos");
				for(int p=0;p<jPhotos.length();p++) {
					laPhoto=jPhotos.getJSONObject(i);
					String photo_reference;
					int height,width;
					height=laPhoto.getInt("height");
					width=laPhoto.getInt("width");
					photo_reference=laPhoto.getString("photo_reference");
					
					photos.add(new Photo(photo_reference, height, width));
				}
				//les types
				List <String> types = new ArrayList<String>();
				jTypes = result.getJSONArray("types");
				for(int t=0;t<jTypes.length();t++) {
					types.add(jTypes.getString(t));
				}
				
				lesLieux.add(new Lieu(lat, lng, icon, id, name, photos, reference, types, vicinity));
			}		
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		return lesLieux;
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
	
//	public static void main(String[] args) {
//		FouilleDonnee fd=new FouilleDonnee();
//		List<Lieu> Lieux = fd.getLieuProximite(47.845489, 1.939776, 10);
//		for(Lieu l : Lieux) {
//			l.toString();
//		}
//	}
}
