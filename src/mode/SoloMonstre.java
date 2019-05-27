package mode;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JOptionPane;

import game.Chasseur;
import game.Monstre;
import game.Plateau;
import game.Position;
import ia.ChasseurIA;
import menu.MenuConsole;
import options.BattleRoyale;
import options.Teleport;

/**
 * Classe de SoloMonstre
 * @author KOZLOV-PC
 *
 */

public abstract class SoloMonstre {
	
	/**
	 * Permet de jouer en solo en �tant le monstre, jouant contre ChasseurIA
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void start() throws IOException {
		Plateau plateau = new Plateau();
		Monstre monstre = new Monstre();
		Chasseur chasseur = new Chasseur();
		ChasseurIA chasseurIA = new ChasseurIA(plateau);
		Position position;
		BattleRoyale br = new BattleRoyale(plateau);
		
		Object[] options = { "Gauche", "Droite", "Haut", "Bas" };

		boolean fin = false;
		boolean avertir = false;
		boolean monstreGagne = false;
		
		int tours = 0;
		int n;
		int x, y;
		int revealX = -1, revealY = -1;
		int scoreMonstre = 0;
		
		plateau.incrPos(monstre);
		
		while(!fin && !monstreGagne) {
			tours++;
			if(tours % 10 == 0 && MenuConsole.modeBR) {
				br.retrecit(chasseurIA);
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
			
			/*
			 * DEBUT DU TOUR POUR L'IA
			 */
			if(!monstreGagne && !fin) {
				position = chasseurIA.pos(plateau);
				chasseur.setPosition(position.getX(), position.getY());
				fin = plateau.reveal(position.getX(), position.getY(), monstre);
			}
			/*
			 * FIN DU TOUR DE L'IA
			 */
			monstreGagne = plateau.fini();
		}
		MenuConsole.saveScore(scoreMonstre, 'M');
		System.out.println("Fini!");
		if(monstreGagne) System.out.println("Le monstre a gagn� !");
		else System.out.println("Le chasseur a gagn� !");
		
		MenuConsole.main(null);
	}
	private static void clear() {
		for(int i = 0 ; i < 100 ; i++) System.out.println();
	}
}
