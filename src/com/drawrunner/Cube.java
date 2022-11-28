package com.drawrunner;
import processing.core.PApplet;

public class Cube implements Comparable<Object>{
	
	private PApplet p;
	
	private int posX;
	private int posY;
	private int posZ;
	
	private int taille;
	
	private Couleur couleur;
	
	private boolean vide;

	public Cube(PApplet p, int posX, int posY, int posZ, int taille, Couleur couleur) {
		this.p = p;
		
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		
		this.taille = taille;
		
		this.couleur = couleur;
		
		vide = false;
	}
	
	public void afficher(){
		p.translate(posX, posY, posZ);
		p.fill(couleur.getTeinte(), couleur.getSaturation(), couleur.getLuminosité());
		if(taille == Grille.TAILLE_CUBE_BAT)
			p.stroke(0, 10);
		else
			p.noStroke();
		p.box(taille);
		p.translate(-posX, -posY, -posZ);
	}
	
	public int compareTo(Object cubeTmp){
		int res = 0;
		
		Cube cubeCourant = (Cube) cubeTmp;
		
		if(posZ < cubeCourant.getPosZ()){
			res = -1;
		} else if(posZ == cubeCourant.getPosZ()){
			if(posY < cubeCourant.getPosY()){
				res = -1;
			} else if(posY == cubeCourant.getPosY()){
				if(posX < cubeCourant.getPosX()){
					res = -1;
				} else if(posX == cubeCourant.getPosX()){
					res = 0;
				} else {
					res = 1;
				}
			} else {
				res = 1;
			}
		} else {
			res = 1;
		}
		return res;
	}
	
	public boolean equals(Object cubeTmp) {
		Cube other = (Cube) cubeTmp;
		return (posX == other.posX && posY == other.posY && posZ == other.posZ);
	}
	
	public int getPosX() {
		return posX;
	}
	public void setPosX(int posX) {
		this.posX = posX;
	}
	public int getPosY() {
		return posY;
	}
	public void setPosY(int posY) {
		this.posY = posY;
	}
	public int getPosZ() {
		return posZ;
	}
	public void setPosZ(int posZ) {
		this.posZ = posZ;
	}
	
	public void setCouleur(Couleur couleur) {
		this.couleur = couleur;
	}

	public boolean isVide() {
		return vide;
	}
	public void setVide(boolean vide) {
		this.vide = vide;
	}

	public Couleur getCouleur() {
		return couleur;
	}
	public String toString() {
		return "Cube [posX=" + posX + ", posY=" + posY + ", posZ=" + posZ + "]";
	}
}