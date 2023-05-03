import javafx.application.Application;
import javafx.stage.Stage;
import model.Network;
import utils.Globals;
import controllers.*;
import model.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import java.awt.geom.Point2D.Double;

import shortestpath.*;
import shortestpath.graph.NodeSize;
import utils.IllegalTravelException;

/*
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
*/

public class Main {

    public static Path createPathWithDefaultValues(String lineName, String variant, Station source, Station destination) {
        ArrayList<LocalTime> schedule = new ArrayList<LocalTime>();
        schedule.add(LocalTime.of(8, 30));
        schedule.add(LocalTime.of(8, 40));
        schedule.add(LocalTime.of(8, 50));
        Duration duration = Duration.ofMinutes(5);
        double distance = 5.0;

        Path path = new Path(lineName, variant, schedule, duration, distance, source, destination);

        return path;
    }

    public static void main(String[] args) throws IllegalTravelException {
        Network network;

        ArrayList<Station> stationList = new ArrayList<Station>();
        stationList.add(new Station("Station 0", new Double(48.858093, 2.294694)));
        stationList.add(new Station("Station 1", new Double(48.858203, 2.295068)));
        stationList.add(new Station("Station 2", new Double(48.857126, 2.296561)));
        stationList.add(new Station("Station 3", new Double(48.857689, 2.295063)));
        stationList.add(new Station("Station 4", new Double(48.857756, 2.294634)));
        stationList.add(new Station("Station 5", new Double(48.858029, 2.294400)));

        ArrayList<Path> pathList = new ArrayList<Path>();
        /* (Ordre) Ligne n [Variant]: Station m -> Station o */
        /* (0) L0 [0]: S0 -> S1 */
        pathList.add(createPathWithDefaultValues("Line 0", "0", stationList.get(0), stationList.get(1)));
        
        /* (1) L0 [0]: S1 -> S2 */
        pathList.add(createPathWithDefaultValues("Line 0", "0", stationList.get(1), stationList.get(2)));
        
        /* (2) L0 [1]: S1 -> S0 */
        pathList.add(createPathWithDefaultValues("Line 0", "1", stationList.get(1), stationList.get(0)));
        
        /* (3) L0 [1]: S2 -> S1 */
        pathList.add(createPathWithDefaultValues("Line 0", "1", stationList.get(2), stationList.get(1)));
        
        /* (4) L1 [0]: S3 -> S1 */
        pathList.add(createPathWithDefaultValues("Line 1", "0", stationList.get(3), stationList.get(1)));
        
        /* (5) L1 [0]: S1 -> S4 */
        pathList.add(createPathWithDefaultValues("Line 1", "0", stationList.get(1), stationList.get(4)));
        
        /* (6) L1 [1]: S1 -> S3 */
        pathList.add(createPathWithDefaultValues("Line 1", "1", stationList.get(1), stationList.get(3)));
        
        /* (7) L1 [1]: S4 -> S1 */
        pathList.add(createPathWithDefaultValues("Line 1", "1", stationList.get(4), stationList.get(1)));

        /* (8) L2 [0]: S0 -> S5 */
        pathList.add(createPathWithDefaultValues("Line 2", "0", stationList.get(0), stationList.get(5)));
        
        /* (9) L2 [1]: S5 -> S0 */
        pathList.add(createPathWithDefaultValues("Line 2", "1", stationList.get(5), stationList.get(0)));

        /* 
         * Plan du réseau: (Sn -> Station n, Ln -> Ligne n)
         * S5 <-L2-> S0 <-L0-> S1
         *                      | <-L1-> S4
         *                      | <-L1-> S3
         *                      | <-L0-> S2
         */

        network = new Network(stationList, pathList);

        ArrayList<Double> coordinatesList = new ArrayList<Double>();
        coordinatesList.add(new Double(48.858370, 2.294532));
        coordinatesList.add(new Double(48.858841, 2.292953));
        coordinatesList.add(new Double(48.857946, 2.295365));
        coordinatesList.add(new Double(48.832994, 2.335764));
        coordinatesList.add(new Double(48.832068, 2.339215));
        coordinatesList = coordinatesList;


        LocalTime departureTime = LocalTime.of(8, 20);
        Travel travel = new Travel
            .Builder(network)
            .setDepartureTime(departureTime)
            .setDepartureStation(stationList.get(0))
            .setArrivalStation(stationList.get(2))
            .build();
        Itinerary itinerary = travel.createItinerary();

        ArrayList<Transport> transports = new ArrayList<Transport>();
        transports.add(pathList.get(0));
        transports.add(pathList.get(1));
        Itinerary expected = new Itinerary(departureTime, transports);

        System.out.println("expected: ");
        System.out.println(expected.toString());
        System.out.println("---------------");
        System.out.println(itinerary.toString());
    }
}
