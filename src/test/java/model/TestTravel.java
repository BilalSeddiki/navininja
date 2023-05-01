package model;

import shortestpath.*;
import model.*;
import utils.IllegalTravelException;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.awt.geom.Point2D.Double;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.BeforeAll;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestTravel {
    
    private ShortestPathAlgorithm algorithm;
    private ArrayList<Station> stationList;
    private Double coordinates1;
    private Double coordinates2;

    /**
     * Crée un réseau, algorithme de calcul du meilleur itinéraire
     * ainsi que des coordonnées.
     */
    @BeforeAll
    public void createDijkstra() {
        ArrayList<Station> stationList = new ArrayList<Station>();
        stationList.add(new Station("Station 0", new Double(48.858093, 2.294694)));
        stationList.add(new Station("Station 1", new Double(48.858203, 2.295068)));
        stationList.add(new Station("Station 2", new Double(48.857126, 2.296561))); //TODO: 48.857946, 2.295365
        stationList.add(new Station("Station 3", new Double(48.857689, 2.295063)));
        stationList.add(new Station("Station 4", new Double(48.857756, 2.294634)));
        stationList.add(new Station("Station 5", new Double(48.858029, 2.294400)));
        this.stationList = stationList;

        ArrayList<Path> pathList = new ArrayList<Path>();
        pathList.add(createPathWithDefaultValues("Line 0", "0", stationList.get(0), stationList.get(1)));
        pathList.add(createPathWithDefaultValues("Line 0", "0", stationList.get(1), stationList.get(2)));
        pathList.add(createPathWithDefaultValues("Line 0", "1", stationList.get(1), stationList.get(0)));
        pathList.add(createPathWithDefaultValues("Line 0", "1", stationList.get(2), stationList.get(1)));
        
        pathList.add(createPathWithDefaultValues("Line 1", "0", stationList.get(3), stationList.get(1)));
        pathList.add(createPathWithDefaultValues("Line 1", "0", stationList.get(1), stationList.get(4)));
        pathList.add(createPathWithDefaultValues("Line 1", "1", stationList.get(1), stationList.get(3)));
        pathList.add(createPathWithDefaultValues("Line 1", "1", stationList.get(4), stationList.get(1)));

        pathList.add(createPathWithDefaultValues("Line 2", "0", stationList.get(0), stationList.get(5)));
        pathList.add(createPathWithDefaultValues("Line 2", "1", stationList.get(5), stationList.get(0)));

        /* 
         * Plan du réseau: (Sn -> Station n, Ln -> Ligne n)
         * S5 <-L2-> S0 <-L0-> S1
         *                      | <-L1-> S4
         *                      | <-L1-> S3
         *                      | <-L0-> S2
         */

        Network network = new Network(stationList, pathList);
        Dijkstra dijkstra = new Dijkstra(network);
        this.algorithm = dijkstra;

        this.coordinates1 = new Double(48.858370, 2.294532);
        this.coordinates2 = new Double(48.858841, 2.292953);
    }

    /**
     * Crée un chemin avec des valeurs par defaut.
     * Les valeurs par défaut sont:
     * Durée du trajet: 5 minutes.
     * Distance entre deux stations: 5 mètres.
     * Horaires de passage des trains: 8h30, 8h40, 8h50.
     * @return chemin avec des valeurs par defaut.
     */
    public Path createPathWithDefaultValues(String lineName, String variant, Station source, Station destination) {
        ArrayList<LocalTime> schedule = new ArrayList<LocalTime>();
        schedule.add(LocalTime.of(8, 30));
        schedule.add(LocalTime.of(8, 40));
        schedule.add(LocalTime.of(8, 50));
        Duration duration = Duration.ofMinutes(5);
        double distance = 5.0;

        Path path = new Path(lineName, variant, schedule, duration, distance, source, destination);

        return path;
    }

    /**
     * TODO
     */
    @Test
    public void testBuilderValidStations() throws IllegalTravelException {
        LocalTime departureTime = LocalTime.of(8, 20);
        Station departure = stationList.get(0);
        Station arrival = stationList.get(1);
        int searchLimit = 5;
        double searchDistance = 1000.0;
        Travel travel = new Travel
            .Builder(algorithm)
            .setDepartureTime(departureTime)
            .setDepartureStation(departure)
            .setArrivalStation(arrival)
            .build();
        assertEquals(travel.getShortestPathAlgorithm(), this.algorithm);
        assertEquals(travel.getDepartureTime(), departureTime);
        assertEquals(travel.getDepartureStation(), departure);
        assertEquals(travel.getArrivalStation(), arrival);
        assertNull(travel.getDepartureCoordinates());
        assertNull(travel.getArrivalCoordinates());
        assertEquals(travel.getSearchLimit(), searchLimit);
        assertEquals(travel.getSearchDistance(), searchDistance);
    }

    /**
     * TODO
     */
    @Test
    public void testBuilderValidCoordinates() throws IllegalTravelException {
        LocalTime departureTime = LocalTime.of(8, 20);
        int searchLimit = 5;
        double searchDistance = 1000.0;
        Travel travel = new Travel
            .Builder(algorithm)
            .setDepartureTime(departureTime)
            .setDepartureCoordinates(coordinates1)
            .setArrivalCoordinates(coordinates2)
            .build();
        assertEquals(travel.getShortestPathAlgorithm(), this.algorithm);
        assertEquals(travel.getDepartureTime(), departureTime);
        assertEquals(travel.getDepartureCoordinates(), coordinates1);
        assertEquals(travel.getArrivalCoordinates(), coordinates2);
        assertNull(travel.getDepartureStation());
        assertNull(travel.getArrivalStation());
        assertEquals(travel.getSearchLimit(), searchLimit);
        assertEquals(travel.getSearchDistance(), searchDistance);
    }

    /**
     * TODO
     */
    @Test
    public void testBuilderSetSearchParameters() throws IllegalTravelException {
        LocalTime departureTime = LocalTime.of(8, 20);
        Station arrival = stationList.get(0);
        int searchLimit = 2;
        double searchDistance = 100.0;
        Travel travel = new Travel
            .Builder(algorithm)
            .setDepartureTime(departureTime)
            .setDepartureCoordinates(coordinates1)
            .setArrivalStation(arrival)
            .setSearchLimit(searchLimit)
            .setSearchDistance(searchDistance)
            .build();
        assertEquals(travel.getShortestPathAlgorithm(), this.algorithm);
        assertEquals(travel.getDepartureTime(), departureTime);
        assertEquals(travel.getDepartureCoordinates(), coordinates1);
        assertEquals(travel.getArrivalStation(), arrival);
        assertNull(travel.getDepartureStation());
        assertNull(travel.getArrivalCoordinates());
        assertEquals(travel.getSearchLimit(), searchLimit);
        assertEquals(travel.getSearchDistance(), searchDistance);
    }
}