package mode;

import game.Chasseur;
import game.Monstre;
import game.Plateau;
import game.Position;
import ia.ChasseurIA;
import ia.MonstreIA;
import menu.MenuConsole;
import options.BattleRoyale;
import options.Teleport;

public class IAvsIA {
	
	public static void main(String[] args) {
		MenuConsole.SIZE = 10;
		MenuConsole.debloquer = true;
		MenuConsole.modeBR = true;
		MenuConsole.monstreMange = true;
		start();
	}
	
	public static void start() {
		long current = System.currentTimeMillis();
		int victoireChasseur = 0, victoireMonstre = 0, monstreBloquer = 0, chasseurTrouveMonstre = 0;
		int total = 0;
		Plateau plateau;
		Monstre monstre;
		Chasseur chasseur;
		Position position;
		ChasseurIA chasseuria;
		BattleRoyale br;
		//Boucle principale, tant que le chasseur n'a pas trouv� le monstre ou que le monstre n'est plus de case a explor�
		
		while (total < 50000) {
			plateau = new Plateau();
			monstre = new Monstre();
			chasseur = new Chasseur();
			chasseuria = new ChasseurIA(plateau);
			position = null;
			br = new BattleRoyale(plateau);

			boolean fin = false;
			boolean monstreGagne = false;
			
			int tours = 0;
			int n = 0;
			int x = 0, y = 0;
			
			plateau.incrPos(monstre);

			while (!fin && !monstreGagne) {
				tours++;
				if(tours % 10 == 0 && MenuConsole.modeBR) {
					br.retrecit(chasseuria);
				}
				/*
				 * DEBUT TOUR POUR LE MONSTRE
				 */
				if(MenuConsole.debloquer) {
					while(monstre.bloquer(plateau) && !plateau.fini()) {
						plateau.setExplorer(monstre.getX(), monstre.getY());
						Teleport.teleport(plateau, monstre);
					}
				}
				do {
					n = MonstreIA.move();
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
				if(MenuConsole.monstreMange) {
					if(monstre.getPosition().equals(chasseur)) {
						System.out.println("Monstre mange le chasseur");
						monstreGagne = true;
					}
				}
				if(!MenuConsole.debloquer && monstre.bloquer(plateau)) {
					monstreBloquer++;
					fin = true; 
				} else if(MenuConsole.debloquer && plateau.fini()) monstreGagne = true;
				/*
				 * FIN DU TOUR POUR LE MONSTRE
				 */
				/*
				 * DEBUT DU TOUR POUR L'IA
				 */
				if(!monstreGagne && !fin && !plateau.fini()) {
					position = chasseuria.pos(plateau);
					chasseur.setPosition(position.getX(), position.getY());
					fin = plateau.reveal(position.getX(), position.getY(), monstre);
					if(fin) chasseurTrouveMonstre++;
				}
				/*
				 * FIN DU TOUR DE L'IA
				 */
				monstreGagne = plateau.fini();
			}
			total++;
			System.out.println("Fini!");
			if(monstreGagne) victoireMonstre++;
			else victoireChasseur++;
		}
		System.out.println("Chasseur trouve monstre : " + chasseurTrouveMonstre);
		System.out.println("Monstre bloquer : "+monstreBloquer);
		System.out.println("Victoire monstre : "+victoireMonstre);
		System.out.println("Victoire chasseur : "+victoireChasseur);
		System.out.println("Temps : " + (System.currentTimeMillis() - current) + "ms");
	}

}
