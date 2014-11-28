import java.util.Iterator;
import java.util.LinkedList;

import processing.core.PVector;

public class ConstructeurBackup {
	private ConstructeurBackup(){}
	
	public static LinkedList<Route> ArteresPrincipales(){
		int nbDeparts = (int) (Math.sqrt(Math.pow(Grille.TAILLE_GRILLE_X, 2) + Math.pow(Grille.TAILLE_GRILLE_Y, 2))/300 + Math.random() * 2);
//		int depart = 10;
		
		return route(nbDeparts - 1, Grille.LARGEUR_ARTERE_PRINCIPALE);
	}
	public static LinkedList<Route> Avenues(){
		int nbDeparts = (int) (Math.sqrt(Math.pow(Grille.TAILLE_GRILLE_X, 2) + Math.pow(Grille.TAILLE_GRILLE_Y, 2))/150 + Math.random() * 5);
//		int depart = 10;

		return route(nbDeparts - 1, Grille.LARGEUR_AVENUE);
	}
	public static LinkedList<Route> Rues(){
		int nbDeparts = (int) (Math.sqrt(Math.pow(Grille.TAILLE_GRILLE_X, 2) + Math.pow(Grille.TAILLE_GRILLE_Y, 2))/75 + Math.random() * 15);
//		int depart = 10;

		return route(nbDeparts - 1, Grille.LARGEUR_RUE);
	}
	
	public static LinkedList<Route> route(int nbDeparts, int largeur){
		LinkedList<Route> NouvellesRoutes = new LinkedList<Route>();
		long repereTempo = (long) (System.nanoTime()/1000000000F);

		for(int i = 0; i < nbDeparts; i++){
			PVector coords = coordonnees();
			int posX1, posY1, posX2, posY2;
			
			boolean intersection = (Math.random() > 0.25F) ? true : false;
			
			String alignement = (Math.random() > 0.5F) ? "N-S" : "E-O";

			Route nouvelleRoutePrimaire = null;
			do{
				coords = coordonnees();
				posX1 = (int) coords.x;
				posY1 = (int) coords.y;
				
				if(alignement.equals("N-S")){
					posY2 = trouverRoute(new Route(0, 0, Grille.TAILLE_GRILLE_X, 0, largeur, "E-O"), posX1, NouvellesRoutes);
					nouvelleRoutePrimaire = new Route(posX1, 0, posX1 + (largeur - 1), posY2, largeur, "N-S");
				}
				if(alignement.equals("E-O")){
					posX2 = trouverRoute(new Route(0, 0, 0, Grille.TAILLE_GRILLE_Y, largeur, "N-S"), posY1, NouvellesRoutes);
					nouvelleRoutePrimaire = new Route(0, posY1, posX2, posY1 + (largeur - 1), largeur, "E-O");
				}
				
				if(System.nanoTime()/1000000000F - repereTempo > 3){
					System.out.println("Échec, regénération...");
					i = nbDeparts;
					break;
				}
			}while(!placementConforme(nouvelleRoutePrimaire, NouvellesRoutes));
			NouvellesRoutes.add(nouvelleRoutePrimaire);
			
			if(intersection){
				Route nouvelleRoutePrependiculaire = null;
				do{
					if(alignement.equals("N-S")){
//						posX2 = Grille.TAILLE_GRILLE_X - 1;
						posY1 = calculerPosY1();
						posX2 = trouverRoute(nouvelleRoutePrimaire, posY1, NouvellesRoutes);
						nouvelleRoutePrependiculaire = new Route(posX1 + largeur, posY1, posX2, posY1 + (largeur - 1), largeur, "E-O");
					}
					if(alignement.equals("E-O")){
						posX1 = calculerPosX1();
//						posY2 = Grille.TAILLE_GRILLE_Y - 1;
						posY2 = trouverRoute(nouvelleRoutePrimaire, posX1, NouvellesRoutes);
						nouvelleRoutePrependiculaire = new Route(posX1, posY1 + largeur, posX1 + (largeur - 1), posY2, largeur, "N-S");
					}
					if(System.nanoTime()/1000000000F - repereTempo > 3 && i != nbDeparts){
						System.out.println("Échec, regénération...");
						i = nbDeparts;
						break;
					}
				}while(!placementConforme(nouvelleRoutePrependiculaire, NouvellesRoutes));
				
				NouvellesRoutes.add(nouvelleRoutePrependiculaire);
			}
//			System.out.println("Départs " + ((largeur == 5) ? "de grandes artères :  " + (i + 1) : (largeur == 3) ? "d'avenues :  " + (i + 1) : (largeur == 1) ? "de rues :  " + (i + 1) : "d'Autre"));
		}
		return NouvellesRoutes;
	}
	
