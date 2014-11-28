import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class ConstructeurBatiments {

	private ConstructeurBatiments(){}
	
	public static ArrayList<Batiment> batiments() {
		ArrayList<Batiment> nouveauxBatiments = new ArrayList<Batiment>();
		int posX1, posX2, posY1, posY2, posZ1, posZ2;
		final int COEFF_DIFF_TAILLE = Grille.TAILLE_CUBE_BAT / Grille.TAILLE_CUBE_ROUTE;
		Batiment nouveauBatiment = null;
		long repereTempo = (long) (System.nanoTime()/1000000000F);
		Random facteurHauteur = new Random();
		for (Iterator<Route> itRoutes = Grille.getRoutes().iterator(); itRoutes.hasNext();) {
			Route route = itRoutes.next();
			try{
				if(route.getAlignement() == "N-S"){
					for(int indexRouteHoriz = indexPremiereRouteHoriz(); indexRouteHoriz < Grille.getRoutes().size() - 1;){
//						do {
						
//						if((Grille.getRoutes().get(indexRouteHoriz + 1).getPosY1() - 1) - (Grille.getRoutes().get(indexRouteHoriz).getPosY1() + Grille.getRoutes().get(indexRouteHoriz).getLargeur()) < 3)
//							continue;
						
						posX1 = route.getPosX1() + route.getLargeur();
						posY1 = Grille.getRoutes().get(indexRouteHoriz).getPosY1() + Grille.getRoutes().get(indexRouteHoriz).getLargeur(); // En cubes taille route
						
						posX2 = posX1 + 2 /*Constructeur.trouverRoute(route, posY1, routes, true)*/;
						indexRouteHoriz++;
						posY2 = Grille.getRoutes().get(indexRouteHoriz).getPosY1() - (((posY1 - Grille.getRoutes().get(indexRouteHoriz).getPosY1()) % COEFF_DIFF_TAILLE == 0) ? 1 : COEFF_DIFF_TAILLE)
								- (Grille.getRoutes().get(indexRouteHoriz).getPosY1() - posY1)/COEFF_DIFF_TAILLE;		// En cubes taille bâtiments
						
						posZ1 = 1;
						posZ2 = (int) (4 + Math.abs(5 * facteurHauteur.nextGaussian()));
						
						nouveauBatiment = new Batiment(posX1, posX2, posY1, posY2, posZ1, posZ2, Main.couleursPredefinies.get((int) (Math.random() * Main.couleursPredefinies.size())));
						
						if(System.nanoTime()/1000000000F - repereTempo > 1)
							throw new Exception("Échec au niveau des batiments, regénération...");
//						} while(!placementConforme(nouveauBatiment, route.getAlignement()));
						
						nouveauxBatiments.add(nouveauBatiment);
					}
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return nouveauxBatiments;
	}

	private static int indexPremiereRouteHoriz() {
		int res = 0;
		for(int i = 0; i < Grille.getRoutes().size(); i++){
			if(Grille.getRoutes().get(i).getAlignement() == "E-O"){
				res = i;
				break;
			}
		}
		return res;
	}

	private static boolean placementConforme(Batiment nouveauBatiment, String alignement) {
		boolean res = true;
		for (Iterator<Route> itRoutes = Grille.getRoutes().iterator(); itRoutes.hasNext();) {
			Route route = itRoutes.next();
			if(route.getAlignement() == "N-S"){
				if((route.getPosX1() >= nouveauBatiment.getPosX1() && route.getPosX1() <= nouveauBatiment.getPosX2()
				|| route.getPosX2() >= nouveauBatiment.getPosX1() && route.getPosX2() <= nouveauBatiment.getPosX2())
				&& ConstructeurRoutes.perpendiculaireCommune(route, new Route(0, 0, nouveauBatiment.getPosY1(), nouveauBatiment.getPosY2(), 1, "N-S")))
					res = false; 
			}
			if(route.getAlignement() == "E-O"){
				if((route.getPosY1() >= nouveauBatiment.getPosY1() && route.getPosY1() <= nouveauBatiment.getPosY2()
				|| route.getPosY2() >= nouveauBatiment.getPosY1() && route.getPosY2() <= nouveauBatiment.getPosY2())
				&& ConstructeurRoutes.perpendiculaireCommune(route, new Route(nouveauBatiment.getPosX1(), nouveauBatiment.getPosX2(), 0, 0, 1, "E-O")))
					res = false; 
			}
		}
		return res;
	}
}
