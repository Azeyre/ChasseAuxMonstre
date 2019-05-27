package graphics.controller;

import game.Chasseur;
import game.Monstre;
import graphics.CreationJoueur;
import graphics.Joueur;
import graphics.Menu;
import graphics.SelectionMode;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class ControllerJoueurDuo {

	@FXML
	private TextField pseudoTF;
	@FXML
	private RadioButton monstreRB, chasseurRB;

	public void initialize() {
		Joueur joueur = Menu.getJoueur(1);
		if (joueur != null) {
			monstreRB.setSelected(!joueur.estMonstre());
			chasseurRB.setSelected(joueur.estMonstre());
			chasseurRB.setDisable(true);
			monstreRB.setDisable(true);
		}

		monstreRB.setOnAction(e -> {
			if (monstreRB.isSelected()) {
				chasseurRB.setSelected(false);
			} else {
				monstreRB.setSelected(true);
			}
		});

		chasseurRB.setOnAction(e -> {
			if (chasseurRB.isSelected()) {
				monstreRB.setSelected(false);
			} else {
				chasseurRB.setSelected(true);
			}
		});
	}

	public void confirm() {
		if (!pseudoTF.getText().isEmpty()) {
			if (monstreRB.isSelected()) {
				Menu.addPlayer(new Monstre(pseudoTF.getText()));
			} else
				Menu.addPlayer(new Chasseur(pseudoTF.getText()));
			
			if(Menu.getJoueur(2) == null) {
				CreationJoueur.duo();
			} else {
				Menu.hide();
				SelectionMode.duo();
			}
			Stage stage = (Stage) pseudoTF.getScene().getWindow();
			stage.close();
		} else
			System.err.println("Le pseudo ne doit pas être vide");
	}

	public void enter(KeyEvent e) {
		if (e.getCode().equals(KeyCode.ENTER))
			confirm();
	}

}