	private static int trouverRoute(Route routePrimaire, int posInit, LinkedList<Route> NouvellesRoutes) {
		LinkedList<Route> RoutesCrees = new LinkedList<Route>();
		RoutesCrees.addAll(NouvellesRoutes);
		RoutesCrees.addAll(Grille.getRoutes());
		
		int proba = probaIntersections(routePrimaire, RoutesCrees);
		int res = routePrimaire.getAlignement() == "N-S" ? Grille.TAILLE_GRILLE_X : Grille.TAILLE_GRILLE_Y;
		
		for (Iterator<Route> itRoutes = RoutesCrees.iterator(); itRoutes.hasNext();) {
			Route route = itRoutes.next();
			
			if(route.getAlignement() == routePrimaire.getAlignement() && perpendiculaireCommune(route, routePrimaire) && route.enAmont(routePrimaire)
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
	private static int probaIntersections(Route routePrimaire, LinkedList<Route> RoutesCrees){
		int res = 0;
		for (Iterator<Route> itRoutes = RoutesCrees.iterator(); itRoutes.hasNext();) {
			Route route = itRoutes.next();
			if(route.getAlignement() == routePrimaire.getAlignement() && route.enAmont(routePrimaire))
				res++;
		}
		return res;
	}
	
	private static PVector coordonnees(){
		return new PVector((int) (Grille.TAILLE_GRILLE_X/8 + (Grille.TAILLE_GRILLE_X - 3 - Grille.TAILLE_GRILLE_X /4) * Math.random()),
						   (int) (Grille.TAILLE_GRILLE_Y/8 + (Grille.TAILLE_GRILLE_Y - 3 - Grille.TAILLE_GRILLE_Y /4) * Math.random()));
	}
	private static int calculerPosX1(){
		return (int) (Grille.TAILLE_GRILLE_X/8 + (Grille.TAILLE_GRILLE_X - 3 - Grille.TAILLE_GRILLE_X /4) * Math.random());
	}
	private static int calculerPosY1(){
		return (int) (Grille.TAILLE_GRILLE_Y/8 + (Grille.TAILLE_GRILLE_Y - 3 - Grille.TAILLE_GRILLE_Y /4) * Math.random());
	}
	
	private static boolean placementConforme(Route nouvelleRoute, LinkedList<Route> NouvellesRoutes){
		boolean res = true;

		LinkedList<Route> RoutesCrees = new LinkedList<Route>();
		RoutesCrees.addAll(NouvellesRoutes);
		RoutesCrees.addAll(Grille.getRoutes());
		
		for (Iterator<Route> itRoutes = RoutesCrees.iterator(); itRoutes.hasNext();) {
			Route route = itRoutes.next();
			
			if(nouvelleRoute.getAlignement().equals(nouvelleRoute.getAlignement())){
				if(nouvelleRoute.getAlignement().equals("N-S") && Math.abs(route.getPosX1() - nouvelleRoute.getPosX1()) <= route.getLargeur() * nouvelleRoute.getLargeur() + 5
				|| nouvelleRoute.getAlignement().equals("E-O") && Math.abs(route.getPosY1() - nouvelleRoute.getPosY1()) <= route.getLargeur() * nouvelleRoute.getLargeur() + 5){
					if(perpendiculaireCommune(route, nouvelleRoute)){
						res = false;
						break;
					}
				}
			}
		}
		return res;
	}

	private static boolean perpendiculaireCommune(Route routeCourante, Route nouvelleRoute){
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
