package mode;

import java.io.IOException;

import javax.swing.JOptionPane;

import game.Chasseur;
import game.Monstre;
import game.Plateau;
import ia.MonstreIA;
import menu.Menu;
import options.BattleRoyale;
import options.Teleport;

public abstract class SoloChasseur {

	/**
	 * Boucle principale du jeu, ï¿½ 2 joueurs en LOCAL <br>
	 * Initialisation variable : - Plateau ï¿½ une taille
	 * donnï¿½ SIZE <br>
	 *  - Instance de Monstre et Chasseur
	 * <br>
	 * <br>
	 * Actions faites par tour : - Affichage du plateau pour le monstre <br> - Demande au
	 * monstre son dï¿½placement <br> - Dï¿½placement du monstre en fonction du choix <br>-
	 * Incrï¿½mentation des anciens dï¿½placements <br> - Affichage du plateau pour le
	 * chasseur <br> - Choix du chasseur pour revï¿½ler la case <br> - Revele la case choisit
	 * par le chasseur
	 * <br>
	 * <br>
	 * Boucle se termine lorsque le chasseur trouve le monstre ou lorsque toutes les
	 * case ont ï¿½tï¿½ jouï¿½ par le monstre
	 * @param args - rien
	 * @throws IOException
	 */
	
	public static void start() throws IOException {
		Plateau plateau = new Plateau();
		Monstre monstre = new Monstre();
		Chasseur chasseur = new Chasseur();
		BattleRoyale br = new BattleRoyale(plateau);

		boolean fin = false;
		boolean avertir = false;
		boolean monstreGagne = false;
		
		int tours = 0;
		int n;
		int x, y;
		int revealX = -1, revealY = -1;
		int scoreChasseur = 0;
		
		plateau.incrPos(monstre);
		String entreChasseur;
		//Boucle principale, tant que le chasseur n'a pas trouvï¿½ le monstre ou que le monstre n'est plus de case a explorï¿½
		while (!fin && !monstreGagne) {
			tours++;
			if(tours % 10 == 0 && Menu.modeBR) {
				br.retrecit();
			}
			/*
			 * DEBUT TOUR POUR LE MONSTRE
			 */
			clear();
			if(Menu.debloquer) {
				while(monstre.bloquer(plateau) && !plateau.fini()) {
					plateau.setExplorer(monstre.getX(), monstre.getY());
					Teleport.teleport(plateau, monstre);
					System.out.println("Bloqué ! TP en cours...");
					avertir = true;
				}
			}
			do {
				
				n = MonstreIA.move();
				/*
				 * X, Y prennent des valeurs selon le choix du dï¿½placement du joueur
				 */
				if (n == 0) {
					x = 0;
					y = -1;
				} else if (n == 1) {
					x = 0;
					y = 1;
				} else if (n == 2) {
					x = -1;
					y = 0;
				} else {
					x = 1;
					y = 0;
				}
			} while (!monstre.move(x, y, plateau) && !monstre.bloquer(plateau));
			plateau.incrPos(monstre);
			if(Menu.monstreMange) {
				if(monstre.getPosition().equals(chasseur)) {
					System.out.println("Monstre mange le chasseur");
					monstreGagne = true;
				}
			}
			if(!Menu.debloquer && monstre.bloquer(plateau)) fin = true; 
			/*
			 * FIN DU TOUR POUR LE MONSTRE
			 */
			
			/*
			 * DEBUT TOUR POUR LE CHASSEUR
			 */
			if(!monstreGagne && !fin) {
				/*
				 * DEBUT TOUR POUR LE CHASSEUR
				 */
				clear();
				JOptionPane.showMessageDialog(null,"Debut du tour n°" + tours,"Chasseur",JOptionPane.INFORMATION_MESSAGE);
				if(avertir) {
					JOptionPane.showMessageDialog(null,"Le monstre s'est bloqué, il s'est téléporté aléatoirement.","Chasseur",JOptionPane.INFORMATION_MESSAGE);
					avertir = false;
				}
				plateau.affichage(chasseur, null);
				do {
					/*
					 * Fenetre demandant au chasseur de reveler la case de son choix
					 */
					entreChasseur = (String) JOptionPane.showInputDialog(null, "Sélection de la case (ex. A6 ou 6A)", "Chasseur", JOptionPane.QUESTION_MESSAGE,null, null,"");
					revealX = stringToX(entreChasseur);
					revealY = stringToY(entreChasseur);
				} while(revealX == -1 || revealY == -1);
				chasseur.setPosition(revealX, revealY);
				fin = plateau.reveal(revealX, revealY, monstre);
				plateau.affichage(chasseur, null);
				JOptionPane.showMessageDialog(null,"Fin du tour","Chasseur",JOptionPane.INFORMATION_MESSAGE);
				scoreChasseur++;
				/*
				 * FIN DU TOUR POUR LE CHASSEUR
				 */
				monstreGagne = plateau.fini();
			}
			/*
			 * FIN DU TOUR POUR LE CHASSEUR
			 */
		}
		Menu.saveScore(scoreChasseur, 'C');
		System.out.println("Fini!");
		if(monstreGagne) System.out.println("Le monstre a gagné !");
		else System.out.println("Le chasseur a gagné !");
		
		Menu.main(null);
	}

	/**
	 * Transforme le 'A6' en coordonnï¿½ X
	 * @param s : String ï¿½crit par le chasseur
	 * @return [0;(SIZE-1)] <b>int</b>
	 */
	private static int stringToX(String s) {
		if(s == null || s.length() != 2) {
			return -1;
		}
		if(s.charAt(0) >= '0' && s.charAt(0) <= '0' + Menu.SIZE - 1) return Character.getNumericValue(s.charAt(0));
		if(s.charAt(1) >= '0' && s.charAt(1) <= '0' + Menu.SIZE - 1) return Character.getNumericValue(s.charAt(1));
		return -1;
	}
	
	/**
	 * Transforme le 'A6' en coordonnï¿½ Y
	 * @param y : String ï¿½crit par le chasseur
	 * @return [0;(SIZE-1)] <b>int</b>
	 */
	private static int stringToY(String s) {
		if(s== null || s.length() != 2) {
			return -1;
		}
		if(s.charAt(0) >= 'A' && s.charAt(0) <= ('A' + (Menu.SIZE-1))) return (int)(s.charAt(0) - 'A');
		if(s.charAt(1) >= 'A' && s.charAt(1) <= ('A' + (Menu.SIZE-1))) return (int)(s.charAt(1) - 'A');
		if(s.charAt(0) >= 'a' && s.charAt(0) <= ('a' + (Menu.SIZE-1))) return (int)(s.charAt(0) - 'a');
		if(s.charAt(1) >= 'a' && s.charAt(1) <= ('a' + (Menu.SIZE-1))) return (int)(s.charAt(1) - 'a');
		return -1;
	}
	
	/*
	 * Faire un espace dans la console pour passer au joueur suivant
	 */
	private static void clear() {
		for(int i = 0 ; i < 100 ; i++) System.out.println();
	}
}
