import javafx.application.Application;
import javafx.stage.Stage;
import model.Network;
import utils.Globals;
import controllers.*;

import model.*;
import shortestpath.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.awt.geom.Point2D.Double;

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

    public static void main(String[] args) {
        var stationList = new ArrayList<Station>();
        Station s0 = new Station("Station 0", new Double(0, 0));
        Station s1 = new Station("Station 1", new Double(1, 1));
        Station s2 = new Station("Station 2", new Double(2, 2));
        Station s3 = new Station("Station 3", new Double(3, 3));
        Station s4 = new Station("Station 4", new Double(4, 4));
        Station s5 = new Station("Station 5", new Double(5, 5));
        stationList.add(s0);
        stationList.add(s1);
        stationList.add(s2);
        stationList.add(s3);
        stationList.add(s4);
        stationList.add(s5);

        var schedule = new ArrayList<LocalTime>();
        schedule.add(LocalTime.of(8, 30));
        schedule.add(LocalTime.of(8, 40));
        schedule.add(LocalTime.of(8, 50));
        var duration = Duration.ofMinutes(5);
        var distance = 5;

        var pathList = new ArrayList<Path>();
        pathList.add(new Path("Line 0", "0", schedule, duration, distance, stationList.get(0), stationList.get(1)));
        pathList.add(new Path("Line 0", "0", schedule, duration, distance, stationList.get(1), stationList.get(2)));
        pathList.add(new Path("Line 1", "0", schedule, duration, distance, stationList.get(3), stationList.get(1)));
        pathList.add(new Path("Line 1", "0", schedule, duration, distance, stationList.get(1), stationList.get(4)));
        pathList.add(new Path("Line 2", "0", schedule, duration, distance, stationList.get(0), stationList.get(5)));
        pathList.add(new Path("Line 0", "1", schedule, duration, distance, stationList.get(1), stationList.get(0)));
        pathList.add(new Path("Line 0", "1", schedule, duration, distance, stationList.get(2), stationList.get(1)));
        pathList.add(new Path("Line 1", "1", schedule, duration, distance, stationList.get(1), stationList.get(3)));
        pathList.add(new Path("Line 1", "1", schedule, duration, distance, stationList.get(4), stationList.get(1)));
        pathList.add(new Path("Line 2", "1", schedule, duration, distance, stationList.get(5), stationList.get(0)));

        var network = new Network(stationList, pathList);
        Dijkstra algorithm = new Dijkstra(network);

        Travel travel = 
            new Travel
                .Builder(algorithm)
                //.setDepartureStation(s0)
                .setDepartureCoordinates(new Double(0, 0))
                .setArrivalStation(s0)
                .setDepartureTime(LocalTime.of(8, 20))
                .build();
        Itinerary itinerary = travel.createItinerary();
        System.out.println(itinerary.getDuration());
        System.out.println(itinerary.getTransports().get(0).toString());
    }
}