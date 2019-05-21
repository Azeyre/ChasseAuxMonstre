package graphics.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

public class ControllerModes {
	
	@FXML CheckBox choix1;
	@FXML CheckBox choix2;
	@FXML CheckBox choix3;
	
	public void initialize() {
		choix1.selectedProperty().addListener((observable, oldValue, newValue) -> {
		});
		choix2.selectedProperty().addListener((observable, oldValue, newValue) -> {
		});
		choix3.selectedProperty().addListener((observable, oldValue, newValue) -> {
		});
		
	}
	
	public void apply() {
		Stage stage  = (Stage) choix1.getScene().getWindow();
		stage.close();
	}

}
