package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import java.util.ArrayList;
import java.util.List;

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
        stations.add("corvisare");
        stations.add("copéra");
        stations.add("nation");
        stations.add("glacière");
        stations.add("Gare de lyon");
        hourComboBoxA.getItems().addAll(hours);
        hourComboBoxB.getItems().addAll(hours);
        minComboBoxA.getItems().addAll(minutes);
        minComboBoxB.getItems().addAll(minutes);
        coordinatesAInput.textProperty().addListener((observable, oldValue, newValue) -> textFieldListener(newValue, coordinatesAInput));
        coordinatesBInput.textProperty().addListener((observable, oldValue, newValue) -> textFieldListener(newValue, coordinatesBInput));

    }

    /**
     * Écouteur de changement dans un textfield afin d'afficher le menu de suggestions d'auto complétion
     * @param newValue nouvelle valeur saisie pour filtrer la liste des suggestions
     * @param textField textfield auquel on rattache le menu
     */
    private void textFieldListener(String newValue, TextField textField){
        if (!newValue.isBlank()) {
            List<String> filteredSuggestions = new ArrayList<>();
            for (String s : stations) {
                if (s.toLowerCase().startsWith(newValue.toLowerCase())) {
                    filteredSuggestions.add(s);
                }
            }
            if (!filteredSuggestions.isEmpty()) {
                showCompletionList(suggestionMenu, filteredSuggestions, textField);
            } else {
                suggestionMenu.hide();
            }
        } else {
            suggestionMenu.hide();
        }
    }

    /**
     * Fonction permettant d'afficher le menu de complétion
     * @param completionMenu le menu à afficher
     * @param suggestions liste de suggestions
     * @param textField textfield auquel le menu est rattaché
     */
    private void showCompletionList(ContextMenu completionMenu, List<String> suggestions, TextField textField) {
        completionMenu.getItems().clear();
        for (String suggestion : suggestions) {
            completionMenu.getItems().add(new MenuItem(suggestion));
        }
        completionMenu.setOnAction(event -> {
            MenuItem selectedItem = (MenuItem) event.getTarget();
            textField.setText(selectedItem.getText());
            textField.positionCaret(selectedItem.getText().length());
            completionMenu.hide();
        });
        textField.setContextMenu(completionMenu);
        Bounds bounds = textField.localToScreen(textField.getBoundsInLocal());
        completionMenu.show(textField, bounds.getMinX(), bounds.getMaxY());
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


