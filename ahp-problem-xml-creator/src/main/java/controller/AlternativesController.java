package controller;

import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.AhpCore;
import model.Alternative;
import view.GUI;

public class AlternativesController {

	public AlternativesController() {
		problem = AhpCore.getInstance();
	}
	
	private void addAlternative() {
		if ( altList.getItems().contains(new Alternative(addAltField.getText(), 0)) ) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Uwaga!");
			alert.setHeaderText("Wprowadzona alternatywa jest juz na liscie");
			alert.setContentText("Wybierz inn? nazw?.");

			alert.showAndWait();
		} else if ( addAltField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Uwaga!");
			alert.setHeaderText("Nazwa nie mo?e by? pusta!");
			alert.setContentText("Wybierz inn? nazw?.");

			alert.showAndWait();
		} else {
			idCount++;
			altList.getItems().add(new Alternative(addAltField.getText(), idCount));
			problem.addAlternative(new Alternative(addAltField.getText(), idCount));
			addAltField.setText("");
		}
	}
	
	@FXML
	public void setCRHandle(KeyEvent e) {
		try {
		problem.setCR(Double.parseDouble(crField.getText()));
		} catch (Exception ex) {
		}
	}
	
	@FXML
	public void setGoalHandle(KeyEvent e) {
		problem.setGoal(goalTextField.getText());
	}
	
	@FXML
	public void addAlternativeBtnHandle(ActionEvent e) {
		addAlternative();
    }
	
	@FXML
	private void addAlternativeKeyboardHandle(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER)
			addAlternative();
	}
	
	@FXML
	private void removeAlternativeHandle(MouseEvent event) {
		if (!(altList.getSelectionModel().getSelectedItem() == null)) {
			problem.removeAlternative( new Alternative(
					altList.getSelectionModel().getSelectedItem().getName(), altList.getSelectionModel().getSelectedItem().getId()
					));
			altList.getItems().remove( altList.getSelectionModel().getSelectedItem() );
		}
	}
	
	@FXML
    public void criteriaButtonHandle(ActionEvent event) {
		Node  source = (Node)  event.getSource(); 
	    Stage stage  = (Stage) source.getScene().getWindow();
	    stage.close();
        try {
        	Stage secondStage = new Stage();
        	
        	URL url = new File("Cri.fxml").toURI().toURL();
            Parent page =  FXMLLoader.load(url);
            Scene scene = new Scene(page);
            secondStage.setScene(scene);
            secondStage.setTitle("AHP Problem");
            secondStage.show();
        } catch (Exception ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
	
	@FXML
	public void processXML() {
		problem.processXML();
	}
	
	@FXML
	public void exitHandle() {
		Platform.exit();
	}
	
	@FXML
	public void showHelp() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Pomoc");
		alert.setHeaderText("Pomoc");
		alert.setContentText("Aby rozpocz?? wybierz cel, ustaw maksymalny wsp?czynnik sp?jno?ci i dodaj alternatywy u?ywaj?c pola tekstowego na dole okna programu. Dodane alternatywy pojawi? si? na li?cie. W ka?dej chwili mo?na wygenerowa? plik XML z dotychczas doadnymi danymi u?ywaj?c menu AHP.");

		alert.showAndWait();
	}
	
	@FXML
	public void showAbout() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("O programie");
		alert.setHeaderText("O programie");
		alert.setContentText("Program generuje plik XML z zapisanym pe?nym problemem decyzyjnym AHP \n Arkadiusz Olesek");
		alert.showAndWait();
	}
	
	
	private AhpCore problem;
	private int idCount = 0;
	
	@FXML TextField crField;
	@FXML TextField goalTextField;
	@FXML Button addAltBtn;
	@FXML TextField addAltField;
	@FXML ListView<Alternative> altList;
}
