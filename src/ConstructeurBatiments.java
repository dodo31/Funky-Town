import java.util.ArrayList;
import java.util.Iterator;

public class ConstructeurBatiments {

	private ConstructeurBatiments(){}
	
	public static ArrayList<Batiment> batiments() {
		ArrayList<Batiment> nouveauxBatiments = new ArrayList<Batiment>();
		int posX1, posX2, posY1, posY2, posZ1, posZ2;
		final int COEFF_DIFF_TAILLE = Grille.TAILLE_CUBE_BAT / Grille.TAILLE_CUBE_ROUTE;
		Batiment nouveauBatiment = null;
		long repereTempo = (long) (System.nanoTime()/1000000000F);
		for (Iterator<Route> itRoutes = Grille.getRoutes().iterator(); itRoutes.hasNext();) {
			Route route = itRoutes.next();
			int indexRouteHoriz = indexPremiereRouteHoriz();
			try{
				if(route.getAlignement() == "N-S"){
					int enumY = 0;
					while(enumY < Grille.TAILLE_GRILLE_Y){
						do {
							posX1 = route.getPosX1() + route.getLargeur();
							posY1 = Grille.getRoutes().get(indexRouteHoriz).getPosY1() / COEFF_DIFF_TAILLE;
							enumY += posY1 + 1;
							posX2 = posX1 + 2 /*Constructeur.trouverRoute(route, posY1, routes, true)*/;
							if(posY1 > Grille.getRoutes().get(indexRouteHoriz + 1).getPosY1() / COEFF_DIFF_TAILLE)
								indexRouteHoriz++;
							posY2 = Grille.getRoutes().get(indexRouteHoriz + 1).getPosY1() / COEFF_DIFF_TAILLE - COEFF_DIFF_TAILLE;
							posZ1 = 0;
							posZ2 = (int) (2 + 10 * Math.random());
							
							nouveauBatiment = new Batiment(posX1, posX2, posY1, posY2, posZ1, posZ2, new Couleur((int) (256 * Math.random()), 255, 255));
							
							if(System.nanoTime()/1000000000F - repereTempo > 1)
								throw new Exception("Échec au niveau des batiments, regénération...");
							enumY += 1;
						} while(!placementConforme(nouveauBatiment, route.getAlignement()));
						
						nouveauxBatiments.add(nouveauBatiment);
						System.out.println(nouveauBatiment);
						enumY += 3;
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
