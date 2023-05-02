import javafx.application.Application;
import javafx.stage.Stage;
import model.Network;
import utils.Globals;
import controllers.*;


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