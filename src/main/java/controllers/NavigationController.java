package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.Globals;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

/**
 * Classe NavigationController, Singleton, se charge de définir la pile de
 * l'historique de navigation dans l'application.
 * @author R. MARTINI
 */
public final class NavigationController {

    /**
     * Instance.
     */
    private static NavigationController instance;

    /**
     * Pile de l'historique.
     */
    private final Stack<String> history = new Stack<>();

    /**
     * Fenêtre de l'application.
     */
    private final Stage stage;

    /**
     * Constructeur privé.
     * @param stage fenêtre de l'application
     */
    private NavigationController(final Stage stage) {
        this.stage = stage;
    }

    /**
     * Getter de l'instance du NavigationController.
     * @param stage fenêtre de l'application
     * @return instance du NavigationController
     */
    public static NavigationController getInstance(final Stage stage) {
        if (instance == null) {
            instance = new NavigationController(stage);
        }
        return instance;
    }

    /**
     * Fonction pour changer de scène dans l'application.
     * @param viewName nouvelle scène à afficher
     */
    public void navigateTo(final String viewName) {
        try {
            File file = new File(Globals.pathToView(viewName + ".fxml"));

            FXMLLoader loader = new FXMLLoader(file.toURI().toURL());
            Parent root = loader.load();
            Scene scene = new Scene(root);
            // Instance actuelle du controlleur :
            // permet de passer la pile de navigation en paramètre à celui-ci
            Controller currentController = loader.getController();
            currentController.setNavigationController(this);
            history.push(viewName);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fonction pour retourner à la scène précédente dans l'application.
     */
    public void navigateBack() {
        if (!history.isEmpty()) {
            history.pop();
            if (!history.isEmpty()) {
                navigateTo(history.peek());
            }
        }
    }
}
