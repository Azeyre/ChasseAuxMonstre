package graphics;

import java.io.File;

import game.Chasseur;
import game.Monstre;
import game.Plateau;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public abstract class Game {

	protected final int WIDTH = 600, HEIGHT = 800;
	protected ImageView monstre, backPlateau;
	protected int taille_case;
	protected int tours;
	protected Stage stage;
	protected Scene scene;
	protected VBox root;
	protected StackPane upper, bottom;
	protected Pane middle;
	protected Canvas canvas;
	protected GraphicsContext gc;
	protected Plateau plateau;
	protected int size;
	protected Label nbTours;
	protected int offsetX, offsetY;
	protected Label info, infoBas;
	protected Monstre m;
	protected Chasseur c;
	protected FadeTransition fade;
	protected TranslateTransition translate;
	protected int sec;
	protected static MediaPlayer mp;
	protected static double volume = 0.5;

	
	public Game(String title) {
		this.tours = 1;

		size = 10;
		plateau = new Plateau(size);
		taille_case = (int) (600 / size);
		double offsetTailleMonstre = (5.0 / size) * 1.4;
		offsetX = (int) ((5.0 / size) * 30);
		offsetY = (int) ((5.0 / size) * 15);
		
		stage = new Stage();
		backPlateau = new ImageView(new Image("file:ressources/img/plateau.png",600, 600,true,true));
		monstre = new ImageView(new Image("file:ressources/img/monstre.png",100 * offsetTailleMonstre, 70 * offsetTailleMonstre,true,true));
		root = new VBox();
		root.setAlignment(Pos.TOP_LEFT);
		
		canvas = new Canvas(600,600);
		canvas.setOpacity(0.6);
		gc = canvas.getGraphicsContext2D();
		
		info = new Label("Au monstre de jouer !");
		upper = new StackPane();
		upper.setAlignment(Pos.CENTER);
		upper.getChildren().add(info);
		upper.setMinSize(WIDTH, 100);
		upper.setMaxSize(WIDTH, 100);
		
		infoBas = new Label();
		bottom = new StackPane();
		bottom.setAlignment(Pos.CENTER);
		bottom.getChildren().add(infoBas);
		bottom.setMinSize(WIDTH, 100);
		bottom.setMaxSize(WIDTH, 100);
		
		middle = new Pane();
		middle.setMaxSize(600, 600);
		middle.getChildren().addAll(backPlateau, canvas, monstre);
		
		root.getChildren().addAll(upper, middle, bottom);
		scene = new Scene(root, WIDTH, HEIGHT);
		
		stage.setScene(scene);
		stage.sizeToScene();
		stage.setResizable(false);
		stage.setTitle(title);
		stage.show();
	}
	
	protected void draw() {
		gc.clearRect(0, 0, 600, 600);
		gc.setStroke(Color.BLACK);
		for(int i = 0 ; i < size ; i++) {
			for(int j = 0 ; j < size ; j++) {
				gc.strokeRect(i * taille_case, j * taille_case, taille_case, taille_case);
			}
		}
		drawCasePleine();
	}
	
	protected void drawCasePleine() {
		gc.setFill(Color.BLACK);
		for(int i = 0 ; i < size ; i++) {
			for(int j = 0 ; j < size ; j++) {
				if(m != null && m.peutJouer()) {
					if(plateau.getCaseExplorer(i, j)) gc.fillRect(i * taille_case, j * taille_case, taille_case, taille_case);
				}
				else if(c != null && c.peutJouer()) {
					if(plateau.getCaseExplorerChasseur(i, j)) gc.fillRect(i * taille_case, j * taille_case, taille_case, taille_case);
				}
			}
		}
	}
	
	protected abstract void loop();
	
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
			
			sec = 5;
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
					c.setJouer(true);
					info.setText("Au chasseur de jouer !");
					infoBas.setText("");
					draw();
				}
			});
			pause.play();
		} else infoBas.setText("Case explorée ! Impossible d'y aller !");
	}
	
	protected boolean reveal(int x, int y) {
		int anciennePosition = plateau.getMonstreAnciennePosition(x, y);
		if(anciennePosition != -1) {
			infoBas.setText("Le monstre est passé par là il y a : " + anciennePosition + " tours !");
		} else infoBas.setText("Dommage, rien par ici...");
		c.setJouer(false);
		sec = 5;
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
	
	/*protected void audioShoot() {
		Media me = new Media(new File("ressources/audio/shoot.m4a").toURI().toString());
		mp = new MediaPlayer(me);
		mp.setVolume(volume);
		mp.play();
		mp.setCycleCount(1);
	}
	
	protected void audioLoup() {
		Media me = new Media(new File("ressources/audio/loup.m4a").toURI().toString());
		mp = new MediaPlayer(me);
		mp.setVolume(volume);
		mp.play();
		mp.setCycleCount(1);
	}
	
	public static void setVolume(double v) {
		volume = v;
		mp.setVolume(volume);
	}
	
	public static double getVolume() {
		return mp.getVolume();
	}*/
	
	protected void draw(int x, int y) {
		gc.setFill(Color.DARKRED);
		gc.fillRect(x * taille_case, y * taille_case, taille_case, taille_case);
	}
	
	protected void reset() { 
		gc.clearRect(0, 0, 600, 600); 
		gc.setStroke(Color.BLACK);
		for(int i = 0 ; i < size ; i++) {
			for(int j = 0 ; j < size ; j++) {
				gc.strokeRect(i * taille_case, j * taille_case, taille_case, taille_case);
			}
		}
	}
}

