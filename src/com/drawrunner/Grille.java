package com.drawrunner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import processing.core.PApplet;

public class Grille {
	private static PApplet p;

	private static int posXref = Main.TAILLE_FENETRE_X/2;
	private static int posYref = Main.TAILLE_FENETRE_Y/2;
	private static int posZref = 0;
	
	private static int rotX;
	private static int rotY;
	private static int rotZ;
	
	private static float zoom = 1;
	
	public final static int TAILLE_CUBE_ROUTE = 2;
	public final static int TAILLE_CUBE_BAT = 4;

	public final static int TAILLE_GRILLE_X = 150;
	public final static int TAILLE_GRILLE_Y = 150;
	public final static int TAILLE_GRILLE_Z = 1;
	public final static int DIAGONALE = (int) Math.sqrt(Math.pow(TAILLE_GRILLE_X, 2) + Math.pow(TAILLE_GRILLE_Y, 2));
	
	public static final int LARGEUR_ARTERE_PRINCIPALE = 5;
	public static final int LARGEUR_AVENUE= 3;
	public static final int LARGEUR_RUE = 1;
	
	private static ArrayList<Cube> cubesComposants = new ArrayList<Cube>();
	private static ArrayList<Cube> cubes = new ArrayList<Cube>();
	private static ArrayList<Route> routes = new ArrayList<Route>();
	private static ArrayList<Batiment> batiments = new ArrayList<Batiment>();

	private static boolean affichageSol = false;
	
	private Grille(){}
	
	public static void genererRoutes(){
		System.out.println("Génération du plan...");
		
		ArrayList<Route> GrandesArteres = ConstructeurRoutes.arteresPrincipales();
		routes.addAll(GrandesArteres);
		
		ArrayList<Route> Avenues = ConstructeurRoutes.avenues();
		routes.addAll(Avenues);
		
		ArrayList<Route> Rues = ConstructeurRoutes.rues();
		routes.addAll(Rues);

		Collections.sort(routes);
		
		System.out.println("Plan généré avec succès : " + GrandesArteres.size() + " Grandes artères  |  " + Avenues.size() + " Avenues  |  " + Rues.size() + " Rues");
	}
	public static void injecterRoutes(){
		System.out.println("Injection des routes...");
		for (Iterator<Route> itRoutes = routes.iterator(); itRoutes.hasNext();) {
			Route route = itRoutes.next();
			for (int x = Math.min(route.getPosX1(), route.getPosX2()); x <= Math.max(route.getPosX1(), route.getPosX2()); x += 1) {
				for (int y = Math.min(route.getPosY1(), route.getPosY2()); y <= Math.max(route.getPosY1(), route.getPosY2()); y += 1) {
//					Collections.sort(cubesComposants);
					int posX = posXref + x * TAILLE_CUBE_ROUTE;
					int posY = posYref + y * TAILLE_CUBE_ROUTE;
//					if(!cubeCorrespondant(posX, posY, 0, TAILLE_CUBE_ROUTE)){
						cubesComposants.add(new Cube(p, posX, posY, 0, TAILLE_CUBE_ROUTE, new Couleur(36, 255, 255)));
//					}
				}
			}
		}
	}

