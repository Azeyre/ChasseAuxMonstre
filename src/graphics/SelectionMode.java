package graphics;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SelectionMode {

	public static void duo() {
		try {
			Stage s = new Stage();
			URL url = new File("ressources/fxml/modesDuo.fxml").toURL();
			Parent settings = FXMLLoader.load(url);
			s.setScene(new Scene(settings));
			s.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void solo() {
		try {
			Stage s = new Stage();
			URL url = new File("ressources/fxml/modesSolo.fxml").toURL();
			Parent settings = FXMLLoader.load(url);
			s.setScene(new Scene(settings));
			s.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
