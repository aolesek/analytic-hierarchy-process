package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.util.StringConverter;
import model.AhpCore;
import model.Criterion;
import model.NestedCriterion;

public class CompareController {
	
	public CompareController() {
		problem = AhpCore.getInstance();
	}

	@FXML void buttonHandle(ActionEvent e) {
		if (updateCRs(problem.getRootCriterion().getSubcriteria())) {
			problem.processXML();
			Alert alert = new Alert(AlertType.INFORMATION, "Zapisano plik XML");
			alert.setHeaderText("Eksport XML");
			alert.showAndWait();

		} else {
			Alert alert = new Alert(AlertType.CONFIRMATION, "Czy zapisaæ do pliku pomimo niespójnoœci?", ButtonType.YES, ButtonType.CANCEL);
			alert.setHeaderText("Dokonano niespójnych wyborów");
			alert.showAndWait();

			if (alert.getResult() == ButtonType.YES) {
				problem.processXML();
			}
		}


	}
	
	@FXML
	public void initialize() {
		renderComparsions();
	}
	
    public void renderComparsions() {
		System.out.println(problem);
		showComparsion(problem.getRootCriterion());
    }
	
	private void showComparsion(Criterion parent) {
		getSingleComparsion(parent);
		if (parent != null && parent.getSubcriteria() != null) {
			for (int i = 0; i < parent.getSubcriteria().size(); i++) {
				showComparsion(parent.getSubcriteria().get(i));
			}
		}

	}
	
	private void getSingleComparsion(Criterion c) {
		if (c.getClass().toString().equals("class model.SimpleCriterion") ) {
			for (int i = 1; i <= c.getComparsionsCount() ; i++) {
				singleAltComp(
						problem.getAlternatives().get(c.getSingleComparsionCoords(i).x).getName(),
						problem.getAlternatives().get(c.getSingleComparsionCoords(i).y).getName(),
						c, i
						);
			}

		} else {
			for (int i = 1; i <= c.getComparsionsCount() ; i++) {
				singleAltComp(
						c.getSubcriteria().get(c.getSingleComparsionCoords(i).x).getName(),
						c.getSubcriteria().get(c.getSingleComparsionCoords(i).y).getName(),
						c, i
						);
			}

		}
	}
	
	public void singleAltComp(String alt1, String alt2, Criterion criterion, int i) {
		BorderPane decisionRoot = new BorderPane();
		decisionRoot.setPrefHeight(100);
		decisionRoot.setPrefWidth(500);
		
		String titleText;
		
		if (criterion.getClass().toString().equals("class model.SimpleCriterion") ) {
			titleText = "\nIle razy "+ alt1 + " jest lepsze od "+ alt2 +" pod wzglêdem kryterium "+ criterion.getName();
		} else {
			titleText = "\nIle razy kryterium "+ alt1 + " jest wa¿niejsze od kryterium "+ alt2 +"?";

		}
		
		Label title = new Label(titleText);
		title.setMaxWidth(500);
        title.setAlignment(Pos.CENTER);
		decisionRoot.setTop(title);
		
		Label minValue = new Label("Gorsze");
		decisionRoot.setLeft(minValue);
		
		Label maxValue = new Label("Lepsze");
		decisionRoot.setRight(maxValue);
		
		
		Slider slider = new Slider();
		slider.setMin(-7);
		slider.setMax(9);
		slider.setValue(1);
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setMinorTickCount(0);
		slider.setMajorTickUnit(2);
		slider.setBlockIncrement(1);
		slider.setSnapToTicks(true);
		slider.setPrefWidth(100);
		
		slider.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double n) {
                if (n < -5) return "1/9";
                if (n < -3) return "1/7";
                if (n < -1) return "1/5";
                if (n < 1) return "1/3";
                if (n < 3) return "1";
                if (n < 5) return "3";
                if (n < 7) return "5";
                if (n < 9) return "7";
                
                return "9";
            }

            @Override
            public Double fromString(String s) {
                switch (s) {
                    case "1/9":
                        return 0d;
                    case "1/7":
                        return 1d;
                    case "1/5":
                        return 2d;
                    case "1/3":
                        return 3d;
                    case "1":
                        return 4d;
                    case "3":
                        return 5d;
                    case "5":
                        return 6d;

                    default:
                        return 7d;
                }
            }
        });
		
		slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
        			criterion.setSingleValue(i, translateValue(new_val.doubleValue()) );
        			updateCRs(problem.getRootCriterion().getSubcriteria());
                }
            });
			
		
		decisionRoot.setCenter(slider);
				
		Label label = new Label();
		decisionRoot.setBottom(label);
		//Separator separator = new Separator();
		//decisionRoot.setBottom(separator);
		
		
		
		//decisionRoot.setStyle("-fx-background-color: #0f0f0f");

		
		decisionPane.getChildren().add(decisionRoot);
		
		criteriaPanes.put(decisionRoot, criterion);
		panes.add(decisionRoot);
	}
	
	private Double translateValue(Double value) {
		if (value < -6) return 1.0/9.0;
		if (value < -5) return 1.0/8.0;
		if (value < -4) return 1.0/7.0;
		if (value < -3) return 1.0/6.0;
		if (value < -2) return 1.0/5.0;
		if (value < -1) return 1.0/4.0;
		if (value <  0) return 1.0/3.0;
		if (value <  1) return 1.0/2.0;
		if (value <  2) return 1.0;
		if (value <  3) return 2.0;
		if (value <  4) return 3.0;
		if (value <  5) return 4.0;
		if (value <  6) return 5.0;
		if (value <  7) return 6.0;
		if (value <  8) return 7.0;
		if (value <  8) return 8.0;
		return 9.0;		
	}
	
	private Boolean updateCRs(ArrayList<Criterion> criteria) {
		Boolean isConsistent = true;
		for (BorderPane bp : panes) {
			Criterion criterion = criteriaPanes.get(bp);
			System.out.println(criterion.getConsistencyRatio());
			if (criterion.isConsistent()) {
				bp.setStyle("-fx-background-color: #F4F4F4");
			} else {
				bp.setStyle("-fx-background-color: #ff704d");
				isConsistent = false;
			}
			Label label = (Label) bp.getChildren().get(4);
			label.setText("CR: "+ String.format("%.2f", criterion.getConsistencyRatio()));
		}
		return isConsistent;
	}
	
	@FXML
	public void showHelp() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Pomoc");
		alert.setHeaderText("Pomoc");
		alert.setContentText("Na tym etapie nale¿y porównac parami wszystkie alternatywy pod wzglêdem podanych kryteriów a tak¿e zadecydowaæ które  z kryteriów jest istotniejsze w danym przypadku. Je¿eli dojdzie do niespójnoœci w podjêtej decyzji, problematyczne porównania zostan¹ podœwietlone na czerwono.");
		alert.showAndWait();
	}
	
	@FXML
	public void showAbout() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("O programie");
		alert.setHeaderText("O programie");
		alert.setContentText("Program generuje plik XML z zapisanym pe³nym problemem decyzyjnym AHP \nArkadiusz Olesek");
		alert.showAndWait();
	}
	
	@FXML
	public void processXML() {
		problem.processXML();
	}
	
	@FXML
	public void exitHandle() {
		Platform.exit();
	}
	
	

	@FXML FlowPane decisionPane;
	private ArrayList<BorderPane> panes = new ArrayList<BorderPane>();
	private Map<BorderPane,Criterion> criteriaPanes = new HashMap<BorderPane, Criterion>();
	private AhpCore problem;
}
