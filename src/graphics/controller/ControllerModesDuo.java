package graphics.controller;

import graphics.DeuxJoueurs;
import graphics.Game;
import graphics.Menu;
import graphics.MenuOptions;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class ControllerModesDuo {
	
	@FXML CheckBox buttonTp;
	@FXML CheckBox buttonBr;
	@FXML ComboBox<String> comboSize;
	public void initialize() {
		comboSize.getItems().addAll("4","5","6","7","8","9","10");
		comboSize.getSelectionModel().select(4);
	}
	
	public void apply() {
		Game.Mode_BR = buttonBr.isSelected();
		Game.Mode_Tp = buttonTp.isSelected();
		DeuxJoueurs dj = new DeuxJoueurs(Menu.getJoueur(1), Menu.getJoueur(2), Integer.valueOf(comboSize.getSelectionModel().getSelectedItem().toString()));
		
		Stage stage  = (Stage) buttonTp.getScene().getWindow();
		stage.close();
	}

	public void commands() {
		MenuOptions.commands();
	}
}
