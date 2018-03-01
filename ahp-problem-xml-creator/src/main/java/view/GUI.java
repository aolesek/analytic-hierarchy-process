package view;

import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI extends Application {
    /**
     * @param args the command line arguments
     */
    public void startGUI() {
        Application.launch(GUI.class, (java.lang.String[])null);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
        	URL url = new File("Alt.fxml").toURI().toURL();
            Parent page =  FXMLLoader.load(url);
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("AHPProblem");
            primaryStage.show();
        } catch (Exception ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
