package com.example.eyeway.fouilleDedonne;

public class Photo {
	
	private int height;
	private int width;
	private String photo_reference;

	public Photo(String ref, int h,int w) {
		height=h;
		width=w;
		photo_reference=ref;
	}
	
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
