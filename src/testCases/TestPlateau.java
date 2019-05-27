package testCases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import game.Monstre;
import game.Plateau;
import menu.MenuConsole;

class TestPlateau {
	Plateau p;
	Monstre m;
	@Test
	void testGetMonstreAnciennePosition() {
		MenuConsole.SIZE = 5;
		p = new Plateau();
		m = new Monstre();
		
		m.setPosition(0, 0);
		p.incrPos(m);
		assertEquals(0, p.getMonstreAnciennePosition(0, 0));
		assertEquals(-1, p.getMonstreAnciennePosition(1, 1));
		

		m.setPosition(0, 1);
		p.incrPos(m);
		assertEquals(-1, p.getMonstreAnciennePosition(1, 1));
		assertEquals(1, p.getMonstreAnciennePosition(0, 0));
		assertEquals(0, p.getMonstreAnciennePosition(0, 1));
	}
	
	@Test
	void testReveal() {
		MenuConsole.SIZE = 5;
		p = new Plateau();
		m = new Monstre();
		m.setPosition(0, 0);
		
		assertTrue(p.reveal(0, 0, m));
		assertFalse(p.reveal(1, 1, m));
	}
	
	@Test
	void testCaseExplorer() {
		MenuConsole.SIZE = 5;
		p = new Plateau();
		m = new Monstre();
		m.setPosition(0, 0);
		p.incrPos(m);
		m.setPosition(0, 1);
		p.incrPos(m);
		
		assertTrue(p.getCaseExplorer(0, 0));
		assertTrue(p.getCaseExplorer(0, 1));
		assertFalse(p.getCaseExplorer(1, 1));
	}

}
