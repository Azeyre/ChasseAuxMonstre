package game;

import graphics.Joueur;
import menu.Menu;

/**
 * Classe de Monstre
 * @author KOZLOV-PC
 *
 */

public class Monstre extends Joueur {
	
	private Position p;
	private int size;
	
	/**
	 * Genere une position <b><i>alï¿½atoire</i></b> du monstre en fonction de la taille du plateau
	 * @param size int
	 */
	public Monstre() {
		super("");
		this.size = Menu.SIZE;
		this.p = new Position((int) (Math.random() * (size)), (int) (Math.random() * (size)));
	}
	
	public Monstre(String pseudo) {
		super(pseudo);
	}
	
	public void setup(int size) {
		this.size = size;
		this.p = new Position((int) (Math.random() * (size)), (int) (Math.random() * (size)));
	}
	/**
	 * Retourne la position du joueur 
	 * @return position Position
	 */
	public Position getPosition() {
		return p;
	}
	
	/**
	 * Retourne la position du joueur en abscisse
	 * @return x int
	 */
	public int getX() {
		return p.getX();
	}
	
	/**
	 * Retourne la position du joueur en ordonnï¿½e
	 * @return y int
	 */
	public int getY() {
		return p.getY();
	}
	
	/**
	 * Met ï¿½ jour la position du joueur en abscisse
	 * @param x int
	 */
	public void setX(int x) {
		p.setX(x);
	}
	
	/**
	 * Met ï¿½ jour la position du joueur en ordonnï¿½e
	 * @param y int
	 */
	public void setY(int y) {
		p.setY(y);
	}
	
	/**
	 * Met ï¿½ jour la position du joueur en abscisse et en ordonnï¿½e
	 * @param x int
	 * @param y int
	 */
	public void setPosition(int x, int y) {
		p.setX(x);
		p.setY(y);
	}

	/**
	 * Dï¿½place le joueur en fonction de sa position actuelle
	 * @param x int
	 * @param y int
	 * @return <b>true</b> <i>si le joueur peut se dï¿½placer dans le sens voulu</i> sinon <b>false</b>
	 */
	public boolean move(int x, int y, Plateau p) {
		int xTemp = this.getX() + x;
		int yTemp = this.getY() + y;
		
		if(xTemp < 0) xTemp = size - 1;
		if(yTemp < 0) yTemp = size - 1;
		if(xTemp >= size) xTemp = 0;
		if(yTemp >= size) yTemp = 0;
		
		if(p.getCaseExplorer(xTemp, yTemp)) {
			System.out.println("Case déjà explorée !");
			return false;
		}
		this.setPosition(xTemp, yTemp);
		return true;
	}
	
	/**
	 * 
	 * @param p Plateau
	 * @return true: Si le monstre est bloque, sinon false
	 */
	public boolean bloquer(Plateau p) {
		int xTemp = this.getX(), yTemp = this.getY();
		
		//X - 1
		if(this.getX() - 1 < 0) {
			xTemp = size - 1;
		} else xTemp = this.getX() - 1;
		if(!p.getCaseExplorer(xTemp, yTemp)) return false;
		
		//X + 1
		xTemp = this.getX();
		if(this.getX() + 1 >= size) {
			xTemp = 0;
		} else xTemp = this.getX() + 1;
		if(!p.getCaseExplorer(xTemp, yTemp)) return false;
		
		//Y - 1
		xTemp = this.getX();
		if(this.getY() - 1 < 0) {
			yTemp = size - 1;
		} else yTemp = this.getY() - 1;
		if(!p.getCaseExplorer(xTemp, yTemp)) return false;
		
		//Y + 1
		xTemp = this.getX();
		yTemp = this.getY();
		if(this.getY() + 1 >= size) {
			yTemp = 0;
		} else yTemp = this.getY() + 1;
		if(!p.getCaseExplorer(xTemp, yTemp)) return false;
		
		return true;
	}

	@Override
	public boolean estMonstre() {
		return true;
	}
}
