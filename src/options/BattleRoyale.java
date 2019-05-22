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
		for(int i = 0 ; i < p.getSize() ; i++) {
			for(int j = 0 ; j < p.getSize() ; j++) {
				if(i == tailleZone || (i == (p.getSize() - 1 - tailleZone)) || (j == tailleZone) || (j == (p.getSize() - 1 - tailleZone))) {
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
		for(int i = 0 ; i < p.getSize() ; i++) {
			for(int j = 0 ; j < p.getSize() ; j++) {
				if(i == tailleZone || (i == (p.getSize() - 1 - tailleZone)) || (j == tailleZone) || (j == (p.getSize() - 1 - tailleZone))) {
					plateau[i][j] = Plateau.casePleine;
					p.setExplorer(i, j);
					ia.setExplorer(i, j);
				}
			}
		}
		tailleZone++;
		p.setPlateau(plateau);
	}
	
	public void rectrecitGraphique() {
		for(int i = 0 ; i < p.getSize() ; i++) {
			for(int j = 0 ; j < p.getSize() ; j++) {
				if(i == tailleZone || (i == (p.getSize() - 1 - tailleZone)) || (j == tailleZone) || (j == (p.getSize() - 1 - tailleZone))) {
					p.setExplorer(i, j);
					p.setExplorerChasseur(i, j);
				}
			}
		}
		tailleZone++;
	}
}
