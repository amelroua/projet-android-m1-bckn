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
	 * @param latitude (parametre requis)
	 * @param longitude (parametre requis)
	 * @param distance (parametre requis)
	 * @param type de batiment (parametre facultatif)
	 * @param distance en metre (parametre facultatif)
	 * @return une liste de Lieu
	 */
	//Exemple d'une requete qui marche : les restaurants près du Courtepaille : lat =47.8686030 lng =1.9124340 

	public List<Lieu> getLieuProximiteParType(double lat,double lng,String type,int distance) {
		//Url pour la requête
		String url = "https://maps.googleapis.com/maps/api/place/search/json?location="+lat+","+lng+"&radius="+distance;
		if(type!=""){
			url+="&types="+type; 
		}
		completePlaceQuery(url);
		String reponse = executeQuery(url);
		return JsonToLieu(reponse);
	}


	/**
	 * Text Search Requests : recherche d'un lieu grace a des mots clés
	 * match query avec n'importe quel champs
	 * Ex : pizza in New York
	 * @param query : les mots clés (parametre requis)
	 * @param latitude A quoi sert ce parametre ici ?
	 * @param longitude A quoi sert ce parametre ici ?
	 * @param distance en metre A quoi sert ce parametre ici ?
	 * @return une liste de Lieu 
	 */
	//Exemple d'une requete qui marche : les restaurants d'olivet
	//https://maps.googleapis.com/maps/api/place/textsearch/json?query=restaurant+olivet&sensor=true&language=fr&key=AIzaSyDjWK46sXjISDvz38EsP0N-YegOAU_I0Cs
	public List<Lieu> getLieuParRecherche(String query,double lat,double lng, int distance) {
		String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query="+query;
		if(lat!=0 && lng !=0){
			url+="&location="+lat+","+lng;
		}
		if(distance!=0){
			url+="&radius="+distance;
		}
		completePlaceQuery(url);
		String reponse = executeQuery(url);
		return JsonToLieu(reponse);
	}

	//il nous faut une classe qui contienne les champs de l'adresse tels que numero de rue, nom de la rue...
	/*
	 * J'ai hésité a utiliser reverse geocoding pour faire ceci mais il ne faut pas : reverse geocoding fais une approximation a partir des coordonnées et renvoie plusieurs résultats
	 * alors que place/details travail sur la référence (qui est unique) et ne retourne qu'un resultat
	 */
	/**
	 * Demande les details du lieu
	 * @param reference : l'identifiant unique du lieu
	 */
	//Exemple de requete qui marche : https://maps.googleapis.com/maps/api/place/details/json?reference=CpQBggAAAGAqhZ-mEBAbbEvpYxwLkfs268DA44qO4IIISsKMjFodvHpu_eEdoefg3sn9g-nRwUo6Uc2XcIXZ4uJlq6-LlkzalDfcOn6XLwboK-x53pWyQDowTzGyj6HXJSUATDK0_pgxRXM6hKjKpYmZHERQ9LTwuXz3A4jlvCv1nuZ2klI3jlitoQgUk2A1AqMUNFybSBIQQWJrTEvNEKOOE0kZZwDoOxoUU2jguW8ph6uwfincnrSd6VK_Img&sensor=true&language=fr&key=AIzaSyDjWK46sXjISDvz38EsP0N-YegOAU_I0Cs
	public DetailLieu getDetails(String reference) {
		DetailLieu d;
		String url = "https://maps.googleapis.com/maps/api/place/details/json?reference="+reference;
		completePlaceQuery(url);
		String reponse = executeQuery(url);
		d=parseDetailResult(reponse);
		return d;
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
			String icon="";
			String id="";
			String name="";
			String reference="";
			String vicinity="";

			for(int i=0;i<results.length();i++) {
				//Un résultat
				result=results.getJSONObject(i);

				//Position GPS
				location=result.getJSONObject("geometry").getJSONObject("location");
				lat=location.getLong("lat");
				lng=location.getLong("lng");

				//L'icone
				if(result.has("icon")){
					icon=result.getString("icon");
				}
				//ID
				if(result.has("id")){
					id=result.getString("id");
				}
				//Le nom
				if(result.has("name")){
					name=result.getString("name");
				}
				//reference
				if(result.has("reference")){
					reference=result.getString("reference");
				}
				//vicinity
				if(result.has("vicinity")){
					vicinity=result.getString("vicinity");
				}
				//Les photos
				ArrayList <Photo> photos=new ArrayList<Photo>();
				if(result.has("photos")){
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
				}
				//les types
				ArrayList <String> types = new ArrayList<String>();
				if(result.has("types")){
					jTypes = result.getJSONArray("types");
					for(int t=0;t<jTypes.length();t++) {
						types.add(jTypes.getString(t));
					}
				}
				lesLieux.add(new Lieu(lat, lng, icon, id, name, photos, reference, types, vicinity));
			}		
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		return lesLieux;
	}
	/**
	 * Parse un Json en un LieuDetaille
	 * @param jsonString
	 * @return LieuDetaille
	 */
	private DetailLieu parseDetailResult(String jsonString) {

		try {
			JSONObject jObject = new JSONObject(jsonString);
			JSONObject result = jObject.getJSONObject("result");
			JSONArray address_components = result.getJSONArray("address_components"); 
			String street_number=""; //707
			String route=""; //Avenue de Verdun
			String locality=""; //Olivet
			String administrative_area_level_2=""; //Loiret
			String administrative_area_level_1=""; //Centre
			String country=""; //FR
			String postal_code=""; //45160
			String phone_number="";
			//Pour chaque entrée dans le tableau : 
			for(int i=0;i<address_components.length();i++) {
				//
				JSONArray types = result.getJSONArray("types");
				String type=types.getString(0);
				if(type=="street_number"){
					street_number=address_components.getJSONObject(i).getString("long_name");
				}else if(type=="route"){
					route=address_components.getJSONObject(i).getString("long_name");
				}else if(type=="locality"){
					locality=address_components.getJSONObject(i).getString("long_name");
				}else if(type=="administrative_area_level_2"){
					administrative_area_level_2=address_components.getJSONObject(i).getString("long_name");
				}else if(type=="administrative_area_level_1"){
					administrative_area_level_1=address_components.getJSONObject(i).getString("long_name");
				}
				else if(type=="country"){
					country=address_components.getJSONObject(i).getString("long_name");
				}else if(type=="postal_code"){
					postal_code=address_components.getJSONObject(i).getString("long_name");
				}else if(type=="phone_number"){
					phone_number=address_components.getJSONObject(i).getString("long_name");
				}
			}
			return new DetailLieu( street_number, route,  locality,  administrative_area_level_2,  administrative_area_level_1, country,  postal_code,  phone_number);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void completePlaceQuery(String url){
		//Ajout de la APIKey, du sensor, de la langue
		url+="&sensor=true&language=fr&key="+PLACE_APIKEY;
	}


	/**
	 * Faire la requete sur google maps api
	 * @param url
	 * @return json résultat
	 */
	private String executeQuery(String url) {
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

	public static void main(String[] args) {
		
		FouilleDonnee fd=new FouilleDonnee();
		/*
		List<Lieu> Lieux = fd.getLieuProximite(47.845489, 1.939776, 10);
		for(Lieu l : Lieux) {
			l.toString();
		}
		*/
		
		DetailLieu d=fd.getDetails("https://maps.googleapis.com/maps/api/place/details/json?reference=CpQBggAAAGAqhZ-mEBAbbEvpYxwLkfs268DA44qO4IIISsKMjFodvHpu_eEdoefg3sn9g-nRwUo6Uc2XcIXZ4uJlq6-LlkzalDfcOn6XLwboK-x53pWyQDowTzGyj6HXJSUATDK0_pgxRXM6hKjKpYmZHERQ9LTwuXz3A4jlvCv1nuZ2klI3jlitoQgUk2A1AqMUNFybSBIQQWJrTEvNEKOOE0kZZwDoOxoUU2jguW8ph6uwfincnrSd6VK_Img&sensor=true&language=fr&key=AIzaSyDjWK46sXjISDvz38EsP0N-YegOAU_I0Cs");
		System.out.println(d);
	}
}
