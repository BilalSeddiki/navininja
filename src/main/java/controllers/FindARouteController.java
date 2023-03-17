package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.stage.Window;

import java.util.ArrayList;
import java.util.List;

public class FindARouteController {

    @FXML
    Label title;
    @FXML
    Button searchBtn;
    @FXML
    ContextMenu suggestionMenu;
    @FXML
    TextField coordinatesAInput;
    @FXML
    TextField coordinatesBInput;
    ArrayList<String> stations = new ArrayList<>();

    public void initialize() {
        //todo remplacer par getAllStations
        stations.add("BNF");
        stations.add("place d'italie");
        stations.add("corvisare");
        stations.add("copéra");
        stations.add("nation");
        stations.add("glacière");
        stations.add("Gare de lyon");
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
}


