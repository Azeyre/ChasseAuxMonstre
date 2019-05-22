package graphics.controller;

import graphics.Game;
import graphics.Menu;
import graphics.UnJoueur;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class ControllerModesSolo {
	
	@FXML CheckBox buttonTp;
	@FXML CheckBox buttonBr;
	@FXML CheckBox buttonMonstreMange;
	@FXML ComboBox<String> comboSize;
	public void initialize() {
		comboSize.getItems().addAll("4","5","6","7","8","9","10");
		comboSize.getSelectionModel().select(4);
	}
	
	public void apply() {
		Game.Mode_BR = buttonTp.isSelected();
		Game.Mode_Tp = buttonBr.isSelected();
		Game.Mode_MonstreMange = buttonMonstreMange.isSelected();
		UnJoueur uj = new UnJoueur(Menu.getJoueur(1), Integer.valueOf(comboSize.getSelectionModel().getSelectedItem().toString()));
		
		Stage stage  = (Stage) buttonTp.getScene().getWindow();
		stage.close();
	}

}
