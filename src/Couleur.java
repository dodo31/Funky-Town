
public class Couleur {
	private int teinte;
	private int saturation;
	private int luminosit�;
	private int transparence;
	
	public Couleur(int teinte, int saturation, int luminosit�, int transparence) {
		this.teinte = teinte;
		this.saturation = saturation;
		this.luminosit� = luminosit�;
		this.transparence = transparence;
	}
	
	public Couleur(int teinte, int saturation, int luminosit�) {
		this.teinte = teinte;
		this.saturation = saturation;
		this.luminosit� = luminosit�;
		this.transparence = 255;
	}
	
	public int getTeinte() {
		return teinte;
	}
	public void setTeinte(int teinte) {
		this.teinte = teinte;
	}
	
	public int getSaturation() {
		return saturation;
	}
	public void setSaturation(int saturation) {
		this.saturation = saturation;
	}
	
	public int getLuminosit�() {
		return luminosit�;
	}
	public void setLuminosit�(int luminosit�) {
		this.luminosit� = luminosit�;
	}
	
	public int getTransparence() {
		return transparence;
	}
	public void setTransparence(int transparence) {
		this.transparence = transparence;
	}
}
