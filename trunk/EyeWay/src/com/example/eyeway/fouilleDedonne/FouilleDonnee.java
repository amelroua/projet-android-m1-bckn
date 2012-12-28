package com.example.eyeway.fouilleDedonne;

import java.io.IOException;
import java.util.ArrayList;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import android.util.Log;

public class FouilleDonnee {
	//Attention si ce tableau est modifié, il faut modifier aussi le tableau des string en francais aussi
	//et garder le meme ordre dans les deux tableaux

	/** Global instance of the HTTP transport. */
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	private static String PLACE_APIKEY="AIzaSyDjWK46sXjISDvz38EsP0N-YegOAU_I0Cs";

	private static final String PLACES_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";
	private static final String PLACES_TEXT_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?";
	private static final String PLACES_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json?";

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


	/****************************
	 * UTIL 
	 * **************************/

	//	private ArrayList<String> convertirArrayListTypes(ArrayList<String> array){
	//
	//		for(int i=0; i<array.size(); i++){
	//			array.set(i,stringTypeLieuCorrespondant(array.get(i)));
	//		}
	//		return array;
	//	}
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

	private ArrayList<String> FrancaisToApi(ArrayList<String> types) {
		ArrayList<String> result = new ArrayList<String>();
		for(String s : types) {
			result.add(stringTypeLieuCorrespondant(s));
		}
		return result;
	}

	/**
	 * Ajout de la APIKey, du sensor, de la langue. INDISPENSABLE
	 * @param genericUrl non complétée
	 */
	private void completePlaceQuery(GenericUrl Url){
		Url.put("sensor", "true");
		Url.put("language", "fr");	
		Url.put("key", PLACE_APIKEY);
	}


	/**
	 * Formatte la liste de type afin de l'utiliser lors de la requête
	 * @param liste de types
	 * @return type1|type2|...
	 */
	String typesFormatUrl(ArrayList<String> types){
		String queryFormated="";
		for(int i=0; i<types.size(); i++){
			queryFormated+=types.get(i)+"|";
		}
		queryFormated=queryFormated.substring(0, queryFormated.length()-1);
		return queryFormated;
	}


	String chaineFormatUrl(String query){
		/*
		 * construire la chaine qui va etre mise dans l'url
		 * parce que la chaine en paremetre est le texte saisi par l'utilisateur : ex : restaurant olivet
		 * et dans l'url on doit mettre restaurant+olivet
		 * 
		 */
		return query.replace(' ', '+');
	}

	/**
	 * Creating http request Factory
	 * */
	public static HttpRequestFactory createRequestFactory(
			HttpTransport transport) {
		return transport.createRequestFactory(new HttpRequestInitializer() {
					public void initialize(HttpRequest request) {
						GoogleHeaders headers = new GoogleHeaders();
						headers.setApplicationName("EyeWay");
						headers.put("Content-Type", "application/json");
						request.setHeaders(headers);
	
						
						JsonObjectParser parser = new JsonObjectParser(new JacksonFactory());
			            request.setParser(parser);

					}
		});
	}

	/****************************
	 * GOOGLE
	 * **************************/



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
	public ListeLieu getLieuProximiteParType(double lat,double lng,ArrayList<String> types,int distance) {
		/*
		 * Exemple d'une requete qui marche : les restaurants près du Courtepaille : lat =47.8686030 lng =1.9124340 
		 * https://maps.googleapis.com/maps/api/place/search/json?location=47.8686030,1.9124340&radius=100&sensor=true&language=fr&key=AIzaSyDjWK46sXjISDvz38EsP0N-YegOAU_I0Cs
		 * Testé , ok		
		 */
		try {

			HttpRequestFactory httpRequestFactory = createRequestFactory(HTTP_TRANSPORT);
			HttpRequest request = httpRequestFactory
					.buildGetRequest(new GenericUrl(PLACES_SEARCH_URL));


			request.getUrl().put("location", lat + "," + lng);
			request.getUrl().put("radius", distance); // in meters

			if(types != null) {
				types=FrancaisToApi(types);
				request.getUrl().put("types", typesFormatUrl(types));
			}

			completePlaceQuery(request.getUrl());

			ListeLieu list = request.execute().parseAs(ListeLieu.class);

			// Max de 60 resultats
			while(list.next_page_token!=null || list.next_page_token!=""){
				Thread.sleep(4000);
				/*Since the token can be used after a short time it has been  generated*/
				request.getUrl().put("pagetoken",list.next_page_token);
				ListeLieu temp = request.execute().parseAs(ListeLieu.class);
				list.results.addAll(temp.results);
			}
			return list;

		} catch (Exception e) {
			Log.e("Error:", e.getMessage());
			return null;
		}

	}


