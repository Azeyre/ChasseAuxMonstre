package game;

import menu.MenuConsole;

/**
 * Classe du Plateau
 * @author KOZLOV-PC
 *
 */
public class Plateau {

	private int size;
	private int[][] monstreAnciennePosition;
	private final char monstre = 'M';
	private final char chasseur = 'C';
	private final char caseVide = '*';
	public static final char casePleine = '#';
	private char[][] plateau;
	private boolean[][] caseExplorer;
	private boolean[][] caseExplorerChasseur;
	
	/**
	 * Cr�ation du plateau avec <b><i>SIZE</i></b> cases en <i> LONGUEUR et LARGEUR</i> 
	 * @param size - Taille du plateau
	 */
	public Plateau() {
		this.size = MenuConsole.SIZE;
		this.plateau = new char[size][size];
		this.monstreAnciennePosition = new int[size][size];
		this.caseExplorer = new boolean[size][size];
		this.caseExplorerChasseur = new boolean[size][size];
		for(int i = 0 ; i < size ; i++) {
			for(int j = 0 ; j < size ; j++) {
				this.monstreAnciennePosition[i][j] = -1;
				this.plateau[i][j] = caseVide;
				this.caseExplorer[i][j] = false;
			}
		}
	}
	
	public Plateau(int size) {
		this.size = size;
		System.out.println(size);
		this.plateau = new char[size][size];
		this.monstreAnciennePosition = new int[size][size];
		this.caseExplorer = new boolean[size][size];
		this.caseExplorerChasseur = new boolean[size][size];
		for(int i = 0 ; i < size ; i++) {
			for(int j = 0 ; j < size ; j++) {
				this.monstreAnciennePosition[i][j] = -1;
				this.plateau[i][j] = caseVide;
				this.caseExplorer[i][j] = false;
			}
		}
	}
	
	private void incrPos() {
		for(int i = 0 ; i < size ; i++) {
			for(int j = 0 ; j < size ; j++) {
				if (this.monstreAnciennePosition[i][j] >= 0 && this.monstreAnciennePosition[i][j] < 9) this.monstreAnciennePosition[i][j]++;
			}
		}
	}
	
	/**
	 * Incr�mentation du tableau des anciens d�placements du monstres 
	 * @param m - Monstre
	 */
	public void incrPos(Monstre m) {
		incrPos();
		this.monstreAnciennePosition[m.getX()][m.getY()] = 0;
		this.plateau[m.getX()][m.getY()] = this.monstre;
		this.caseExplorer[m.getX()][m.getY()] = true;
	}
	
	/**
	 * Affichage du plateau en fonction du joueur : soit pour le monstre, soit pour le chasseur
	 * @param o - Object : Monstre ou Chasseur
	 */
	public void affichage(Object o, Chasseur c) {
		System.out.print("  ");
		for(int a = 0 ; a < size ; a++) {System.out.print((char) ('A' + a));}
		System.out.println();
		
		for(int i = 0 ; i < size ; i++) {
			System.out.print(i + " ");
			for(int j = 0 ; j < size ; j++) {
				if(o instanceof Monstre) {
					if(((Monstre) o).getX() == i && ((Monstre) o).getY() == j) System.out.print(monstre);
					else if(c.getX() != -1 && c.getY() != -1 && c.getX() == i && c.getY() == j) System.out.print(chasseur);
					else if(caseExplorer[i][j] && monstreAnciennePosition[i][j] != 0 || plateau[i][j] == casePleine) {
						System.out.print(casePleine);
					}
					else System.out.print(caseVide);
				}
				else {
					if(plateau[i][j] != monstre && monstreAnciennePosition[i][j] > 0) {
						System.out.print(monstreAnciennePosition[i][j]);
					}
					else if(plateau[i][j] != monstre) System.out.print(plateau[i][j]);
					else System.out.print(caseVide);
				}
			}
			System.out.print("\n");
		}
	}
	
	/**
	 * Le chasseur r�v�le la case en X,Y du plateau
	 * @param x int
	 * @param y int 
	 * @return true: si le chasseur a trouv� le monstre sinon false
	 */
	public boolean reveal(int x, int y, Monstre m) {
		if(m.getX() == x && m.getY() == y) return true;
		if(monstreAnciennePosition[x][y] >= 0) {
			System.out.println("\nLe monstre est pass� par la il y a : " + monstreAnciennePosition[x][y] + " tours !");
			if(monstreAnciennePosition[x][y] < 10) plateau[x][y] = (char) ('0' + monstreAnciennePosition[x][y]);
			else plateau[x][y] = '9';
		}
		else {
			System.out.println("\nDommage, rien par ici...");
			plateau[x][y] = 'C';
		}
		
		caseExplorerChasseur[x][y] = true;
		return false;
	}
	
	/**
	 * Renvoie vrai ou faux si la case a �t� explor� en X,Y
	 * @param x int
	 * @param y int
	 * @return true si la case � d�j� �t� explor� par le monstre sinon false
	 */
	public boolean getCaseExplorer(int x, int y){
		return caseExplorer[x][y];
	}
	
	public boolean getCaseExplorerChasseur(int x, int y){
		return caseExplorerChasseur[x][y];
	}
	
	public int getMonstreAnciennePosition(int x, int y) {
		return monstreAnciennePosition[x][y];
	}

	public char[][] getPlateau() {
		return plateau;
	}

	public void setPlateau(char[][] plateau) {
		this.plateau = plateau;
	}
	
	public void setExplorer(int x, int y) {
		this.caseExplorer[x][y] = true;
	}
	
	public void setExplorerChasseur(int x, int y) {
		this.caseExplorerChasseur[x][y] = true;
	}
	
	public int getSize() {
		return this.size;
	}
	public boolean fini() {
		for(int i = 0 ; i < caseExplorer.length ; i++) {
			for(int j = 0 ; j < caseExplorer[i].length ; j++) {
				//Renvoie faux s'il reste des case � explorer
				if(!caseExplorer[i][j]) return false;
			}
		}
		return true;
	}
}