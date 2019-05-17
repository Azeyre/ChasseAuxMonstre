package graphics;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MenuOptions {
	
	public static void affiche() {
		try {
			Stage s = new Stage();
			URL url = new File("ressources/fxml/settings.fxml").toURL();
			Parent settings = FXMLLoader.load(url);
			s.setScene(new Scene(settings));
			s.initModality(Modality.APPLICATION_MODAL);
			s.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
