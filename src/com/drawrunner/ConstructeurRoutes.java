package com.drawrunner;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class ConstructeurRoutes{
	private ConstructeurRoutes(){}

	public static ArrayList<Route> arteresPrincipales(){
		int nbDeparts = (int) (Grille.DIAGONALE/75F + Math.random() * (Grille.DIAGONALE/31250F));
		return routes(nbDeparts - 1, Grille.LARGEUR_ARTERE_PRINCIPALE);
	}
	public static ArrayList<Route> avenues(){
		int nbDeparts = (int) (Grille.DIAGONALE/30F + Math.random() * (Grille.DIAGONALE/12500F));
		return routes(nbDeparts - 1, Grille.LARGEUR_AVENUE);
	}
	public static ArrayList<Route> rues(){
		int nbDeparts = (int) (Grille.DIAGONALE/5F + Math.random() * (Grille.DIAGONALE/500F));
		return routes(nbDeparts - 1, Grille.LARGEUR_RUE);
	}
	
	public static ArrayList<Route> routes(int nbDeparts, int largeur){
		ArrayList<Route> nouvellesRoutes = new ArrayList<Route>();
		long repereTempo = (long) (System.nanoTime()/1000000000F);

		if(Grille.getRoutes().size() == 0 && nouvellesRoutes.size() == 0){
			if(Math.random() > 0.5F){
				int positionnement = (int) (Grille.TAILLE_GRILLE_X * Math.random());
				nouvellesRoutes.add(new Route(positionnement, positionnement + largeur - 1, 0, Grille.TAILLE_GRILLE_Y - 1, largeur, "N-S"));
			}
			else{
				int positionnement = (int) (Grille.TAILLE_GRILLE_Y * Math.random());
				nouvellesRoutes.add(new Route(0, Grille.TAILLE_GRILLE_X, positionnement, positionnement + largeur - 1, largeur, "E-O"));
			}
		}
		
		for(int i = 0; i < nbDeparts; i++){
			Route routeRef;
			Route nouvelleRoute = null;

			if(Math.random() > 0.8F)
				routeRef = routeReference(nouvellesRoutes);
			else{
				if(Math.random() > 0.5F)
					routeRef = new Route(0, 1, 0, Grille.TAILLE_GRILLE_Y - largeur, 1, "N-S");
				else
					routeRef = new Route(0, Grille.TAILLE_GRILLE_X - largeur, 0, 1, 1, "E-O");
			}
			try{
				do{
					nouvelleRoute = nouvelleRoute(routeRef, largeur, nouvellesRoutes);
					
					if(System.nanoTime()/1000000000F - repereTempo > 1 && i != nbDeparts){
						i = nbDeparts;
						throw new Exception("Échec au niveau des " + (largeur == 5 ? "grandes artères" : largeur == 3 ? "avenues" : "rues") + ", regénération...");
					}
				}while(!placementConforme(nouvelleRoute, nouvellesRoutes));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
			nouvellesRoutes.add(nouvelleRoute);
				
//			System.out.println("Départs " + ((largeur == 5) ? "de grandes artères :  " + (i + 1) : (largeur == 3) ? "d'avenues :  " + (i + 1) : (largeur == 1) ? "de rues :  " + (i + 1) : "d'Autre"));
		}
		return nouvellesRoutes;
	}

	private static Route routeReference(ArrayList<Route> nouvellesRoutes){
		LinkedList<Route> routesCrees = new LinkedList<Route>();
		routesCrees.addAll(nouvellesRoutes);
		routesCrees.addAll(Grille.getRoutes());
		
		Route res = null;
		
		for (Iterator<Route> itRoutes = routesCrees.iterator(); itRoutes.hasNext();) {
			Route route = itRoutes.next();
			if(routesCrees.size() * Math.random() > 0 && routesCrees.size() * Math.random() < routesCrees.size()){
				res = route;
				break;
			}
		}
		
		return res;
	}
	
	private static Route nouvelleRoute(Route routeRef, int largeur, ArrayList<Route> nouvellesRoutes){
		int posX1, posX2, posY1, posY2;
		Route nouvelleRoute = null;
		
		if(routeRef.getAlignement() == "N-S"){
			posY1 = (int) (routeRef.getPosY1() + (routeRef.getPosY2() - routeRef.getPosY1()) * Math.random());
			posY2 = posY1 + largeur - 1;
			posX1 = routeRef.getPosX1() + routeRef.getLargeur();
			posX2 = trouverRoute(routeRef, posY1, nouvellesRoutes, false);
			
			nouvelleRoute = new Route(posX1, posX2, posY1, posY2, largeur, "E-O");
		}
		if(routeRef.getAlignement() == "E-O"){
			posX1 = (int) ((routeRef.getPosX2() - routeRef.getPosX1()) * Math.random());
			posX2 = posX1 + largeur - 1;
			posY1 = routeRef.getPosY1() + routeRef.getLargeur();
			posY2 = trouverRoute(routeRef, posX1, nouvellesRoutes, false);
			
			nouvelleRoute = new Route(posX1, posX2, posY1, posY2, largeur, "N-S");
		}
		
		return nouvelleRoute;
	}
	
	public static int trouverRoute(Route routeRef, int posInit, ArrayList<Route> nouvellesRoutes, boolean premiereRoute) {
		ArrayList<Route> routesCrees = new ArrayList<Route>();
		routesCrees.addAll(nouvellesRoutes);
		routesCrees.addAll(Grille.getRoutes());
		
		int proba = premiereRoute ? 1 : probaIntersection(routeRef, routesCrees);
		int res = routeRef.getAlignement() == "N-S" ? Grille.TAILLE_GRILLE_X : Grille.TAILLE_GRILLE_Y;
		
		for (Iterator<Route> itRoutes = routesCrees.iterator(); itRoutes.hasNext();) {
			Route route = itRoutes.next();
			
			if(route.getAlignement() == routeRef.getAlignement() && perpendiculaireCommune(route, routeRef) && route.enAmont(routeRef)
					&& (int) (proba * Math.random()) >= 0 && (int) (proba * Math.random()) <= proba/2){
				if(route.getAlignement() == "N-S" && posInit > route.getPosY1() && posInit < route.getPosY2()){
					res = route.getPosX1() - 1;
					break;
				}
				if(route.getAlignement() == "E-O" && posInit > route.getPosX1() && posInit < route.getPosX2()){
					res = route.getPosY1() - 1;
					break;
				}
			}
		}
		return res;
	}
	private static int probaIntersection(Route routeRef, ArrayList<Route> routesCrees){
		int res = 0;
		for (Iterator<Route> itRoutes = routesCrees.iterator(); itRoutes.hasNext();) {
			Route route = itRoutes.next();
			if(route.getAlignement() == routeRef.getAlignement() && route.enAmont(routeRef))
				res++;
		}
		return res;
	}
	
	private static boolean placementConforme(Route nouvelleRoute, ArrayList<Route> nouvellesRoutes){
		boolean res = true;

		LinkedList<Route> routesCrees = new LinkedList<Route>();
		routesCrees.addAll(nouvellesRoutes);
		routesCrees.addAll(Grille.getRoutes());
		
		for (Iterator<Route> itRoutes = routesCrees.iterator(); itRoutes.hasNext();) {
			Route route = itRoutes.next();
			
			if(nouvelleRoute.getAlignement().equals(nouvelleRoute.getAlignement())){
				if(nouvelleRoute.getAlignement().equals("N-S") && Math.abs(route.getPosX1() - nouvelleRoute.getPosX1()) <= route.getLargeur() * nouvelleRoute.getLargeur() + 10
				|| nouvelleRoute.getAlignement().equals("E-O") && Math.abs(route.getPosY1() - nouvelleRoute.getPosY1()) <= route.getLargeur() * nouvelleRoute.getLargeur() + 10){
					if(perpendiculaireCommune(route, nouvelleRoute)){
						res = false;
						break;
					}
				}
			}
		}
		return res;
	}

	public static boolean perpendiculaireCommune(Route routeCourante, Route nouvelleRoute){
		boolean res = false;
		if(routeCourante.getAlignement().equals("N-S")){
			for(int i = Math.min(routeCourante.getPosY1(), nouvelleRoute.getPosY1()); i <= Math.max(routeCourante.getPosY2(), nouvelleRoute.getPosY2()); i++){
				if(routeCourante.getPosY1() < i && routeCourante.getPosY2() > i && nouvelleRoute.getPosY1() < i && nouvelleRoute.getPosY2() > i){
					res = true;
					break;
				}
			}
		}
		if(routeCourante.getAlignement().equals("E-O")){
			for(int i = Math.min(routeCourante.getPosX1(), nouvelleRoute.getPosX1()); i <= Math.max(routeCourante.getPosX2(), nouvelleRoute.getPosX2()); i++){
				if(routeCourante.getPosX1() < i && routeCourante.getPosX2() > i && nouvelleRoute.getPosX1() < i && nouvelleRoute.getPosX2() > i){
					res = true;
					break;
				}
			}
		}
		return res;
	}
}
