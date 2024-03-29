package com.example.eyeway.fouilleDedonne;

import java.io.Serializable;

import com.google.api.client.util.Key;
 
/** Implement this class from "Serializable"
* So that you can pass this class Object to another using Intents
* Otherwise you can't pass to another actitivy
* */
public class PlaceDetails implements Serializable {
 
	private static final long serialVersionUID = -4377663422871979816L;

	@Key
    public String status;
 
    @Key
    public Lieu result;
    
    public String getId(){
    	return result.getId();
    }
    @Override
    public String toString() {
        if (result!=null) {
            return result.toString();
        }
        return super.toString();
    }
}