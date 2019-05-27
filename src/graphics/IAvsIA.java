package graphics;

import game.Chasseur;
import game.Monstre;
import ia.ChasseurIA;
import ia.MonstreIA;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.util.Duration;
import options.Teleport;

public class IAvsIA extends Game {
	
	private ChasseurIA chasseurIA;
	
	public IAvsIA(int size) {
		super("IA vs IA", size);
		run();
	}

	@Override
	protected void run() {
		c = new Chasseur();
		m = new Monstre();
		chasseurIA = new ChasseurIA(plateau);
		
		m.setup(size);
		plateau.incrPos(m);
		draw();
		
		translate = new TranslateTransition();
		translate.setDuration(Duration.millis(1000));
		translate.setToX(offsetX + (m.getX() * taille_case));
		translate.setToY(offsetY + (m.getY() * taille_case));
		translate.setNode(monstre);
		translate.play();
		
		PauseTransition pauseMonstre = new PauseTransition();
		pauseMonstre.setDuration(Duration.millis(1000));
		PauseTransition pauseChasseur = new PauseTransition();
		pauseChasseur.setDuration(Duration.millis(1000));

		pauseMonstre.play();
		pauseMonstre.setOnFinished(e -> {
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
			pauseChasseur.play();
		});
		pauseChasseur.setOnFinished(e -> {
			c.setJouer(true);
			if(!fini) {
				c.setPosition(chasseurIA.pos(plateau));
				if (reveal(c.getX(), c.getY()))
					victoireChasseur();
				else {
					System.out.println("Tour suivant");
					pauseMonstre.play();
				}
			}
		});
	}
	
	@Override
	protected void moveMonstre(int x, int y) {
		/*
		 * Deplacement du monstre sur le plateau
		 */
		tours++;
		plateau.incrPos(m);
		drawCasePleine();
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
			
			infoBas.setText("");
			PauseTransition pause = new PauseTransition(Duration.millis(1000));
			pause.play();
			pause.setOnFinished(e -> {
				m.setJouer(false);
				draw();
			});
		}
	}

	@Override
	protected boolean reveal(int x, int y) {
		/*
		 * Recuperation de l'ancienne position du chasseur pour pouvoir l'afficher au
		 * monstre
		 */
		int anciennePosition = plateau.getMonstreAnciennePosition(x, y);
		if (anciennePosition != -1) {
			infoBas.setText("Le monstre est passe par la il y a " + anciennePosition + " tours");
		} else
			infoBas.setText("Rien ici");
		if (retrecitBr) {
			infoBas.setText("Le plateau se reduit");
		}
		/*
		 * Compteur de 5 secondes pour passer à l'autre joueur
		 */
		draw();
		draw(x,y);
		PauseTransition pause = new PauseTransition(Duration.millis(1000));
		pause.play();
		pause.setOnFinished(e -> {
			Platform.runLater(new Runnable() {
				public void run() {
					c.setJouer(false);
					draw();
					monstre.setOpacity(1.0);
					infoBas.setText("");
					retrecitBr = ModeBR();
					if (retrecitBr) {
						infoBas.setText("Le plateau se reduit");
					}
					while (Mode_Tp && m.bloquer(plateau) && !plateau.fini()) {
						plateau.setExplorer(m.getX(), m.getY());
						Teleport.teleport(plateau, m);
						draw();
						infoBas.setText("Le monstre a ete teleporte");
						translate = new TranslateTransition();
						translate.setDuration(Duration.millis(1));
						translate.setToX(offsetX + (m.getX() * taille_case));
						translate.setToY(offsetY + (m.getY() * taille_case));
						translate.setNode(monstre);
						translate.play();
					}
					if (plateau.fini()) {
						victoireMonstre();
					}
				}
			});
		});

		anciennePositionMonstre[x][y] = plateau.getMonstreAnciennePosition(x, y);
		return plateau.reveal(x, y, m);
	}

}
