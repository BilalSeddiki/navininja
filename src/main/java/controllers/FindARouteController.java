package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.ArrayList;

/**
 * Controlleur de la vue trouver un itinéraire
 * @author R. MARTINI
 */
public class FindARouteController extends Controller {

    @FXML
    Button goBackBtn;
    @FXML
    ComboBox<String> hourComboBoxA;
    @FXML
    ComboBox<String> minComboBoxA;
    @FXML
    ComboBox<String> hourComboBoxB;
    @FXML
    ComboBox<String> minComboBoxB;
    @FXML
    Button searchBtn;
    @FXML
    ContextMenu suggestionMenu;
    @FXML
    TextField coordinatesAInput;
    @FXML
    TextField coordinatesBInput;
    /**
     * todo remove this list and replace it by the actual stations
     */
    ArrayList<String> stations = new ArrayList<>();


    /**
     * Liste contenant les heures de la journée
     */
    private final ObservableList<String> hours = FXCollections.observableArrayList(
            "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
            "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"
    );
    /**
     * Liste contenant les minutes
     */
    private final ObservableList<String> minutes = FXCollections.observableArrayList();

    public void initialize() {
        //Initialisation de la liste des minutes
        for (int i = 0; i < 60; i++) {
            String minute = String.format("%02d", i);
            minutes.add(minute);
        }
        //todo remove all of this
        stations.add("BNF");
        stations.add("place d'italie");
        stations.add("nation");
        stations.add("glacière");
        stations.add("Gare de lyon");
        hourComboBoxA.getItems().addAll(hours);
        hourComboBoxB.getItems().addAll(hours);
        minComboBoxA.getItems().addAll(minutes);
        minComboBoxB.getItems().addAll(minutes);
        coordinatesAInput.textProperty().addListener(new TextFieldListener( coordinatesAInput, suggestionMenu, stations));
        coordinatesBInput.textProperty().addListener( new TextFieldListener( coordinatesBInput, suggestionMenu, stations));

    }




    /**
     * Écouteur du bouton 'Search' : lance la recherche d'itinéraire
     * @param actionEvent événement détécté
     */
    public void searchPathListener(ActionEvent actionEvent) {
        //todo implement search action
    }

    /**
     * Écouteur du bouton 'Back' permet de retourner à la page précédente
     * @param actionEvent événement détécté
     */
    public void goBackListener(ActionEvent actionEvent) {
        navigationController.navigateBack();
    }
}


