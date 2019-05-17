package options;

import game.Plateau;
import ia.ChasseurIA;
import menu.Menu;

public class BattleRoyale {	
		
	private int tailleZone = 0; 
	private Plateau p;
	
	/**
	 * Retricit le plateau d'une case
	 * @param p
	 */
	public BattleRoyale(Plateau p) {
		this.p = p;
		tailleZone = 0;
	}
	
	public void retrecit() {
		System.out.println("Retrecit la zone");
		char[][] plateau = p.getPlateau();
		for(int i = 0 ; i < Menu.SIZE ; i++) {
			for(int j = 0 ; j < Menu.SIZE ; j++) {
				if(i == tailleZone || (i == (Menu.SIZE - 1 - tailleZone)) || (j == tailleZone) || (j == (Menu.SIZE - 1 - tailleZone))) {
					plateau[i][j] = Plateau.casePleine;
					p.setExplorer(i, j);
				}
			}
		}
		tailleZone++;
		p.setPlateau(plateau);
	}
	
	public void retrecit(ChasseurIA ia) {
		System.out.println("Retrecit la zone");
		char[][] plateau = p.getPlateau();
		for(int i = 0 ; i < Menu.SIZE ; i++) {
			for(int j = 0 ; j < Menu.SIZE ; j++) {
				if(i == tailleZone || (i == (Menu.SIZE - 1 - tailleZone)) || (j == tailleZone) || (j == (Menu.SIZE - 1 - tailleZone))) {
					plateau[i][j] = Plateau.casePleine;
					p.setExplorer(i, j);
					ia.setExplorer(i, j);
				}
			}
		}
		tailleZone++;
		p.setPlateau(plateau);
	}
	
}
