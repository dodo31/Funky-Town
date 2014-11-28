public class Batiment extends Composant{
	
	private int posZ1;
	private int posZ2;

	private Couleur couleurRef;
	
	public Batiment(int posX1, int posX2, int posY1, int posY2, int posZ1, int posZ2, Couleur couleurRef) {
		super(posX1, posX2, posY1, posY2);
		
		this.posZ1 = posZ1;
		this.posZ2 = posZ2;
		
		this.couleurRef = couleurRef;
	}

	public int getPosZ1() {
		return posZ1;
	}
	public void setPosZ1(int posZ1) {
		this.posZ1 = posZ1;
	}
	public int getPosZ2() {
		return posZ2;
	}
	public void setPosZ2(int posZ2) {
		this.posZ2 = posZ2;
	}

	public Couleur getCouleurRef() {
		return couleurRef;
	}

	@Override
	public String toString() {
		return "Batiment [posZ1=" + posZ1 + ", posZ2=" + posZ2
				+ ", couleurRef=" + couleurRef + ", posX1=" + posX1
				+ ", posX2=" + posX2 + ", posY1=" + posY1 + ", posY2=" + posY2
				+ "]";
	}
}
