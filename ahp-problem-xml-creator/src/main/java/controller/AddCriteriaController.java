package controller;

import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.AhpCore;
import model.Alternative;
import model.Criterion;
import model.NestedCriterion;
import model.SimpleCriterion;
import view.GUI;

public class AddCriteriaController {
	public AddCriteriaController() {
		problem = AhpCore.getInstance();
	}
	
	@FXML
	public void initialize() {
		TreeItem<Criterion> root = new TreeItem<Criterion>(new NestedCriterion(problem.getGoal(), 0));
		 root.setExpanded(true);
		if (criteriaTree.getRoot() == null)
			criteriaTree.setRoot(root);
		
		MultipleSelectionModel<TreeItem<Criterion>> msm = criteriaTree.getSelectionModel();

		// This line is the not-so-clearly documented magic.
		int row = criteriaTree.getRow( root );

		// Now the row can be selected.
		msm.select( row );
	}
	
	@FXML
	public void addCriterionBtnHandle(ActionEvent e) {
		addCriterion();
	}
	
	@FXML
	private void addCriterionKeyboardHandle(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER)
			addCriterion();
	}
	
	public void addCriterion() {
		TreeItem<Criterion> selected = criteriaTree.getSelectionModel().getSelectedItem();
		if (selected != null) {
			TreeItem<Criterion> newItem = new TreeItem<Criterion>(new NestedCriterion(criterionNameField.getText(), idCount));
			newItem.setExpanded(true);
			selected.getChildren().add(newItem);
			
			criterionNameField.setText("");
			idCount++;
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Uwaga!");
			alert.setHeaderText("Nale¿y wybraæ rodzica.");
			alert.setContentText("Kliknij na wybranym kryterium aby dodaæ podkryteria.");

			alert.showAndWait();
		}
	}
	
	@FXML
	public void deleteBtnHandle(ActionEvent e) {
		deleteElement();
	}
	
	public void processCriteriaTree() {
		NestedCriterion root = (NestedCriterion) criteriaTree.getRoot().getValue();
		DenseMatrix64F matrix = new DenseMatrix64F(criteriaTree.getRoot().getChildren().size(), criteriaTree.getRoot().getChildren().size());
		CommonOps.fill(matrix, 1);
		root.setMatrix( matrix );
		System.out.println(root);
		root.setCR(problem.getCR());
		
		problem.setRoot(root);
		
		processSubcriteria(criteriaTree.getRoot().getChildren());
		
		System.out.println(problem);
		problem.processXML();
	}
	
	public void processSubcriteria(ObservableList<TreeItem<Criterion>> subcriteria) {
		for (TreeItem<Criterion> item : subcriteria) {
			if (item.getChildren().isEmpty()) {
				SimpleCriterion sc = new SimpleCriterion(item.getValue().getName(), item.getValue().getId(), problem.getCR());
				Integer altCount = problem.getAlternatives().size();
				DenseMatrix64F matrix = new DenseMatrix64F(altCount, altCount);
				CommonOps.fill(matrix, 1);
				sc.setMatrix( matrix );
				sc.setCR(problem.getCR());
				problem.addCriterion(sc, item.getParent().getValue().getId());
			} else {
				NestedCriterion nc = new NestedCriterion(item.getValue().getName(), item.getValue().getId(), problem.getCR());
				Integer subcriCount = item.getChildren().size();
				DenseMatrix64F matrix = new DenseMatrix64F(subcriCount, subcriCount);
				CommonOps.fill(matrix, 1);
				nc.setMatrix(matrix);
				nc.setCR(problem.getCR());
				problem.addCriterion(nc, item.getParent().getValue().getId());
				processSubcriteria(item.getChildren());
			}
		}
	}
	
	public void deleteElement() {
		TreeItem<Criterion> selected = criteriaTree.getSelectionModel().getSelectedItem();
		if (selected != null) {
			selected.getParent().getChildren().remove(selected);

		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Uwaga!");
			alert.setHeaderText("Nale¿y wybraæ rodzica.");
			alert.setContentText("Kliknij na wybranym kryterium aby dodaæ podkryteria.");

			alert.showAndWait();
		}
	}
	
	@FXML
    public void nextBtnHandle(ActionEvent event) {
		processCriteriaTree();
		System.out.println(problem.getRootCriterion());
		
		Node  source = (Node)  event.getSource(); 
	    Stage stage  = (Stage) source.getScene().getWindow();
	    stage.close();
        try {
        	Stage secondStage = new Stage();
        	
        	URL url = new File("Comp.fxml").toURI().toURL();
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
	public void showHelp() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Pomoc");
		alert.setHeaderText("Pomoc");
		alert.setContentText("Nale¿y dodaæ kryteria wzglêdem których oceniane bêda wczeœniej podane alternatywy. Kryteria maj¹ strukturê drzewiast¹, mo¿na je zagnie¿d¿aæ wybieraj¹c rodzica i dodaj¹c podkryterium przy u¿yciu interfejsu na dole programu. W ka¿dej chwili mo¿na wygenerowaæ plik XML z dotychczas doadnymi danymi u¿ywaj¹c menu AHP.");
		alert.showAndWait();
	}
	
	@FXML
	public void showAbout() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("O programie");
		alert.setHeaderText("O programie");
		alert.setContentText("Program generuje plik XML z zapisanym pe³nym problemem decyzyjnym AHP \n Arkadiusz Olesek");
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
	
	@FXML TextField criterionNameField;
	@FXML TreeView<Criterion> criteriaTree;
	private AhpCore problem;
	private int idCount = 1;
}
