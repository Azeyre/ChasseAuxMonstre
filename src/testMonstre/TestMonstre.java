package testMonstre;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import game.Monstre;
import game.Plateau;
import menu.MenuConsole;

class TestMonstre {

	@Test
	void testMove() {
		MenuConsole.SIZE = 5;
		Plateau p = new Plateau();
		Monstre m = new Monstre();
		
		m.setPosition(0, 1);
		p.setExplorer(1, 1);
		
		assertFalse(m.move(1, 0, p));
		assertTrue(m.move(0, 1, p));
		assertTrue(m.move(0, -1, p));
		assertTrue(m.move(-1, 0, p));
	}
	
	@Test
	void testBloquer() {
		MenuConsole.SIZE = 5;
		Plateau p = new Plateau();
		Monstre m = new Monstre();
		
		m.setPosition(1, 1);
		p.setExplorer(0, 1);
		p.setExplorer(1, 0);
		p.setExplorer(1, 2);
		p.setExplorer(2, 1);
		
		assertTrue(m.bloquer(p));
		
		m.setPosition(4, 4);
		
		assertFalse(m.bloquer(p));
		
	}

}
