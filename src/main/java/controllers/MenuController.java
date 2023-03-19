package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import utils.Globals;
import java.io.File;
import java.io.IOException;

/**
 * Controlleur de la vue Menu, se charge de définir les écouteurs des boutons présents sur l'interface.
 * @author R. MARTINI
 */
public class MenuController {
    /**
     * Référence au composant FXML du bouton 'Network plan'
     */
    @FXML
    public Button networkPlanBtn;
    /**
     * Référence au composant FXML du bouton 'Find a route'
     */
    @FXML
    public Button findARouteBtn;
    /**
     * Référence au composant FXML du bouton 'Time plan'
     */
    @FXML
    public Button timePlanBtn;

    /**
     * Constructeur
     */
    public MenuController() {
    }

    /**
     * Écouteur du bouton 'Find a route'
     * @param actionEvent événement
     */
    public void findARouteListener(ActionEvent actionEvent) throws IOException {
        File file = new File(Globals.pathToView("FindARouteView.fxml"));
        FXMLLoader loader = new FXMLLoader(file.toURI().toURL());
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Search");
        Scene scene = new Scene(root, Globals.windowWidth(), Globals.windowHeight());
        scene.getStylesheets().add(getClass().getResource(Globals.pathToStyleSheetFromController("style.css")).toExternalForm());

        stage.setScene(scene);
        stage.show();
    }
    /**
     * Écouteur du bouton 'Time plan'
     * @param actionEvent événement
     */
    public void timePlanListener(ActionEvent actionEvent) {
    }
    /**
     * Écouteur du bouton 'Network plan'
     * @param actionEvent événement
     */
    public void networkPlanListener(ActionEvent actionEvent) {
    }
}
