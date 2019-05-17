package graphics;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
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
import javafx.scene.input.KeyCode;
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

public class Menu extends Application {

	public static void main(String[] args) { launch(); };
	
	private final int WIDTH = 640, HEIGHT = 480;
	
	private static MediaPlayer mp;
	private static ImageView speaker_on, speaker_off;
	private static boolean mute = false;
	private static double volume = 1.0;
	private static Joueur j1, j2;
	
	private ImageView chasseur, monstre, back;
	private StackPane background;
	private Pane paneChasseur, paneMonstre;
	private VBox toutTitre;
	private Label unJoueur, deuxJoueurs, quitter, classement, options, selection;
	private int actualSelection = 0;
	private Font titre, sub;
	private Canvas canvas;
	private GraphicsContext gc;
	private DeuxJoueurs dj;
	private Stage menu;
	
	public void start(Stage stage) throws IOException {
		StackPane root = new StackPane();
		titre = Font.loadFont(new FileInputStream(new File("ressources/font/8-BIT_WONDER.TTF")), 40);
		sub = Font.loadFont(new FileInputStream(new File("ressources/font/8-BIT_WONDER.TTF")), 30);
		canvas = new Canvas(WIDTH, HEIGHT);
		gc = canvas.getGraphicsContext2D();
		
		playMusic();
		loadImage();
		setupLabel();
		setupTransition();
		
		StackPane.setAlignment(speaker_on, Pos.TOP_LEFT);
		StackPane.setAlignment(speaker_off, Pos.TOP_LEFT);
		
		root.getChildren().addAll(background,canvas, paneChasseur, paneMonstre, toutTitre, speaker_on, speaker_off);
		
		Scene scene = new Scene(root, 640, 480);
		scene.setOnKeyPressed(e -> {
			if(actualSelection != -1) {
				KeyCode k = e.getCode();
				if(k.equals(KeyCode.ENTER)) {
					confirmSelection();
				} else if(k.equals(KeyCode.UP)) {
					actualSelection--;
					if(actualSelection < 0) actualSelection = 4;
					changeSelection(actualSelection);
				} else if(k.equals(KeyCode.DOWN)) {
					actualSelection++;
					if(actualSelection > 4) actualSelection = 0;
					changeSelection(actualSelection);
				}
			}
		});
		scene.getStylesheets().add(new File("ressources/css/style.css").toURI().toString());
		stage.setScene(scene);
		stage.getIcons().add(new Image("file:ressources/img/icone_principale.png"));
		stage.setTitle("Chasse Aux Monstres");
		stage.setResizable(false);
		stage.centerOnScreen();
		stage.setOnCloseRequest(e -> { e.consume(); quit(); });
		stage.show();
		menu = stage;
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
	
	private void loadImage() {
		back = new ImageView(new Image("file:ressources/img/back.png",WIDTH + 50,HEIGHT + 50,true,true));
		chasseur = new ImageView(new Image("file:ressources/img/chasseur.png",100*3,70*3,true,true));
		monstre = new ImageView(new Image("file:ressources/img/monstre.png",100*3,70*3,true,true));
		speaker_on = new ImageView(new Image("file:ressources/img/speaker_on.png",40,40,true,true));
		speaker_off = new ImageView(new Image("file:ressources/img/speaker_off.png",40,40,true,true));
		speaker_off.setVisible(false);
		
		speaker_on.setOnMouseClicked(e -> { setMute(true); });
		speaker_off.setOnMouseClicked(e -> { setMute(false); });
	}
	
	private void setupTransition() {
		background = new StackPane();
		background.getChildren().add(back);
		background.setLayoutX(0);
		background.setLayoutY(0);
		
		paneChasseur = new Pane();
		paneMonstre = new Pane();
		
		paneChasseur.getChildren().add(chasseur);
		chasseur.setLayoutX(-180);
		chasseur.setLayoutY(185);
		
		paneMonstre.getChildren().add(monstre);
		monstre.setLayoutX(WIDTH + 200);
		monstre.setLayoutY(185);
		
		FadeTransition f1 = new FadeTransition();
		f1.setDuration(Duration.millis(1500));
		f1.setNode(toutTitre);
		f1.setFromValue(0);
		f1.setToValue(1);
		f1.play();
		
		FadeTransition f2 = new FadeTransition();
		f2.setDuration(Duration.millis(1500));
		f2.setNode(canvas);
		f2.setFromValue(0);
		f2.setToValue(1);
		f2.play();
		
		TranslateTransition t1 = new TranslateTransition();
		t1.setDuration(Duration.millis(1500));
		t1.setToX(200);
		t1.setNode(paneChasseur);
		
		TranslateTransition t2 = new TranslateTransition();
		t2.setDuration(Duration.millis(1500));
		t2.setToX(-330);
		t2.setNode(paneMonstre);
		
		TranslateTransition t3 = new TranslateTransition();
		t3.setDuration(Duration.millis(1000));
		t3.setToY(15);
		t3.setAutoReverse(true);
		t3.setNode(paneChasseur);
		t3.setCycleCount(Transition.INDEFINITE);
		
		TranslateTransition t4 = new TranslateTransition();
		t4.setDuration(Duration.millis(1300));
		t4.setToY(15);
		t4.setAutoReverse(true);
		t4.setNode(paneMonstre);
		t4.setCycleCount(Transition.INDEFINITE);
		
		SequentialTransition s = new SequentialTransition(t1, t3);
		s.play();
		
		SequentialTransition s2 = new SequentialTransition(t2, t4);
		s2.play();
		
		TranslateTransition t5 = new TranslateTransition();
		t5.setDuration(Duration.millis(700));
		t5.setToX(10);
		t5.setAutoReverse(true);
		t5.setNode(canvas);
		t5.setCycleCount(Transition.INDEFINITE);
		t5.play();
		
	}
	
	private void playMusic() {
		Media me = new Media(new File("ressources/audio/music.wav").toURI().toString());
		mp = new MediaPlayer(me);
		mp.setVolume(volume);
		mp.play();
		mp.setCycleCount(MediaPlayer.INDEFINITE);
	}
	
	public static void setVolume(double v) {
		volume = v;
		mp.setVolume(volume);
	}
	
	public static double getVolume() {
		return mp.getVolume();
	}
	
	public static void setMute(boolean b) {
		mute = b;
		mp.setMute(b);
		
		if(!mute) {
			speaker_on.setVisible(true);
			speaker_off.setVisible(false);
		} else {
			speaker_on.setVisible(false);
			speaker_off.setVisible(true);
		}
	}
	
	public static boolean getMute() {
		return mute;
	}
	
	private void setupLabel() {
		toutTitre = new VBox();
		
		VBox vTitre = new VBox();
		vTitre.setAlignment(Pos.CENTER);
		Label titre1 = new Label("MONSTRE");
		Label titre2 = new Label("VS");
		Label titre3 = new Label("CHASSEUR");
		titre1.setId("titre");
		titre2.setId("titre");
		titre3.setId("titre");
		titre1.setFont(titre);
		titre2.setFont(titre);
		titre3.setFont(titre);
		vTitre.getChildren().addAll(titre1,titre2,titre3);
		
		unJoueur = new Label("1 JOUEUR");
		unJoueur.setFont(sub);
		unJoueur.setId("unJoueur");
		
		deuxJoueurs = new Label("2 JOUEURS");
		deuxJoueurs.setFont(sub);
		deuxJoueurs.setId("deuxJoueurs");
		
		classement = new Label("CLASSEMENT");
		classement.setFont(sub);
		classement.setId("classement");
		
		options = new Label("OPTIONS");
		options.setFont(sub);
		options.setId("options");
		
		quitter = new Label("QUITTER");
		quitter.setFont(sub);
		quitter.setId("quitter");
		
		toutTitre.getChildren().addAll(vTitre, unJoueur, deuxJoueurs, classement, options, quitter);
		toutTitre.setAlignment(Pos.CENTER);
		toutTitre.setSpacing(25);
		
		unJoueur.setOnMouseMoved(e -> {	if(selection != unJoueur) changeSelection(0); });
		unJoueur.setOnMouseClicked(e -> { confirmSelection(); });
		deuxJoueurs.setOnMouseMoved(e -> { if(selection != deuxJoueurs) changeSelection(1); });
		deuxJoueurs.setOnMouseClicked(e -> { confirmSelection(); });
		classement.setOnMouseMoved(e -> { if(selection != classement) changeSelection(2); });
		classement.setOnMouseClicked(e -> { confirmSelection(); });
		options.setOnMouseMoved(e -> { if(selection != options) changeSelection(3); });
		options.setOnMouseClicked(e -> { confirmSelection(); });
		quitter.setOnMouseMoved(e -> { if(selection != quitter) changeSelection(4); });
		quitter.setOnMouseClicked(e -> { confirmSelection(); });
	}
	
	private void drawTriangle(double x, double y, double size, Color c) {
		gc.clearRect(0, 0, WIDTH, HEIGHT);
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(2.5);
		gc.setFill(c);
		//FLECHE GAUCHE
		gc.beginPath();
	    gc.moveTo(x - 80, y);
	    gc.lineTo(x - 40, y + 15);
	    gc.lineTo(x - 80, y + 30);
	    gc.lineTo(x - 80, y);
	    gc.fill();
	    gc.stroke();
	    gc.closePath();

	    //FLECHE DROITE
		gc.beginPath();
	    gc.moveTo(x + 60 + size, y);
	    gc.lineTo(x + 20 +size, y + 15);
	    gc.lineTo(x + 60 + size, y + 30);
	    gc.lineTo(x + 60 + size, y);
	    gc.fill();
	    gc.stroke();
	    gc.closePath();
	}
	
	@SuppressWarnings("restriction")
	private void changeSelection(int n) {
		FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
		double sizeUn, sizeDeux, sizeClassement ,sizeQuitter;
		sizeUn = fontLoader.computeStringWidth(unJoueur.getText(), unJoueur.getFont());
		sizeDeux = fontLoader.computeStringWidth(deuxJoueurs.getText(), deuxJoueurs.getFont());
		sizeQuitter = fontLoader.computeStringWidth(quitter.getText(), quitter.getFont());
		sizeClassement = fontLoader.computeStringWidth(classement.getText(), classement.getFont());
		
		switch(n) {
		case 0: selection = unJoueur;
			drawTriangle(unJoueur.getLayoutX(), unJoueur.getLayoutY(), sizeUn, Color.ORANGE);
			actualSelection = 0;
			break;
		case 1: selection = deuxJoueurs;
			drawTriangle(deuxJoueurs.getLayoutX(), deuxJoueurs.getLayoutY(), sizeDeux, Color.BLUE);
			actualSelection = 1;
			break;
		case 2: selection = classement;
			drawTriangle(classement.getLayoutX(), classement.getLayoutY(), sizeClassement, Color.RED);
			actualSelection = 2;
			break;
		case 3: selection = options;
			drawTriangle(options.getLayoutX(), options.getLayoutY(), sizeQuitter, Color.BLUEVIOLET);
			actualSelection = 3;
			break;
		case 4: selection = quitter;
			drawTriangle(quitter.getLayoutX(), quitter.getLayoutY(), sizeQuitter, Color.BLACK);
			actualSelection = 4;
			break;
		}
	}
	
	private void confirmSelection() {
		if(selection == quitter) quit();
		else if(selection == unJoueur) {
			j1 = null;
			CreationJoueur.affiche();
			Timer t = new Timer();
			t.schedule(new TimerTask() {
				@Override
				public void run() {
					if(j1 != null) {
						t.cancel();
						System.out.println(j1.toString());
					}
				}				
			}, 100, 100);
		} else if(selection == deuxJoueurs) {
			j1 = null; j2 = null;
			CreationJoueur.affiche();
			Timer t = new Timer();
			t.schedule(new TimerTask() {
				@Override
				public void run() {
					//System.out.println("Waiting for creation players");
					if(j1 != null && j2 == null && !CreationJoueur.estOuvert()) {
						//t.cancel();
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								CreationJoueur.affiche();
							}
						});
					} else if(j2 != null) {
						t.cancel();
						System.out.println(j1.toString() + "   " + j2.toString());
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								menu.close();
								dj = new DeuxJoueurs(j1, j2);
							}
						});
					}
				}
			}, 100, 100);
		} else if(selection == classement) {
			
		} else if(selection == options) MenuOptions.affiche();
	}
	
	public static boolean addPlayer(Joueur j) {
		if(j1 == null) {
			j1 = j;
			return true;
		} else if(j2 == null) {
			j2 = j;
			return true;
		}
		System.err.println("Il y a d�j� 2 joueurs !");
		return false;
	}
	
	public static Joueur getJoueur(int n) {
		if(n == 1) return j1;
		return j2;
	}
}