	/**
	 * Text Search Requests : recherche d'un lieu grace a des mots clés
	 * match query avec n'importe quel champs
	 * Ex : pizza in New York
	 * @param query : les mots clés (parametre requis)
	 * @return une liste de Lieu 
	 */
	public ListeLieu getLieuParRecherche(String query) { 
		/*
		 * Exemple d'une requete qui marche : les restaurants d'olivet
		 * https://maps.googleapis.com/maps/api/place/textsearch/json?query=restaurant+olivet&sensor=true&language=fr&key=AIzaSyDjWK46sXjISDvz38EsP0N-YegOAU_I0Cs
		 */		

		try {

			HttpRequestFactory httpRequestFactory = createRequestFactory(HTTP_TRANSPORT);
			HttpRequest request = httpRequestFactory
					.buildGetRequest(new GenericUrl(PLACES_TEXT_SEARCH_URL));

			query=chaineFormatUrl(query);
			
			request.getUrl().put("query", query);

			completePlaceQuery(request.getUrl());
			
			
				
			ListeLieu list = request.execute().parseAs(ListeLieu.class);

			return list;

		} catch (HttpResponseException e) {
            Log.e("Error:", e.getMessage());
        } catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
	}


	/**
	 * Demande les details du lieu
	 * @param reference : l'identifiant unique du lieu
	 */
	public PlaceDetails getDetails(String reference) {
		/* il nous faut une classe qui contienne les champs de l'adresse tels que numero de rue, nom de la rue...
		 *
		 * J'ai hésité a utiliser reverse geocoding pour faire ceci mais il ne faut pas : reverse geocoding fais une approximation a partir des coordonnées et renvoie plusieurs résultats
		 * alors que place/details travail sur la référence (qui est unique) et ne retourne qu'un resultat
		 *
		 * Exemple de requete qui marche : 
		 * https://maps.googleapis.com/maps/api/place/details/json?reference=CpQBggAAAGAqhZ-mEBAbbEvpYxwLkfs268DA44qO4IIISsKMjFodvHpu_eEdoefg3sn9g-nRwUo6Uc2XcIXZ4uJlq6-LlkzalDfcOn6XLwboK-x53pWyQDowTzGyj6HXJSUATDK0_pgxRXM6hKjKpYmZHERQ9LTwuXz3A4jlvCv1nuZ2klI3jlitoQgUk2A1AqMUNFybSBIQQWJrTEvNEKOOE0kZZwDoOxoUU2jguW8ph6uwfincnrSd6VK_Img&sensor=true&language=fr&key=AIzaSyDjWK46sXjISDvz38EsP0N-YegOAU_I0Cs
		 */
		try {

			HttpRequestFactory httpRequestFactory = createRequestFactory(HTTP_TRANSPORT);
			HttpRequest request = httpRequestFactory
					.buildGetRequest(new GenericUrl(PLACES_DETAILS_URL));


			request.getUrl().put("reference", reference);

			completePlaceQuery(request.getUrl());

			PlaceDetails place = request.execute().parseAs(PlaceDetails.class);

			return place;

		} catch (Exception e) {
			Log.e("Error in Perform Details", e.getMessage());
			return null;
		}
	}
}
