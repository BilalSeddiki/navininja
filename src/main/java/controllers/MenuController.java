package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

/**
 * Contrôleur de la vue Menu, se charge de définir les écouteurs des boutons
 * présents sur l'écran.
 * @author R. MARTINI
 */
public class MenuController extends Controller {

    /**
     * Référence au composant FXML du bouton 'Network plan'.
     */
    @FXML
    public Button networkPlanBtn;

    /**
     * Référence au composant FXML du bouton 'Find a route'.
     */

    @FXML
    public Button findARouteBtn;

    /**
     * Référence au composant FXML du bouton 'Time plan'.
     */
    @FXML
    Button scheduleBtn;

    /**
     * Écouteur du bouton 'Find a route'.
     * @param actionEvent événement
     */
    public void findARouteListener(final ActionEvent actionEvent)
            throws IOException {
        navigationController.navigateTo("FindARouteView");
    }

    /**
     * Écouteur du bouton 'Network plan'.
     * @param actionEvent événement
     */
    public void networkPlanListener(final ActionEvent actionEvent) {
    }

    /**
     * Écouteur du bouton 'Schedule'.
     * @param actionEvent événement
     */
    public void scheduleListener(final ActionEvent actionEvent) {
        navigationController.navigateTo("ScheduleView");
    }
}