	public static void genererBatiments(){
		System.out.println("Génération des batiments...");
		batiments.addAll(ConstructeurBatiments.batiments());
		System.out.println("Batiments générés avec succès : " + batiments.size() + " batiments.");
	}
	public static void injecterBatiments(){		
		System.out.println("Injection des batiments...");
		for (Iterator<Batiment> itBatiments = batiments.iterator(); itBatiments.hasNext();) {
			Batiment batiment = itBatiments.next();

			for (int x = Math.min(batiment.getPosX1(), batiment.getPosX2()); x <= Math.max(batiment.getPosX1(), batiment.getPosX2()); x += 1) {
				for (int y = Math.min(batiment.getPosY1(), batiment.getPosY2()); y <= Math.max(batiment.getPosY1(), batiment.getPosY2()); y += 1) {
					for (int z = 0; z <= Math.max(batiment.getPosZ1(), batiment.getPosZ2()); z += 1) {
						int posX = posXref + batiment.getPosX1() * TAILLE_CUBE_ROUTE + TAILLE_CUBE_ROUTE/2 + (x - batiment.getPosX1()) * TAILLE_CUBE_BAT;
						int posY = posYref + batiment.getPosY1() * TAILLE_CUBE_ROUTE + TAILLE_CUBE_ROUTE/2 + (y - batiment.getPosY1()) * TAILLE_CUBE_BAT;
						int posZ = posZref + batiment.getPosZ1() * TAILLE_CUBE_ROUTE + TAILLE_CUBE_ROUTE/2 + (z - batiment.getPosZ1()) * TAILLE_CUBE_BAT;
						int teinte = (int) (batiment.getCouleurRef().getTeinte()/* + 6 * Math.random() - 3*/);
						boolean allumé = (Math.random() > 0.2F && z != batiment.getPosZ2());
						int saturation = (allumé) ? 255 : 200;
						int luminosité = (allumé) ? 255 : 50;
						
						cubesComposants.add(new Cube(p, posX, posY, posZ, TAILLE_CUBE_BAT, new Couleur(teinte, saturation, luminosité)));
					}
				}
			}
		}
	}
	
	public static void genererSol(){
		System.out.println("Génération et injection du sol...");
		for (int x = 0; x < TAILLE_GRILLE_X; x += 1) {
			for (int y = 0; y < TAILLE_GRILLE_Y; y += 1) {
				if(!cubeCorrespondant(posXref + x * TAILLE_CUBE_ROUTE, posYref + y * TAILLE_CUBE_ROUTE, posZref + 0, TAILLE_CUBE_ROUTE))
					cubes.add(new Cube(p, posXref + x * TAILLE_CUBE_ROUTE, posYref + y * TAILLE_CUBE_ROUTE, posZref + 0, TAILLE_CUBE_ROUTE, new Couleur(0, 0, 20)));
			}
		}
		cubes.addAll(cubesComposants);
		System.out.println("\n	Ville générée !\n");
	}
	
	private static boolean cubeCorrespondant(int x, int y, int z, int tailleCube){
		if(cubesComposants.size() > 0)
			return Collections.binarySearch(cubesComposants, new Cube(p, x, y, z, tailleCube, null)) > -1;
		else 
			return false;
	}
	
	public static void reinitialiser(){
		cubes.clear();
		cubesComposants.clear();
		routes.clear();
		batiments.clear();
		
		genererRoutes();
		injecterRoutes();
		genererBatiments();
		injecterBatiments();
		genererSol();
	}
	
	public static void gérerEclairageEtCamera() {
		p.camera((posXref + (TAILLE_GRILLE_X * TAILLE_CUBE_ROUTE)) * zoom, (posYref + (TAILLE_GRILLE_Y * TAILLE_CUBE_ROUTE)) * zoom, 60 * zoom,
				 posXref + (TAILLE_GRILLE_X * TAILLE_CUBE_ROUTE)/2, posYref + (TAILLE_GRILLE_Y * TAILLE_CUBE_ROUTE)/2, posZref + (TAILLE_GRILLE_Z * TAILLE_CUBE_ROUTE)/2,
				 0F, 1F, 0F);	
//		p.pointLight(0, 0, 255, (posXref + (TAILLE_GRILLE_X * TAILLE_CUBE_ROUTE)) * 2, (posYref + (TAILLE_GRILLE_Y * TAILLE_CUBE_ROUTE)) * 2, 60 * 2);
		//p.directionalLight((posXref + (TAILLE_GRILLE_X * TAILLE_CUBE_ROUTE)) * 2, (posYref + (TAILLE_GRILLE_Y * TAILLE_CUBE_ROUTE)) * 2, 60 * 2,
			//	posXref + (TAILLE_GRILLE_X * TAILLE_CUBE_ROUTE)/2, posYref + (TAILLE_GRILLE_Y * TAILLE_CUBE_ROUTE)/2, posZref + 60);	
	}
	
