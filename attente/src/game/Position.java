package game;
public class Position {

	private int x, y;
	
	/**
	 * Creer une position en [X;Y]
	 * @param x
	 * @param y
	 */
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Retourne x
	 * @return x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Met a jour l'abscisse
	 * @param y
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Retourne y
	 * @return y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Met a jour l'ordonnee
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}

	
	/**
	 * @param o Chasseur ou Monstre
	 * @return true: si les 2 ont la meme position sinon false
	 */
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null) return false;
		if(o instanceof Monstre) {
			if(((Monstre) o).getX() == this.x && ((Monstre) o).getY() == this.y) {
				return true;
			}
		}
		if(o instanceof Chasseur) {
			if (((Chasseur) o).getX() == this.x && ((Chasseur) o).getY() == this.y) {
				return true;
			}
		}
		return false;
	}
}
