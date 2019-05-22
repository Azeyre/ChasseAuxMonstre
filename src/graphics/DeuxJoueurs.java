package graphics;

import game.Chasseur;
import game.Monstre;
import javafx.animation.TranslateTransition;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class DeuxJoueurs extends Game {
	
	public DeuxJoueurs(Joueur j1, Joueur j2, int size) {
		super("Chasse aux monstres : 2 joueurs", size);
			if(j1.estMonstre()) {
				m = (Monstre) j1;
				c = (Chasseur) j2;
			} else {
				c = (Chasseur) j1;
				m = (Monstre) j2;
			}
			middle.setOnMouseClicked(e -> {
				int x = (int) (e.getX() / taille_case);
				int y = (int) (e.getY() / taille_case);
				if(c.peutJouer()) {
					if(reveal(x,y)) {
						fini = true;
						victoireChasseur();
					} else if(plateau.fini()) {
						fini = true;
						victoireMonstre();
					} else {
						draw(x,y);
						c.setPosition(x, y);
					}
				} else if(m.peutJouer() && monstre.getOpacity() == 1.0) {
					/*
					 * Permet le deplacement du monstre avec la souris
					 */
					int deltaX = x - m.getX();
					int deltaY = y - m.getY();
					System.out.println("X:" + deltaX + " ; Y:" + deltaY);
					if(Math.abs(deltaX) + Math.abs(deltaY) == 1) { 
						moveMonstre(deltaX, deltaY);
					} 
					/*
					 * Deplacement a partir du bord du plateau a un autre
					 */
					else if(deltaX == 0 && Math.abs(deltaY) == size - 1) {
						if(deltaY > 0) moveMonstre(0, -1);
						else moveMonstre(0, 1);
	 				} else if(deltaY == 0 && Math.abs(deltaX) == size - 1) {
	 					if(deltaX > 0) moveMonstre(-1, 0);
						else moveMonstre(1, 0);
	 				}
				} else if(m.peutJouer()) {
					monstre.setOpacity(1.0);
					draw();
					draw(c.getX(), c.getY());
					info.setText("Deplacez vous");
				}
			});
			scene.setOnKeyPressed(e -> {
				if(m.peutJouer() && monstre.getOpacity() == 1.0) {
					KeyCode k = e.getCode();
					switch(k) {
						case UP: moveMonstre(0,-1); break;
						case DOWN: moveMonstre(0,1); break;
						case LEFT: moveMonstre(-1,0); break;
						case RIGHT: moveMonstre(1,0); break;
					}
				}
			});
			run();
	}

	@Override
	public void run() {
		m.setup(size);
		plateau.incrPos(m);
		draw();
		
		translate = new TranslateTransition();
		translate.setDuration(Duration.millis(1000));
		translate.setToX(offsetX + (m.getX() * taille_case));
		translate.setToY(offsetY + (m.getY() * taille_case));
		translate.setNode(monstre);
		translate.play();
		
		m.setJouer(true);
		c.setJouer(false);
	}
}
