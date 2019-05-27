package options;

import game.Monstre;
import game.Plateau;
import menu.Menu;

public class Teleport {
	
	/**
	 * Teleporte le monstre sur une case non explore
	 * @param p
	 * @param m
	 */
	
	
	public static void teleport(Plateau p, Monstre m) {
		int x = randInt(p.getSize()), y = randInt(p.getSize());
		while(p.getCaseExplorer(x, y) && !p.fini()) {
			x = randInt(p.getSize());
			y = randInt(p.getSize());
		}
		m.setPosition(x, y);
	}
	private static int randInt(int size) {
		return (int) (Math.random() * size);
	}
}