	public static void afficherContour(){
		p.pushMatrix();
			p.translate(posXref + (TAILLE_GRILLE_X * TAILLE_CUBE_ROUTE)/2, posYref + (TAILLE_GRILLE_Y * TAILLE_CUBE_ROUTE)/2, (TAILLE_GRILLE_Z * TAILLE_CUBE_ROUTE)/2);
			p.rotateX(PApplet.radians(rotX + 43));
			p.rotateY(PApplet.radians(rotY + 70));
			p.rotateZ(PApplet.radians(rotZ + 30));
			p.translate(-(posXref + (TAILLE_GRILLE_X * TAILLE_CUBE_ROUTE)/2), -(posYref + (TAILLE_GRILLE_Y * TAILLE_CUBE_ROUTE)/2), -(TAILLE_GRILLE_Z * TAILLE_CUBE_ROUTE)/2);
		
			p.stroke(110, 255, 255);
			p.noFill();
			p.rect(posXref, posYref, TAILLE_GRILLE_X * TAILLE_CUBE_ROUTE, TAILLE_GRILLE_Y * TAILLE_CUBE_ROUTE);
			p.stroke(0);
		p.popMatrix();
	}
	
	public static void afficherCubes(){
		p.camera((posXref + (TAILLE_GRILLE_X * TAILLE_CUBE_ROUTE)) * zoom, (posYref + (TAILLE_GRILLE_Y * TAILLE_CUBE_ROUTE)) * zoom, 60 * zoom,
				 posXref + (TAILLE_GRILLE_X * TAILLE_CUBE_ROUTE)/2, posYref + (TAILLE_GRILLE_Y * TAILLE_CUBE_ROUTE)/2, posZref + (TAILLE_GRILLE_Z * TAILLE_CUBE_ROUTE)/2,
				 0F, 1F, 0F);
		
		p.pushMatrix();
			p.translate(posXref + (TAILLE_GRILLE_X * TAILLE_CUBE_ROUTE)/2, posYref + (TAILLE_GRILLE_Y * TAILLE_CUBE_ROUTE)/2, (TAILLE_GRILLE_Z * TAILLE_CUBE_ROUTE)/2);
			p.rotateX(PApplet.radians(rotX + 43));
			p.rotateY(PApplet.radians(rotY + 70));
			p.rotateZ(PApplet.radians(rotZ + 30));
			p.translate(-(posXref + (TAILLE_GRILLE_X * TAILLE_CUBE_ROUTE)/2), -(posYref + (TAILLE_GRILLE_Y * TAILLE_CUBE_ROUTE)/2), -(TAILLE_GRILLE_Z * TAILLE_CUBE_ROUTE)/2);
			
			for (Iterator<Cube> itCubes = cubes.iterator(); itCubes.hasNext();) {
				Cube cube = itCubes.next();
				if(!cube.isVide() && !(!affichageSol && cube.getCouleur().getSaturation() == 0 && cube.getCouleur().getLuminosité() == 20))
					cube.afficher();
			}

		p.popMatrix();
	}
	
	public static void gererRotation() {
		if (p.keyPressed) {
			if (p.key == 'z')
				rotX++;
			if (p.key == 's')
				rotX--;
			if (p.key == 'e')
				rotY++;
			if (p.key == 'a')
				rotY--;
			if (p.key == 'q')
				rotZ++;
			if (p.key == 'd')
				rotZ--;
		}
	}
	
	public static void gererZoom(){
		float increment = 0.01F;
		
		if(zoom > 0 && zoom < 2){
			if(Main.POS_MOLETTE == -1)
				zoom -= increment;
			if(Main.POS_MOLETTE == 1)
				zoom += increment;
		}
		if(zoom <= 0)
			zoom += increment;
		if(zoom >= 2)
			zoom -= increment;
	}
	
	public static void setPapplet(PApplet p) {
		Grille.p = p;
	}

	public static ArrayList<Route> getRoutes() {
		return routes;
	}
	public static void setroutes(ArrayList<Route> routes) {
		Grille.routes = routes;
	}
	
	public static boolean isAffichageSol() {
		return affichageSol;
	}
	public static void setAffichageSol(boolean affichageSol) {
		Grille.affichageSol = affichageSol;
	}
}
