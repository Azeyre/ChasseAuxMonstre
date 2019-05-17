package graphics;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class CreationJoueur {
	
	static Stage s;
	
	public static void affiche() {
		try {
			s = new Stage();
			URL url = new File("ressources/fxml/creationJoueur.fxml").toURL();
			Parent settings = FXMLLoader.load(url);
			s.setScene(new Scene(settings));
			s.initModality(Modality.APPLICATION_MODAL);
			s.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean estOuvert() {
		if(s == null || !s.isShowing())	return false;
		return true;
	}
}
