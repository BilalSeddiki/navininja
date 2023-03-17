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
        File file = new File(Globals.pathToView("MenuView.fxml"));
        FXMLLoader loader = new FXMLLoader(file.toURI().toURL());
        Parent root = loader.load();
        primaryStage.setResizable(false);
        primaryStage.setTitle("NaviNinja");
        Scene scene = new Scene(root, Globals.windowWidth(), Globals.windowHeight());
        scene.getStylesheets().add(getClass().getResource("views/style/style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
