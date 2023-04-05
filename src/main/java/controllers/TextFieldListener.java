package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.List;

public class TextFieldListener implements ChangeListener<String> {
    private final ArrayList<String> stations;
    private final ContextMenu suggestionMenu;
    private final TextField textField;
    public TextFieldListener(TextField textField, ContextMenu menu, ArrayList<String> stations) {
        this.stations  = stations;
        this.suggestionMenu = menu;
       this.textField = textField;
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

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        textFieldListener(newValue , textField);
    }
}
