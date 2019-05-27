package graphics;

import game.Chasseur;
import game.Monstre;
import ia.ChasseurIA;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import options.Teleport;

public class UnJoueurMonstre extends Game {

	ChasseurIA chasseurIA;
	
	public UnJoueurMonstre(Joueur j, int size) {
		super("Chasse au Monstre : Monstre", size);
		m = (Monstre) j;
		
		scene.setOnKeyPressed(e -> {
			if(m.peutJouer() && monstre.getOpacity() == 1.0 && !fini) {
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
		
		middle.setOnMouseClicked(e -> {
			if (!fini) {
				int x = (int) (e.getX() / taille_case);
				int y = (int) (e.getY() / taille_case);
				if (m.peutJouer() && monstre.getOpacity() == 1.0) {
					/*
					 * Permet le deplacement du monstre avec la souris
					 */
					int deltaX = x - m.getX();
					int deltaY = y - m.getY();
					System.out.println("X:" + deltaX + " ; Y:" + deltaY);
					if (Math.abs(deltaX) + Math.abs(deltaY) == 1) {
						moveMonstre(deltaX, deltaY);
					}
					/*
					 * Deplacement a partir du bord du plateau a un autre
					 */
					else if (deltaX == 0 && Math.abs(deltaY) == size - 1) {
						if (deltaY > 0)
							moveMonstre(0, -1);
						else
							moveMonstre(0, 1);
					} else if (deltaY == 0 && Math.abs(deltaX) == size - 1) {
						if (deltaX > 0)
							moveMonstre(-1, 0);
						else
							moveMonstre(1, 0);
					} else {
						infoBas.setTextFill(Color.RED);
						infoBas.setText("Case selectionnee impossible");
					}
				}
			}
		});
	}

	@Override
	protected void run() {
		chasseurIA = new ChasseurIA(plateau);
		c = new Chasseur();
		
		m.setup(size);
		m.setJouer(true);
		plateau.incrPos(m);
		draw();
		
		translate = new TranslateTransition();
		translate.setDuration(Duration.millis(1000));
		translate.setToX(offsetX + (m.getX() * taille_case));
		translate.setToY(offsetY + (m.getY() * taille_case));
		translate.setNode(monstre);
		translate.play();
	}

	@Override
	protected void moveMonstre(int x, int y) {
		if(m.move(x, y, plateau)) {
			loupAudio();
			tours++;
			plateau.incrPos(m);
			m.setJouer(false);
			drawCasePleine();
			for(int i = 0 ; i < size ; i++) {
				for(int j = 0 ; j < size ; j++) {
					if(anciennePositionMonstre[i][j] > 0) anciennePositionMonstre[i][j]++;
				}
			}
			if(!Mode_Tp && m.bloquer(plateau) && !plateau.fini()) {
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
				info.setText("Au chasseur de jouer");
				if(retrecitBr) {
					infoBas.setText("Le plateau se reduit");
				}
				retrecitBr = false;
				PauseTransition pause = new PauseTransition(Duration.millis(1000));
				pause.setOnFinished(e -> {reveal();});
				pause.play();
			}
			
		} else infoBas.setText("Impossible la case est deja exploree");
	}

	private void reveal() {
		c.setPosition(chasseurIA.pos(plateau));
		if (reveal(c.getX(), c.getY())) {
			fini = true;
			victoireChasseur();
		}
		draw(c.getX(), c.getY());
	}
	
	@Override
	protected boolean reveal(int x, int y) {
		tirAudio();
		int anciennePosition = plateau.getMonstreAnciennePosition(x, y);
		if(anciennePosition != -1) {
			infoBas.setText("Le monstre est passe par la il y a " + anciennePosition + " tours");
		} else infoBas.setText("Rien ici");
		c.setJouer(false);
		sec = 1;
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
					reset();
					m.setJouer(true);
					infoBas.setText("");
					retrecitBr = ModeBR();
					if(retrecitBr) {
						infoBas.setText("Le plateau se reduit");
					}
					while(Mode_Tp && m.bloquer(plateau) && !plateau.fini()) {
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
					if(plateau.fini()) {
						fini = true;
						victoireMonstre();
					}
					draw();
					draw(c.getX(), c.getY());
				}
			}
		});
		anciennePositionMonstre[x][y] = plateau.getMonstreAnciennePosition(x, y);
		return plateau.reveal(x, y, m);
	}

}
