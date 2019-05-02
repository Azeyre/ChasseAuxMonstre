package graphics;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

public class Controller {
	
	@FXML Slider volumeSlider;
	@FXML CheckBox muteCheckBox;
	private double volume = 0;
	private boolean mute = false;
	
	public void initialize() {
		volume = MenuGUI.getVolume() * 100;
		mute = MenuGUI.getMute();
		
		muteCheckBox.setSelected(mute);
		volumeSlider.setValue(volume);
		volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			MenuGUI.setVolume(newValue.doubleValue() / 100);
		});
		muteCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			MenuGUI.setMute(newValue);
		});
    }
	
	public void apply() {
		Stage stage  = (Stage) volumeSlider.getScene().getWindow();
		stage.close();
	}
	
	public void cancel() {
		Stage stage  = (Stage) volumeSlider.getScene().getWindow();
		stage.close();
		MenuGUI.setVolume(volume / 100);
		MenuGUI.setMute(mute);
	}

}
