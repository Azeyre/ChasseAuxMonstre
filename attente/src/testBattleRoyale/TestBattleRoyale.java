package testBattleRoyale;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import game.Plateau;
import menu.Menu;
import options.BattleRoyale;

public class TestBattleRoyale {
	
	@Test
	void testRetrecit() {
		Menu.SIZE = 5;
		Plateau p = new Plateau();
		BattleRoyale br = new BattleRoyale(p);
		
		assertFalse(p.getCaseExplorer(0, 0));
		assertFalse(p.getCaseExplorer(0, 1));
		assertFalse(p.getCaseExplorer(1, 0));
		assertFalse(p.getCaseExplorer(0, 2));
		assertFalse(p.getCaseExplorer(2, 0));
		
		br.retrecit();
		
		assertTrue(p.getCaseExplorer(0, 0));
		assertTrue(p.getCaseExplorer(0, 1));
		assertTrue(p.getCaseExplorer(1, 0));
		assertTrue(p.getCaseExplorer(0, 2));
		assertTrue(p.getCaseExplorer(2, 0));
		
	}
	
	

}