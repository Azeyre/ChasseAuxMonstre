package graphics;

import game.Chasseur;
import game.Monstre;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import options.Teleport;

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
	 				} else {
	 					infoBas.setTextFill(Color.RED);
	 					infoBas.setText("Case selectionnee impossible");
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
	
	protected void moveMonstre(int x, int y) {
		/*
		 * Deplacement du monstre sur le plateau
		 */
		if(m.move(x, y, plateau)) {
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
				
				
				sec = 5;
				/*
				 * Compteur de 5 secondes pour passer à l'autre joueur
				 */
				PauseTransition pause = new PauseTransition();
				pause.setDuration(Duration.millis(1000));
				pause.setDelay(Duration.ZERO);
				info.setText("" + sec + " secondes");
				pause.setOnFinished(e -> {
					if(sec > 1) {
						sec--;
						info.setText("" + sec + " secondes");
						pause.play();
					} else if(!fini){
						c.setJouer(true);
						info.setText("Au chasseur de jouer");
						draw();
						if(retrecitBr) {
							infoBas.setText("Le plateau se reduit");
						}
						retrecitBr = false;
					}
				});
				pause.play();
			}
			
		} else infoBas.setText("Impossible la case est deja exploree");
	}
	
	protected boolean reveal(int x, int y) {
		/*
		 * Recuperation de l'ancienne position du chasseur pour pouvoir l'afficher au monstre
		 */
		int anciennePosition = plateau.getMonstreAnciennePosition(x, y);
		if(anciennePosition != -1) {
			infoBas.setText("Le monstre est passe par la il y a " + anciennePosition + " tours");
		} else infoBas.setText("Rien ici");
		c.setJouer(false);
		sec = 5;
		if(retrecitBr) {
			infoBas.setText("Le plateau se reduit");
		}
		/*
		 * Compteur de 5 secondes pour passer à l'autre joueur
		 */
		PauseTransition pause = new PauseTransition();
		pause.setDuration(Duration.millis(1000));
		pause.setDelay(Duration.ZERO);
		info.setText("" + sec + " secondes");
		pause.setOnFinished(e -> {
			if(sec > 1) {
				if(fini) {
					sec = 0;
					info.setText("");
					infoBas.setText("");
					pause.stop();
				} else {
					sec--;
					info.setText("" + sec + " secondes");
					pause.play();
				}
			} else if(!fini) {
				/*
				 * Attente du clique sur le canvas de la part du monstre pour pouvoir afficher 
				 */
				Platform.runLater(new Runnable() {
					public void run() {
						reset();
						m.setJouer(true);
						info.setText("Cliquez pour afficher le monstre");
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
							victoireMonstre();
						}
					}
				});
			}
		});
		anciennePositionMonstre[x][y] = plateau.getMonstreAnciennePosition(x, y);
		pause.play();
		return plateau.reveal(x, y, m);
	}
	
}
