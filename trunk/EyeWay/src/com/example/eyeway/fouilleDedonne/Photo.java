package com.example.eyeway.fouilleDedonne;

import java.io.Serializable;

import com.google.api.client.util.Key;

public class Photo implements Serializable {

	private static final long serialVersionUID = 3797759086960210431L;

	@Key
	public int height;
	
	@Key
	public int width;
	
	@Key
	public String photo_reference;
	
	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public String getPhoto_reference() {
		return photo_reference;
	}
}
