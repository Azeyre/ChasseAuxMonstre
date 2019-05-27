package graphics.controller;

import game.Chasseur;
import game.Monstre;
import graphics.Joueur;
import graphics.Menu;
import graphics.SelectionMode;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class ControllerJoueurSolo {
	
	@FXML private TextField pseudoTF;
	@FXML private RadioButton monstreRB, chasseurRB, iaRB;
	
	public void initialize() {
		
		monstreRB.setOnAction(e -> {
			if(monstreRB.isSelected()) {
				chasseurRB.setSelected(false);
				iaRB.setSelected(false);
			} else {
				monstreRB.setSelected(true);
			}
		});
		
		chasseurRB.setOnAction(e -> {
			if(chasseurRB.isSelected()) {
				monstreRB.setSelected(false);
				iaRB.setSelected(false);
			} else {
				chasseurRB.setSelected(true);
			}
		});
		
		iaRB.setOnAction(e -> {
			if(iaRB.isSelected()) {
				monstreRB.setSelected(false);
				chasseurRB.setSelected(false);
			} else {
				iaRB.setSelected(true);
			}
		});
	}
	
	public void confirm() {
		if(!pseudoTF.getText().isEmpty()) {
			if(monstreRB.isSelected()) {
				Menu.addPlayer(new Monstre(pseudoTF.getText()));
			} else if(chasseurRB.isSelected()) Menu.addPlayer(new Chasseur(pseudoTF.getText()));
			Menu.hide();
			SelectionMode.solo();
			Stage stage  = (Stage) pseudoTF.getScene().getWindow();
			stage.close();
		} else System.err.println("Le pseudo ne doit pas être vide");
	}

	public void enter(KeyEvent e) {
		if(e.getCode().equals(KeyCode.ENTER)) confirm();
	}

}
