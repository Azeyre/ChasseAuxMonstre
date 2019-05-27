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
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import options.BattleRoyale;
import options.Teleport;

/**
 * 
 * @author kozlova
 * Classe mère Game, crée une nouvelle fenetre de jeu
 */
public abstract class Game {

	protected final int WIDTH = 600, HEIGHT = 700;
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
	protected int offsetX, offsetY;
	protected Label info, infoBas;
	protected Monstre m;
	protected Chasseur c;
	protected FadeTransition fade;
	protected TranslateTransition translate;
	protected boolean fini;
	protected int sec;
	protected int[][] anciennePositionMonstre;
	public static boolean Mode_BR = false, Mode_Tp = false, Mode_MonstreMange = false;
	protected BattleRoyale br;
	protected boolean retrecitBr = false;
	
	/**
	 * Permet de créer une fenetre de jeu avec un titre passé en paramètre
	 * @param title String
	 */
	public Game(String title, int size) {
		/*
		 * On attend la fin de la selection des modes pour pouvoir lancer la partie
		 */
		tours = 0;
		this.size = size;
		anciennePositionMonstre = new int[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++)
				anciennePositionMonstre[i][j] = -1;
		}

		plateau = new Plateau(size);
		br = new BattleRoyale(plateau);
		taille_case = (int) (600 / size);
		double offsetTailleMonstre = (5.0 / size) * 1.4;
		offsetX = (int) ((5.0 / size) * 30);
		offsetY = (int) ((5.0 / size) * 15);

		stage = new Stage();

		/*
		 * Chargement de l'image du background et du monstre
		 */
		backPlateau = new ImageView(new Image("file:ressources/img/plateau.png", 600, 600, true, true));
		monstre = new ImageView(new Image("file:ressources/img/monstre.png", 100 * offsetTailleMonstre,
				70 * offsetTailleMonstre, true, true));

		root = new VBox();
		root.setAlignment(Pos.TOP_LEFT);

		/*
		 * Creation du canvas avec une opacite 0.6 pour voir l'image du background
		 */
		canvas = new Canvas(600, 600);
		canvas.setOpacity(0.6);
		gc = canvas.getGraphicsContext2D();

		/*
		 * Chargement des polices personnelles et creation des labels pour informer les
		 * joueurs
		 */
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
		upper.setMinSize(WIDTH, 50);
		upper.setMaxSize(WIDTH, 50);

		infoBas = new Label();
		infoBas.setId("info");
		infoBas.setFont(fontInfo);
		bottom = new StackPane();
		bottom.setAlignment(Pos.CENTER);
		bottom.getChildren().add(infoBas);
		bottom.setMinSize(WIDTH, 50);
		bottom.setMaxSize(WIDTH, 50);

		middle = new Pane();
		middle.setMaxSize(600, 600);
		middle.getChildren().addAll(backPlateau, canvas, monstre);

		root.getChildren().addAll(upper, middle, bottom);
		scene = new Scene(root, WIDTH, HEIGHT);

