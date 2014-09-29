
public class Couleur {
	private int teinte;
	private int saturation;
	private int luminosité;
	private int transparence;
	
	public Couleur(int teinte, int saturation, int luminosité, int transparence) {
		this.teinte = teinte;
		this.saturation = saturation;
		this.luminosité = luminosité;
		this.transparence = transparence;
	}
	
	public Couleur(int teinte, int saturation, int luminosité) {
		this.teinte = teinte;
		this.saturation = saturation;
		this.luminosité = luminosité;
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
	
	public int getLuminosité() {
		return luminosité;
	}
	public void setLuminosité(int luminosité) {
		this.luminosité = luminosité;
	}
	
	public int getTransparence() {
		return transparence;
	}
	public void setTransparence(int transparence) {
		this.transparence = transparence;
	}
}
