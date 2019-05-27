package graphics.controller;

import graphics.Game;
import graphics.IAvsIA;
import graphics.Joueur;
import graphics.Menu;
import graphics.MenuOptions;
import graphics.UnJoueurChasseur;
import graphics.UnJoueurMonstre;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class ControllerModesSolo {
	
	@FXML CheckBox buttonTp;
	@FXML CheckBox buttonBr;
	@FXML ComboBox<String> comboSize;
	public void initialize() {
		comboSize.getItems().addAll("4","5","6","7","8","9","10");
		comboSize.getSelectionModel().select(4);
	}
	
	public void apply() {
		Game.Mode_BR = buttonTp.isSelected();
		Game.Mode_Tp = buttonBr.isSelected();
		Joueur j1 = Menu.getJoueur(1);
		if(j1 == null) {
			IAvsIA ia = new IAvsIA(Integer.valueOf(comboSize.getSelectionModel().getSelectedItem().toString()));
		} else if(j1.estMonstre()) {
			UnJoueurMonstre uj = new UnJoueurMonstre(j1, Integer.valueOf(comboSize.getSelectionModel().getSelectedItem().toString()));
		} else {
			UnJoueurChasseur chasse = new UnJoueurChasseur(j1, Integer.valueOf(comboSize.getSelectionModel().getSelectedItem().toString()));
		}
		Stage stage  = (Stage) buttonTp.getScene().getWindow();
		stage.close();
	}
	
	public void commands() {
		MenuOptions.commands();
	}
}