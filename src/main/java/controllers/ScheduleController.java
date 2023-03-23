package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TextField;

import java.util.ArrayList;

/**
 * Controlleur de la vue trouver un itinéraire
 * @author R. MARTINI
 */
public class ScheduleController extends Controller {

    @FXML
    ComboBox<String> lineComboBox;
    @FXML
    TextField stationInput;
    @FXML
    ContextMenu suggestionMenuStation;
    @FXML
    ContextMenu suggestionMenuLine;
    @FXML
    Button searchBtn;
    @FXML
    Button goBackBtn;

    private final ArrayList<String> stations = new ArrayList<>();
    public void initialize(){

        //TODO modifier liste des lignes
         final ObservableList<String> lines = FXCollections.observableArrayList(
                "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
                "13", "14"
        );
        //TODO modifier liste stations ici
        stations.add("BNF");
        stations.add("place d'italie");
        stations.add("nation");
        stations.add("Glacière");
        stations.add("Gare de lyon");
        stationInput.textProperty().addListener(new TextFieldListener( stationInput, suggestionMenuStation, stations));
        //lineInput.textProperty().addListener( new TextFieldListener( lineInput, suggestionMenuLine, stations));
        lineComboBox.getItems().addAll(lines);
    }
    /**
     * Écouteur du bouton 'Back' permet de retourner à la page précédente
     * @param actionEvent événement détécté
     */
    public void goBackListener(ActionEvent actionEvent) {
        navigationController.navigateBack();

    }
}
