package graphics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.concurrent.Task;
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
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MenuGUI extends Application {

	public static void main(String[] args) { launch(); };
	
	private ImageView chasseur, monstre, back;
	private final int WIDTH = 640, HEIGHT = 480;
	private StackPane background, root;
	private Pane p1, p2;
	private VBox toutTitre;
	private Label unJoueur, deuxJoueurs, quitter, classement, selection;
	private Font titre, sub;
	private Canvas cv;
	private GraphicsContext gc;
	
	public void start(Stage stage) throws FileNotFoundException {
		root = new StackPane();
		titre = Font.loadFont(new FileInputStream(new File("ressources/font/8-BIT_WONDER.TTF")), 35);
		sub = Font.loadFont(new FileInputStream(new File("ressources/font/8-BIT_WONDER.TTF")), 30);
		cv = new Canvas(WIDTH, HEIGHT);
		gc = cv.getGraphicsContext2D();
		
		playMusic();
		loadImage();
		setupLabel();
		setupTransition();
		root.getChildren().addAll(background, cv, p1, p2, toutTitre);
		
		Scene scene = new Scene(root, 640, 480);
		stage.setScene(scene);
		stage.setTitle("Chasse Aux Monstres");
		stage.setResizable(false);
		stage.show();
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
	}
	
	private void setupTransition() {
		background = new StackPane();
		background.getChildren().add(back);
		background.setLayoutX(0);
		background.setLayoutY(0);
		
		p1 = new Pane();
		p2 = new Pane();
		
		p1.getChildren().add(chasseur);
		chasseur.setLayoutX(-180);
		chasseur.setLayoutY(185);
		
		p2.getChildren().add(monstre);
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
		f2.setNode(cv);
		f2.setFromValue(0);
		f2.setToValue(1);
		f2.play();
		
		TranslateTransition t1 = new TranslateTransition();
		t1.setDuration(Duration.millis(1500));
		t1.setToX(200);
		t1.setNode(p1);
		
		TranslateTransition t2 = new TranslateTransition();
		t2.setDuration(Duration.millis(1500));
		t2.setToX(-330);
		t2.setNode(p2);
		
		TranslateTransition t3 = new TranslateTransition();
		t3.setDuration(Duration.millis(1000));
		t3.setToY(15);
		t3.setAutoReverse(true);
		t3.setNode(p1);
		t3.setCycleCount(Transition.INDEFINITE);
		
		TranslateTransition t4 = new TranslateTransition();
		t4.setDuration(Duration.millis(1300));
		t4.setToY(15);
		t4.setAutoReverse(true);
		t4.setNode(p2);
		t4.setCycleCount(Transition.INDEFINITE);
		
		SequentialTransition s = new SequentialTransition(t1, t3);
		s.play();
		
		SequentialTransition s2 = new SequentialTransition(t2, t4);
		s2.play();
		
		TranslateTransition t5 = new TranslateTransition();
		t5.setDuration(Duration.millis(700));
		t5.setToX(5);
		t5.setAutoReverse(true);
		t5.setNode(cv);
		t5.setCycleCount(Transition.INDEFINITE);
		t5.play();
		
	}
	
	private void playMusic() {
		final Task task = new Task() {

	        @Override
	        protected Object call() throws Exception {
	            AudioClip audio = new AudioClip(getClass().getResource("music.wav").toExternalForm());
	            audio.setVolume(0.1f);
	            audio.setCycleCount(AudioClip.INDEFINITE);
	            audio.play();
	            return null;
	        }
	    };
	    Thread thread = new Thread(task);
	    thread.start();
	}
	
	@SuppressWarnings("restriction")
	private void setupLabel() {
		toutTitre = new VBox();
		
		VBox vTitre = new VBox();
		vTitre.setAlignment(Pos.CENTER);
		Label titre1 = new Label("MONSTRE");
		Label titre2 = new Label("VS");
		Label titre3 = new Label("CHASSEUR");
		titre1.setFont(titre);
		titre2.setFont(titre);
		titre3.setFont(titre);
		titre1.setStyle("-fx-text-fill: white; -fx-effect: dropshadow( gaussian , rgba(0,0,0,1) , 3,3,0,0 );");
		titre2.setStyle("-fx-text-fill: white; -fx-effect: dropshadow( gaussian , rgba(0,0,0,1) , 3,3,0,0 );");
		titre3.setStyle("-fx-text-fill: white; -fx-effect: dropshadow( gaussian , rgba(0,0,0,1) , 3,3,0,0 );");
		vTitre.getChildren().addAll(titre1,titre2,titre3);
		
		unJoueur = new Label("1 JOUEUR");
		unJoueur.setFont(sub);
		unJoueur.setStyle("-fx-text-fill: orange; -fx-effect: dropshadow( gaussian , rgba(255,255,255,1) , 3,3,0,0 );");
		
		deuxJoueurs = new Label("2 JOUEURS");
		deuxJoueurs.setFont(sub);
		deuxJoueurs.setStyle("-fx-text-fill: blue; -fx-effect: dropshadow( gaussian , rgba(255,255,255,1) , 3,3,0,0 );");
		
		classement = new Label("CLASSEMENT");
		classement.setFont(sub);
		classement.setStyle("-fx-text-fill: red; -fx-effect: dropshadow( gaussian , rgba(255,255,255,1) , 3,3,0,0 );");
		
		quitter = new Label("QUITTER");
		quitter.setFont(sub);
		quitter.setStyle("-fx-text-fill: black; -fx-effect: dropshadow( gaussian , rgba(255,255,255,1) , 3,3,0,0 );");
		
		toutTitre.getChildren().addAll(vTitre, unJoueur, deuxJoueurs, classement, quitter);
		toutTitre.setAlignment(Pos.CENTER);
		toutTitre.setSpacing(35);
		FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
		double sizeUn, sizeDeux, sizeClassement ,sizeQuitter;
		sizeUn = fontLoader.computeStringWidth(unJoueur.getText(), unJoueur.getFont());
		sizeDeux = fontLoader.computeStringWidth(deuxJoueurs.getText(), deuxJoueurs.getFont());
		sizeQuitter = fontLoader.computeStringWidth(quitter.getText(), quitter.getFont());
		sizeClassement = fontLoader.computeStringWidth(classement.getText(), classement.getFont());
		unJoueur.setOnMouseMoved(e -> {
			if(selection == null || selection != unJoueur) {
				drawTriangle(unJoueur.getLayoutX(), unJoueur.getLayoutY(), sizeUn, Color.ORANGE);
				selection = unJoueur;
			}
		});
		
		deuxJoueurs.setOnMouseMoved(e -> {
			if(selection == null || selection != deuxJoueurs) {
				drawTriangle(deuxJoueurs.getLayoutX(), deuxJoueurs.getLayoutY(), sizeDeux, Color.BLUE);
				selection = deuxJoueurs;
			}
		});
		
		classement.setOnMouseMoved(e -> {
			if(selection == null || selection != classement) {
				drawTriangle(classement.getLayoutX(), classement.getLayoutY(), sizeClassement, Color.RED);
				selection = classement;
			}
		});
		
		quitter.setOnMouseMoved(e -> {
			if(selection == null || selection != quitter) {
				drawTriangle(quitter.getLayoutX(), quitter.getLayoutY(), sizeQuitter, Color.BLACK);
				selection = quitter;
			}
		});
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
}
