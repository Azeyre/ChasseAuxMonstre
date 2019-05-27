package graphics;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class MenuQuit {
	
	/**
	 * Ouvre une fenetre pour confirmer le choix de quitter l'application
	 */
	public static void open() {
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
