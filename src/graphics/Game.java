package graphics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
import javafx.scene.text.Font;
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
	private FadeTransition fade;
	protected TranslateTransition translate;
	protected boolean fini;
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
		canvas.setOpacity(0.6);
		gc = canvas.getGraphicsContext2D();
		
		Font fontInfo = new Font("Arial", 16);
		Font fontCase = new Font("Arial", 30); 
		try {
			fontInfo = Font.loadFont(new FileInputStream(new File("ressources/font/8-BIT_WONDER.TTF")), 16);
			fontCase = Font.loadFont(new FileInputStream(new File("ressources/font/8-BIT_WONDER.TTF")), 26);
		} catch (FileNotFoundException e) {
			System.err.println("La police de caractère 8-Bit-Wonder.tff est introuvable.");
		}
		gc.setFont(fontCase);
		
		info = new Label("Au monstre de jouer");
		info.setId("info");
		info.setFont(fontInfo);
		upper = new StackPane();
		upper.setAlignment(Pos.CENTER);
		upper.getChildren().add(info);
		upper.setMinSize(WIDTH, 100);
		upper.setMaxSize(WIDTH, 100);
		
		infoBas = new Label();
		infoBas.setId("info");
		infoBas.setFont(fontInfo);
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
		scene.getStylesheets().add(new File("ressources/css/style.css").toURI().toString());
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
					gc.setFill(Color.BLACK);
					if(plateau.getCaseExplorerChasseur(i, j)) gc.fillRect(i * taille_case, j * taille_case, taille_case, taille_case);
					
					/*
					 * Affichage sur le canvas de l'ancienne position du monstre pour les cases explorées par le chasseur
					 */
					gc.setFill(Color.WHITE);
					if(plateau.getMonstreAnciennePosition(i, j) > 0 && plateau.getCaseExplorerChasseur(i, j)) 
						gc.fillText("" + plateau.getMonstreAnciennePosition(i, j), (i * taille_case) + (taille_case / 2) - 10, (j * taille_case) + (taille_case / 2) + 10);
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
			
			sec = 5;
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
					infoBas.setText("");
					draw();
				}
			});
			pause.play();
		} else if(m.bloquer(plateau)) {
			fini = true;
			victoireChasseur();
		} else infoBas.setText("Impossible la case est deja exploree");
	}
	
	protected boolean reveal(int x, int y) {
		int anciennePosition = plateau.getMonstreAnciennePosition(x, y);
		if(anciennePosition != -1) {
			infoBas.setText("Le monstre est passe par la il y a " + anciennePosition + " tours");
		} else infoBas.setText("Rien ici");
		c.setJouer(false);
		sec = 5;
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
				reset();
				m.setJouer(true);
				info.setText("Cliquez pour afficher la position du monstre");
				infoBas.setText("");
			}
		});
		pause.play();
		return plateau.reveal(x, y, m);
	}
	
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
	
	protected void victoireChasseur() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		monstre.setOpacity(0.0);
		
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
		vbox.getChildren().addAll(chasseurTitre, winTitre);
		vbox.setLayoutX(150);
		vbox.setLayoutY(60);
		
		
		ImageView imgChasseur = new ImageView(new Image("file:ressources/img/chasseur.png",100*5,70*5,true,true));
		imgChasseur.setOpacity(0.0);
		middle.getChildren().addAll(imgChasseur, vbox);
		imgChasseur.setLayoutX(80);
		imgChasseur.setLayoutY(150);
		
		FadeTransition fadeInChasseur = new FadeTransition();
		fadeInChasseur.setDuration(Duration.millis(1000));
		fadeInChasseur.setNode(imgChasseur);
		fadeInChasseur.setFromValue(0.0);
		fadeInChasseur.setToValue(1.0);
		fadeInChasseur.play();	
	}
	
	protected void victoireMonstre() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		Font titre = new Font("Arial", 40);
		try {
			titre = Font.loadFont(new FileInputStream(new File("ressources/font/8-BIT_WONDER.TTF")), 40);
		} catch (FileNotFoundException e) {
			System.err.println("La police de caractère 8-Bit-Wonder.tff est introuvable.");
		}
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		Label monstreTitre = new Label("Monstre");
		Label winTitre = new Label("WIN");
		monstreTitre.setFont(titre);
		winTitre.setFont(titre);
		monstreTitre.setId("titre");
		winTitre.setId("titre");
		vbox.getChildren().addAll(monstreTitre, winTitre);
		vbox.setLayoutX(150);
		vbox.setLayoutY(60);
		
		
		ImageView imgChasseur = new ImageView(new Image("file:ressources/img/monstre.png",100*5,70*5,true,true));
		imgChasseur.setOpacity(0.0);
		middle.getChildren().addAll(imgChasseur, vbox);
		imgChasseur.setLayoutX(80);
		imgChasseur.setLayoutY(150);
		
		FadeTransition fadeInChasseur = new FadeTransition();
		fadeInChasseur.setDuration(Duration.millis(1000));
		fadeInChasseur.setNode(imgChasseur);
		fadeInChasseur.setFromValue(0.0);
		fadeInChasseur.setToValue(1.0);
		fadeInChasseur.play();
	}
}
