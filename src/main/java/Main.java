import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import controllers.*;
import utils.Globals;

import java.io.File;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        NavigationController navigation = NavigationController.getInstance(primaryStage);
        primaryStage.setResizable(false);
        primaryStage.setTitle("NaviNinja");
        navigation.navigateTo("MenuView");
        primaryStage.show();

    }
}
