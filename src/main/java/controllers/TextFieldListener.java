package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import model.Network;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class TextFieldListener implements ChangeListener<String> {

    /** Liste des stations observées. */
    private final ObservableList<String> stations;

    /** Menu contextuel des suggestions de stations. */
    private final ContextMenu suggestionMenu;

    /** Champ de texte d'entrée de station. */
    private final TextField textField;

    /** Network contenant les stations. */
    private final Network network;

    /** Liste déroulante. */
    private final ComboBox<String> lineComboBox;

    /**
     * Constructeur de l'écouteur de changement dans un textfield afin
     * d'afficher le menu de suggestions d'auto complétion.
     * @param textField textfield auquel on rattache le menu
     * @param menu menu de suggestions d'auto complétion
     * @param stations liste des stations
     * @param network network contenant les stations
     */
    public TextFieldListener(final TextField textField, final ContextMenu menu,
        final ObservableList<String> stations, final Network network) {
        this.stations  = stations;
        this.suggestionMenu = menu;
        this.textField = textField;
        this.network = network;
        lineComboBox = null;

    }

    /**
     * Constructeur de l'écouteur de changement dans un textfield afin
     * d'afficher le menu de suggestions d'auto complétion.
     * @param textField textfield auquel on rattache le menu
     * @param menu menu de suggestions d'auto complétion
     * @param stations liste des stations
     * @param network network contenant les stations
     * @param lineComboBox liste déroulante
     */
    public TextFieldListener(final TextField textField, final ContextMenu menu,
            final ObservableList<String> stations, final Network network,
            final ComboBox<String> lineComboBox) {
        this.stations  = stations;
        this.suggestionMenu = menu;
        this.textField = textField;
        this.lineComboBox = lineComboBox;
        this.network = network;
    }

    /**
     * Écouteur de changement dans un textfield afin d'afficher le menu
     * de suggestions d'auto complétion.
     * @param newValue nouvelle valeur saisie pour filtrer la liste
     * des suggestions
     * @param textField textfield auquel on rattache le menu
     */
    private void textFieldListener(final String newValue,
            final TextField textField) {
        if (!newValue.isBlank()) {
            List<String> filteredSuggestions = new ArrayList<>();
            String normalizedInput =
                Normalizer.normalize(
                    newValue, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
            for (String s : stations) {
                String normalizedStation =
                    Normalizer.normalize(
                        s, Normalizer.Form.NFD).replaceAll("\\p{M}", "");

                if (normalizedInput.length() == 1) {
                    if (normalizedStation.toLowerCase()
                            .startsWith(normalizedInput.toLowerCase())) {
                        filteredSuggestions.add(s);
                    }
                } else {
                    if (normalizedStation.toLowerCase()
                            .contains(normalizedInput.toLowerCase())) {
                        filteredSuggestions.add(s);
                    }
                }
            }
            if (!filteredSuggestions.isEmpty()) {
                showCompletionList(suggestionMenu, filteredSuggestions,
                    textField);
                if (lineComboBox != null && network.hasStation(newValue)) {
                    final ObservableList<String> lines =
                        FXCollections.observableArrayList(
                            network.getLinesByStation(newValue));
                    lineComboBox.getItems().clear();
                    lineComboBox.getItems().addAll(lines);
                    }
                }
        } else {
            suggestionMenu.hide();
        }
    }

    /**
     * Fonction permettant d'afficher le menu de complétion.
     * @param completionMenu le menu à afficher
     * @param suggestions liste de suggestions
     * @param textField textfield auquel le menu est rattaché
     */
    private void showCompletionList(final ContextMenu completionMenu,
            final List<String> suggestions, final TextField textField) {
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
    public final void changed(
            final ObservableValue<? extends String> observable,
            final String oldValue, final String newValue) {
        textFieldListener(newValue, textField);
    }
}
