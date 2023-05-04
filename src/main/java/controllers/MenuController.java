package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


/**
 * Contrôleur de la vue Menu, se charge de définir les écouteurs des boutons
 * présents sur l'écran.
 * @author R. MARTINI
 */
public class MenuController extends Controller {

    /**
     * Référence au composant FXML du bouton 'Find a route'.
     */
    @FXML
    Button findARouteBtn;

    /**
     * Référence au composant FXML du bouton 'Time plan'.
     */
    @FXML
    Button scheduleBtn;

    /**
     * Écouteur du bouton 'Find a route'.
     * @param actionEvent événement
     */
    public void findARouteListener(final ActionEvent actionEvent) {
        navigationController.navigateTo("FindARouteView");
    }

    /**
     * Écouteur du bouton 'Schedule'.
     * @param actionEvent événement
     */
    public void scheduleListener(final ActionEvent actionEvent) {
        navigationController.navigateTo("ScheduleView");
    }
}
