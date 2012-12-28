package com.example.eyeway.fouilleDedonne;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
	//Attention si ce tableau est modifié, il faut modifier aussi le tableau des string en francais aussi
	//et garder le meme ordre dans les deux tableaux
	public static String [] types_place_api= {
		"bank",
		"bar",
		"bicycle_store",
		"book_store",
		"bowling_alley",
		"bus_station",
		"cafe",
		"car_repair",
		"car_wash",
		"clothing_store",
		"food",
		"grocery_or_supermarket",
		"gym",
		"hair_care",
		"hardware_store",
		"hospital",
		"insurance_agency",
		"library",
		"police",
		"post_office",
		"restaurant",
		"stadium",
		"store",
		"university"
	};
	public static String [] types_place_fr= {
		"Banque",
		"Bar",
		"Vendeur de Velos",
		"Libraire",
		"Bowling",
		"Arret de bus",
		"Cafe",
		"Garage",
		"Station de lavage auto",
		"Magasin de vetements",
		"A manger",
		"Commerce ou supermarché",
		"Salle de Gym",
		"Coiffeur",
		"Commerce de materiel informatique",
		"Hopital",
		"Assurance",
		"Bibliotheque",
		"Police",
		"Poste",
		"Restaurant",
		"Stade",
		"Magasin",
		"Université"
	};

	private ArrayList<String> convertirArrayListTypes(ArrayList<String> array){

		for(int i=0; i<array.size(); i++){
			array.set(i,stringTypeLieuCorrespondant(array.get(i)));
		}
		return array;
	}
	/**
	 * @param type_francais : par exemple "Université"
	 * @return  le type correspondant dans l'autre tableau, par exemple : "University"
	 */
	private String stringTypeLieuCorrespondant(String type_francais){
		String res="";
		int indiceChaine=-1;
		for(int i=0;i<types_place_fr.length; i++){
			if(types_place_fr[i].equalsIgnoreCase(type_francais)){
				indiceChaine=i;
			}
		}
		if(indiceChaine!=-1){
			res=types_place_api[indiceChaine];
		}
		return res;
	}

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
	//https://maps.googleapis.com/maps/api/place/search/json?location=47.8686030,1.9124340&radius=100&sensor=true&language=fr&key=AIzaSyDjWK46sXjISDvz38EsP0N-YegOAU_I0Cs
	//Testé , ok
	public ArrayList<Lieu> getLieuProximiteParType(double lat,double lng,ArrayList<String> types,int distance) {
		//Url pour la requête
		types=convertirArrayListTypes(types);
		String url = "https://maps.googleapis.com/maps/api/place/search/json?location="+lat+","+lng+"&radius="+distance;
		String types_format_url="";
		if(types.size()!=0){
			types_format_url="&types="+typesFormatUrl(types);
			url+=types_format_url;
		}
		url=completePlaceQuery(url);
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
	public ArrayList<Lieu> getLieuParRecherche(String query) { //,double lat,double lng, int distance
		String queryFormated=chaineFormatUrl(query);
		//enlever le + du dernier parametre

		String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query="+queryFormated;
		/*
		if(lat!=0 && lng !=0){
			url+="&location="+lat+","+lng;
		}
		if(distance!=0){
			url+="&radius="+distance;
		}*/
		url=completePlaceQuery(url);
		String reponse = executeQuery(url);
		Log.d("Reponse", reponse);
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

		String url = "https://maps.googleapis.com/maps/api/place/details/json?reference="+reference;
		url=completePlaceQuery(url);
		String reponse = executeQuery(url);
		DetailLieu d=parseDetailResult(reponse);
		return d;
	}
	/**
	 * Parse un Json en une liste de lieu
	 * @param jsonString
	 * @return liste de Lieu
	 */
	private ArrayList<Lieu> JsonToLieu(String jsonString) {
		ArrayList<Lieu> lesLieux=new ArrayList<Lieu>();
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
			
			Log.d("taille",""+	 results.length());

			for(int i=0;i<results.length();i++) {

				//Un résultat
				result=results.getJSONObject(i);

				//Position GPS
				location=result.getJSONObject("geometry").getJSONObject("location");

				lat=location.getDouble("lat");
				lng=location.getDouble("lng");

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
						laPhoto=jPhotos.getJSONObject(p);
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
			Log.d("Il y a une erreur",e.toString());
			e.printStackTrace();
		} 


		return lesLieux;
	}
	/**
	 * Parse un Json en un LieuDetaille
	 * @param jsonString
	 * @return LieuDetaille
	 */
	//Testée avec succès avec cette requete : https://maps.googleapis.com/maps/api/place/details/json?reference=CpQBggAAAGAqhZ-mEBAbbEvpYxwLkfs268DA44qO4IIISsKMjFodvHpu_eEdoefg3sn9g-nRwUo6Uc2XcIXZ4uJlq6-LlkzalDfcOn6XLwboK-x53pWyQDowTzGyj6HXJSUATDK0_pgxRXM6hKjKpYmZHERQ9LTwuXz3A4jlvCv1nuZ2klI3jlitoQgUk2A1AqMUNFybSBIQQWJrTEvNEKOOE0kZZwDoOxoUU2jguW8ph6uwfincnrSd6VK_Img&sensor=true&language=fr&key=AIzaSyDjWK46sXjISDvz38EsP0N-YegOAU_I0Cs
	private DetailLieu parseDetailResult(String jsonString) {
		Log.i("testFouille","debut de parseDetail");
		try {
			JSONObject jObject = new JSONObject(jsonString);
			JSONObject result = jObject.getJSONObject("result"); //cette instruction bug , pkoi ?
			Log.i("testFouille","apres recup result");
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
				//Java est capricieux et ne veux pas retourner vrai avec une comparaison de string == , obliger de faire .equals
				JSONObject un_composant=address_components.getJSONObject(i);
				JSONArray types = un_composant.getJSONArray("types");
				String type=types.getString(0);
				if(type.equals(new String("street_number"))){
					street_number=address_components.getJSONObject(i).getString("long_name");
				}else if(type.equals(new String("route"))){
					route=address_components.getJSONObject(i).getString("long_name");
				}else if(type.equals(new String("locality"))){
					locality=address_components.getJSONObject(i).getString("long_name");
				}else if(type.equals(new String("administrative_area_level_2"))){
					administrative_area_level_2=address_components.getJSONObject(i).getString("long_name");
				}else if(type.equals(new String("administrative_area_level_1"))){
					administrative_area_level_1=address_components.getJSONObject(i).getString("long_name");
				}
				else if(type.equals(new String("country"))){
					country=address_components.getJSONObject(i).getString("long_name");
				}else if(type.equals(new String("postal_code"))){
					postal_code=address_components.getJSONObject(i).getString("long_name");
				}else if(type.equals(new String("phone_number"))){
					phone_number=address_components.getJSONObject(i).getString("long_name");
				}
			}
			return new DetailLieu(street_number, route,  locality,  administrative_area_level_2,  administrative_area_level_1, country,  postal_code,  phone_number);

		} catch (JSONException e) {
			Log.i("testFouille", "Exeception recuperee "+e.getMessage()+" , cause : "+e.getCause());
			e.printStackTrace();
		}
		return null;
	}
	/****************************
	 * UTIL 
	 * **************************/
	private String completePlaceQuery(String url){
		//Ajout de la APIKey, du sensor, de la langue
		return url+="&sensor=true&language=fr&key="+PLACE_APIKEY;
	}
	String typesFormatUrl(ArrayList<String> types){
		String queryFormated="";
		for(int i=0; i<types.size(); i++){
			queryFormated+=types.get(i)+"+";
		}
		queryFormated=queryFormated.substring(0, queryFormated.length()-1);
		return queryFormated;
	}
	String chaineFormatUrl(String query){
		//construire la chaine qui va etre mise dans l'url
		//parce que la chaine en paremetre est le texte saisi par l'utilisateur : ex : restaurant olivet
		//et dans l'url on doit mettre restaurant+olivet
		String queryFormated="";
		Scanner s = new Scanner(query).useDelimiter(" ");
		//TODO trouver le pattern qui reconnait un espace entre deux mots
		while(s.hasNext()){
			queryFormated+=s.next()+"+";
		}
		queryFormated=queryFormated.substring(0, queryFormated.length()-1);
		return queryFormated;
	}

	/**
	 * Faire la requete sur google maps api
	 * @param url
	 * @return json résultat
	 */
	public String executeQuery(String url) {
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
