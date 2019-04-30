package ia;

import java.util.Random;

import game.Plateau;
import game.Position;
import menu.Menu;

/**
 * Classe de ChasseurIA
 * @author KOZLOV-PC
 *
 */
public class ChasseurIA {
	
	private boolean[][] caseExplorerParMonstre = new boolean[Menu.SIZE][Menu.SIZE];
	private Position position;
	private int proche = -1;
	private Plateau p;
	
	public ChasseurIA(Plateau p) {
		this.p = p;
		caseExplorerParMonstre = new boolean[Menu.SIZE][Menu.SIZE];
		this.position = null;
		this.proche = -1;
	}
	
	public Position pos(Plateau p) {
		this.p = p;
		int x, y;
		int rayon, aleaX, aleaY;
		boolean debug = false;
		boolean trouve = false;
		int boucle = 0;
		
		if(debug) System.out.println("Proche = " + proche);
		
		//Fait des tentatives aléatoire tant qu'il n'y a pas d'information sur la position du monstre
		if(proche == -1) {
			do {
				if(debug) System.out.println("Random");
				x = randomInt(0, Menu.SIZE-1);
				y = randomInt(0, Menu.SIZE-1);
				if(debug) System.out.println("Essai en : X=" + x  + " ; Y=" + y);
				proche = p.getMonstreAnciennePosition(x, y);
				/*
				 * Proche vaut -1 si le monstre n'est pas passé sur la case ciblé
				 * Proche = le nombre de tours depuis que le monstre est passé sur la case ciblé
				 * 
				 * Proche = -1 = Inconnu
				 * Proche = [0;x] = Monstre ancienne position il y a x tours
				 * 
				 * Si proche != -1 on sauvegarde la position [x;y]
				 */
				if(proche != -1) {
					position = new Position(x,y);
					trouve = true;
				}
				boucle++;
			} while(caseExplorerParMonstre[x][y] && boucle < Menu.SIZE && !trouve);
			if(trouve) caseExplorerParMonstre[x][y] = true;
		} else {
			//Si proche dépasse la taille du plateau, on refait des tentatives aléatoires
			if(proche > Menu.SIZE) {proche = -1; return pos(p);}
			do {
				if(debug) System.out.println("Ancienne position : X=" + position.getX()  + " ; Y=" + position.getY() + ", proche=" + proche);
				
				rayon = proche;
				if(debug) System.out.println("Rayon=" + rayon);
				
				aleaX = randomInt(-rayon,rayon);
				if(debug) System.out.println("AleaX=" + aleaX);
				
				x = position.getX() + aleaX;
				rayon = rayon - Math.abs(aleaX);
				if(debug) System.out.println("Rayon=" + rayon);
				
				aleaY = randomInt(-rayon,rayon);
				if(debug) System.out.println("AleaY=" + aleaY);
				
				y = position.getY() + aleaY;
				if(x < 0) x = Menu.SIZE + x;
				if(x >= Menu.SIZE) x = x - Menu.SIZE;
				if(y < 0) y = Menu.SIZE + y;
				if(y >= Menu.SIZE) y = y - Menu.SIZE; 
				if(debug) System.out.println("Essai en : X=" + x  + " ; Y=" + y);
				boucle++;
			} while(caseExplorerParMonstre[x][y] && !p.fini() && boucle < (proche * 4)); //Le monstre ne peut pas repasser sur une case qu'il a deja exploré
			if(p.getMonstreAnciennePosition(x, y) > -1) caseExplorerParMonstre[x][y] = true;
			//On sauvegarde la nouvelle position si seulement le monstre est plus proche de cette position là que celle sauvegarde
			if(p.getMonstreAnciennePosition(x, y) > -1 && p.getMonstreAnciennePosition(x, y) <= proche) {
				position = new Position(x,y);
				proche = p.getMonstreAnciennePosition(x, y);
			} else proche++; //A chaque tour le monstre s'éloigne
		}		
		return new Position(x, y);
	}
	
	private static int randomInt(int min, int max) {
		return new Random().nextInt(max - min + 1) + min;
	}
	
	public void setExplorer(int x, int y) {
		caseExplorerParMonstre[x][y] = true;
	}

}
