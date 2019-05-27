package graphics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

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
	private boolean retrecitBr = false;
	
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
			quit();
		});
		stage.show();
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
	
	private boolean ModeBR() {
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
	
	/**
	 * Affichage lors de la victoire du monstre
	 */
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
	
	private void quit() {
		Stage stage = new Stage();
		VBox root = new VBox();
		Label l = new Label("Voulez-vous vraiment quitter ?");
		root.setAlignment(Pos.CENTER);
		
		HBox b = new HBox();
		b.setAlignment(Pos.CENTER);
		Button oui = new Button("Oui");
		Button non = new Button("Non");
		b.setPadding(new Insets(5));
		b.setSpacing(20);
		oui.setOnAction(ev -> { System.exit(0); });
		non.setOnAction(ev -> { stage.close(); });
		b.getChildren().addAll(oui,non);
		
		root.getChildren().addAll(l, b);
		
		Scene scene = new Scene(root, 180, 60);
		stage.getIcons().add(new Image("file:ressources/img/icone.png"));
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}
}
