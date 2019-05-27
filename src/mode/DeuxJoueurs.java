package mode;

import java.io.IOException;

import javax.swing.JOptionPane;

import game.Chasseur;
import game.Monstre;
import game.Plateau;
import menu.MenuConsole;
import options.BattleRoyale;
import options.Teleport;

/**
 * Classe pour le mode de jeux 2 joueurs
 * 
 * @author KOZLOV-PC
 */

public abstract class DeuxJoueurs {

	/**
	 * Boucle principale du jeu, � 2 joueurs en LOCAL <br>
	 * Initialisation variable : - Plateau � une taille
	 * donn� SIZE <br>
	 *  - Instance de Monstre et Chasseur
	 * <br>
	 * <br>
	 * Actions faites par tour : - Affichage du plateau pour le monstre <br> - Demande au
	 * monstre son d�placement <br> - D�placement du monstre en fonction du choix <br>-
	 * Incr�mentation des anciens d�placements <br> - Affichage du plateau pour le
	 * chasseur <br> - Choix du chasseur pour rev�ler la case <br> - Revele la case choisit
	 * par le chasseur
	 * <br>
	 * <br>
	 * Boucle se termine lorsque le chasseur trouve le monstre ou lorsque toutes les
	 * case ont �t� jou� par le monstre
	 * @param args - rien
	 */
	
	public static void start() throws IOException {
		Plateau plateau = new Plateau();
		Monstre monstre = new Monstre();
		Chasseur chasseur = new Chasseur();
		BattleRoyale br = new BattleRoyale(plateau);
		
		Object[] options = { "Gauche", "Droite", "Haut", "Bas" };

		boolean fin = false;
		boolean avertir = false;
		boolean monstreGagne = false;
		
		int tours = 0;
		int n;
		int x, y;
		int revealX = -1, revealY = -1;
		int scoreMonstre = 0, scoreChasseur = 0;
		
		plateau.incrPos(monstre);
		String entreChasseur;
		//Boucle principale, tant que le chasseur n'a pas trouv� le monstre ou que le monstre n'est plus de case a explor�
		while (!fin && !monstreGagne) {
			System.out.println("Nouveau tour");
			tours++;
			if(tours % 10 == 0 && MenuConsole.modeBR) {
				br.retrecit();
			}
			/*
			 * DEBUT TOUR POUR LE MONSTRE
			 */
			clear();
			JOptionPane.showMessageDialog(null,"Debut du tour n�" + tours,"Monstre",JOptionPane.INFORMATION_MESSAGE);
			plateau.affichage(monstre, chasseur);
			if(MenuConsole.debloquer) {
				while(monstre.bloquer(plateau) && !plateau.fini()) {
					plateau.setExplorer(monstre.getX(), monstre.getY());
					Teleport.teleport(plateau, monstre);
					plateau.affichage(monstre, chasseur);
					System.out.println("Bloqu� ! TP en cours...");
					avertir = true;
				}
			}
			do {
				/*
				 * Fenetre demandant au joueur de faire le d�placement du monstre selon 4 choix possibles : Gauche, Droite, Haut, Bas
				 */
				n = JOptionPane.showOptionDialog(null, "Selection du mouvement (fermer = BAS)", "Monstre",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				
				/*
				 * X, Y prennent des valeurs selon le choix du d�placement du joueur
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
			clear();
			plateau.affichage(monstre, chasseur);
			JOptionPane.showMessageDialog(null,"Fin du tour","Monstre",JOptionPane.INFORMATION_MESSAGE);
			scoreMonstre++;
			if(MenuConsole.monstreMange) {
				if(monstre.getPosition().equals(chasseur)) {
					System.out.println("Monstre mange le chasseur");
					monstreGagne = true;
				}
			}
			if(!MenuConsole.debloquer && monstre.bloquer(plateau)) fin = true; 
			/*
			 * FIN DU TOUR POUR LE MONSTRE
			 */
			
			if(!monstreGagne && !fin) {
				/*
				 * DEBUT TOUR POUR LE CHASSEUR
				 */
				clear();
				JOptionPane.showMessageDialog(null,"Debut du tour n�" + tours,"Chasseur",JOptionPane.INFORMATION_MESSAGE);
				if(avertir) {
					JOptionPane.showMessageDialog(null,"Le monstre s'est bloqu�, il s'est t�l�port� al�atoirement.","Chasseur",JOptionPane.INFORMATION_MESSAGE);
					avertir = false;
				}
				plateau.affichage(chasseur, null);
				do {
					/*
					 * Fenetre demandant au chasseur de reveler la case de son choix
					 */
					entreChasseur = (String) JOptionPane.showInputDialog(null, "S�lection de la case (ex. A6 ou 6A)", "Chasseur", JOptionPane.QUESTION_MESSAGE,null, null,"");
					revealX = stringToX(entreChasseur);
					revealY = stringToY(entreChasseur);
				} while(revealX == -1 || revealY == -1);
				chasseur.setPosition(revealX, revealY);
				clear();
				fin = plateau.reveal(revealX, revealY, monstre);
				plateau.affichage(chasseur, null);
				JOptionPane.showMessageDialog(null,"Fin du tour","Chasseur",JOptionPane.INFORMATION_MESSAGE);
				scoreChasseur++;
				/*
				 * FIN DU TOUR POUR LE CHASSEUR
				 */
				monstreGagne = plateau.fini();
			}
		}
		MenuConsole.saveScore(scoreMonstre, scoreChasseur);
		System.out.println("Fini!");
		if(monstreGagne) System.out.println("Le monstre a gagn� !");
		else System.out.println("Le chasseur a gagn� !");
		
		MenuConsole.main(null);
	}

	/**
	 * Transforme le 'A6' en coordonn� X
	 * @param s : String �crit par le chasseur
	 * @return [0;(SIZE-1)] <b>int</b>
	 */
	private static int stringToX(String s) {
		if(s == null || s.length() != 2) {
			return -1;
		}
		if(s.charAt(0) >= '0' && s.charAt(0) <= '0' + MenuConsole.SIZE - 1) return Character.getNumericValue(s.charAt(0));
		if(s.charAt(1) >= '0' && s.charAt(1) <= '0' + MenuConsole.SIZE - 1) return Character.getNumericValue(s.charAt(1));
		return -1;
	}
	
	/**
	 * Transforme le 'A6' en coordonn� Y
	 * @param y : String �crit par le chasseur
	 * @return [0;(SIZE-1)] <b>int</b>
	 */
	private static int stringToY(String s) {
		if(s== null || s.length() != 2) {
			return -1;
		}
		if(s.charAt(0) >= 'A' && s.charAt(0) <= ('A' + (MenuConsole.SIZE-1))) return (int)(s.charAt(0) - 'A');
		if(s.charAt(1) >= 'A' && s.charAt(1) <= ('A' + (MenuConsole.SIZE-1))) return (int)(s.charAt(1) - 'A');
		if(s.charAt(0) >= 'a' && s.charAt(0) <= ('a' + (MenuConsole.SIZE-1))) return (int)(s.charAt(0) - 'a');
		if(s.charAt(1) >= 'a' && s.charAt(1) <= ('a' + (MenuConsole.SIZE-1))) return (int)(s.charAt(1) - 'a');
		return -1;
	}
	
	/*
	 * Faire un espace dans la console pour passer au joueur suivant
	 */
	private static void clear() {
		for(int i = 0 ; i < 100 ; i++) System.out.println();
	}
}
