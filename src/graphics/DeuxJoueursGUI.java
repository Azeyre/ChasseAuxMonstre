package graphics;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import menu.Menu;

public class DeuxJoueursGUI extends Application {

	private static int tailleCase = 40;
	
	@Override
	public void start(Stage stage) throws Exception {
		 VBox root = new VBox();
         Canvas canvas = new Canvas (Menu.SIZE * 40, Menu.SIZE * 40);
         GraphicsContext gc = canvas.getGraphicsContext2D();
         gc.setFill(Color.ORANGE);
         gc.setStroke(Color.BLACK);
         for(int i = 0 ; i < Menu.SIZE ; i++) {
        	 for(int j = 0 ; j < Menu.SIZE ; j++) {
                 gc.fillRect(i * tailleCase, j * tailleCase, tailleCase, tailleCase);
                 gc.strokeRect(i * tailleCase, j * tailleCase, tailleCase, tailleCase);
        	 }
         }
         root.getChildren().add(canvas); 

         Scene scene = new Scene(root);
         stage.setTitle("Chasse au Monstre");
         stage.setScene(scene);
         stage.show();
	}
	
	public static void displayGame() {
		Application.launch();
	}

}
