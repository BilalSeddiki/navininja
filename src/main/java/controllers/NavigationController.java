package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.Globals;

import java.io.File;
import java.io.IOException;
import java.util.Stack;

public class NavigationController {
    private static NavigationController instance;
    private Stack<String> history = new Stack<>();
    private final Stage stage;
    private Controller currentController;

    private NavigationController(Stage stage) {
        this.stage = stage;
    }

    public static NavigationController getInstance(Stage stage) {
        if (instance == null) {
            instance = new NavigationController(stage);
        }
        return instance;
    }
    public void navigateTo(String viewName) {
        try {
            File file = new File(Globals.pathToView(viewName+".fxml"));

            FXMLLoader loader = new FXMLLoader(file.toURI().toURL());
            Parent root = loader.load();
            Scene scene = new Scene(root);
            currentController = loader.getController();
            currentController.setNavigationController(this);
            history.push(viewName);
            scene.getStylesheets().add(getClass().getResource(Globals.pathToStyleSheetFromController("style.css")).toExternalForm());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void navigateBack() {
        if (!history.isEmpty()) {
            history.pop();
            if (!history.isEmpty()) {
                navigateTo(history.peek());
            }
        }
    }
}
