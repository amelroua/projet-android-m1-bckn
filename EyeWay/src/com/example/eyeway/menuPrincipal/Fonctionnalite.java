package com.example.eyeway.menuPrincipal;

public class Fonctionnalite {
	public int icon;
	public String title;
	
	public Fonctionnalite(){
		super();
	}

	public Fonctionnalite(int icon, String title) {
		super();
		this.icon = icon;
		this.title = title;
	}
	@Override
	public boolean equals(Object o) {
		Fonctionnalite f=(Fonctionnalite)o;
		return this.icon==f.icon && this.title.equals(f.title);
	}

}