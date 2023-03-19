package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import java.util.ArrayList;
import java.util.List;

public class FindARouteController {


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
    ArrayList<String> stations = new ArrayList<>();
    private final ObservableList<String> hours = FXCollections.observableArrayList(
            "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
            "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"
    );
    private final ObservableList<String> minutes = FXCollections.observableArrayList();


    public void initialize() {
        for (int i = 0; i < 60; i++) {
            String minute = String.format("%02d", i);
            minutes.add(minute);
        }
        //todo remplacer par getAllStations
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
        coordinatesAInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                List<String> filteredSuggestions = new ArrayList<>();

                for (String s : stations) {
                    if (s.toLowerCase().startsWith(newValue.toLowerCase())) {
                        filteredSuggestions.add(s);
                    }
                }
                if (!filteredSuggestions.isEmpty()) {
                    showCompletionList(suggestionMenu, filteredSuggestions);
                } else {
                   suggestionMenu.hide();
                }
            } else {
                suggestionMenu.hide();
            }
        });
    }

    private void showCompletionList(ContextMenu completionMenu, List<String> suggestions) {
        completionMenu.getItems().clear();

        for (String suggestion : suggestions) {
            completionMenu.getItems().add(new MenuItem(suggestion));
        }

        completionMenu.setOnAction(event -> {
            MenuItem selectedItem = (MenuItem) event.getTarget();
            coordinatesAInput.setText(selectedItem.getText());
            coordinatesAInput.positionCaret(selectedItem.getText().length());
            completionMenu.hide();
        });
        coordinatesAInput.setContextMenu(completionMenu);
        Bounds bounds = coordinatesAInput.localToScreen(coordinatesAInput.getBoundsInLocal());
        // show the menu below the text field
        completionMenu.show(coordinatesAInput, bounds.getMinX()+5, bounds.getMaxY());
    }

    public void searchPathListener(ActionEvent actionEvent) {
        //todo implement search action
    }

    public void goBackListener(ActionEvent actionEvent) {
    }
}


