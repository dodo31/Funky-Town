import processing.core.*;
import processing.event.MouseEvent;

public class Main extends PApplet{
	private static final long serialVersionUID = -6790885338225751514L;

	public static void main(String args[]) {
		PApplet.main(new String[] { "--present", "Main" });
	}

	public static int POS_MOLETTE = 0;
	
	public final static int TAILLE_FENETRE_X = 800;
	public final static int TAILLE_FENETRE_Y = 300;
	
	private static boolean affichageContour = true;
	private static boolean affichageCubes = false;
	private static char touchePrecedente;
	
	public void setup(){		
		size(TAILLE_FENETRE_X, TAILLE_FENETRE_Y, P3D);
		colorMode(HSB);

		Grille.setPapplet(this);
		
		Grille.genererRoutes();
		Grille.injecterRoutes();
		
		Grille.genererBatiments();
		Grille.injecterBatiments();
		
		Grille.genererSol();
	}
	
	public void draw(){
		background(0);

		gererTouchesControle();
		
		Grille.gererRotation();
		Grille.gererZoom();
		
		if(affichageContour)
			Grille.afficherContour();
		if(affichageCubes)
			Grille.afficherCubes();
		
		POS_MOLETTE = 0;
	}
	
	public void gererTouchesControle(){
		if (keyPressed) {
			if (key == 'g' && touchePrecedente != 'g')
				affichageContour = !affichageContour;
			if (key == 'c' && touchePrecedente != 'c')
				affichageCubes = !affichageCubes;
			if (key == 'f' && touchePrecedente != 'f')
				Grille.setAffichageSol(!Grille.isAffichageSol());
			if (key == ENTER && touchePrecedente != ENTER)
				Grille.reinitialiser();
			touchePrecedente = key;
		} else {
			touchePrecedente = '0';
		}
	}
	
	public void mouseWheel(MouseEvent event) {
		POS_MOLETTE = event.getCount();
	}
}
