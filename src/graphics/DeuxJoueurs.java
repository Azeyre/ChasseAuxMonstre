package graphics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Timer;

import game.Chasseur;
import game.Monstre;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class DeuxJoueurs extends Game {
	
	private Timer t;
	
	public DeuxJoueurs(Joueur j1, Joueur j2) {
		super("Chasse aux monstres : 2 joueurs");
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
			System.out.println("Tape " + x + "   " + y);
			if(c.peutJouer()) {
				if(reveal(x,y)) {
					fini = true;
					victoireChasseur();
				} 
				draw(x,y);
				c.setPosition(x, y);
			} else if(m.peutJouer()) {
				monstre.setOpacity(1.0);
				draw();
				draw(c.getX(), c.getY());
				info.setText("Monstre : d�placez vous !");
			} else if(m.peutJouer() && monstre.getOpacity() == 1.0) {
				
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
		loop();
	}

	@Override
	public void loop() {
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

	private void victoireChasseur() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		Font titre = new Font("Arial", 40);
		try {
			titre = Font.loadFont(new FileInputStream(new File("ressources/font/8-BIT_WONDER.TTF")), 40);
		} catch (FileNotFoundException e) {
			System.err.println("La police de caractère 8-Bit-Wonder.tff est introuvable.");
		}
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		Label chasseurTitre = new Label("CHASSEUR");
		Label winTitre = new Label("WIN");
		chasseurTitre.setFont(titre);
		winTitre.setFont(titre);
		chasseurTitre.setId("titre");
		winTitre.setId("titre");
		chasseurTitre.setLayoutX(100);
		vbox.getChildren().addAll(chasseurTitre, winTitre);
		
		
		ImageView imgChasseur = new ImageView(new Image("file:ressources/img/chasseur.png",100*5,70*5,true,true));
		imgChasseur.setOpacity(0.0);
		middle.getChildren().addAll(imgChasseur, vbox);
		
		FadeTransition fadeInChasseur = new FadeTransition();
		fadeInChasseur.setDuration(Duration.millis(1000));
		fadeInChasseur.setNode(imgChasseur);
		fadeInChasseur.setFromValue(0.0);
		fadeInChasseur.setToValue(1.0);
		fadeInChasseur.play();
		
		
	}
}
