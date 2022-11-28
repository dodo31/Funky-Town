package com.drawrunner;
public abstract class Composant {
	
	protected int posX1;
	protected int posX2;
	
	protected int posY1;
	protected int posY2;
	
	public Composant(int posX1, int posX2, int posY1, int posY2) {
		this.posX1 = posX1;
		this.posX2 = posX2;
		
		this.posY1 = posY1;
		this.posY2 = posY2;
	}
	
	public int getPosX1() {
		return posX1;
	}
	public void setPosX1(int posX1) {
		this.posX1 = posX1;
	}
	public int getPosX2() {
		return posX2;
	}
	public void setPosX2(int posX2) {
		this.posX2 = posX2;
	}
	
	public int getPosY1() {
		return posY1;
	}
	public void setPosY1(int posY1) {
		this.posY1 = posY1;
	}
	public int getPosY2() {
		return posY2;
	}
	public void setPosY2(int posY2) {
		this.posY2 = posY2;
	}
}
