package graphics;

import game.Chasseur;
import game.Monstre;
import game.Position;
import ia.ChasseurIA;
import ia.MonstreIA;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class UnJoueur extends Game {

	ChasseurIA chasseurIa;
	
	public UnJoueur(Joueur j1, int size) {
		super("Chasse au Monstre : 1 Joueur", size);
		
		if(j1.estMonstre()) {
			c = new Chasseur();
			chasseurIa = new ChasseurIA(plateau);
			m = (Monstre) j1;
			m.setJouer(true);
		} else {
			m = new Monstre();
			c = (Chasseur) j1;
			c.setJouer(false);
		}
		middle.setOnMouseClicked(e -> {
			int x = (int) (e.getX() / taille_case);
			int y = (int) (e.getY() / taille_case);
			System.out.println("Tape " + x + "   " + y);
			if(c.peutJouer() && !j1.estMonstre()) {
				if(reveal(x,y)) {
					victoireChasseur();
				} 
				draw(x,y);
				c.setPosition(x, y);
			} else if(m.peutJouer() && j1.estMonstre()) {
				monstre.setOpacity(1.0);
				draw();
				draw(c.getX(), c.getY());
				info.setText("Monstre : deplacez vous !");
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

	protected void moveMonstre(int x, int y) {
		if(m.move(x, y, plateau)) {
			plateau.incrPos(m);
			m.setJouer(false);
			drawCasePleine();
			
			translate = new TranslateTransition();
			translate.setDuration(Duration.millis(1000));
			translate.setToX(offsetX + (m.getX() * taille_case));
			translate.setToY(offsetY + (m.getY() * taille_case));
			translate.setNode(monstre);
			translate.play();
			
			fade = new FadeTransition();
			fade.setDuration(Duration.millis(1000));
			fade.setNode(monstre);
			fade.setFromValue(1);
			fade.setToValue(0);
			fade.play();
			//audioLoup();
			
			sec = 1;
			PauseTransition pause = new PauseTransition();
			pause.setDuration(Duration.millis(1000));
			pause.setDelay(Duration.ZERO);
			info.setText("" + sec + " secondes !");
			pause.setOnFinished(e -> {
				if(sec > 1) {
					sec--;
					info.setText("" + sec + " secondes !");
					pause.play();
				} else {
					info.setText("Au chasseur de jouer !");
					Position p = chasseurIa.pos(plateau);
					if(reveal(p.getX(), p.getY())) {
						System.out.println("LE CHASSEUR A GAGNE WOLA");
						System.exit(0);
					} 
					draw(p.getX(), p.getY());
					c.setPosition(p.getX(), p.getY());	
				}
			});
			pause.play();
		} else infoBas.setText("Case explorée ! Impossible d'y aller !");
	}
	
	protected boolean reveal(int x, int y) {
		int anciennePosition = plateau.getMonstreAnciennePosition(x, y);
		sec = 1;
		PauseTransition pause = new PauseTransition();
		pause.setDuration(Duration.millis(1000));
		pause.setDelay(Duration.ZERO);
		info.setText("" + sec + " secondes !");
		pause.setOnFinished(e -> {
			if(sec > 1) {
				sec--;
				info.setText("" + sec + " secondes !");
				pause.play();
			} else {
				reset();
				m.setJouer(true);
				info.setText("Monstre : cliquer pour afficher votre position.");
				infoBas.setText("");
			}
		});
		pause.play();
		//audioShoot();
		return plateau.reveal(x, y, m);
	}

	@Override
	protected void run() {
		m.setup(size);
		plateau.incrPos(m);
		draw();
		translate = new TranslateTransition();
		translate.setDuration(Duration.millis(1000));
		translate.setToX(offsetX + (m.getX() * taille_case));
		translate.setToY(offsetY + (m.getY() * taille_case));
		translate.setNode(monstre);
		translate.play();
	}
}
