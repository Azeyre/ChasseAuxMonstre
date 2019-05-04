package graphics;

public abstract class Joueur {

	private String pseudo;
	private boolean peutJouer;
	
	public Joueur(String pseudo) {
		this.pseudo = pseudo;
	}

	
	public String getPseudo() {
		return pseudo;
	}
	
	public String toString() {
		return pseudo;
	}
	
	public boolean peutJouer() {
		return peutJouer;
	}
	
	public void setJouer(boolean joue) {
		peutJouer = joue;
	}
	
	public abstract boolean estMonstre();
}
