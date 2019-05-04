package graphics;

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
	protected Label info;
	protected Monstre m;
	protected Chasseur c;
	private FadeTransition fade;
	protected TranslateTransition translate;
	private int sec;
	
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
		canvas.setOpacity(0.8);
		gc = canvas.getGraphicsContext2D();
		
		info = new Label("Au monstre de jouer !");
		upper = new StackPane();
		upper.setAlignment(Pos.CENTER);
		upper.getChildren().add(info);
		upper.setMinSize(WIDTH, 100);
		upper.setMaxSize(WIDTH, 100);
		
		Label b = new Label("BOT");
		bottom = new StackPane();
		bottom.setAlignment(Pos.CENTER);
		bottom.getChildren().add(b);
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
				if(m.peutJouer()) {
					if(plateau.getCaseExplorer(i, j)) gc.fillRect(i * taille_case, j * taille_case, taille_case, taille_case);
				}
				else if(c.peutJouer()) {
					if(plateau.getCaseExplorerChasseur(i, j)) gc.fillRect(i * taille_case, j * taille_case, taille_case, taille_case);
				}
			}
		}
	}
	
	protected abstract void loop();
	
	protected void moveMonstre(int x, int y) {
		plateau.setExplorer(m.getX(), m.getY());
		if(m.move(x, y, plateau)) {
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
					draw();
				}
			});
			pause.play();
		}
	}
	
	protected boolean reveal(int x, int y) {
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
				m.setJouer(true);
				monstre.setOpacity(1.0);
				info.setText("Au monstre de jouer !");
				draw();
			}
		});
		pause.play();
		return plateau.reveal(x, y, m);
	}
}
