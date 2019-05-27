package graphics;

import game.Chasseur;
import game.Monstre;
import ia.ChasseurIA;
import ia.MonstreIA;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import options.Teleport;

public class UnJoueurChasseur extends Game {
	
	ChasseurIA chasseurIA;
	
	public UnJoueurChasseur(Joueur j, int size) {
		super("Chasse au Monstre : Monstre", size);
		c = (Chasseur) j;
		run();
		
		middle.setOnMouseClicked(e -> {
			int x = (int) (e.getX() / taille_case);
			int y = (int) (e.getY() / taille_case);
			if(c.peutJouer()) {
				if(reveal(x,y)) {
					fini = true;
					victoireChasseur();
					monstre.setOpacity(1.0);
				} else if(plateau.fini()) {
					fini = true;
					victoireMonstre();
				} else {
					draw(x,y);
					c.setPosition(x, y);
					move();
				}
			}
		});
	}

	@Override
	protected void run() {
		m = new Monstre();
		
		m.setup(size);
		monstre.setOpacity(0.0);
		m.setJouer(true);
		plateau.incrPos(m);
		draw();
		move();
		
		translate = new TranslateTransition();
		translate.setDuration(Duration.millis(1000));
		translate.setToX(offsetX + (m.getX() * taille_case));
		translate.setToY(offsetY + (m.getY() * taille_case));
		translate.setNode(monstre);
		translate.play();
	}
	
	private void move() {
		m.setJouer(true);
		int x = 0, y = 0;
		do {
			int n = MonstreIA.move();
			switch (n) {
			case 0:
				x = 1;
				y = 0;
				break;
			case 1:
				x = -1;
				y = 0;
				break;
			case 2:
				x = 0;
				y = 1;
				break;
			case 3:
				x = 0;
				y = -1;
				break;
			}
		} while (!m.move(x, y, plateau) && !plateau.fini() && !m.bloquer(plateau));
		moveMonstre(x,y);
	}

	@Override
	protected void moveMonstre(int x, int y){
		/*
		 * Deplacement du monstre sur le plateau
		 */
		loupAudio();
		m.setJouer(false);
		tours++;
		plateau.incrPos(m);
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (anciennePositionMonstre[i][j] > 0)
					anciennePositionMonstre[i][j]++;
			}
		}
		if (!Mode_Tp && m.bloquer(plateau) && !plateau.fini()) {
			fini = true;
			victoireChasseur();
		} else {
			/*
			 * Creation d'une animation pour le deplacement du monstre
			 */
			loupMediaPlayer.stop();
			translate = new TranslateTransition();
			translate.setDuration(Duration.millis(1000));
			translate.setToX(offsetX + (m.getX() * taille_case));
			translate.setToY(offsetY + (m.getY() * taille_case));
			translate.setNode(monstre);
			translate.play();
			
			infoBas.setText("");
			c.setJouer(true);
			draw();
		}
	}
	
	@Override
	protected boolean reveal(int x, int y) {
		tirAudio();
		int anciennePosition = plateau.getMonstreAnciennePosition(x, y);
		if(anciennePosition != -1) {
			infoBas.setText("Le monstre est passe par la il y a " + anciennePosition + " tours");
		} else infoBas.setText("Rien ici");
		c.setJouer(false);
		if(retrecitBr) {
			infoBas.setText("Le plateau se reduit");
		}
		/*
		 * Compteur de 5 secondes pour passer à l'autre joueur
		 */
		tirMediaPlayer.stop();
		Platform.runLater(new Runnable() {
			public void run() {
				if(!fini) {
					m.setJouer(true);
					infoBas.setText("");
					retrecitBr = ModeBR();
					if(retrecitBr) {
						infoBas.setText("Le plateau se reduit");
					}
					while(Mode_Tp && m.bloquer(plateau) && !plateau.fini()) {
						plateau.setExplorer(m.getX(), m.getY());
						Teleport.teleport(plateau, m);
						infoBas.setText("Le monstre a ete teleporte");
						translate = new TranslateTransition();
						translate.setDuration(Duration.millis(1));
						translate.setToX(offsetX + (m.getX() * taille_case));
						translate.setToY(offsetY + (m.getY() * taille_case));
						translate.setNode(monstre);
						translate.play();
					}	
					if(plateau.fini()) {
						fini = true;
						victoireMonstre();
					}
				}
			}
		});
		anciennePositionMonstre[x][y] = plateau.getMonstreAnciennePosition(x, y);
		return plateau.reveal(x, y, m);
	}
}
