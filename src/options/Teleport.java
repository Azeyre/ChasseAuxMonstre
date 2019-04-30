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
		int x = randInt(), y = randInt();
		while(p.getCaseExplorer(x, y) && !p.fini()) {
			x = randInt();
			y = randInt();
		}
		m.setPosition(x, y);
	}
	private static int randInt() {
		return (int) (Math.random() * Menu.SIZE);
	}
}
