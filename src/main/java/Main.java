import controllers.NavigationController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     * la fonction qui demarre le programme.
     * @param args les arguments de lancement
     */
    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public final void start(final Stage primaryStage) throws Exception {
        NavigationController navigation =
            NavigationController.getInstance(primaryStage);

        primaryStage.setResizable(false);
        primaryStage.setTitle("NaviNinja");
        navigation.navigateTo("MenuView");
        primaryStage.show();
    }
}
