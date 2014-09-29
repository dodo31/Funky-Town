public class Route extends Composant implements Comparable<Object>{
	
	private String alignement;
	private int largeur;
	
	public Route(int posX1, int posX2, int posY1, int posY2, int largeur, String alignement) {
		super(posX1, posX2, posY1, posY2);
		
		this.largeur = largeur;
		this.alignement = alignement;
		
		try {
			if(alignement != "N-S" && alignement != "E-O")
				throw new IllegalArgumentException("Orienttion invalide");
		}
		catch(IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public boolean enAmont(Route routePrimaire) {
		return ((alignement.equals("N-S") && routePrimaire.getPosX2() < posX1)
			 || (alignement.equals("E-O") && routePrimaire.getPosY1() < posY1));
	}
	
	public int getLargeur() {
		return largeur;
	}
	public void setLargeur(int largeur) {
		this.largeur = largeur;
	}

	public String getAlignement() {
		return alignement;
	}
	public void setAlignement(String alignement) {
		this.alignement = alignement;
	}

	@Override
	public int compareTo(Object routeTmp) {
		// Les routes orientées nord-sud sont placée en premier dans la liste
		Route route = (Route) routeTmp;
		if(alignement == "E-O" && route.getAlignement() == "N-S")
			return 1;
		else if(alignement == "N-S" && route.getAlignement() == "E-O")
			return -1;
		else{
			if(alignement == "N-S" && route.getAlignement() == "N-S"){
				if(posY1 < route.getPosY1())
					return -1;
				else if(posY1 == route.getPosY1())
					return 0;
				else
					return 1;
			} else {
				if(posX1 < route.getPosX1())
					return -1;
				else if(posX1 == route.getPosX1())
					return 0;
				else
					return 1;
			}
		}
	}
}
