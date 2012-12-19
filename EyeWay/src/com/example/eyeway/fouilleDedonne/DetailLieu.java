package com.example.eyeway.fouilleDedonne;

/**
 * Classe permettant le stockage des informations récupérées a la suite d'une requete "détails"
 * ex : https://maps.googleapis.com/maps/api/place/details/json?reference=CpQBggAAAGAqhZ-mEBAbbEvpYxwLkfs268DA44qO4IIISsKMjFodvHpu_eEdoefg3sn9g-nRwUo6Uc2XcIXZ4uJlq6-LlkzalDfcOn6XLwboK-x53pWyQDowTzGyj6HXJSUATDK0_pgxRXM6hKjKpYmZHERQ9LTwuXz3A4jlvCv1nuZ2klI3jlitoQgUk2A1AqMUNFybSBIQQWJrTEvNEKOOE0kZZwDoOxoUU2jguW8ph6uwfincnrSd6VK_Img&sensor=true&language=fr&key=AIzaSyDjWK46sXjISDvz38EsP0N-YegOAU_I0Cs
 * @author chris
 */
public class DetailLieu {
	private String street_number; //707
	private String route; //Avenue de Verdun
	private String locality; //Olivet
	private String administrative_area_level_2; //Loiret
	private String administrative_area_level_1; //Centre
	private String country; //FR
	private String postal_code; //45160
	private String phone_number;
	public DetailLieu(String _street_number,String _route, String _locality, String _administrative_area_level_2, String _administrative_area_level_1,String _country, String _postal_code, String _phone_number){
		street_number=_street_number;
		route=_route; 
		locality=_locality;
		administrative_area_level_2=_administrative_area_level_2;
		administrative_area_level_1=_administrative_area_level_1;
		country=_country; 
		postal_code=_postal_code;
		phone_number=_phone_number;
	}
	public String getStreet_number() {
		return street_number;
	}
	public String getRoute() {
		return route;
	}
	public String getLocality() {
		return locality;
	}
	public String getAdministrative_area_level_2() {
		return administrative_area_level_2;
	}
	public String getAdministrative_area_level_1() {
		return administrative_area_level_1;
	}
	public String getCountry() {
		return country;
	}
	public String getPostal_code() {
		return postal_code;
	}
	public String getPhone_number() {
		return phone_number;
	}

	@Override
	public String toString(){
		//707 Avenue de Verdun 45160 Olivet (Loiret, Centre) 02 .....
		String res="";
		res+=street_number+" "+route+","+postal_code+" "+locality+" ("+administrative_area_level_1+", "+administrative_area_level_2+")"+" "+phone_number;
		return res;
	}

}
