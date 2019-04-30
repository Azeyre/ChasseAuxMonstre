package game;

/**
 * Classe du Chasseur
 * @author KOZLOV-PC
 *
 */
public class Chasseur {
	
	private Position lastPos = new Position(-1,-1);

	/**
	 * Retourne la position du chasseur
	 * @return Position
	 */
	public Position getPosition() {return lastPos;}
	
	/**
	 * Retourne la position du chasseur en abscisse
	 * @return x int
	 */
	public int getX() {return lastPos.getX();}
	
	/**
	 * Retourne la position du chasseur en ordonn�e
	 * @return y int
	 */
	public int getY() {return lastPos.getY();}
	
	/**
	 * Met � jour la position du chasseur en abscisse
	 * @param x int
	 */
	public void setX(int x) {lastPos.setX(x);}
	
	/**
	 * Met � jour la position du chasseur en ordonn�e
	 * @param y int
	 */
	public void setY(int y) {lastPos.setY(y);}
	
	/**
	 * Met � jour la position du chasseur en abscisse et en ordonn�e
	 * @param x int
	 * @param y int
	 */
	public void setPosition(int x, int y) {lastPos.setX(x); lastPos.setY(y);}
}