		/*
		 * Chargement du css pour customiser les labels
		 */
		scene.getStylesheets().add(new File("ressources/css/style.css").toURI().toString());
		stage.setScene(scene);
		stage.getIcons().add(new Image("file:ressources/img/icone_principale.png"));
		stage.sizeToScene();
		stage.setResizable(false);
		stage.setTitle(title);
		stage.setOnCloseRequest(e -> {
			e.consume();
			MenuQuit.open();
		});
		stage.show();
	}
	
	protected void tirAudio() {
		Media me = new Media(new File("ressources/audio/shoot.m4a").toURI().toString());
		MediaPlayer mp = new MediaPlayer(me);
		mp.setVolume(0.5);
		mp.play();
	}
	
	protected void loupAudio() {
		Media me = new Media(new File("ressources/audio/loup.m4a").toURI().toString());
		MediaPlayer mp = new MediaPlayer(me);
		mp.setVolume(0.5);
		mp.play();
	}
	
	/*
	 * Dessine les cases du plateau 
	 */
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

	/**
	 * Affichage du plateau en fonction du joueur
	 */
	protected void drawCasePleine() {
		gc.setFill(Color.BLACK);
		for(int i = 0 ; i < size ; i++) {
			for(int j = 0 ; j < size ; j++) {
				if(m.peutJouer()) { //Monstre
					if(plateau.getCaseExplorer(i, j)) gc.fillRect(i * taille_case, j * taille_case, taille_case, taille_case);
				}
				else if(c.peutJouer()) { //Chasseur 
					gc.setFill(Color.BLACK);
					if(plateau.getCaseExplorerChasseur(i, j)) gc.fillRect(i * taille_case, j * taille_case, taille_case, taille_case);
					
					/*
					 * Affichage sur le canvas de l'ancienne position du monstre pour les cases explorées par le chasseur
					 */
					gc.setFill(Color.WHITE);
					if(anciennePositionMonstre[i][j] > 0) 
						gc.fillText("" + anciennePositionMonstre[i][j], (i * taille_case) + (taille_case / 2) - 10, (j * taille_case) + (taille_case / 2) + 10);
				}
			}
		}
	}
	
	/*
	 * Permet de lancer la partie
	 */
	protected abstract void run();
	
	/*
	 * Mouvement du monstre d'une case en fonction de sa position actuelle
	 */
	protected abstract void moveMonstre(int x, int y);
	
	/*
	 * Mode battle royale
	 */
	protected boolean ModeBR() {
		if(tours % 10 == 0 && Mode_BR) {
			br.rectrecitGraphique();
			if(!Mode_Tp && m.bloquer(plateau) && !plateau.fini()) {
				victoireChasseur();
			}
			return true;
		} else if(tours % 10 == 9 && Mode_BR) {
			infoBas.setText("Prochain tour le plateau va se retrecir");
		}
		return false;
	}

	/**
	 * 
	 * @param x Position x du plateau
	 * @param y Position y du plateau
	 * @return true si le chasseur a trouvé le monstre sinon false
	 */
	protected abstract boolean reveal(int x, int y);
	
	/*
	 * Affiche la derniere case exploree du chasseur en rouge
	 */
	protected void draw(int x, int y) {
		gc.setFill(Color.DARKRED);
		gc.fillRect(x * taille_case, y * taille_case, taille_case, taille_case);
	}
	
	/**
	 * Reset le plateau pour permettre l'affichage du gagnant
	 */
	protected void reset() { 
		gc.clearRect(0, 0, 600, 600); 
		gc.setStroke(Color.BLACK);
		for(int i = 0 ; i < size ; i++) {
			for(int j = 0 ; j < size ; j++) {
				gc.strokeRect(i * taille_case, j * taille_case, taille_case, taille_case);
			}
		}
	}
	
	/**
	 * Affichage lors de la victoire du chasseur
	 */
	protected void victoireChasseur() {
		victoire(c);
	}
	
	/**
	 * Affichage lors de la victoire du monstre
	 */
	protected void victoireMonstre() {
		victoire(m);
	}
	
	private void victoire(Joueur j) {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		Font titre = new Font("Arial", 40);
		try {
			titre = Font.loadFont(new FileInputStream(new File("ressources/font/8-BIT_WONDER.TTF")), 40);
		} catch (FileNotFoundException e) {
			System.err.println("La police de caractère 8-Bit-Wonder.tff est introuvable.");
		}
		Label gagnantTitre;
		Label winTitre = new Label("WIN");
		ImageView img;
		if(j.estMonstre()) {
			gagnantTitre = new Label("Monstre");
			img = new ImageView(new Image("file:ressources/img/monstre.png",100*5,70*5,true,true));
		} else {
			gagnantTitre = new Label("Chasseur");
			img = new ImageView(new Image("file:ressources/img/chasseur.png",100*5,70*5,true,true));
		}
		
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		gagnantTitre.setFont(titre);
		winTitre.setFont(titre);
		gagnantTitre.setId("titre");
		winTitre.setId("titre");
		vbox.getChildren().addAll(gagnantTitre, winTitre);
		vbox.setLayoutX(150);
		vbox.setLayoutY(60);
		
		VBox options = new VBox();
		Label menuTitre = new Label("Menu");
		Label quitTitre = new Label("Quitter");
		menuTitre.setFont(titre);
		menuTitre.setId("titre");
		quitTitre.setFont(titre);
		quitTitre.setId("quitter");
		options.getChildren().addAll(menuTitre, quitTitre);
		options.setLayoutX(300);
		options.setLayoutY(240);
		
		img.setOpacity(0.0);
		middle.getChildren().addAll(img, vbox, options);
		img.setLayoutX(80);
		img.setLayoutY(150);
		
		FadeTransition fadeInChasseur = new FadeTransition();
		fadeInChasseur.setDuration(Duration.millis(1000));
		fadeInChasseur.setNode(img);
		fadeInChasseur.setFromValue(0.0);
		fadeInChasseur.setToValue(1.0);
		fadeInChasseur.play();
		
		menuTitre.setOnMouseClicked(e -> {
			Menu.show();
			stage.close();
		});
		quitTitre.setOnMouseClicked(e -> {MenuQuit.open();} );
	}
}
